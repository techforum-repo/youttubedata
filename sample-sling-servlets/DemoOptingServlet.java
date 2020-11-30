package com.sample.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.OptingServlet;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;

//http://localhost:4503/content/wknd/us/en/faqs.opting.html

@Component(service = Servlet.class, 
property = { ServletResolverConstants.SLING_SERVLET_NAME + "=" + "Demo Opting Servlet",
		     ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		     ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES + "=" + "wknd/components/page",
		     ServletResolverConstants.SLING_SERVLET_SELECTORS + "=" + "opting",
		     ServletResolverConstants.SLING_SERVLET_EXTENSIONS + "=" + "html",})
@ServiceDescription("DemoOptingServlet")
public class DemoOptingServlet extends SlingSafeMethodsServlet  implements OptingServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.getWriter().println("Inside DemoOptingServlet");

	}

	@Override
	public boolean accepts(SlingHttpServletRequest request) {
		if(request.getPathInfo().startsWith("/content/wknd/us/en/faqs"))
		{
			return true;
		}
		return false;
	}	


}
