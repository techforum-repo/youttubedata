package com.gatedcontent.core.filters;

import com.day.cq.wcm.api.WCMMode;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.gatedcontent.core.constants.Constants.*;

import javax.servlet.*;
import java.io.IOException;

import static com.day.cq.wcm.api.WCMMode.DISABLED;
import static com.day.cq.wcm.api.WCMMode.fromRequest;
import static org.apache.http.HttpStatus.SC_MOVED_TEMPORARILY;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.apache.sling.engine.EngineConstants;
import com.gatedcontent.core.models.GatedFilterInfo;
import com.gatedcontent.core.models.GatedResource;
import com.gatedcontent.core.services.GatedContentService;


@Component(service = Filter.class, name = "GatedContentFilter", property = {
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_REQUEST, 
		EngineConstants.SLING_FILTER_SCOPE + "=" + EngineConstants.FILTER_SCOPE_FORWARD,
		Constants.SERVICE_RANKING + ":Integer=0"})
public class GatedContentFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(GatedContentFilter.class);

    @Reference
    private GatedContentService gatedContentService;

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
                         final FilterChain filterChain) throws IOException, ServletException {
        final SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) servletRequest;
        final SlingHttpServletResponse slingResponse = (SlingHttpServletResponse) servletResponse;
        final  GatedFilterInfo filterInfo = gatedContentService.getGatedFilterInfo(slingRequest);
        final WCMMode mode = fromRequest(servletRequest);
        
        System.out.println("mode: "+mode);

        setHeaderToSkipDispatcherCache(filterInfo.getGatedResource(), slingResponse);

        if (filterInfo.isGate() && DISABLED.equals(mode)) {
            redirectToGate(slingResponse, filterInfo);            
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void redirectToGate(final SlingHttpServletResponse response, final GatedFilterInfo gatedContentInfo) throws IOException {
        setCacheControlHeadersFor302(response);
        response.setStatus(SC_MOVED_TEMPORARILY);
        response.sendRedirect(gatedContentInfo.getRedirectPath());
    }

    private void setCacheControlHeadersFor302(final SlingHttpServletResponse redirectResponse) {
        redirectResponse.setHeader(CACHE_CONTROL, NO_CACHE_NO_STORE_REVALIDATE);
        redirectResponse.setHeader(PRAGMA, NO_CACHE);
        redirectResponse.setDateHeader(EXPIRES, 0);
    }

    private void setHeaderToSkipDispatcherCache(final GatedResource gatedResource, final SlingHttpServletResponse response) {
        if (gatedResource != null && gatedResource.isGated()) {
            response.setHeader(DISPATCHER_CACHE_KEY, NO_CACHE);
        }
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
