
/**
 *
 * @author wadetollefson
 */

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request,HttpServletResponse response)
            throws ServletException, IOException 
    {
    ServletConfig config = this.getServletConfig();
    String MLA = config.getInitParameter("MaxLoginAttempts");
    Integer max_attempts = Integer.parseInt(MLA);

    HttpSession session = request.getSession(true);
    Integer countdown_value = (Integer)session.getAttribute("countdown");
    if (countdown_value == null)
    {
        session.setAttribute("countdown",max_attempts);
        countdown_value = max_attempts;
    }
    else
        countdown_value = new Integer(countdown_value.intValue() - 1);
    
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    String username_key = config.getInitParameter("username");
    String password_key = config.getInitParameter("password");

    request.setAttribute("initial",(Object)"off");
    if (countdown_value > 1)
    {
        if (username.equals(username_key) && password.equals(password_key))
        {
            request.setAttribute("retry",(Object)"off");
            request.setAttribute("success",(Object)"on");
            countdown_value = max_attempts;
        }
        else
        {
            request.setAttribute("retry",(Object)"on");
            request.setAttribute("success",(Object)"off");
        }
    }
    else
    {
       request.setAttribute("retry",(Object)"off");
       request.setAttribute("success",(Object)"off");
       request.setAttribute("failure",(Object)"on");
    }
    session.setAttribute("countdown",countdown_value);

    String url = "/login.jsp";
    RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(url);
    dispatcher.forward(request,response);
    
    }
}
