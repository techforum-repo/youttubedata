package com.core.oauth.provider.azureadb2c;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.auth.core.spi.AuthenticationHandler;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;

import com.core.oauth.provider.azureadb2c.utils.AzureADB2COAuth2ProviderUtils;

/**
 * 
 * @author albin
 *
 */

@Component(name = "Azure AD B2C - OAuth Logout Handler", service = AuthenticationHandler.class, property = {
		AuthenticationHandler.PATH_PROPERTY + "=" + "/", })
@ServiceRanking(-5001)
@ServiceDescription("Azure AD B2C - OAuth Logout Handler")
public class AzureADB2COAuthLogoutHandler implements AuthenticationHandler {

	@Reference
	private ConfigurationAdmin configurationAdmin;

	@Override
	public AuthenticationInfo extractCredentials(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}

	@Override
	public boolean requestCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return false;
	}

	@Override
	public void dropCredentials(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String operation = request.getParameter("operation");

		if (operation != null && operation.equals("b2clogout")) {

			Configuration b2CProviderConfig = configurationAdmin
					.getConfiguration("com.core.oauth.provider.azureadb2c.config");
			
			if (b2CProviderConfig != null) {
				response.sendRedirect("https://" + b2CProviderConfig.getProperties().get("b2cLoginDomain") + "/"
						+ b2CProviderConfig.getProperties().get("b2cTenantName") + ".onmicrosoft.com/"
						+ b2CProviderConfig.getProperties().get("b2cSignInSignUpPolicyName")
						+ "/oauth2/v2.0/logout?post_logout_redirect_uri="
						+ AzureADB2COAuth2ProviderUtils.getHostURL(request));
			}

		}
		
	}

}