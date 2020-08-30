package com.contextawareconf.core.servlets;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import configs.SampleCollection;
import configs.SampleConfig;

@Component(immediate = true, service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Get Configurations Data", "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/getConfigurations", "service.ranking=" + 100001 })
public class ConfigurationData extends SlingSafeMethodsServlet {
	
	@Reference
	ConfigurationResolver configurationResolver;

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		Resource contentResourceen = req.getResourceResolver().getResource("/content/contextawareconf/us/en"); 
		SampleConfig configen = contentResourceen.adaptTo(ConfigurationBuilder.class).as(SampleConfig.class);
		
		resp.getWriter().write("===========Configuration from /content/contextawareconf/us/en==============<br/>");
		resp.getWriter().write("configen.param1()"+": "+configen.param1()+"<br/>");
		resp.getWriter().write("configen.intParam()"+": "+configen.intParam()+"<br/>");
		resp.getWriter().write("configen.paramWithDefault()"+": "+configen.paramWithDefault()+"<br/><br/><br/>");
		
	
		Resource contentResourcees = req.getResourceResolver().getResource("/content/contextawareconf/us/es"); 
		SampleConfig configes = contentResourcees.adaptTo(ConfigurationBuilder.class).as(SampleConfig.class);
		
		resp.getWriter().write("===========Configuration from /content/contextawareconf/us/es==============<br/>");
		resp.getWriter().write("configes.param1()"+": "+configes.param1()+"<br/>");
		resp.getWriter().write("configes.intParam()"+": "+configes.intParam()+"<br/>");
		resp.getWriter().write("configes.paramWithDefault()"+": "+configes.paramWithDefault()+"<br/><br/><br/>");
		
		
		Resource contentResourceresolver = req.getResourceResolver().getResource("/content/contextawareconf/us/en");
		
		ConfigurationBuilder cabuilder=configurationResolver.get(contentResourceresolver);		
		SampleConfig configresolver=cabuilder.as(SampleConfig.class);
		
		resp.getWriter().write("===========Configuration through ConfigurationResolver==============<br/>");
		resp.getWriter().write("configresolver.param1()"+": "+configresolver.param1()+"<br/>");
		resp.getWriter().write("configresolver.intParam()"+": "+configresolver.intParam()+"<br/>");
		resp.getWriter().write("configresolver.paramWithDefault()"+": "+configresolver.paramWithDefault()+"<br/><br/><br/>");
		

		Resource contentResourcecollection = req.getResourceResolver().getResource("/content/contextawareconf/us/en"); 
				
		Collection<SampleCollection> configsCollection = contentResourcecollection.adaptTo(ConfigurationBuilder.class).asCollection(SampleCollection.class);
		
		resp.getWriter().write("===========Configuration Collection==============<br/>");		
		
		configsCollection.forEach((SampleCollection data) -> {
			try {
				resp.getWriter().write("configsCollection.param1()"+": "+data.name()+"<br/>");
			    resp.getWriter().write("configresolver.intParam()"+": "+data.pagePath()+"<br/><br/><br/>");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	      });		
		
		
		resp.setContentType("text/html");

	}

}