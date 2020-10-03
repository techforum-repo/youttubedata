package aem.groovy.samples

import static aem.groovy.samples.Template.*;

import com.day.cq.wcm.api.Page
import javax.jcr.query.Query
import javax.jcr.query.QueryManager

Page page = getPage("/content/we-retail/us/en")

def queryManager = session.workspace.queryManager;
def param='weretail/components/structure/page'
def statement = 'select * from nt:base where jcr:path like \''+page.path+'/%\' and sling:resourceType = \'' + param + '\'';
Query query=queryManager.createQuery(statement, 'sql');

final def result = query.execute()

println "Total pages found = " + result.nodes.size();

result.nodes.each { node ->
	println 'nodePath::'+node.path
}