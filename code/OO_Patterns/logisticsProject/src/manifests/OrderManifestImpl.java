/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manifests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class OrderManifestImpl implements Manifest{

    private String manifestID;
    private String unsourcedItemInfo;
    private String invalidItemInfo;
    private String specialHandlingInfo;
    private HashMap<String,ContainerManifest> containerManifests =
                                        new HashMap<String,ContainerManifest>();
    private ArrayList<ManifestLineItem> lineItems = new ArrayList<ManifestLineItem>();

    public OrderManifestImpl(String id) throws InvalidParameterException
    {
       setManifestID(id);
    }

    public String getManifestID()
    {
       return manifestID;
    }

    public void addManifestItem(ManifestLineItem item) throws InvalidParameterException
    {
        ManifestLineItem loadItem = new ManifestLineItem(
                            item.getLineItemID(),item.getLineItemQuantity(),
                            item.getSource(),item.getContainersUsed());
        lineItems.add(loadItem);
    }

    public void addUnsourcedItemInfo(String info) throws InvalidParameterException
    {
        if (info == null)
            throw new InvalidParameterException("Error in Class 'OrderManifestImpl'"
                                + "in Method 'addUnsourcedInfo' - Invalid Paramter:NULL");
        unsourcedItemInfo += info + " \n";
    }

    public void addInvalidItemInfo(String invalid) throws InvalidParameterException
    {
        if (invalid == null)
            throw new InvalidParameterException("Error in Class 'OrderManifestImpl'"
                                + "in Method 'addInvalidItemIfno' - Invalid Paramter:NULL");
        invalidItemInfo += invalid + " \n";
    }

    public void addSpecialHandlingItem(String special) throws InvalidParameterException
    {
        if (special == null)
            throw new InvalidParameterException("Error in Class 'OrderManifestImpl'"
                                + "in Method 'addSpecialHandlingItem' - Invalid Paramter:NULL");
        specialHandlingInfo += special + " \n";
    }

    public void addContainerManifests(HashMap<String,ContainerManifest> containers)
            throws InvalidParameterException
    {
       Set<String> containerIDs = containers.keySet();
       for (String containerID : containerIDs)
       {
           ContainerManifest cm = containers.get(containerID);
           ContainerManifest cmLoad = new ContainerManifest(cm.getContainerManifestID());
           cmLoad.addContainerManifestInfo(cm.getContainerManifestInfo());
           containerManifests.put(containerID, cmLoad);
           //containerManifests.put(containerID, containers.get(containerID));
       }
    }

    public boolean hasLineItems()
    {
        if (lineItems.isEmpty())
            return false;
        return true;
    }

    public ArrayList<ManifestLineItem> getLineItems() throws InvalidParameterException //reference
    {
        ArrayList<ManifestLineItem> transfer = new ArrayList<ManifestLineItem>();
        for (ManifestLineItem lineItem : lineItems)
        {
            //ManifestLineItem transferLineItem =
            //        new ManifestLineItem(lineItem.getLineItemID(),lineItem.getLineItemQuantity(),
            //                    lineItem.getSource(),lineItem.getContainersUsed());
            transfer.add(lineItem);
        }
        return transfer;
    }

    private void setManifestID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'OrderManifestImpl'"
                                + "in Method 'setManifestID' - Invalid Paramter:NULL");
        manifestID = id;
    }

    public String manifestToString()
    {
        String manifest = "ItemID  "+"Quantity       "+"Source        "+"Travel Time"+"      Containers\n";
        manifest +=       "-----   "+"--------       "+"------        "+"-----------"+"      ----------\n";
        for (ManifestLineItem lineItem : lineItems)
                manifest += lineItem.manifestLineItemToString()+"\n";
        if (unsourcedItemInfo!=null)
               manifest += "\nUnsourced Items: " + unsourcedItemInfo + "\n";
        if (invalidItemInfo!=null)
               manifest += "Invalid Items: " + invalidItemInfo + "\n";
        if (specialHandlingInfo!=null)
               manifest += "Special Handling: " + specialHandlingInfo +"\n";
        manifest+="---------------------\n";
        for (ContainerManifest containerManifest : containerManifests.values())
            manifest += containerManifest.containerManifestToString()+"\n";
        return manifest;
    }
}
