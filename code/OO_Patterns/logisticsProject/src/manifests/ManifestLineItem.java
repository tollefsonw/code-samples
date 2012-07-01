/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manifests;

import java.util.ArrayList;
import java.util.Iterator;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class ManifestLineItem {
    
    private String itemID;
    private int itemQuantity;
    private String source;
    private ArrayList<String> usedContainers = new ArrayList<String>();
    private String daysOfTravel;

    public ManifestLineItem(String item, int quantity, String warehouseID,
                                                ArrayList<String> containers)
                                                    throws InvalidParameterException
    {
        setItemID(item);
        setItemQuantity(quantity);
        setSource(warehouseID);
        setContainersUsed(containers);
    }

    public String getSource()
    {
        return source;
    }

    public String getLineItemID()
    {
        return itemID;
    }

    public int getLineItemQuantity()
    {
        return itemQuantity;
    }

    public ArrayList<String> getContainersUsed()
    {
        ArrayList<String> transfer = new ArrayList<String>();
        Iterator<String> it = usedContainers.iterator();
        while (it.hasNext())
        {
            transfer.add(it.next());
        }
        return transfer;
    }

    public int getNumberOfContainers()
    {
        return usedContainers.size();
    }


    public void setTravelTime(String days) throws InvalidParameterException
    {
        if (days == null)
            throw new InvalidParameterException("Error in Class 'ManifestLineItem'"
                                + "in Method 'setTravelTime' - Invalid Paramter:NULL");
        daysOfTravel = days;
    }

    private void setItemID(String item) throws InvalidParameterException
    {
        if (item == null)
            throw new InvalidParameterException("Error in Class 'ManifestLineItem'"
                                + "in Method 'setItemID' - Invalid Paramter:NULL");
        itemID = item;
    }

    private void setItemQuantity(int quantity) throws InvalidParameterException
    {
        if (quantity <= 0)
           throw new InvalidParameterException("Error in Class 'ManifestLineItem'"
                                + "in Method 'setItemQuantity' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + quantity);
       itemQuantity = quantity;
    }

    private void setSource(String warehouseID) throws InvalidParameterException
    {
        if (warehouseID == null)
            throw new InvalidParameterException("Error in Class 'ManifestLineItem'"
                                + "in Method 'setSource' - Invalid Paramter:NULL");
        source = warehouseID;
    }

    private void setContainersUsed(ArrayList<String> containers) throws InvalidParameterException
    {
        if (containers == null)
            throw new InvalidParameterException("Error in Class 'ManifestLineItem'"
                                + "in Method 'setContainerUsed' - Invalid Paramter:NULL");
        else
        {
            for (String container : containers)
                usedContainers.add(container);
        }
    }

    public String manifestLineItemToString()
    {
        String lineItem = itemID+"     "+itemQuantity+"            "
                                +source+"       "+daysOfTravel+"            ";
        for (String container : usedContainers)
            lineItem+=container+" ";
        return lineItem;
    }

}
