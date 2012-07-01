/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logistics;

import manifests.OrderManifestImpl;
import manifests.ManifestLineItem;
import utilities.InvalidParameterException;
import orders.Order;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import items.ItemDTO;
import items.ItemMgr;
import facilities.warehouses.Container;
import facilities.warehouses.Warehouse;
import facilities.warehouses.WarehouseMgr;

/**
 *
 * @author wadetollefson
 */
public class SourcingMgr {

    private static SourcingMgr instance;

    private SourcingMgr() {}

    public static SourcingMgr getInstance()
    {
            if (instance == null)
                instance = new SourcingMgr();
            return instance;
    }

    public OrderManifestImpl sourceOrder(Order order)
            throws IOException, InvalidParameterException 
    {
       HashMap<String,Integer> orderLineItems =
                   new HashMap<String,Integer>(order.getOrderItems()); 
       OrderManifestImpl orderManifest = new OrderManifestImpl(order.getOrderID());
       ArrayList<ArrayList<String>> usedContainers = new ArrayList<ArrayList<String>>();
       Set<String> items = orderLineItems.keySet();
       Iterator<String> orderItems = items.iterator();
       while (orderItems.hasNext())
       {
            String itemID = orderItems.next();
            int desiredQuantity = orderLineItems.get(itemID);
            if (ItemMgr.getInstance().isItem(itemID))
            {
            int remainingQuantity = desiredQuantity;
            ArrayList<String> warehousePreferences =
                    WarehouseMgr.getInstance().getPreferredWarehousesList();
            for (String preferredWarehouse : warehousePreferences)
            {
                HashMap<String,Warehouse> warehouseList = WarehouseMgr.getInstance().getWarehousesInfo();
                Warehouse warehouse = warehouseList.get(preferredWarehouse);
                if (warehouse.getInventory().containsKey(itemID))
                {
                    int verifiedQuantity = verifyQuantity(itemID,remainingQuantity);
                    if (warehouse.getInventory().get(itemID) >= verifiedQuantity)
                    {
                        int remainingInventory =
                            warehouse.getInventory().get(itemID) - verifiedQuantity;
                        warehouse.decreaseInventory(itemID, remainingInventory);
                        remainingQuantity = 0;
                        if (checkForSpecialHandling(warehouse.getID(),itemID))
                            orderManifest.addSpecialHandlingItem(itemID+" Requires"
                                    + "Special Handling : Shipped Seperately"
                                    + "From "+warehouse.getID());
                        else
                        {
                            ArrayList<String> containersUsed =
                                    WarehouseMgr.getInstance().loadContainers
                                    (order.getOrderID(),warehouse.getID(), itemID, verifiedQuantity);
                            //if cannot get to destination??
                            usedContainers.add(containersUsed);
                            ManifestLineItem lineitem = new ManifestLineItem(itemID,
                                    verifiedQuantity,warehouse.getID(),containersUsed);
                            orderManifest.addManifestItem(lineitem);
                        }
                        WarehouseMgr.getInstance().decreaseWarehouseInventory(warehouse.getID(), warehouse);

                    }
                    else
                    {
                        int partialQuantity = verifiedQuantity - warehouse.getInventory().get(itemID);
                        verifiedQuantity = verifyQuantity(itemID,partialQuantity);
                        remainingQuantity -= verifiedQuantity;
                        warehouse.decreaseInventory(itemID, 0);
                        if (checkForSpecialHandling(warehouse.getID(),itemID))
                            orderManifest.addSpecialHandlingItem(itemID+" Requires"
                                    + "Special Handling : Shipped Seperately"
                                    + "From "+warehouse.getID());
                        else
                        {
                            ArrayList<String> containersUsed =
                                    WarehouseMgr.getInstance().loadContainers 
                                    (order.getOrderID(),warehouse.getID(), itemID, verifiedQuantity);
                            usedContainers.add(containersUsed);
                            //if cannot get to destination??
                            ManifestLineItem lineitem = new ManifestLineItem(itemID,
                                    verifiedQuantity,warehouse.getID(),containersUsed);//clone
                            orderManifest.addManifestItem(lineitem);
                        }
                        WarehouseMgr.getInstance().decreaseWarehouseInventory
                                (warehouse.getID(), warehouse);
                    }
                }
                if (remainingQuantity == 0)
                    break;
            }
            if (remainingQuantity != 0)
            {
                orderManifest.addUnsourcedItemInfo(itemID+" has an unsourced quantity of "+
                        remainingQuantity);
            }
            }
            else
            {
                orderManifest.addInvalidItemInfo(itemID+" is Invalid");
            }
       }
       if (WarehouseMgr.getInstance().getContainerManifests()!=null)
       {
            orderManifest.addContainerManifests(WarehouseMgr.getInstance().getContainerManifests());
            setContainersInUse(usedContainers);
            return orderManifest;
       }
       else
       {
           throw new InvalidParameterException("Error in Class 'SourcingMgr'"
                                + "in Method 'sourceOrder' - " + order.getOrderID()
                                + " - All Items Invalid");
       }
   }

   private boolean checkForSpecialHandling(String warehouseID, String itemID)
           throws IOException, InvalidParameterException
   {
        ArrayList<Container> containers =
                WarehouseMgr.getInstance().getWarehousesInfo().get(warehouseID).getContainers();
        boolean noAvailableFit = true;
        for (Container container : containers)
        {
            ItemDTO item = ItemMgr.getInstance().getItem(itemID);
            if (item.length <= container.getLength() && item.height <= container.getHeight()
                    && item.width <= container.getWidth())
                noAvailableFit = false;
            if (noAvailableFit == false)
                break;
        }
        return noAvailableFit;
   }

   private int verifyQuantity(String itemID, int quantity) 
           throws IOException, InvalidParameterException
   {
        ItemDTO item = ItemMgr.getInstance().getItem(itemID);
        if (quantity % item.quantityPerPack != 0)
        {
            if (quantity < item.quantityPerPack)
            {
                quantity = quantity + (item.quantityPerPack - quantity);
            }
            else
            {
                int findQuantity = item.quantityPerPack;
                while (findQuantity < quantity)
                {
                    findQuantity += item.quantityPerPack;
                }
                quantity = findQuantity;
            }
        }
        return quantity;
   }


   private void setContainersInUse(ArrayList<ArrayList<String>> usedContainers)
           throws IOException, InvalidParameterException
   {
       Iterator<ArrayList<String>> it = usedContainers.iterator();
       while (it.hasNext())
       {
           Iterator<String> it2 = it.next().iterator();
           while (it2.hasNext())
           {
               String containerID = it2.next();
               WarehouseMgr.getInstance().setContainerAsUnavailable(containerID);
           }
       }
   }

}
