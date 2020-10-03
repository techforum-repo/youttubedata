package aem.groovy.samples

import static aem.groovy.samples.Template.*;

import org.apache.sling.api.resource.ModifiableValueMap
import com.day.cq.wcm.api.Page

import groovy.transform.Field


String[] nodes=[
	'/content/we-retail/us/en/jcr:content',
	'/content/we-retail/us/es/jcr:content'
]

nodes.eachWithIndex { self,i->

	javax.jcr.Node node=getNode(nodes[i])

	ModifiableValueMap mVMap=resourceResolver.resolve(node.path).adaptTo(ModifiableValueMap.class);

	mVMap.put("customProperty", "testValue");

	println "Property modified to "+node.path;

	println "Dry Run "+data.dryRun;

	if(!data.dryRun) {
		session.save();
	}
}