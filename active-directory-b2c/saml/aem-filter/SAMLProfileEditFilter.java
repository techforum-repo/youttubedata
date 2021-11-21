
package com.adobe.aem.auth.core.filters;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.servlets.annotations.SlingServletFilter;
import org.apache.sling.servlets.annotations.SlingServletFilterScope;
import org.apache.sling.xss.XSSAPI;
import org.apache.xml.security.utils.Base64;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.component.propertytypes.ServiceRanking;
import org.osgi.service.component.propertytypes.ServiceVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@SlingServletFilter(scope = { SlingServletFilterScope.REQUEST }, pattern = ".*", methods = { "GET" })
@ServiceDescription("SAML Profile Edit Filter")
@ServiceRanking(-700)
@ServiceVendor("Adobe")
public class SAMLProfileEditFilter implements Filter {

	@Reference
	XSSAPI xssAPI;

	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String B2C_TENANT_ID="albinsblog";
	
	private static final DateTimeFormatter XML_DATE_FORMATTER = ISODateTimeFormat.dateTimeNoMillis()
			.withZone(DateTimeZone.forOffsetHours(0));
	private static final String POST_URL = "https://"+B2C_TENANT_ID+".b2clogin.com/"+B2C_TENANT_ID+".onmicrosoft.com/B2C_1A_ProfileEdit/samlp/sso/login";
	 
	private static final String ISSUER="https://"+B2C_TENANT_ID+".onmicrosoft.com";

	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException {
		
		String operation = request.getParameter("operation");
		
		 if(((HttpServletRequest)request).getMethod().equalsIgnoreCase("GET") && ((HttpServletRequest)request).getRequestURI().endsWith("/saml_login")) {
			 
			((HttpServletResponse)response).sendRedirect("https://"+((HttpServletRequest)request).getServerName()+"/");//HttpServletRequest.getHeader("x-forwarded-host")this only works if your load balancer sets the header correctly 
			 
			return;
			 
		 }

		if (operation != null && operation.equals("profileedit")) {

			try {

				String parameterName = "SAMLRequest";

				String encodedRequest = Base64.encode(getSamlRequest().getBytes());

				String encodedContextPath = (((HttpServletRequest)request).getContextPath() == null || ((HttpServletRequest)request).getContextPath().isEmpty())
						? "/"
						: this.xssAPI.encodeForJSString(((HttpServletRequest)request).getContextPath());
				StringBuilder builder = new StringBuilder();
				builder.append(
						"<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>");
				builder.append("<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en' lang='en'>");
				builder.append("<head>");
				builder.append("<meta http-equiv='content-type' content='text/html; charset=utf-8' />");
				builder.append("<title>POST data</title>");

				builder.append("<script>    function setRequestPathCookies() {");
				if (!StringUtils.isBlank(request.getParameter("saml_request_path"))) {
					builder.append("        var requestPath = \"" + request.getParameter("saml_request_path") + "\";");
				} else {
					builder.append(
							"        var requestPath = window.location.pathname+window.location.hash;");
				}
				builder.append(
						"       document.cookie = \"saml_request_path=\"+encodeURIComponent(requestPath)+\";path="
								+ encodedContextPath + ";\";   }</script>");

				builder.append("</head>");
				builder.append("<body onload='");
				builder.append("setRequestPathCookies(); ");
				builder.append("document.forms[0].submit();'>");
				builder.append("<noscript>");
				builder.append(
						"<p><strong>Note:</strong> Since your browser does not support JavaScript, you must press the button below once to proceed.</p>");
				builder.append("</noscript>");
				builder.append("<form method='post' action='");
				builder.append(POST_URL);
				builder.append("'>");

				builder.append("<input type='hidden' name='");
				builder.append(parameterName);
				builder.append("' value='");
				builder.append(encodedRequest);
				builder.append("' />");

				builder.append("<noscript><input type='submit' value='Submit' /></noscript>");
				builder.append("</form>");
				builder.append("</body>");
				builder.append("</html>");
				response.setContentType("text/html");
				((HttpServletResponse) response).addHeader("cache-control", "private, max-age=0, no-cache, no-store");
				response.getOutputStream().print(builder.toString());
				response.flushBuffer();
				return;
			} catch (Exception e) {
				this.log.error("Fatal error while sending Authn request.", (Throwable) e);
				((HttpServletResponse) response).sendError(500,
						"Internal server error, please contact your administrator");

			}
		}		
		
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

	private String getSamlRequest() {
		String samlrequest = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
				+ "<samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\""
				+ "	AssertionConsumerServiceURL=\"https://localhost/saml_login\""
				+ "	Destination=\""+POST_URL+"\""
				+ "  ID=\"" + "_" + UUID.randomUUID().toString() + "\"" + "	IssueInstant=\""
				+ XML_DATE_FORMATTER.print(Calendar.getInstance().getTimeInMillis()) + "\""
				+ "  ProtocolBinding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\"" + "  Version=\"2.0\">"
				+ "  <saml:Issuer xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">"+ISSUER+"</saml:Issuer>"
				+ "  <samlp:NameIDPolicy AllowCreate=\"true\""
				+ "	Format=\"urn:oasis:names:tc:SAML:2.0:nameid-format:transient\" /></samlp:AuthnRequest>";
		return samlrequest;
	}

}