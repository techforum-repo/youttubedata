package com.multisitefrontenddemo.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;

import com.day.cq.commons.inherit.InheritanceValueMap;

@Model(adaptables = { SlingHttpServletRequest.class })
public class StyleNameModel {

	@Inject
	private InheritanceValueMap pageProperties;

	Object styleName;

	@PostConstruct
	protected void init() {
		styleName = pageProperties.get("styleName");
	}

	public String getStyleNameSite() {

		return styleName != null ? styleName.toString().concat(".site") : "site1.site";
	}

	public String getStyleNameDependency() {
		return styleName != null ? styleName.toString().concat(".dependencies") : "site1.dependencies";
	}

}
