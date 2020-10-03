package aem.groovy.samples

import com.day.cq.wcm.api.Page
import com.day.cq.wcm.api.PageManager
import org.apache.sling.api.resource.ResourceResolver
import org.apache.sling.api.resource.ResourceResolverFactory

Map<String, Object> param = new HashMap<String, Object>();
param.put(ResourceResolverFactory.SUBSERVICE, "script-service");


ResourceResolverFactory resolverFactory=osgi.getService(ResourceResolverFactory.class)
resolver = resolverFactory.getServiceResourceResolver(param);

PageManager pageManager = resolver.adaptTo(PageManager.class);
Page currentPage = pageManager.getPage("/content/we-retail/us/en");  

out.println("Hello from " + currentPage.getTitle());