package com.gatedcontent.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static org.apache.commons.lang3.StringUtils.*;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.Externalizer;
import com.gatedcontent.core.constants.Constants;

import static java.net.URLEncoder.encode;

public class Helpers {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Helpers.class);
	
	private static final String REFERER="referer";

	public static String encodeResourcePath(final Resource resource) {
		if (resource != null) {
			final String path = resource.getPath();
			try {
				return encode(path, Constants.UTF_8);
			} catch (UnsupportedEncodingException e) {

			}
		}
		return EMPTY;
	}

	public static String addParameterToUri(final String uri, final String parameterKey, final String parameterValue) 
	{
		final URIBuilder builder = getUriBuilder(uri);
		if (builder != null && isNoneBlank(parameterKey, parameterValue)) {
				builder.addParameter(parameterKey, parameterValue);
				return builder.toString();
		} else {
			return uri;
		}
	}
	
	public static String getRelativeLink(final String path, final SlingHttpServletRequest slingRequest) {
		return slingRequest.getResourceResolver().adaptTo(Externalizer.class).relativeLink(slingRequest, path+".html");
	}
	
	private static URIBuilder getUriBuilder(final String uri) {
		try {
				return isNotBlank(uri) ? new URIBuilder(uri) : null;
		} catch (URISyntaxException e) {
				return null;
		}
	 }
	
	
	  public static String getQueryStringParam(String param, SlingHttpServletRequest request) throws Exception {
		  URIBuilder builder=new URIBuilder(getRefererURL(request));		  
		  if (builder != null && builder.getQueryParams()!=null) {
			  NameValuePair nvp= builder.getQueryParams().stream().filter(v-> v.getName().equals(param)).findFirst().orElse(null);
			  return nvp!=null ? nvp.getValue():EMPTY;
		  }
		  return EMPTY;
	   
	  }
	 
	
	public static String getRefererURL(final SlingHttpServletRequest slingRequest) {
		if (slingRequest != null) {
			String referer = slingRequest.getHeader(REFERER);
			if (isNotBlank(referer)) {
				return referer;
			}
		}
		return EMPTY;
	}

}
