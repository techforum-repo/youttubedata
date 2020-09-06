package renderconditions.core.cutomconditions;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.granite.ui.components.rendercondition.RenderCondition;
import com.adobe.granite.ui.components.rendercondition.SimpleRenderCondition;

import org.osgi.framework.Constants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

@Component(service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Custom Path RenderConditions Servlet",
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.resourceTypes=" + "renderconditions/custom/sample/customparam" })
public class CustomRenderConditions  extends SlingSafeMethodsServlet  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
		
	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {		
	
		ValueMap property = req.getResource().adaptTo(ValueMap.class);
		
				
		req.setAttribute(RenderCondition.class.getName(),new SimpleRenderCondition(Boolean.parseBoolean(property.get("isRenderingEnabed", "false"))));
	}

}
