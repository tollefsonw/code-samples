
package tags;

import edu.se452.HoundBean;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 * @author wadetollefson
 */
public class aHoundTag extends TagSupport
{
    
    private static final long serialVersionUID = -8360344691931165807L;

    @Override
    public int doStartTag() throws JspException
    {
        HoundBean hound = (HoundBean)pageContext.getAttribute("hound");
        String name = hound.getName();
        String gender = hound.getGender();
        String age = hound.getAge();
        String weight = hound.getWeight();
        JspWriter out = pageContext.getOut();
        try {
            out.print("Name = " + name + "<br>");
            out.print("Gender = " + gender + "<br>");
            out.print("Age = " + age + "<br>");
            out.print("Weight = " + weight + "<br>");
        } catch (IOException ioe) {
        }
        return SKIP_BODY;
    }
}
