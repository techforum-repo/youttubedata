package com.core.oauth.linkedin;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.oauth.OAuthService;

public class LinkedinOAuth2Api extends DefaultApi20 {

	private static final String AUTHORIZATION_URL = "https://www.linkedin.com/oauth/v2/authorization?client_id=%s&redirect_uri=%s&scope=%s&response_type=code";

	protected LinkedinOAuth2Api() {
	}

	private static class InstanceHolder {
		private static final LinkedinOAuth2Api INSTANCE = new LinkedinOAuth2Api();
	}

	public static LinkedinOAuth2Api instance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * Returns the access token extractor.
	 * 
	 * @return access token extractor
	 */
	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new LinkedinOauth2TokenExtracter();
	}

	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	@Override
	public String getAccessTokenEndpoint() {
		return "https://www.linkedin.com/oauth/v2/accessToken";
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

		return String.format(AUTHORIZATION_URL, config.getApiKey(), config.getCallback(), config.getScope());
	}

	/**
	 * {@inheritDoc}
	 */
	public OAuthService createService(OAuthConfig config) {
		return new LinkedinOauth2ServiceImpl(this, config);
	}

}
