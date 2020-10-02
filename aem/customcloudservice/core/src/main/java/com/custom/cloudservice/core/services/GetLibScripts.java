package com.custom.cloudservice.core.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.wcm.webservicesupport.Service;

@Component(name = "Script Service", service = GetLibScripts.class, immediate = true)
public class GetLibScripts {

	@Reference
	private ResourceResolverFactory resolverFactory;

	List<String> libsScripts = null;
	ResourceResolver resolver = null;

	public List<String> getScripts(Iterator<Service> services, String type) {

		libsScripts = new ArrayList<String>();
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(ResourceResolverFactory.SUBSERVICE, "script-service");
			resolver = resolverFactory.getServiceResourceResolver(param);

			while (services.hasNext()) {
				Service service = services.next();
				String defaultScript = null;
				if (type.equals("head"))
					defaultScript = getHeadScript(resolver, service.getComponentReference());
				else
					defaultScript = getFootScript(resolver, service.getComponentReference());

				if (defaultScript != null) {
					libsScripts.add(defaultScript);
				}

			}
		} catch (Exception e) {

		} finally {
			if (resolver != null && resolver.isLive()) {
				resolver.close();
			}
		}

		return libsScripts;
	}

	protected String getHeadScript(ResourceResolver resolver, String componentPath) {
		if (componentPath != null) {
			Resource compRes = resolver.getResource(componentPath);
			Iterator<Resource> it = resolver.listChildren(compRes);
			while (it.hasNext()) {
				Resource script = it.next();
				if ("headScript.jsp".equalsIgnoreCase(script.getName())) {
					return script.getPath();
				}
			}
		}
		return null;
	}

	protected String getFootScript(ResourceResolver resolver, String componentPath) {
		if (componentPath != null) {
			Resource compRes = resolver.getResource(componentPath);
			Iterator<Resource> it = resolver.listChildren(compRes);
			while (it.hasNext()) {
				Resource script = it.next();
				if ("footScript.jsp".equalsIgnoreCase(script.getName())) {
					return script.getPath();
				}
			}
		}
		return null;
	}

}
