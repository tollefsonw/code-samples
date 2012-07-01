/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import logistics.DeliveryMgr;
import manifests.ManifestLineItem;
import manifests.OrderManifestImpl;
import utilities.InvalidParameterException;
import logistics.SourcingMgr;
import facilities.warehouses.WarehouseMgr;
import java.io.BufferedWriter;
import java.io.FileWriter;
import utilities.ErrorCheckUtility;

/**
 *
 * @author wadetollefson
 */
public class OrderMgr {

    private static OrderMgr instance;
    private ArrayList<Order> ordersList = new ArrayList<Order>();
    private int orderNumber = 0;
    ArrayList<String> manifests = new ArrayList<String>();

    private OrderMgr() throws IOException
    {
            loadOrders();
    }

    public static OrderMgr getInstance() throws IOException
    {
            if (instance == null)
                instance = new OrderMgr();
            return instance;
    }

    private void loadOrders() throws IOException 
    {
        String orderInfo = "orders.txt";
        ErrorCheckUtility errorCheck = new ErrorCheckUtility();
        BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
        BufferedReader input =  new BufferedReader(new FileReader(orderInfo));
        if (input == null)
        {
            fstream.write("Error in Class 'OrderMgr'"
                                + "in Method 'loadOrders' - Order Information Does Not Exist "
                                + "Check File 'order.txt'\r\n");
        }
        String order;
        while ((order = input.readLine()) != null)
        {
            StringTokenizer st = new StringTokenizer(order);
            if (isValidOrderData(st.countTokens()))
            {
                String orderID = st.nextToken();
                String destination = st.nextToken();
                HashMap<String,Integer> lineItems = new HashMap<String,Integer>();
                while (st.hasMoreTokens())
                {
                    String temp1 = st.nextToken();
                    String temp2 = st.nextToken();
                    if (errorCheck.isIntNumber(temp2))
                    {
                        if (lineItems.containsKey(temp1))
                        {
                            Integer sameItem = lineItems.get(temp1);
                            lineItems.put(temp1, Integer.parseInt(temp2)+sameItem);
                        }
                        else
                        {
                            lineItems.put(temp1, Integer.parseInt(temp2));
                        }
                    }
                    else
                    {
                        fstream.write("Error in Class 'OrderMgr'"
                                + "in Method 'loadOrders' - Invalid Data: Order Data Parameters "
                                + "Quantity Must Be Numeric\r\n");
                    }
                }
                try{
                OrderImpl newOrder = new OrderImpl(orderID, destination, lineItems);
                ordersList.add(newOrder);
                }
                catch(InvalidParameterException ipe)
                    {
                        fstream.write(ipe.getMessage()+"\r\n");
                    }
            }
            else
            {
                fstream.write("Error in Class 'OrderMgr'"
                                + "in Method 'loadItems' - Invalid Order: "
                                + "Incorrect Order Format\r\n");
            }
        }
    }

    private boolean isValidOrderData(int evenTokens)
    {
        if (evenTokens % 2 == 0)
            return true;
        return false;
    }

    public void processOrders() throws IOException
    {
        for (Order order : ordersList) 
        {
            try{
            OrderManifestImpl manifest = SourcingMgr.getInstance().sourceOrder(order);
            HashSet<String> routes = new HashSet<String>();
            String invalidDestination = "";
            if (manifest.hasLineItems())
            {
                HashSet<String> sources = new HashSet<String>();
                for (ManifestLineItem lineItem : manifest.getLineItems())
                    sources.add(lineItem.getSource());
                for (String source : sources)
                {
                    if (isDestinationPossible(source,order.getDestination()))
                    {
                        int numberOfContainersForSource = 0;
                        for (ManifestLineItem lineItem : manifest.getLineItems())
                        {
                            if (lineItem.getSource().equalsIgnoreCase(source))
                                numberOfContainersForSource += lineItem.getNumberOfContainers();
                        }
                        HashMap<String,Integer> pathAndTravelTime = DeliveryMgr.getInstance().
                                shipOrder(source,order.getDestination(), numberOfContainersForSource);
                        Set<String> path = pathAndTravelTime.keySet();
                        Iterator<String> it = path.iterator();
                        String route = it.next(); 
                        routes.add(route);
                        int deliveryTime = pathAndTravelTime.get(route);
                        for (ManifestLineItem lineItem : manifest.getLineItems())
                            lineItem.setTravelTime(Integer.toString(deliveryTime));
                    }
                    else
                    {
                        for (ManifestLineItem lineItem : manifest.getLineItems())
                            lineItem.setTravelTime("N/A");
                        invalidDestination += (source+" to "+order.getDestination()+
                                " is inaccesible.\n");
                        //put stuff back in warehouses 
                    }
                }
            }
            String orderManifest = createCompleteOrderManifest
                    (order.getDestination(),manifest,invalidDestination,routes);
            manifests.add(orderManifest);
            WarehouseMgr.getInstance().clearContainerManifest();
            }
            catch(InvalidParameterException ipe)
            {
                FileWriter fstream = new FileWriter("log.txt");
                fstream.write(ipe.getMessage());
            }
        }
    }

    public boolean isDestinationPossible(String source, String destination)
        {
            if (source.equalsIgnoreCase("WarehouseB") && (destination.equalsIgnoreCase("Air1")
                    || destination.equalsIgnoreCase("Air3")))
                return false;
            else if (source.equalsIgnoreCase("WarehouseC") && (destination.equalsIgnoreCase("Air1")
                    || destination.equalsIgnoreCase("Air2")
                    || destination.equalsIgnoreCase("Air3")))
                return false;
            return true;

        }


    public void printOrders()
    {
        for (Order order: ordersList)
            System.out.println(order.orderToString());
    }

    private String createCompleteOrderManifest(String destination, OrderManifestImpl manifest,
            String voidOrder, HashSet<String> routes)
    {
        String completeManifest = "";
        if (voidOrder.equalsIgnoreCase(""))
        {
            completeManifest = "___________________________________________________________\n";
            completeManifest += "Order# "+(++orderNumber)+"  Destination: "+destination+"\n";
            completeManifest += manifest.manifestToString();
            completeManifest += "Associated Routes:\n";
            for (String route : routes)
                completeManifest += route+"\n";
            return completeManifest;
        }
        else
        {
            completeManifest = "___________________________________________________________\n";
            completeManifest += "Order# "+(++orderNumber)+"  Destination: "+destination+" - ORDER "
                    + "VOIDED\n";
            completeManifest += voidOrder;
            completeManifest += manifest.manifestToString();
            return completeManifest;
        }
    }

    public void printManifests()
    {
        for (String orderManifest : manifests)
            System.out.print(orderManifest);
    }

}
