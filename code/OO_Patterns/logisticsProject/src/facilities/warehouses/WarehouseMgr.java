/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import items.ItemMgr;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import manifests.ContainerManifest;
import utilities.InvalidParameterException;
import items.ItemDTO;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Set;
import utilities.ErrorCheckUtility;

/**
 *
 * @author wadetollefson
 */
public class WarehouseMgr {

    private static WarehouseMgr instance;
    private HashMap<String,Warehouse> warehouseList = new HashMap<String,Warehouse>();
    private ArrayList<String> warehousePreferenceList = new ArrayList<String>();
    private HashMap<String,ContainerManifest>
                perOrderContainerManifests = new HashMap<String,ContainerManifest>(); 

    /**
     * constructor that load relevant information at instantiation
     */
    private WarehouseMgr() throws IOException, InvalidParameterException
    {
        loadWarehouses();
        loadWarehousePreferences();
    }

    /**
     * creates and maintains a singleton instance
     */
    public static WarehouseMgr getInstance() throws IOException, InvalidParameterException
    {
        if (instance == null)
            instance = new WarehouseMgr();
        return instance;
    }

    /**
     * loads warehouse information for processing
     */
    private void loadWarehouses() throws IOException, InvalidParameterException
    {
        String warehouseInfo = "warehouses.txt";
        ErrorCheckUtility errorCheck = new ErrorCheckUtility();
        BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
        BufferedReader input =  new BufferedReader(new FileReader(warehouseInfo));
        if (input == null)
        {
            fstream.write("Error in Class 'OrderMgr'"
                                + "in Method 'loadOrders' - Warehouse Information Does Not Exist "
                                + "Check File 'order.txt'\r\n");
        }
        String warehouse;
        while ((warehouse = input.readLine()) != null)
        {
            StringTokenizer st = new StringTokenizer(warehouse);
            if (st.countTokens() >= 3)
            {
                String warehouseID = st.nextToken();
                String temp1 = st.nextToken();
                String temp2 = st.nextToken();
                if (errorCheck.isIntNumber(temp1) && errorCheck.isIntNumber(temp2))
                {
                int numberOfContainers = Integer.parseInt(temp1);
                int containersProcessedPerDay = Integer.parseInt(temp2);
                HashMap<String,Integer> inventory = new HashMap<String,Integer>();
                if (inputWarehouseInventoryDataIsValid(st.countTokens()))
                {
                    while (st.hasMoreTokens())
                    {
                        String temp3 = st.nextToken();
                        String temp4 = st.nextToken();
                        if (errorCheck.isIntNumber(temp4))
                        {
                        inventory.put(temp3,
                            Integer.parseInt(temp4));
                        }
                        else
                        {
                            fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadWarehouses' - Invalid Data: Warehouse Inventory"
                                + " Parameter Must Be Numeric\r\n");
                        }
                    }
                    ArrayList<Container> containers = buildContainers(warehouseID, numberOfContainers);
                    try{
                    WarehouseImpl newWarehouse = new WarehouseImpl(warehouseID,
                        numberOfContainers, containersProcessedPerDay,
                        inventory, containers);
                    warehouseList.put(newWarehouse.getID(), newWarehouse);
                    }
                    catch(InvalidParameterException ipe)
                    {
                        fstream.write(ipe.getMessage()+"\r\n");
                    }
                }
                else {
                    fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadWarehouses' - Invalid Inventory Parameters: "
                                + "Incorrect Order Format\r\n");
                }
                }
                else
                {
                    fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadWarehouses' - Invalid Data: Warehouse Data Parameters: "
                                + "Number of Containers And Containers Processed Per"
                                + " Day, Must Be Numeric\r\n");
                }

            }
            else {
                fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadWarehouses' - Invalid Warehouse Information: "
                                + "Incorrect Order Format\r\n");
            }
        }
    }

    /**
     * determines if inputed warehouse information data is valid
     */
    private boolean inputWarehouseInventoryDataIsValid(int evenTokens)
    {
        if (evenTokens % 2 == 0)
            return true;
        return false;
    }

    /**
     * loads warehouse preference information for processing
     */
    private void loadWarehousePreferences() throws IOException 
    {
            String warehousePreferenceInfo = "warehouse-preferences.txt";
            BufferedReader input =  new BufferedReader(new FileReader(warehousePreferenceInfo));
            String warehousePref;
            while ((warehousePref = input.readLine()) != null)
            {
                if (isValidWarehouse(warehousePref))
                    warehousePreferenceList.add(warehousePref);
                else
                {
                BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
                fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadWarehousePreferences' - " +
                                "The Preferred Warehouse Information Is Invalid\r\n");
                fstream.flush();
                }
            }
    }

    /**
     * constructs a list of containers for the specified warehouse
     */
    private ArrayList<Container> buildContainers(String id, int numberOfContainers)
            throws InvalidParameterException
    {
        if (numberOfContainers > 0)
        {
            ArrayList<Container> containers = new ArrayList<Container>();
            for (int i = 1; i <= numberOfContainers; i++)
            {
                ContainerImpl newContainer = new ContainerImpl(id+i);
                containers.add(newContainer);
            }
            return containers;
        }
        else
            throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'buildContainers' - "
                                + "Number Of Containers Must Be Greater"
                                + "Than Zero: "
                                + " Was, " + numberOfContainers);
    }

    /**
     * determines if specified warehouse id is valid
     */
   public boolean isValidWarehouse(String id)
   {
        return warehouseList.containsKey(id);
   }

   /**
     * returns a collection of warehouse information, with the warehouse id as key value
     */
   public HashMap<String,Warehouse> getWarehousesInfo() //reference
   {
       HashMap<String,Warehouse> transfer = new HashMap<String,Warehouse>();
       Set<String> keys = warehouseList.keySet();
       for (String key : keys)
           transfer.put(key, warehouseList.get(key));
       return transfer;
   }

   /**
     * returns a list of the preferred warehouses
     */
   public ArrayList<String> getPreferredWarehousesList()
   {
       ArrayList<String> transfer = new ArrayList<String>();
       for (String preferredWarehouse : warehousePreferenceList)
           transfer.add(preferredWarehouse);
       return transfer;
   }

    /**
     * decreases the selected warehouses inventory
     */
   public void decreaseWarehouseInventory(String warehouseID, Warehouse warehouse)
           throws InvalidParameterException
   {
       if (isValidWarehouse(warehouseID))
            warehouseList.put(warehouseID, warehouse);
       else
       {
           throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'decreaseWarehouseInventory' - "
                                + "Warehouse ID Is Not Valid:"
                                + " Was, " + warehouseID);
       }
   }

   /**
     * return a collection of containers manifests, with container id as the key value of the pair
     */
   public HashMap<String,ContainerManifest> getContainerManifests() 
   {
       HashMap<String,ContainerManifest> transfer = new HashMap<String,ContainerManifest>();
       for (String containerID : perOrderContainerManifests.keySet())
       {
           transfer.put(containerID, perOrderContainerManifests.get(containerID)); 
       }
       return transfer;
   }

   /**
     * clears the per-order container manifests
     */
   public void clearContainerManifest()
   {
        perOrderContainerManifests.clear();
   }

    /**
     * gets the number of containers processed per day for the specified warehouse
     */
   public int getContainersProcessedPerDay(String warehouseID) throws InvalidParameterException
   {
       if (isValidWarehouse(warehouseID))
       {
            int containersPerDay =
               warehouseList.get(warehouseID).getContainersProcessedPerDay();
            return containersPerDay;
       }
       throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'getContainersProcessedPerDay' - "
                                + "Warehouse ID Is Not Valid:"
                                + " Was, " + warehouseID);
   }

   /**
     * returns the specified warehouse schedule
     */
   public HashSet<Integer> getWarehouseSchedule(String warehouseID)
           throws InvalidParameterException
   {
        if (isValidWarehouse(warehouseID))
        {
            return warehouseList.get(warehouseID).getSchedule();
        }
        throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'getWarehouseSchedule' - Warehouse ID Is Not Valid:"
                                + " Was, " + warehouseID);
   }

   /**
     * sets the specified warehouse schedule, for the specified day
     */
  public void setWarehouseSchedule(String warehouseID, int day)
                throws InvalidParameterException
  {
        if (isValidWarehouse(warehouseID))
        {
            warehouseList.get(warehouseID).bookSchedule(day);
        }
        throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'setWarehouseSchedule' - Warehouse ID Is Not Valid:"
                                + " Was, " + warehouseID);
  }

  /**
     * loads warehouse containers and returns a list of the containers used
     */
   public ArrayList<String> loadContainers
           (String orderID, String warehouseID, String itemID, int quantity)
                throws IOException, InvalidParameterException
   {
        ItemDTO item = ItemMgr.getInstance().getItem(itemID);
        double itemVolume = item.height * item.width * item.length * item.quantityPerPack;
        double itemWeight = item.weight * item.quantityPerPack;
        double packageVolume = (item.height * item.width * item.length) * quantity;
        double packageWeight = item.weight * quantity;
        ArrayList<Container> containers = warehouseList.get(warehouseID).getContainers();
        ArrayList<String> usedContainers = new ArrayList<String>();
        int remainingQuantity = quantity;
        while (remainingQuantity > 0)
        {
            for (Container container : containers)
            {
                if (container.isContainerAvailable())
                {
                if ((container.getCurrentAvailableVolume() > packageVolume) &&
                        (container.getCurrentWeight() + packageWeight
                        <= container.getMaxWeight()))
                {
                    container.addItem(itemID, remainingQuantity);
                    container.reduceAvailableVolume(packageVolume);
                    container.addWeight(packageWeight);
                    updateContainerManifests(container.getID(),item.itemID,
                            item.quantityPerPack, remainingQuantity);
                    remainingQuantity = 0;
                    usedContainers.add(container.getID());
                    
                }
                else
                {
                    if ((container.getCurrentAvailableVolume() < itemVolume)
                            || container.getCurrentWeight() < itemWeight)
                        continue;
                    else
                    {
                        int numberOfItemsThatFit = 0;
                        while (itemVolume <= container.getCurrentAvailableVolume()
                                && itemWeight <= container.getCurrentWeight())
                        {
                            numberOfItemsThatFit++;
                            container.reduceAvailableVolume(itemVolume);
                            container.addWeight(itemWeight);
                        }
                        container.addItem(itemID, (numberOfItemsThatFit * item.quantityPerPack));
                        updateContainerManifests(container.getID(),item.itemID,
                            item.quantityPerPack,(numberOfItemsThatFit * item.quantityPerPack));
                        remainingQuantity = remainingQuantity - 
                                (numberOfItemsThatFit*item.quantityPerPack);
                        usedContainers.add(container.getID());
                    }
                }
                if (remainingQuantity == 0)
                    break;
                }
            }
            if (remainingQuantity > 0)
            {
                BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
                fstream.write("Error in Class 'WarehouseMgr'"
                                + "in Method 'loadContainers' - " + orderID +
                                " Cannot fit All Items " + itemID + " In Containers "
                                + "- Infinately Delayed\r\n");
                break;
            }
        }
        return usedContainers;
   }

   /**
     * updates a set of per-order container manifests
     */
   private void updateContainerManifests(String containerID, String itemID,
                                int quantityPerPack, int totalQuantity)
                                    throws InvalidParameterException
   {
        int numOfPacks = totalQuantity / quantityPerPack;
        if (perOrderContainerManifests.containsKey(containerID))
        {
            ContainerManifest containerManifest = perOrderContainerManifests.get(containerID);
            containerManifest.addContainerManifestInfo(itemID + " " + quantityPerPack
                                + " " + numOfPacks);
            perOrderContainerManifests.put(containerID, containerManifest);
        }
        else
        {
            ContainerManifest containerManifest = new ContainerManifest(containerID);
            containerManifest.addContainerManifestInfo(itemID + " " + quantityPerPack
                                + " " + numOfPacks);
            perOrderContainerManifests.put(containerID, containerManifest);
        }
   }

   /**
     * sets container -in use- to unavailable
     */
   public void setContainerAsUnavailable(String containerID) throws InvalidParameterException
   {
        if (containerID == null)
            throw new InvalidParameterException("Error in Class 'WarehouseMgr'"
                                + "in Method 'setContainerAsUnavailable' - Invalid Paramter:NULL");
        for (Warehouse warehouse : warehouseList.values())
        {
            ArrayList<Container> containers = warehouse.getContainers();
            for (Container container : containers)
            {
                if (container.getID().equalsIgnoreCase(containerID))
                    container.setContainerInUse(true);
            }
        }
   }

   /**
     * prints warehouses information to display
     */
   public void printWarehouses()
   {
        for (Warehouse warehouse: warehouseList.values())
            System.out.println(warehouse.warehouseToString() + 
            "\n*********************************************");
   }

   /**
     * resets WarehouseMgr singleton for testing
     */
   public void resetWarehouseMgr()
   {
        instance = null;
   }

}
