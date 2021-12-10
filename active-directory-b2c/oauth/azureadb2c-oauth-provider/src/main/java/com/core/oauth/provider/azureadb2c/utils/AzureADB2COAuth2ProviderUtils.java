package com.core.oauth.provider.azureadb2c.utils;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author albin
 *
 */

public class AzureADB2COAuth2ProviderUtils {

	public static String getOAuthReturntURI(HttpServletRequest request, Object callbackURL, String sufixURL) {
		StringBuilder buf = new StringBuilder();
		buf.append(request.isSecure() ? "https" : "http");
		buf.append("://");
		buf.append(getHostValue(request));
		if (callbackURL != null && !callbackURL.toString().isBlank())
			buf.append(callbackURL);
		if (sufixURL != null && !sufixURL.isBlank())
			buf.append(sufixURL);
		return buf.toString();
	}

	public static String getHostURL(HttpServletRequest request) {
		StringBuilder buf = new StringBuilder();
		buf.append(request.isSecure() ? "https" : "http");
		buf.append("://");
		buf.append(getHostValue(request));
		return buf.toString();

	}

	public static String getHostValue(HttpServletRequest request) {
		String hostValue = request.getHeader("x-forwarded-host");
		if (hostValue == null) {
			String host = request.getHeader("Host");
			if (host == null) {
				String urlS = request.getRequestURL().toString();
				try {
					return (new URI(urlS)).getAuthority();
				} catch (URISyntaxException use) {
					return request.getServerName() + ":" + request.getServerPort();
				}
			}
			return host;
		}
		if (hostValue.indexOf(',') >= 0)
			hostValue = hostValue.substring(hostValue.indexOf(','));
		return hostValue.trim();
	}

}
