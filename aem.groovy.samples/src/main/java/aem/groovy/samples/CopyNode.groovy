package aem.groovy.samples
import static aem.groovy.samples.Template.*;

import org.apache.sling.api.resource.Resource

srcPath="/content/dam/we-retail/en/products/Product_catalog.jpg";
destinationPath="/content/dam/we-retail/en/test-products";

Resource sourceResource = resourceResolver.getResource(srcPath);
if(sourceResource!=null) {
	destinationPath = destinationPath+"/"+sourceResource.name;
	Resource copiedDestination = pageManager.copy(sourceResource, destinationPath, sourceResource.name, true, true);
	
}
