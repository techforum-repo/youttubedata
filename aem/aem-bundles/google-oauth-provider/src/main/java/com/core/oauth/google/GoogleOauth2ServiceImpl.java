package com.core.oauth.google;

import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;

public class GoogleOauth2ServiceImpl extends OAuth20ServiceImpl  {

	private final GoogleOAuth2Api api;
	private final OAuthConfig config;

	/**
	 * Default constructor
	 * 
	 * @param api    OAuth2.0 api information
	 * @param config OAuth 2.0 configuration param object
	 */
	public GoogleOauth2ServiceImpl(GoogleOAuth2Api api, OAuthConfig config) {
		super(api,config);
		this.api = api;
		this.config = config;
	}

	/**
	 * {@inheritDoc}
	 */
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		request.addQuerystringParameter("grant_type", "authorization_code");

		if (config.hasScope())
			request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
		Response response = request.send();
		return api.getAccessTokenExtractor().extract(response.getBody());
	}
	
}
