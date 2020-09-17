package com.gatedcontent.core.services;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gatedcontent.core.models.GatedFilterInfo;
import com.gatedcontent.core.models.GatedResource;
import com.gatedcontent.core.utils.Helpers;
import static org.apache.commons.lang3.StringUtils.*;

@Component(
	    service = GatedContentService.class,
	    name = "GatedContentService",
	    immediate = true	   
	)

public class GatedContentService {

	private static final String GATED_URL_PARAM = "gatedURL";
	private static final Logger LOGGER = LoggerFactory.getLogger(GatedContentService.class);

   
    @Reference
    private CookieHandler cookieHandler;

    public GatedFilterInfo getGatedFilterInfo(final SlingHttpServletRequest request) {
        final Resource resource = request.getResource();
        final GatedResource gatedResource = getGatedResource(resource);

        if (validatedGatedContentRequest(request, gatedResource)) {
            if (!formSubmittedAlready(gatedResource.getFormName(), request)) {
                String externalizedFormPath = gatedResource != null ? Helpers.getRelativeLink(gatedResource.getFormEndpoint(), request) : EMPTY;
                return GatedFilterInfo.builder().gate(true)
                        .redirectPath(getRedirectPath(request, externalizedFormPath))
                        .gatedURI(request.getRequestURI())
                        .gatedResource(gatedResource)
                        .build();
            }
        }

        return GatedFilterInfo.builder().gate(false).gatedResource(gatedResource).build();
    }

    private String getRedirectPath(SlingHttpServletRequest request, String externalizedFormPath) {
        final String redirectPath = Helpers.addParameterToUri(externalizedFormPath, GATED_URL_PARAM, Helpers.encodeResourcePath(request.getResource()));
        return redirectPath;
    }


    private boolean validatedGatedContentRequest(final SlingHttpServletRequest request, final GatedResource gatedResource) {
        return gatedResource != null && gatedResource.isGated()
                && isNoneBlank(gatedResource.getFormEndpoint(), gatedResource.getFormName());
    }

    private GatedResource getGatedResource(final Resource resource) {
            return resource.adaptTo(GatedResource.class);
                    
    }

  
    private boolean formSubmittedAlready(final String formName, final SlingHttpServletRequest request) {
        return cookieHandler.getCookie(request, formName)!=null;
    }

    
   
}
