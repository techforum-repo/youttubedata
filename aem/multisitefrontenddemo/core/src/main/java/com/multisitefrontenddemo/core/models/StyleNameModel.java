package com.multisitefrontenddemo.core.models;

import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;


@Model(adaptables = { SlingHttpServletRequest.class })
public class StyleNameModel {

	@Inject @Optional
	private String styleName;


	public String getStyleNameSite() {

		return styleName != null ? styleName.concat(".site") : "site1.site";
	}

	public String getStyleNameDependency() {
		return styleName != null ? styleName.concat(".dependencies") : "site1.dependencies";
	}

}
