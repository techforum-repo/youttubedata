package com.core.oauth.provider.azureadb2c;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.Token;
import org.scribe.utils.OAuthEncoder;
import org.scribe.utils.Preconditions;

/**
 * 
 * @author albin
 *
 */

public class AzureADB2COauth2TokenExtracter implements AccessTokenExtractor {

	private static final String TOKEN_REGEX = "access_token\":\"([^\"]+)";
	private static final String EMPTY_SECRET = "";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Token extract(String response) {
		Preconditions.checkEmptyString(response,
				"Response body is incorrect. Can't extract a token from an empty string");

		Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
		if (matcher.find()) {
			String token = OAuthEncoder.decode(matcher.group(1));
			return new Token(token, EMPTY_SECRET, response);
		} else {
			throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'",
					null);
		}
	}

}
