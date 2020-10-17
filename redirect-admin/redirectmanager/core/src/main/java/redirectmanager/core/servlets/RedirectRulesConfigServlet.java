package redirectmanager.core.servlets;

import redirectmanager.core.models.RedirectRule;
import redirectmanager.core.models.RuleResourceModel;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.osgi.service.component.annotations.Component;
import org.apache.sling.api.servlets.HttpConstants;
import javax.servlet.Servlet;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import static redirectmanager.core.utilities.RedirectsRuleUtil.buildRedirectRule;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_OK;

@Component(service = Servlet.class,
			immediate=true,
			property = {
					"sling.servlet.methods=" + HttpConstants.METHOD_GET,
					"sling.servlet.resourceTypes=" + "redirectmanager/components/structure/page",
					"sling.servlet.selectors=" + "301",
					"sling.servlet.selectors=" + "302",
					"sling.servlet.extensions=" + "txt"
			})
public class RedirectRulesConfigServlet extends SlingSafeMethodsServlet {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedirectRulesConfigServlet.class);
	private static final String CONTENT_TYPE = "text/plain";
	private static final String UTF_8 = "UTF-8";
	private static final String TYPE_301 = "301";
	private static final String TYPE_302 = "302";
	private static final String RES_PATH_301 = "redirects-301/rules";
	private static final String RES_PATH_302 = "redirects-302/rules";
	private static final String DISPATCHER_HEADER = "Dispatcher";
	private static final String NO_CACHE = "no-cache";
private static final String NEW_LINE = "\n";

	@Override
	protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
		if (handleInvalidRequest(request, response)) return;
		try {
			response.setHeader(DISPATCHER_HEADER, NO_CACHE);
			writeSuccessResponse(response, getRedirectsConfig(request));
		} catch (Exception e) {
			writeSuccessResponse(response, EMPTY);
		}
	}

	private String getRedirectsConfig(final SlingHttpServletRequest request) {
		final String type = request.getRequestPathInfo().getSelectors()[0];
		final Resource configResource = request.getResource().getChild(type.equals(TYPE_301) ? RES_PATH_301 : RES_PATH_302);
		if (configResource != null) {
			final StringBuilder configBuilder = new StringBuilder();
			configResource.listChildren().forEachRemaining(rule -> writeRuleAsConfig(rule, configBuilder, request));
			return configBuilder.toString();
		}
		return EMPTY;
	}

	private void writeRuleAsConfig(final Resource rule, final StringBuilder configBuilder, final SlingHttpServletRequest request) {
		final RedirectRule redirectRule = buildRedirectRule(rule.adaptTo(RuleResourceModel.class), request);
		if (redirectRule.isValidSource() && redirectRule.isValidTarget()) {
			configBuilder.append(redirectRule.getConfiguration()).append(NEW_LINE);
		}
	}
	
	private boolean handleInvalidRequest(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
		final String[] selectors = request.getRequestPathInfo().getSelectors();
		if (selectors.length != 1 || !(TYPE_301.equals(selectors[0]) || TYPE_302.equals(selectors[0]))) {
			sendError(response, "The request should have either '301' or '302' as selector", SC_BAD_REQUEST);
			return true;
		}
		return false;
	}

	private void writeSuccessResponse(final SlingHttpServletResponse response, final String responseTxt) throws IOException {
		final PrintWriter responseWriter = response.getWriter();
		responseWriter.print(responseTxt);
		response.setContentType(CONTENT_TYPE);
		response.setCharacterEncoding(UTF_8);
		response.setStatus(SC_OK);
		responseWriter.flush();
	}

	private void sendError(final SlingHttpServletResponse response, final String message, final int status) throws IOException {
		LOGGER.debug(message);
		response.sendError(status);
	}
}
