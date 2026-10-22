package com.cm.notofication.core.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.cm.notofication.core.utils.CMNotificationUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component(service = Servlet.class, property = {
		ServletResolverConstants.SLING_SERVLET_NAME + "=" + "CM Notification Servlet",
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET,
		ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_POST,
		ServletResolverConstants.SLING_SERVLET_PATHS + "=" + "/bin/cmnotification" })

@Designate(ocd = CMNotificationServlet.Config.class)
public class CMNotificationServlet extends SlingAllMethodsServlet {

	private CMNotificationServlet.Config config;

	@ObjectClassDefinition(name = "CMNotificationServlet", description = "Configuration Details to Send CM Notification")
	public @interface Config {
		@AttributeDefinition(name = "ORGANIZATION_ID", description = "ORGANIZATION_ID")
		public String organization_id() default "xxxxx@AdobeOrg";

		@AttributeDefinition(name = "TECHNICAL_ACCOUNT_EMAIL", description = "TECHNICAL_ACCOUNT_EMAIL")
		public String technical_account_email() default "xxxxx@techacct.adobe.com";

		@AttributeDefinition(name = "TECHNICAL_ACCOUNT_ID", description = "TECHNICAL_ACCOUNT_ID")
		public String technical_account_id() default "xxxxxx@techacct.adobe.com";

		@AttributeDefinition(name = "API_KEY", description = "API_KEY")
		public String api_key() default "xxxxxxxxxxxx";

		@AttributeDefinition(name = "PRIVATE_KEY_PATH", description = "PRIVATE_KEY_PATH")
		public String private_key_path() default "/META-INF/keys/private.key";

		@AttributeDefinition(name = "CLIENT_SECRET", description = "CLIENT_SECRET")
		public String client_secret() default "xxxxxxxxxxxxx";

		@AttributeDefinition(name = "TEAMS_WEBHOOK", description = "TEAMS_WEBHOOK")
		String teams_webhook() default "https://outlook.office.com/webhook/xxxxxxxxxxxxxxxx";

		@AttributeDefinition(name = "AUTH_SERVER", description = "AUTH_SERVER")
		public String auth_server() default "ims-na1.adobelogin.com";

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter writer = resp.getWriter();

		String challenge = "";
		try {
			challenge = CMNotificationUtil.getParamValue(req.getRequestURI() + "?" + req.getQueryString(), "challenge");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!challenge.equals("")) {
			writer.print(challenge);
		} else {
			resp.sendError(400);
		}

	}
	
    /**
     * Receive Adobe IO Events
     */
	@Override
	protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("application/text");
		PrintWriter printWriter = resp.getWriter();
		printWriter.print("Request Received");

		String STARTED = "https://ns.adobe.com/experience/cloudmanager/event/started";
		String EXECUTION = "https://ns.adobe.com/experience/cloudmanager/pipeline-execution";

		try {
			
			String requestData = req.getReader().lines().collect(Collectors.joining());
			CMNotificationUtil.verifySignature(requestData,req.getHeader("x-adobe-signature"),config);			

			JsonElement jelement = new JsonParser().parse(requestData);
			JsonObject jobject = jelement.getAsJsonObject();

			String eventType = jobject.get("event").getAsJsonObject().get("@type").getAsString();
			String executionType = jobject.get("event").getAsJsonObject().get("xdmEventEnvelope:objectType")
					.getAsString();

			if (STARTED.equals(eventType) && EXECUTION.equals(executionType)) {

				String executionUrl = jobject.get("event").getAsJsonObject().get("activitystreams:object")
						.getAsJsonObject().get("@id").getAsString();

				String executionResponse = CMNotificationUtil.makeApiCall(CMNotificationUtil.getAccessToken(config),
						executionUrl, config);

				JsonElement jelementer = new JsonParser().parse(executionResponse);
				JsonObject jobjecter = jelementer.getAsJsonObject();
				
				

				String pipelineurl = jobjecter.get("_links").getAsJsonObject()
						.get("http://ns.adobe.com/adobecloud/rel/pipeline").getAsJsonObject().get("href").getAsString();

				URI uri = new URL(executionUrl).toURI();

				String pipelineResponse = CMNotificationUtil.makeApiCall(CMNotificationUtil.getAccessToken(config),
						uri.resolve(pipelineurl).toURL().toString(), config);

				JsonElement jelementpipeline = new JsonParser().parse(pipelineResponse);
				JsonObject jobjectpipeline = jelementpipeline.getAsJsonObject();
				
				
				//Get additional data- Environment, pipeline, step, logs, metrics etc
				//based on the URL data available in the fetched JSOn data.
				

				CMNotificationUtil.notifyTeams(jobjectpipeline.get("name").getAsString()+ " Pipeline has been started", config);

			}

		} catch (Exception e) {
			e.printStackTrace();
			resp.sendError(500);
		}

	}

	@Activate
	@Modified
	public void activate(CMNotificationServlet.Config config) {
		this.config = config;
	}

}
