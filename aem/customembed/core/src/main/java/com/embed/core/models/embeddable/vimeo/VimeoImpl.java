package com.embed.core.models.embeddable.vimeo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.day.cq.wcm.api.designer.Style;
import com.day.cq.wcm.api.policies.ContentPolicy;
import com.day.cq.wcm.api.policies.ContentPolicyManager;
import com.day.cq.wcm.commons.policy.ContentPolicyStyle;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = { Vimeo.class },
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
public class VimeoImpl implements Vimeo {

    /**
     * URL pattern is defined in https://developers.google.com/youtube/player_parameters
     */
    private static final String BASE_EMBED_URL = "https://player.vimeo.com/video/%s";
    private static final String PARAMETER_MUTE = "muted"; // not documented but still works
    private static final String PARAMETER_LOOP = "loop";
    private static final String PARAMETER_AUTOPLAY = "autoplay";
    private static final String PARAMETER_PLAYS_INLINE = "playsinline";

    /**
     * Standard logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(VimeoImpl.class);

    @ValueMapValue(name = PN_VIDEO_ID)
    @Nullable
    private String videoId;

    @ValueMapValue(name = PN_WIDTH)
    @Nullable
    private String iFrameWidth;

    @ValueMapValue(name = PN_HEIGHT)
    @Nullable
    private String iFrameHeight;

    @ValueMapValue(name = PN_MUTE)
    @Nullable
    private Boolean isMute;

    @ValueMapValue(name = PN_AUTOPLAY)
    @Nullable
    private Boolean isAutoPlay;

    @ValueMapValue(name = PN_LOOP)
    @Nullable
    private Boolean isLoop;

    @ValueMapValue(name = PN_PLAYS_INLINE)
    @Nullable
    private Boolean isPlaysInline;

    @ScriptVariable(injectionStrategy = InjectionStrategy.REQUIRED)
    private Page currentPage;

    @ScriptVariable(injectionStrategy = InjectionStrategy.REQUIRED)
    private ComponentContext componentContext;

    @Self
    private SlingHttpServletRequest request;

    @Override
    public boolean isEmpty() {
        return StringUtils.isBlank(videoId);
    }

    @Override
	public @Nullable String getIFrameWidth() {
    	LOGGER.debug("inside getIFrameWidth: ");
        return iFrameWidth;
    }

    @Override
	public @Nullable String getIFrameHeight() {
        return iFrameHeight;
    }

	private static @Nullable Resource getWrappedResource(Resource resource) {
        if (resource instanceof ResourceWrapper) {
            return ResourceWrapper.class.cast(resource).getResource();
        } else {
            return null;
        }
    }

    /**
     * Relying on the injected currentStyle does not work, as this uses the wrong resource type "youtube" instead of "embed".
     * This helper method rebuilds the logic from "com.day.cq.wcm.scripting.impl.WcmBindingsValesProvider" but uses the wrapped resource.
     * @return the style for the  wrapped resource
     */
     Style getStyleForWrappedResource(Resource resource) {
        ContentPolicyManager policyManager = resource.getResourceResolver().adaptTo(ContentPolicyManager.class);
        if (policyManager != null) {
            Resource wrappedResource = getWrappedResource(resource);
            if (wrappedResource != null) {
                ContentPolicy currentPolicy = policyManager.getPolicy(wrappedResource, request);
                if (currentPolicy != null) {
                    return new ContentPolicyStyle(currentPolicy, componentContext.getCell());
                } else {
                    LOGGER.debug("No policy found for wrapped resource {}", wrappedResource);
                }
            } else {
                LOGGER.debug("The current resource is not a wrapped resource: {}", resource);
            }
        } else {
            LOGGER.debug("Could not get ContentPolicyManager from resource resolver. Probably service not running!");
        }
        return null;
    }

    @Override
	public @Nullable String getIFrameSrc() throws URISyntaxException {
    	System.out.println("inside: ");
        Optional<URI> uri = getIFrameSrc(getStyleForWrappedResource(request.getResource()));
        if (!uri.isPresent()) {
            return null;
        } else {
            return uri.get().toString();
        }
    }
   
    Optional<URI> getIFrameSrc(Style currentStyle) throws URISyntaxException {
        if (isEmpty()) {
            return Optional.empty();
        }
        URIBuilder uriBuilder = new URIBuilder(String.format(BASE_EMBED_URL, videoId));
        
        System.out.println("uriBuilder: "+uriBuilder.toString());

        // optional parameters
        if (currentStyle != null) {
            if (Boolean.TRUE.equals(currentStyle.get(PN_DESIGN_MUTE_ENABLED, false))) {
                addVimeoBooleanUriParameter(uriBuilder, PARAMETER_MUTE, isMute, currentStyle, PN_DESIGN_MUTE_DEFAULT_VALUE);
            }
            if (Boolean.TRUE.equals(currentStyle.get(PN_DESIGN_AUTOPLAY_ENABLED, false))) {
                // going via WCMMode to determine if one is on publish does not work as this resource is included explicitly with wcmmode=disabled
                // therefore use edit context of parent component
                if (componentContext.getParent().getEditContext() == null) {
                    addVimeoBooleanUriParameter(uriBuilder, PARAMETER_AUTOPLAY, isAutoPlay, currentStyle, PN_DESIGN_AUTOPLAY_DEFAULT_VALUE);
                } else {
                    LOGGER.debug("Autoplay disabled because WCMMode is not disabled");
                }
            }
            if (Boolean.TRUE.equals(currentStyle.get(PN_DESIGN_LOOP_ENABLED, false))) {
                addVimeoBooleanUriParameter(uriBuilder, PARAMETER_LOOP, isLoop, currentStyle, PN_DESIGN_LOOP_DEFAULT_VALUE);                   
                
            }
         
            if (Boolean.TRUE.equals(currentStyle.get(PN_DESIGN_PLAYS_INLINE_ENABLED, false))) {
                addVimeoBooleanUriParameter(uriBuilder, PARAMETER_PLAYS_INLINE, isPlaysInline, currentStyle, PN_DESIGN_PLAYS_INLINE_DEFAULT_VALUE);
            }
        } else {
            LOGGER.debug("No style available, optional YouTube parameters are not used!");
        }
        return Optional.of(uriBuilder.build());
    }

    boolean getBooleanValueOrDefaultFromStyle(Boolean value, Style style, String stylePropertyName) {
        if (value == null) {
            return style.get(stylePropertyName, false);
        } else {
            return value.booleanValue();
        }
    }

    boolean addVimeoBooleanUriParameter(URIBuilder uriBuilder, String parameterName, Boolean value, Style style, String stylePropertyName) {
        boolean effectiveValue = getBooleanValueOrDefaultFromStyle(value, style, stylePropertyName);
        uriBuilder.addParameter(parameterName, effectiveValue ? "1" : "0");
        return effectiveValue;
    }
}
