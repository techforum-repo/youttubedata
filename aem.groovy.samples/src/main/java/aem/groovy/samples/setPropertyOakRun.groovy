package aem.groovy.samples

import org.apache.jackrabbit.oak.spi.commit.CommitInfo
import org.apache.jackrabbit.oak.spi.commit.EmptyHook
import org.apache.jackrabbit.oak.spi.state.NodeStore
import org.apache.jackrabbit.oak.commons.PathUtils
import com.google.common.collect.Lists
import java.util.List

def setProperty(def session, def nodepath, def propertyName, def propertyValue, def isMulti) {
    NodeStore ns = session.store
    def rnb = ns.root.builder()
    def nb = rnb;
    String path;
    if (PathUtils.isAbsolute(nodepath)) {
        path = nodepath;
    } else {
        path = PathUtils.concat(session.getWorkingPath(), nodepath);
    }
    List<String> elements = Lists.newArrayList();
    PathUtils.elements(path).each{String element ->
        if (PathUtils.denotesParent(element)) {
            if (!elements.isEmpty()) {
                elements.remove(elements.size() - 1);
            }
        } else if (!PathUtils.denotesCurrent(element)) {
            elements.add(element);
        }
    }

    elements.each {
      if(it.size() > 0) {
        nb = nb.getChildNode(it)
      }
    }
	
	println nb
    println "Setting property ${propertyName}: ${propertyValue} on node ${nodepath}"
    if(isMulti) {
       nb.setProperty(propertyName, Lists.newArrayList(propertyValue), org.apache.jackrabbit.oak.api.Type.STRINGS)
    } else {
       nb.setProperty(propertyName, propertyValue)
    }
    ns.merge(rnb, EmptyHook.INSTANCE, CommitInfo.EMPTY)

    println "Done"
}