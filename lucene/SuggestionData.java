package demos.core.servlets;

import java.io.IOException;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.InvalidQueryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = Servlet.class, property = {
		Constants.SERVICE_DESCRIPTION + "=Custom Root Mapping", "sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/getSuggestions", "service.ranking=" + 100001 })
public class SuggestionData extends SlingSafeMethodsServlet {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp)
			throws ServletException, IOException {

		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.error("inside custom servlet");

		final Session session = req.getResourceResolver().adaptTo(Session.class);

		final JSONArray suggestions = new JSONArray();

		String queryString = "SELECT [rep:suggest()]  FROM [nt:unstructured] WHERE "
							 +"SUGGEST('te') OPTION(INDEX NAME [testindex]) /* oak-internal */ ";

		try {
			QueryManager queryManager = session.getWorkspace().getQueryManager();
			Query query = queryManager.createQuery(queryString, Query.JCR_SQL2);
			QueryResult result = query.execute();
			RowIterator rows = result.getRows();

			while (rows.hasNext()) {
				suggestions.put(((Row) rows.next()).getValue("rep:suggest()").getString());
			}

		} catch (InvalidQueryException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) { // TODO Auto-generated
			e.printStackTrace();
		} finally {
			session.logout();

		}

		resp.setContentType("application/json");
		resp.getWriter().write(suggestions.toString());

	}

}