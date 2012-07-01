/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import utilities.InvalidParameterException;


/**
 *
 * @author wadetollefson
 */
public class OrderImpl implements Order{

    private String orderID;
    private String destination;
    private HashMap<String,Integer> orderItems; 

    public OrderImpl(String id, String dest, HashMap<String, Integer> lineItems)
            throws InvalidParameterException
    {
        setOrderID(id);
        setDestination(dest);
        addLineItems(lineItems);
    }

    public String getOrderID()
    {
        return orderID;
    }

    public String getDestination()
    {
        return destination;
    }

    public HashMap<String,Integer> getOrderItems() 
    {
        HashMap<String,Integer> transfer = new HashMap<String,Integer>();
        Set<String> keys = orderItems.keySet();
        for (String key : keys)
        {
            Integer temp = orderItems.get(key);
            transfer.put(key, temp);
        }
        return transfer;
    }

    private void setOrderID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'OrderImpl'"
                                + "in Method 'setOrderID' - Invalid Paramter:NULL");
        orderID = id;
    }

    private void setDestination(String dest) throws InvalidParameterException
    {
        if (dest == null)
            throw new InvalidParameterException("Error in Class 'OrderImpl'"
                                + "in Method 'setDestination' - Invalid Paramter:NULL");
        destination = dest;
    }

    private void addLineItems(HashMap<String,Integer> lineItems) throws InvalidParameterException
    {
        if (lineItems == null)
            throw new InvalidParameterException("Error in Class 'OrderImpl'"
                                + "in Method 'addLineItems' - Invalid Paramter:NULL");
        orderItems = lineItems;
    }

    public String orderToString()
    {
        String order = "Order ID: " + orderID + "\n"
                + "Destination: " + destination + "\n";
                Iterator<String> it = orderItems.keySet().iterator();
                    while (it.hasNext())
                    {
                        String orderItemID = it.next();
                        Integer orderItemQuantity = orderItems.get(orderItemID);
                        order += "Item: " + orderItemID + " Quantity: " +
                                orderItemQuantity.toString()+ "\n";
                    }
        return order;
    }
    
}
