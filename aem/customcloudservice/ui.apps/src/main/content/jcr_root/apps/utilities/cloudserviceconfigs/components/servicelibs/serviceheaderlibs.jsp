<%@page session="false"%>
<%
%><%@include file="/libs/foundation/global.jsp" %><%
%><%@page import="org.apache.sling.api.resource.ResourceResolver,
                  org.apache.sling.api.resource.ResourceResolverFactory,
				  com.day.cq.wcm.webservicesupport.ConfigurationManager,
                  com.day.cq.wcm.webservicesupport.Service,
				  java.util.ArrayList,
                  java.util.Comparator,
  				  java.util.Iterator,
  				  java.util.List,
                  com.custom.cloudservice.core.services.GetLibScripts"%>
                  <%

String[] servicePaths = pageProperties.getInherited("cq:cloudserviceconfigs", new String[] {});
if (servicePaths.length > 0) {

    // scripts 
    List<String> libsScripts=sling.getService(GetLibScripts.class).getScripts(servicePaths,"head");
     for (String script : libsScripts) {  
    	%><cq:include script="<%=script%>"/><%
    }
}
%>

