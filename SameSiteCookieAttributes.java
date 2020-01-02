package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CookieAttributes
 */
@WebServlet("/CookieAttributes")
public class SameSiteCookieAttributes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SameSiteCookieAttributes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		setCokkieInfo(request,response,"testCookie","testValue");
				
		Cookie[] cookies = request.getCookies();
		
		String cookieValue="";

		if (cookies != null) {
		 for (Cookie cookie : cookies) {
		   if (cookie.getName().equals("testCookie")) {
			   cookieValue=cookie.getValue();
		    }
		  }
		}
		
		PrintWriter writer = response.getWriter();
		String htmlRespone = "<html>";
        htmlRespone += "<h2>Cookie Name username is: testCookie<br/>";      
        htmlRespone += "Cookie Value: " + cookieValue + "</h2>";  
       
        if(request.getServerName().equals("localhost"))
       {
        	//htmlRespone += "<a href=\"https://127.0.0.1:8443/pocs/CookieAttributes\">Click Here</>"; 
            //htmlRespone += "<iframe src=\"https://127.0.0.1:8443/pocs/CookieAttributes\" id=\"test1\"></iframe>";
        }
        htmlRespone += "</html>";
         
        // return response
        writer.println(htmlRespone);
		 		 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void setCokkieInfo(HttpServletRequest request,HttpServletResponse response,String cookieHeaderName,String cookieValue)
	{
		
		/*javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie(cookieHeaderName,URLEncoder.encode(serialize(cookieInfo), "utf-8"));
		cookie.setPath("/");
		cookie.setMaxAge(365*24*60*60);			
		cookie.setSecure(false);
		cookie.setVersion(0);
		response.addCookie(cookie);*/
		
		int expiration = 365*24*60*60;
	    StringBuilder cookieString = new StringBuilder(cookieHeaderName+"="+cookieValue+"; ");

	    DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.SECOND, expiration);
	    cookieString.append("Expires=" + df.format(cal.getTime()) + "; ");
	    cookieString.append("Domain="+request.getServerName()+"; ");		   
	    cookieString.append("Path=/; ");
	    cookieString.append("Max-Age=" + expiration + "; ");
	    cookieString.append("HttpOnly; ");
	    cookieString.append("Secure; ");
	    //cookieString.append("SameSite=Strict");
	    //cookieString.append("SameSite=Lax");
	    cookieString.append("SameSite=None");
	    
	    response.addHeader("Set-Cookie", cookieString.toString());
	   
	}

}
