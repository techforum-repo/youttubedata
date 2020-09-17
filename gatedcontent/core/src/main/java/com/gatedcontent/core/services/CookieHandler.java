package com.gatedcontent.core.services;

import javax.servlet.http.Cookie;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
	    service = CookieHandler.class,
	    name = "CookieHandler",
	    immediate = true	   
	)
public class CookieHandler {
    private static final String ROOT = "/";
    private static final int DEFAULT_TTL = 30 * 24 * 60 * 60;
    private static final Logger LOGGER = LoggerFactory.getLogger(CookieHandler.class);

    public Cookie updateCookie(final SlingHttpServletRequest request, final String formName) {
        Cookie cookie = request.getCookie(formName);
        if (cookie == null) {
            cookie = new Cookie(formName, "submitted");
        }
        
        cookie.setPath(ROOT);
        cookie.setMaxAge(DEFAULT_TTL);
        return cookie;
    }

    public String getCookie(final SlingHttpServletRequest request,String formName) {
        final Cookie formCookie = request.getCookie(formName);
        if (formCookie != null) {
            return formCookie.getValue();
        }
		return null;
    }

   
    

   
}
