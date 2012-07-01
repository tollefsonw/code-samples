/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

import utilities.InvalidParameterException;


/**
 *
 * @author wadetollefson
 */
public class ItemImpl implements Item {

    private String itemID;
    private String description;
    private double length;
    private double width;
    private double height;
    private double weight;
    private int quantityPerPack;

    public ItemImpl(String id, String desc, double len, double wid,
            double hgt, double wgt, int quanPerPack) throws InvalidParameterException
    {
        setItemID(id);
        setDescription(desc);
        setLength(len);
        setWidth(wid);
        setHeight(hgt);
        setWeight(wgt);
        setQtyPerPack(quanPerPack);
    }

    public String getItemID()
    {
       return itemID;
    }

    public String getDescription()
    {
       return description;
    }
    
    public double getLength()
    {
        return length;
    }

    public double getHeight()
    {
        return height;
    }

    public double getWidth()
    {
        return width;
    }

    public double getWeight()
    {
        return weight;
    }

    public int getQtyPerPack()
    {
        return quantityPerPack;
    }

    private void setItemID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setItemID' - Invalid Paramter:NULL");
        itemID = id;
    }

    private void setDescription(String desc) throws InvalidParameterException
    {
        if (desc == null)
            throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setDescription' - Invalid Paramter:NULL");
        description = desc;
    }

    private void setLength(double len) throws InvalidParameterException
    {
        if (len <= 0)
           throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setLength' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + len);
        length = len;
    }

    private void setHeight(double hgt) throws InvalidParameterException
    {
        if (hgt <= 0)
        throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setHeight' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + hgt);
        height = hgt;
    }

    private void setWidth(double wid) throws InvalidParameterException
    {
        if (wid <= 0)
        throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setWidth' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + wid);
        width = wid;
    }

    private void setWeight(double wgt) throws InvalidParameterException
    {
        if (wgt <= 0)
        throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setWeight' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + wgt);
        weight = wgt;
    }

    private void setQtyPerPack(int quanPerPack) throws InvalidParameterException
    {
        if (quanPerPack <= 0)
        throw new InvalidParameterException("Error in Class 'ItemImpl'"
                                + "in Method 'setQtyPerPack' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + quanPerPack);
        quantityPerPack = quanPerPack;
    }


    public String itemToString()
    {
        String item = "Item ID: " + itemID + "\n"
                + "Description: " + description + "\n"
                + "Height: " + height + "\n"
                + "Width: " + width + "\n"
                + "Length: " + length + "\n"
                + "Weight: " + weight + "\n"
                + "Quantity Per Pack: " + quantityPerPack + "\n";
        return item;
    }

}
