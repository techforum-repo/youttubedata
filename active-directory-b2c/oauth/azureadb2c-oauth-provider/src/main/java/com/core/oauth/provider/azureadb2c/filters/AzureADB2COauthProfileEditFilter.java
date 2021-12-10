
package com.core.oauth.provider.azureadb2c.filters;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.jcr.Session;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.crypto.CryptoException;
import com.adobe.granite.crypto.CryptoSupport;
import com.core.oauth.provider.azureadb2c.models.OAuthTokenModel;
import com.core.oauth.provider.azureadb2c.utils.AzureADB2COAuth2ProviderUtils;
import com.core.oauth.provider.azureadb2c.utils.Constants;
import com.day.crx.security.token.TokenCookie;

/**
 * 
 * @author albin
 *
 */

@Component
@SlingServletFilter(scope = { SlingServletFilterScope.REQUEST }, pattern = ".*", methods = { "GET" })
@ServiceDescription("OAuth Profile Edit Filter")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
public class AzureADB2COauthProfileEditFilter implements Filter {


	@Reference
	private CryptoSupport cryptoSupport;

	@Reference
	private ConfigurationAdmin configurationAdmin;

	private static final String CALBACK_URL_SUFFIX = "/oauth/callback";

	String b2cLoginDomain;
	String b2cTenantName;
	String b2cEditPolicyName;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {

		String operation = request.getParameter("operation");
		String configId = request.getParameter("configId");

		if (((HttpServletRequest) request).getMethod().equalsIgnoreCase("GET")
				&& ((HttpServletRequest) request).getRequestURI().endsWith(CALBACK_URL_SUFFIX)
				&& getCurrentUserId((SlingHttpServletRequest) request).startsWith("b2c-")) {

				((HttpServletResponse) response).sendRedirect(getPostRedirectURL((HttpServletRequest) request));

			return;

		}

		if ((operation != null && operation.equals("profileedit")) && (configId != null)) {

			try {

				String authURL = getAuthorizationURL((HttpServletResponse) response, (HttpServletRequest) request,
						configId);

				((HttpServletResponse) response).sendRedirect(authURL);
				return;
			} catch (Exception e) {
				this.log.error("Fatal error while sending Authn request.", e);
				((HttpServletResponse) response).sendError(500,
						"Internal server error, please contact your administrator");
				return;

			}
		}

		filterChain.doFilter(request, response);
	}

	String getAuthorizationURL(HttpServletResponse response, HttpServletRequest request, String configId)
			throws Exception {

		Configuration[] configurations = getConfigurations(configId);

		if (configurations != null && configurations.length >= 1) {

			setTokenCookies(request, response, configId,
					configurations[0].getProperties().get(Constants.OAUTH_CLIENT_ID).toString(), cryptoSupport);

			StringBuilder result = new StringBuilder();
			String separator = "";
			for (String string : (String[]) configurations[0].getProperties().get("oauth.scope")) {
				result.append(separator);
				result.append(string);
				separator = ",";
			}

			Object callbackURL = configurations[0].getProperties().get("oauth.callBackUrl");

			String returnURL = (callbackURL != null && !callbackURL.toString().startsWith("/")) ? callbackURL.toString()
					: AzureADB2COAuth2ProviderUtils.getOAuthReturntURI(request, callbackURL,
							"/callback/j_security_check");

			Configuration b2CProviderConfig = configurationAdmin
					.getConfiguration("com.core.oauth.provider.azureadb2c.config");

			if (!getCurrentUserId((SlingHttpServletRequest) request).equals("anonymous"))
				returnURL = (callbackURL != null && !callbackURL.toString().startsWith("/")) ? callbackURL.toString()
						: AzureADB2COAuth2ProviderUtils.getOAuthReturntURI(request, callbackURL, CALBACK_URL_SUFFIX);

			String profileEditURL = "https://" + b2CProviderConfig.getProperties().get("b2cLoginDomain") + "/"
					+ b2CProviderConfig.getProperties().get("b2cTenantName") + ".onmicrosoft.com/"
					+ "/oauth2/v2.0/authorize?p=%s&client_id=%s&response_type=code&redirect_uri=%s&response_mode=query&scope=%s&state=%s";

			String state = (new BigInteger(130, new SecureRandom())).toString(32);

			return String.format(profileEditURL, b2CProviderConfig.getProperties().get("b2cEditPolicyName"),
					configurations[0].getProperties().get(Constants.OAUTH_CLIENT_ID).toString(), returnURL, result.toString(),
					state);
		}

		return null;

	}

	private String getCurrentUserId(SlingHttpServletRequest request) {
		ResourceResolver resolver = request.getResourceResolver();
		Session session = resolver.adaptTo(Session.class);
		return session.getUserID();

	}

	private String getPostRedirectURL(HttpServletRequest request) {

		String redirectURL = AzureADB2COAuth2ProviderUtils.getOAuthReturntURI(request, null, null);

		try {

			Configuration[] configurations = getConfigurations(TokenCookie.getCookie(request, "oauth-configid"));

			if (configurations != null && configurations.length >= 1) {
				String cookie = TokenCookie.getCookie(request,
						configurations[0].getProperties().get(Constants.OAUTH_CLIENT_ID).toString());

				redirectURL = OAuthTokenModel.fromJSON(cryptoSupport.unprotect(cookie)).getAttributes()
						.get("oauth-redirect").toString();

			}

		} catch (Exception e) {
			log.error(String.format("Error in getPostRedirectURL:%s",e.getMessage()));
		}

		return redirectURL;

	}

	void setTokenCookies(HttpServletRequest request, HttpServletResponse response, String configId, String clientId,
			CryptoSupport cryptoSupport) throws CryptoException, IOException {

		TokenCookie.setCookie(response, "oauth-configid", configId, -1, "/", null, true, request.isSecure());

		String state = (new BigInteger(130, new SecureRandom())).toString(32);

		OAuthTokenModel token = new OAuthTokenModel(clientId, "", "", 1);

		token.setAttribute("state", state);
		token.setAttribute("oauth-redirect", request.getRequestURI());

		TokenCookie.setCookie(response, clientId, "\"" + cryptoSupport.protect(token.toJSON()) + "\"", -1, "/", null,
				true, request.isSecure());

	}

	Configuration[] getConfigurations(String configId) throws IOException, InvalidSyntaxException  {

		return this.configurationAdmin.listConfigurations("(&(" + "oauth.config.id" + "="
				+ configId + ")(" + "service.factoryPid" + "=" + "com.adobe.granite.auth.oauth.provider" + "))");		 
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}