package demos.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.osgi.framework.Constants.SERVICE_RANKING;
import org.osgi.service.component.annotations.Component;
import org.osgi.framework.Constants;


@Component(service=Servlet.class,
property={

        Constants.SERVICE_DESCRIPTION + "=Page servlet to combine and deliver global and site local robots txt config",
        "sling.servlet.methods=" + "GET",
        "sling.servlet.resourceTypes="+ "weretail/components/structure/robotsTxt",
        "sling.servlet.extensions="+"txt",
        SERVICE_RANKING+"=99990"
   })


public class RobotsTextServlet extends SlingSafeMethodsServlet {
 	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RobotsTextServlet.class);
    private static final String GLOBAL_ROBOTS_PAGE = "/content/we-retail/robots";
    private static final String ROBOTS_TXT = "robotsTxt";
    private static final String CQ_PAGE = "cq:Page";
    private static final String CQ_TEMPLATE = "cq:template";
    private static final String CONTENT_TYPE = "text/plain";
    private static final String UTF_8 = "UTF-8";
    private static final String ROBOTS_TEMPLATE = "/conf/we-retail/settings/wcm/templates/robots-txt-template";

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws ServletException, IOException {
        final Page robotsTxtConfigPage = getRobotsPage(request);
        if (robotsTxtConfigPage != null) {
            final PrintWriter responseWriter = response.getWriter();
            final String responseTxt = getRobotsTextConfig(robotsTxtConfigPage, request.getResourceResolver());
            if (isNotBlank(responseTxt)) {
                writeSuccessResponse(response, responseWriter, responseTxt);
                return;
            } else {
                sendError(response, "robots txt empty in global and local robots page configuration");
            }
        } else {
            sendError(response, "robots txt only returned for robotsTxt template pages. Returning a 404");
        }
    }

    private Page getRobotsPage(final SlingHttpServletRequest request) {
        final Resource requestResource = request.getResource();
        return request.getResourceResolver().adaptTo(PageManager.class).getContainingPage(requestResource);
    }

    private void writeSuccessResponse(final SlingHttpServletResponse response, final PrintWriter responseWriter,
                                      final String responseTxt) {
        responseWriter.print(responseTxt);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding(UTF_8);
        response.setStatus(SC_OK);
        responseWriter.flush();
    }

    private void sendError(final SlingHttpServletResponse response, final String message) throws IOException {
        LOGGER.debug(message);
        response.sendError(SC_NOT_FOUND);
    }

    private String getRobotsTextConfig(final Page page, final ResourceResolver resourceResolver) {
        final StringBuilder combinedRobotsTxtConfig = new StringBuilder();
        final String globalRobotsTxt = getGlobalRobotsTxtConfig(resourceResolver);
        final String localRobotsTxt = getRobotsTxtConfigFromPage(page);
        if (isNotBlank(globalRobotsTxt) && !page.getPath().equals(GLOBAL_ROBOTS_PAGE)) {
            combinedRobotsTxtConfig.append(globalRobotsTxt).append(System.lineSeparator()).append(System.lineSeparator());
        }
        if (isNotBlank(localRobotsTxt)) {
            combinedRobotsTxtConfig.append(localRobotsTxt);
        }
        return combinedRobotsTxtConfig.toString();
    }

    private String getGlobalRobotsTxtConfig(final ResourceResolver resourceResolver) {
        final Resource globalRobotsPageResource = resourceResolver.getResource(GLOBAL_ROBOTS_PAGE);
        if (globalRobotsPageResource != null && StringUtils.equals(CQ_PAGE, globalRobotsPageResource.getResourceType())) {
            final Page globalRobotsPage = globalRobotsPageResource.adaptTo(Page.class);
            return getRobotsTxtConfigFromPage(globalRobotsPage);
        } else {
            LOGGER.warn("Global Robots Page not found at {} or is not a cq:Page", GLOBAL_ROBOTS_PAGE);
        }
        return EMPTY;
    }

    private String getRobotsTxtConfigFromPage(final Page robotsTxtPage) {
        if (robotsTxtPage.getProperties().get(CQ_TEMPLATE, EMPTY).equals(ROBOTS_TEMPLATE)) {
            final Resource pageContent = robotsTxtPage.getContentResource();
            if (pageContent != null) {
                final ValueMap robotsProperties = pageContent.adaptTo(ValueMap.class);
                return robotsProperties.get(ROBOTS_TXT, String.class);
            } else {
                LOGGER.warn("Global Robots Page: {} does not robots.txt configuration", GLOBAL_ROBOTS_PAGE);
            }
        } else {
            LOGGER.debug("Template type not supported for robots text servlet");
        }
        return EMPTY;
    }
}