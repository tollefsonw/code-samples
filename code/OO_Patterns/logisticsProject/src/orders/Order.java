/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orders;

import java.util.HashMap;


/**
 *
 * @author wadetollefson
 */
public interface Order {

    String getOrderID();

    String getDestination();

    HashMap<String,Integer> getOrderItems();

    String orderToString();

}
