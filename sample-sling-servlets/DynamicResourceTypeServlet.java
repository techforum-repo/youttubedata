package com.sample.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;


//http://localhost:4503/content/wknd/us/en.dynamic.html

@Component(service = Servlet.class)
@Designate(ocd = DynamicResourceTypeServlet.Config.class)
public class DynamicResourceTypeServlet extends SlingSafeMethodsServlet {	
  
	  @ObjectClassDefinition(name = "DynamicResourceTypeServlet", description = "Resource Types to Enable this Servlet")
	  public static @interface Config {
	    @AttributeDefinition(name = "Selectors", description = "Standard Sling servlet property")
	    String[] sling_servlet_selectors() default {"dynamic"};
	    
	    @AttributeDefinition(name = "Resource Types", description = "Standard Sling servlet property")
	    String[] sling_servlet_resourceTypes() default {""};
	    
	    @AttributeDefinition(name = "Methods", description = "Standard Sling servlet property")
	    String[] sling_servlet_methods() default {"GET"};
	    
	    @AttributeDefinition(name = "Extensions", description = "Standard Sling servlet property")
	    String[] sling_servlet_extensions() default {"html"};
	  }


	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.getWriter().println("Inside Dynamic Resource Type Servlet");

	}	


}

