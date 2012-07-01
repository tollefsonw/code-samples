/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

/**
 *
 * @author wadetollefson
 */
public class InvalidParameterException extends Exception
{
    private static final long serialVersionUID = 7081846673161471558L;

    public InvalidParameterException(String msg)
	{
		super(msg);
	}

}
