package aem.groovy.samples

import com.day.cq.replication.ReplicationOptions
import com.day.cq.search.QueryBuilder
import com.day.cq.wcm.api.PageManager
import org.apache.sling.api.SlingHttpServletRequest
import org.apache.sling.api.SlingHttpServletResponse
import org.apache.sling.api.resource.ResourceResolver
import org.osgi.framework.BundleContext
import org.slf4j.Logger

import javax.jcr.RepositoryException
import javax.jcr.Session

class Template
{

	public static Logger log
	public static Session session
	public static SlingHttpServletRequest slingRequest
	public static SlingHttpServletResponse slingResponse
	public static PageManager pageManager
	public static ResourceResolver resourceResolver
	public static QueryBuilder queryBuilder
	public static BundleContext bundleContext
	public static StringWriter out
	
	public static def getPage(String path) {}
	
	public static def getNode(String path) {}
	
	public static def getResource(String path) {}
	
	public static def getModel(String path, Class type) {}
	
	public static def getService(Class<?> serviceType) {}
	
	public static def getService(String className) {}
	
	public static def getServices(Class<?> serviceType, String filter) {}
	
	public static def getServices(String className, String filter) {}
	
	public static def save() {}
	
	public static def activate(String path) {}
	
	public static def activate(String path, ReplicationOptions options) {}
	
	public static def deactivate(String path) {}
	
	public static def deactivate(String path, ReplicationOptions options) {}
	
	public static def createQuery(Map predicates) {}
}
