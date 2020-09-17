package com.gatedcontent.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;

@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
@Getter

public class GatedResource {
    @Inject
    @Named("jcr:content/isGated")
    private boolean isGated;
    @Inject
    @Named("jcr:content/formEndpoint")
    private String formEndpoint;
    @Inject
    @Named("jcr:content/gateForm")
    private String formName;
    
}