
package edu.se452;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author wadetollefson
 */
public class HoundController extends HttpServlet {
   

    private static final long serialVersionUID = -8360344691931165807L;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {


       String name = request.getParameter("name");
       String gender = request.getParameter("gender");
       String age = request.getParameter("age");
       String weight = request.getParameter("weight");

       //validating
       String invalid = "";
       //name validation
       String name_error = "Invalid Name";
       if (name.equalsIgnoreCase(""))
           invalid += name_error + "<br>";
       //gender validation
       String gender_error = "Invalid Gender";
       if (!(gender.equals("M") || gender.equals("m") ||
               gender.equals("F") || gender.equals("f")))
           invalid += gender_error + "<br>";
       //age validation
       String age_error = "Invalid Age";
       try {
       if (age.equals(""))
           invalid += age_error + "<br>";
       else
       {
       int int_age = Integer.parseInt(age);
       if (int_age <= 0)
           invalid += age_error + "<br>";
            }
       }
       catch (NumberFormatException e)
       {
           invalid += age_error + "<br>";
       }
       //weight validation
       String weight_error = "Invalid Weight";
       try{
       if (weight.equals(""))
           invalid += weight_error + "<br>";
       else
       {
       int int_weight = Integer.parseInt(weight);
       if (int_weight <= 0)
           invalid += weight_error + "<br>";
           }
       }
       catch (NumberFormatException e)
       {
           invalid += weight_error + "<br>";
       }
       //something invalid forward
       if (!invalid.equals("")){
           invalid = invalid + "<br>";
           request.setAttribute("invalid", invalid);
           request.setAttribute("name", name);
           request.setAttribute("gender", gender);
           request.setAttribute("age", age);
           request.setAttribute("weight", weight);
           String url = "/addHound.jsp";
           RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
           dispatcher.forward(request,response);
       }
       //valid page -> add bean
       else{
           HoundBean user = new HoundBean(name, gender, age, weight);
           ServletContext application = this.getServletContext();
           if (application.getAttribute("houndList") == null){
               ArrayList<HoundBean> hounds = new ArrayList<HoundBean>();
               hounds.add(user);
               application.setAttribute("houndList", hounds);
           }
           else{
                @SuppressWarnings("unchecked")
               ArrayList<HoundBean> hounds =
                       (ArrayList<HoundBean>)application.getAttribute("houndList");
               hounds.add(user);
               application.setAttribute("houndList", hounds);
           }
       //forward info
       String url = "/listHounds.jsp";
       RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
       dispatcher.forward(request,response);
       }
    }
}
    