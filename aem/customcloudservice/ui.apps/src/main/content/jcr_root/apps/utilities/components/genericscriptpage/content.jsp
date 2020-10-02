
<%@page session="false" 
  contentType="text/html"
  pageEncoding="utf-8"%><%
%><%@taglib prefix="cq" uri="http://www.day.com/taglibs/cq/1.0" %><%

%><cq:defineObjects/>
<div>
    <h3>General Script Settings</h3>   
    <ul>
			<li><div class="li-bullet"><strong>Head Script: </strong><br><%= xssAPI.encodeForHTML(properties.get("headScript", "")).replaceAll("\\&\\#xa;","<br>") %></div></li>
			<li><div class="li-bullet"><strong>Foot Script: </strong><br><%= xssAPI.encodeForHTML(properties.get("footScript", "")).replaceAll("\\&\\#xa;","<br>") %></div></li>
    </ul>
</div>