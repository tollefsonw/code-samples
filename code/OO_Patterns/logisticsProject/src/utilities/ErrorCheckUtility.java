/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

/**
 *
 * @author wadetollefson
 */
public class ErrorCheckUtility {

    public ErrorCheckUtility(){}

    public boolean isIntNumber(String num)
    {
        try {
            Integer.parseInt(num);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean isDoubleNumber(String num)
    {
        try {
            Double.parseDouble(num);
        }
        catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
