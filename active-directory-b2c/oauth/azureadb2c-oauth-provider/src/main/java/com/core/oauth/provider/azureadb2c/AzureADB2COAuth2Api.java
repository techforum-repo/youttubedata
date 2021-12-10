package com.core.oauth.provider.azureadb2c;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

import com.core.oauth.provider.azureadb2c.models.AzureADB2CConfig;

/**
 * 
 * @author albin
 *
 */

public class AzureADB2COAuth2Api extends DefaultApi20 {

	AzureADB2CConfig b2CConfig;

	protected AzureADB2COAuth2Api(AzureADB2CConfig b2CConfig) {
		this.b2CConfig = b2CConfig;
	}

	/**
	 * Returns the access token extractor.
	 * 
	 * @return access token extractor
	 */
	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new AzureADB2COauth2TokenExtracter();
	}

	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	@Override
	public String getAccessTokenEndpoint() {
		return "https://" + b2CConfig.getB2CLoginDomain() + "/" + b2CConfig.getB2CTenantName() + ".onmicrosoft.com/"
				+ b2CConfig.getB2CSignInSignUpPolicy() + "/oauth2/v2.0/token";
	}

	/**
	 * Returns the URL where you should redirect your users to authenticate your
	 * application.
	 *
	 * @param config OAuth 2.0 configuration param object
	 * @return the URL where you should redirect your users
	 */
	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		String endpoint = "https://" + b2CConfig.getB2CLoginDomain() + "/" + b2CConfig.getB2CTenantName()
				+ ".onmicrosoft.com/" + b2CConfig.getB2CSignInSignUpPolicy()
				+ "/oauth2/v2.0/authorize?p=%s&response_mode=query&client_id=%s&redirect_uri=%s&scope=%s&response_type=code&promt=login";

		return String.format(endpoint, b2CConfig.getB2CSignInSignUpPolicy(), config.getApiKey(), config.getCallback(),
				config.getScope());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OAuthService createService(OAuthConfig config) {
		return new AzureADB2COauth2ServiceImpl(this, config, b2CConfig);
	}

}
