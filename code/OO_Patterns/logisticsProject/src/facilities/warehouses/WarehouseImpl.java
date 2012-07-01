/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class WarehouseImpl implements Warehouse{

    private String warehouseID;
    private int numberOfContainers;
    private int containersProcessedPerDay;
    private HashMap<String,Integer> inventory = new HashMap<String,Integer>();
    private ArrayList<Container> containers = new ArrayList<Container>();
    private HashSet<Integer> schedule = new HashSet<Integer>();

    /**
     * constructs a WarehouseImpl object which implements the Warehouse interface
     */
    public WarehouseImpl(String id, int numContainers, int containersPPD,
            HashMap<String,Integer> stock, ArrayList<Container> containers)
                throws InvalidParameterException
    {
       setWarehouseID(id);
       setNumberOfContainers(numContainers);
       setContainersProcessedPerDay(containersPPD);
       setInventory(stock);
       createContainers(containers);
    }

    /**
     * returns the warehouse ID number 
     */
    public String getID()
    {
        return warehouseID;
    }

    /**
     * returns the number of containers processed per day
     *
     */
    public int getContainersProcessedPerDay()
    {
        return containersProcessedPerDay;
    }

    /**
     * returns the warehouse inventory
     * as an item id and quantity pair
     */
    public HashMap<String,Integer> getInventory() 
    {
        HashMap<String,Integer> transfer = new HashMap<String,Integer>();
        Set<String> items = inventory.keySet();
        for (String item : items)
            transfer.put(item, inventory.get(item));
        return transfer;
    }

    /**
     * returns a collection of Container objects owned by the current
     * warehouseImpl object
     */
    public ArrayList<Container> getContainers() 
    {
        HashSet<Container> transfer = new HashSet<Container>();
        Iterator<Container> it = containers.iterator();
        while (it.hasNext())
            transfer.add(it.next());
        return containers;
    }

    /**
     * returns the number of containers at the warehouse
     */
    public int getNumberofContainers()
    {
        return numberOfContainers;
    }

    /**
     * returns the warehouse schedule
     * as collection of integer days
     */
    public HashSet<Integer> getSchedule() 
    {
        HashSet<Integer> transfer = new HashSet<Integer>();
        Iterator<Integer> it = schedule.iterator();
        while (it.hasNext())
            transfer.add(it.next());
        return transfer;
    }

    /**
     * books the warehouse schedule for the selected day
     */
    public void bookSchedule(int day) throws InvalidParameterException
    {
        if (day <= 0)
           throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'bookSchedule' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + day);
        schedule.add(day);
    }

    /**
     * decreases the item inventory by the specified amount for the current
     * warehouseImpl object
     */
    public void decreaseInventory(String item, int decrease) throws InvalidParameterException
    {
        if (inventory.containsKey(item) && (decrease > 0))
            inventory.put(item, decrease);
        else
            throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'setWarehouseID' - Invalid Paramter");
    }

    /**
     * sets the warhouseID
     */
    private void setWarehouseID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'setWarehouseID' - Invalid Paramter:NULL");
        warehouseID = id;
    }

    /**
     * set the number of containers for the warehouse
     */
    private void setNumberOfContainers(int numContainers) throws InvalidParameterException
    {
        if (numContainers <= 0)
           throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'setNumberOfContainers' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + numContainers);
        numberOfContainers = numContainers;
    }

    /**
     * creates a collection of containers for the current warehouseImpl object
     */
    private void createContainers(ArrayList<Container> cntnrs) throws InvalidParameterException
    {
        if (cntnrs == null)
            throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'createContainers' - Invalid Paramter:NULL");
        for (Container container : cntnrs)
            containers.add(container);
    }

    /**
     * sets the number containers processed per day for the current
     * warehouseImpl object
     */
    private void setContainersProcessedPerDay(int containersPPD) throws InvalidParameterException
    {
        if (containersPPD <= 0)
           throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'setContainersProcessedPerDay' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + containersPPD);
        containersProcessedPerDay = containersPPD;
    }

    /**
     * sets the inventory for the current warehouseImpl object
     */
    private void setInventory(HashMap<String,Integer> stock) throws InvalidParameterException
    {
        if (stock == null)
            throw new InvalidParameterException("Error in Class 'WarehouseImpl'"
                                + "in Method 'setInventory' - Invalid Paramter:NULL");
        inventory = stock;
    }

    /**
     * returns a string of current warehouse information
     */
    public String warehouseToString()
    {
        String warehouse = "Warehouse ID: " + warehouseID + "\n"
                + "Number of Containers: " + numberOfContainers + "\n"
                + "Containers Processed Per Day: " + containersProcessedPerDay + "\n";
                Iterator<String> it = inventory.keySet().iterator();
                    while (it.hasNext())
                    {
                        String item = it.next();
                        Integer itemQuantity = inventory.get(item);
                        warehouse += "ItemID: " + item + " Stock: "
                                + itemQuantity.toString() + "\n";
                    }
                warehouse += "---------------------------\n";
                for (Container container : containers)
                {
                    warehouse += container.containerToString();
                }
        return warehouse;
    }

}
