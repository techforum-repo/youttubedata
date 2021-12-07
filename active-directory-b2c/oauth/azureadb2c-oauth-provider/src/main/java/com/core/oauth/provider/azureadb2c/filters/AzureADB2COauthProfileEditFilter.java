
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
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.crypto.CryptoSupport;
import com.core.oauth.provider.azureadb2c.models.OAuthToken;
import com.day.crx.security.token.TokenCookie;

@Component
@SlingServletFilter(scope = { SlingServletFilterScope.REQUEST }, pattern = ".*", methods = { "GET" })
@ServiceDescription("OAuth Profile Edit Filter")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
public class AzureADB2COauthProfileEditFilter implements Filter {

	@Reference
	XSSAPI xssAPI;

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

			String redirectURL = ((HttpServletRequest) request).getRequestURI().split(CALBACK_URL_SUFFIX)[0];

			((HttpServletResponse) response).sendRedirect(redirectURL);

			return;

		}

		if ((operation != null && operation.equals("profileedit")) && (configId != null)) {

			try {

				((HttpServletResponse) response).sendRedirect(
						getEditRedirectURL((HttpServletResponse) response, (HttpServletRequest) request, configId));
				return;
			} catch (Exception e) {
				this.log.error("Fatal error while sending Authn request.", (Throwable) e);
				((HttpServletResponse) response).sendError(500,
						"Internal server error, please contact your administrator");
				return;

			}
		}

		filterChain.doFilter(request, response);
	}

	String getEditRedirectURL(HttpServletResponse response, HttpServletRequest request, String configId)
			throws Exception {

		Configuration[] configurations = this.configurationAdmin.listConfigurations("(&(" + "oauth.config.id" + "="
				+ configId + ")(" + "service.factoryPid" + "=" + "com.adobe.granite.auth.oauth.provider" + "))");

		if (configurations != null && configurations.length >= 1) {

			TokenCookie.setCookie(((HttpServletResponse) response), "oauth-configid", configId, -1, "/", null, true,
					request.isSecure());

			String state = (new BigInteger(130, new SecureRandom())).toString(32);

			OAuthToken token = new OAuthToken(configurations[0].getProperties().get("oauth.client.id").toString(), "",
					"", 1);
			token.setAttribute("state", state);
			token.setAttribute("oauth-redirect", "");

			TokenCookie.setCookie(response, configurations[0].getProperties().get("oauth.client.id").toString(),
					"\"" + cryptoSupport.protect(token.toJSON()) + "\"", 600, "/", null, true, request.isSecure());

			StringBuilder result = new StringBuilder();
			String separator = "";
			for (String string : (String[]) configurations[0].getProperties().get("oauth.scope")) {
				result.append(separator);
				result.append(string);
				separator = ",";
			}

			String returnURL = configurations[0].getProperties().get("oauth.callBackUrl").toString();

			Configuration b2CProviderConfig = configurationAdmin
					.getConfiguration("com.core.oauth.provider.azureadb2c.config");

			if (!getCurrentUserId((SlingHttpServletRequest) request).equals("anonymous"))
				returnURL = configurations[0].getProperties().get("oauth.callBackUrl").toString()
						.split("/callback/j_security_check")[0] + "/oauth/callback";

			String profileEditURL = "https://" + b2CProviderConfig.getProperties().get("b2cLoginDomain") + "/"
					+ b2CProviderConfig.getProperties().get("b2cTenantName") + ".onmicrosoft.com/"
					+ "/oauth2/v2.0/authorize?p=%s&client_id=%s&response_type=code&redirect_uri=%s&response_mode=query&scope=%s&state=%s";

			return String.format(profileEditURL, b2CProviderConfig.getProperties().get("b2cEditPolicyName"),
					configurations[0].getProperties().get("oauth.client.id").toString(), returnURL, result.toString(),
					state);
		}

		return null;

	}

	public String getCurrentUserId(SlingHttpServletRequest request) {
		ResourceResolver resolver = request.getResourceResolver();
		Session session = resolver.adaptTo(Session.class);
		String userId = session.getUserID();
		return userId;

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