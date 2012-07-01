/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.util.ArrayList;
import java.util.HashMap;
import facilities.Facility;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public interface Warehouse extends Facility{

    HashMap<String,Integer> getInventory();

    void decreaseInventory(String item, int decrease) throws InvalidParameterException;

    int getNumberofContainers();

    ArrayList<Container> getContainers();

    String warehouseToString();
}
