package com.core.oauth.provider.azureadb2c;

import org.apache.sling.commons.json.JSONObject;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.core.oauth.provider.azureadb2c.models.AzureADB2CConfig;

@SuppressWarnings("deprecation")
public class AzureADB2COauth2ServiceImpl implements OAuthService {
	private static final String VERSION = "2.0";

	private final DefaultApi20 api;
	private final OAuthConfig config;
	private final AzureADB2CConfig b2CConfig;

	private final Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * Default constructor
	 * 
	 * @param api    OAuth2.0 api information
	 * @param config OAuth 2.0 configuration param object
	 */
	public AzureADB2COauth2ServiceImpl(DefaultApi20 api, OAuthConfig config, AzureADB2CConfig b2CConfig) {
		this.api = api;
		this.config = config;
		this.b2CConfig = b2CConfig;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		Response response = getOAuthRequest(verifier, this.b2CConfig.getB2CSignInSignUpPolicy()).send();
		try {

			JSONObject tokenResponse = new JSONObject(response.getBody());
			if (tokenResponse.getString("error") != null && tokenResponse.getString("error").equals("invalid_grant")) {
				response = getOAuthRequest(verifier, this.b2CConfig.getB2CEditPolicy()).send();
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return api.getAccessTokenExtractor().extract(response.getBody());
	}

	OAuthRequest getOAuthRequest(Verifier verifier, String policyName) {

		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		request.addQuerystringParameter("grant_type", "authorization_code");

		request.addQuerystringParameter("p", policyName);

		if (config.hasScope())
			request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());

		return request;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token getRequestToken() {
		throw new UnsupportedOperationException(
				"Unsupported operation, please use 'getAuthorizationUrl' and redirect your users there");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getVersion() {
		return VERSION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void signRequest(Token accessToken, OAuthRequest request) {
		request.addHeader("Authorization", "Bearer " + accessToken.getToken());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthorizationUrl(Token requestToken) {
		return api.getAuthorizationUrl(config);
	}
}
