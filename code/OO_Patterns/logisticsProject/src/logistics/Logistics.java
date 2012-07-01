/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logistics;

import utilities.InvalidParameterException;
import orders.OrderMgr;
import facilities.warehouses.WarehouseMgr;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author wadetollefson 
 */
public class Logistics {

    public static void main(String[] args) throws IOException, InvalidParameterException
    {
        File log = new File("log.txt");
        log.delete();

        /*System.out.println("ITEMS");
        System.out.println("-----");
        System.out.println();
        ItemMgr im = ItemMgr.getInstance();
        im.printCatalog();
        System.out.println("PORTS");
        System.out.println("-----");
        System.out.println();
        PortMgr pm = PortMgr.getInstance();
        pm.printPorts();
        System.out.println("TRANSPORT LINKS");
        System.out.println("---------------");
        System.out.println();
        TransportLinkMgr tlm = TransportLinkMgr.getInstance();
        tlm.printTransPortLinks();*/

        System.out.println("-----------------------------------");
        System.out.println("WAREHOUSES INVENTORY AND CONTAINERS");
        System.out.println("-----------------------------------");
        System.out.println();
        WarehouseMgr wm = WarehouseMgr.getInstance();
        wm.printWarehouses();

        System.out.println("ORDERS");
        System.out.println("------");
        System.out.println();
        OrderMgr om = OrderMgr.getInstance();
        om.printOrders();

        om.processOrders();

        System.out.println("-------------------------------------------");
        System.out.println("WAREHOUSES INVENTORY AFTER ORDER PROCESSING");
        System.out.println("-------------------------------------------");
        System.out.println();
        WarehouseMgr.getInstance().printWarehouses();


        //ORDER MANIFESTS (Includes - Associated Routes / Travel Times with Processing Time)
        System.out.println();
        System.out.println("----------------------------------------------------------");
        System.out.println("//*** ORDER MANIFESTS ***//");
        System.out.println("---------------------------");
        System.out.println();

        om.printManifests();

        System.out.println();
        System.out.println();
        System.out.println("---------------------------");
        System.out.println("View 'log.txt' For Error Messages");
    }
}
