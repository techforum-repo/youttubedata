package com.core.oauth.linkedin;

import java.util.regex.*;

import org.scribe.exceptions.*;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.*;
import org.scribe.utils.*;

public class LinkedinOauth2TokenExtracter implements AccessTokenExtractor {

	private static final String TOKEN_REGEX = "access_token\":\"([^\"]+)";
	private static final String EMPTY_SECRET = "";

	/**
	 * {@inheritDoc}
	 */
	public Token extract(String response) {
		Preconditions.checkEmptyString(response,
				"Response body is incorrect. Can't extract a token from an empty string");

		Matcher matcher = Pattern.compile(TOKEN_REGEX).matcher(response);
		if (matcher.find()) {
			String token = OAuthEncoder.decode(matcher.group(1));
			System.out.println("token: " + token);
			return new Token(token, EMPTY_SECRET, response);
		} else {
			throw new OAuthException("Response body is incorrect. Can't extract a token from this: '" + response + "'",
					null);
		}
	}

}
