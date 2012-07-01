/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class ContainerImpl implements Container{

    private String containerID;
    private double lengthInInches;
    private double widthInInches;
    private double heightInInches;
    private double maxWeight;
    private double currentWeight;
    private double maxVolume;
    private double currentVolume;

    private boolean availableContainer = true;
    private HashMap<String,Integer> containerContents = new HashMap<String,Integer>();


    public ContainerImpl(String id) throws InvalidParameterException
    {
        setContainerID(id); 
        setLength(231);
        setHeight(91);
        setWidth(93);
        setMaxWeight(50000);
        setMaxVolume(1954953);
    }

    public String getID()
    {
        return containerID;
    }

    public double getLength()
    {
        return lengthInInches;
    }

    public double getHeight()
    {
        return heightInInches;
    }

    public double getWidth()
    {
        return widthInInches;
    }

    public double getMaxWeight()
    {
        return maxWeight;
    }

    public double getCurrentWeight()
    {
        return currentWeight;
    }

    public double getMaxVolume()
    {
        return maxVolume;
    }

    public double getCurrentAvailableVolume()
    {
        return currentVolume;
    }

    public void addItem(String id, int qnty) throws InvalidParameterException
    {
        if ((id == null) || (qnty <= 0))
                throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'addItem' - Invalid Paramters");
        containerContents.put(id, qnty);
    }

    public HashMap<String,Integer> getContainerContents() //reference
    {
        HashMap<String,Integer> transfer = new HashMap<String,Integer>();
        Set<String> items = containerContents.keySet();
        for (String item : items)
            transfer.put(item, containerContents.get(item));
        return transfer;
    }

    public boolean isContainerAvailable()
    {
        return availableContainer;
    }

    public void setContainerInUse(boolean available)
    {
        if (available)
            availableContainer = false;
        else
            availableContainer = true;
    }

    private void setContainerID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'setContainerID' - Invalid Paramter:NULL");
        containerID = id;
    }

    public void reduceAvailableVolume(double volume) throws InvalidParameterException
    {
        if (volume <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'reduceAvailableVolume' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + volume);
        currentVolume -= volume;
    }

    public void addWeight(double wgt) throws InvalidParameterException
    {
        if (wgt <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'addWeight' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + wgt);
        currentWeight += wgt;
    }

    private void setLength(double length) throws InvalidParameterException
    {
        if (length <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'setLength' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + length);
        lengthInInches = length;
    }

    private void setHeight(double height) throws InvalidParameterException
    {
        if (height <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'setHeight' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + height);
        heightInInches = height;
    }

    private void setWidth(double width) throws InvalidParameterException
    {
        if (width <= 0)
           throw new InvalidParameterException("Error in Class 'CotainerImpl'"
                                + "in Method 'setWidth' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + width);
        widthInInches = width;
    }

    private void setMaxWeight(double maxWgt) throws InvalidParameterException
    {
        if (maxWgt <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'setLength' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + maxWgt);
        maxWeight = maxWgt;
        currentWeight = 0;
    }

    private void setMaxVolume(double maxVlm) throws InvalidParameterException
    {
        if (maxVlm <= 0)
           throw new InvalidParameterException("Error in Class 'ContainerImpl'"
                                + "in Method 'setMaxVolume' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + maxVlm);
        maxVolume = maxVlm;
        currentVolume = maxVlm;
    }

    public String containerToString()
    {
        String container = "Container ID: " + containerID + "\n"
                + "Contents: ";
        if (containerContents.isEmpty())
        {
            container += "Container Is Currently Empty" + "\n";
        }
        else
        {
                Iterator<String> it = containerContents.keySet().iterator();
                    while (it.hasNext())
                    {
                        String item = it.next();
                        Integer itemQuantity = containerContents.get(item);
                        container += "\nItemID: " + item + " Quantity: "
                                + itemQuantity.toString() + "\nContent Weight: "
                                + currentWeight + " lbs\n********\n";
                    }
        }
        return container;
    }

}
