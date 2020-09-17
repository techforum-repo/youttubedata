package com.gatedcontent.core.services;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gatedcontent.core.constants.Constants;
import com.gatedcontent.core.utils.Helpers;

import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.apache.commons.lang3.StringUtils.*;
import static java.net.URLDecoder.decode;

@Component(service = GatedFormDataProcessor.class, name = "GatedFormResponseProcessor", immediate = true)
public class GatedFormDataProcessor {

	private final Logger LOGGER = LoggerFactory.getLogger(GatedFormDataProcessor.class);
	private static final String FORM_NAME = "formName";
	private static final String GATED_URL_PARAM = "gatedURL";
	private static final String FORM_ENDPOINT = "formEndpoint";
	

	@Reference
	private CookieHandler cookieHandler;

	public void runGatedRedirectAction(SlingHttpServletRequest request, SlingHttpServletResponse response) {

		// Process the form data
		
		try {
			addOrUpdateCookieInResponse(request, response);
			String redirectURL = getDecodedGatedContentUrl(request);
			if(isBlank(redirectURL))
			{
				redirectURL=Helpers.getRelativeLink(request.getResource().getValueMap().get(FORM_ENDPOINT, String.class),request);
			}
			response.setStatus(SC_MOVED_TEMPORARILY);

			response.sendRedirect(redirectURL);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			render("Error Processing the Form "+e.getMessage(),response);

		}
	}

	private void addOrUpdateCookieInResponse(SlingHttpServletRequest request, SlingHttpServletResponse response) {
		final String formName = request.getResource().getValueMap().get(FORM_NAME, String.class);
		if (isNotBlank(formName)) {
			response.addCookie(cookieHandler.updateCookie(request, formName));
		} else {
			LOGGER.error("Empty or null formName at path {}", request.getResource().getPath());
		}

	}

	private String getDecodedGatedContentUrl(final SlingHttpServletRequest request) throws Exception {
		
		String gatedURLParam=Helpers.getQueryStringParam(GATED_URL_PARAM,request);
		if(isNoneBlank(gatedURLParam))
			return Helpers.getRelativeLink(decode(gatedURLParam, Constants.UTF_8), request);
		return EMPTY;
	}

	private void render(String message,SlingHttpServletResponse response) {
		response.setContentType(ContentType.DEFAULT_TEXT.toString());
		response.setHeader(Constants.DISPATCHER_CACHE_KEY, Constants.NO_CACHE);
		response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		try {
			response.getWriter().write(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
