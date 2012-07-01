/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.ports;

import facilities.Facility;


/**
 *
 * @author wadetollefson
 */
public interface Port extends Facility{

    String getPortType();

    String portToString();
}
