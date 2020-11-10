package antisamy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 * Servlet implementation class AntiSamyDemoServlet
 */
@WebServlet("/AntiSamyDemoServlet")
public class AntiSamyDemoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AntiSamyDemoServlet() {
		super();
	}

	Policy getPolicy() {
		try {

			InputStream is = this.getServletContext()
					.getResourceAsStream("/WEB-INF/classes/antisamy/antisamy-slashdot.xml");
			System.out.println("is " + is);
			return Policy.getInstance(is);

		} catch (PolicyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		try {
			out.println("<html><head><title>Hello</title></head><body> <br/> <br/>");

			String link = "<a test=\"test\" href=\"http://www.test.com/test.html\">Link</a>";

			CleanResults cr = new AntiSamy().scan(link, getPolicy());
			out.println(cr.getCleanHTML());

			out.println("</body></html>");

		} catch (ScanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PolicyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
