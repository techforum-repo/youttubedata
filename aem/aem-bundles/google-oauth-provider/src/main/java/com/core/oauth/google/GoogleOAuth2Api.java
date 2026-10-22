package com.core.oauth.google;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class GoogleOAuth2Api extends DefaultApi20 {

	private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth?response_type=code&client_id=%s&redirect_uri=%s&scope=%s";

	protected GoogleOAuth2Api() {
	}

	private static class InstanceHolder {
		private static final GoogleOAuth2Api INSTANCE = new GoogleOAuth2Api();
	}

	public static GoogleOAuth2Api instance() {
		return InstanceHolder.INSTANCE;
	}

	
	/**
	 * Returns the URL that receives the access token requests.
	 * 
	 * @return access token URL
	 */
	@Override
	public String getAccessTokenEndpoint() {
		return "https://oauth2.googleapis.com/token";
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
     * @return request method type to get the code. Need to override this because Google uses POST and
     * default is GET.
     */
    @Override
    public Verb getAccessTokenVerb() {
        return Verb.POST;
    }

	/**
	 * {@inheritDoc}
	 */
	public OAuthService createService(OAuthConfig config) {
		return new GoogleOauth2ServiceImpl(this, config);
	}
	
	  /**
     * @return {@link AccessTokenExtractor} to be used to parse access token from Google's response.
     */
    @Override
    public AccessTokenExtractor getAccessTokenExtractor() {
        return new JsonTokenExtractor();
    }

}
