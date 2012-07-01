/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class WarehouseImplTest {

    String warehouseID = "WarehouseA";
    int numOfContainers = 7;
    int containersPerDay  = 2;
    HashMap<String,Integer> stock = new HashMap<String,Integer>();
    ArrayList<Container> containers = new ArrayList<Container>();

    public WarehouseImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws InvalidParameterException {
       //WarehouseA 7 2 001 160 004 200
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getID method, of class WarehouseImpl.
     */
    @Test
    public void testGetID() throws InvalidParameterException {
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        assertEquals(instance.getID(), "WarehouseA");
    }

    /**
     * Test of setWarehouseID method, of class WarehouseImpl.
     */
    @Test
    public void testSetWarehouseID_Null() {
        try{
        WarehouseImpl instance = new WarehouseImpl(null,numOfContainers,
                                    containersPerDay,stock,containers);
        fail();
        }
        catch (InvalidParameterException ipe)
        {
        }
    }


    /**
     * Test of getContainersProcessedPerDay method, of class WarehouseImpl.
     */
    @Test
    public void testGetContainersProcessedPerDay() throws InvalidParameterException {
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        assertEquals(instance.getContainersProcessedPerDay(), 2);
    }

    /**
     * Test of getInventory method, of class WarehouseImpl.
     */
    @Test
    public void testGetInventory() throws InvalidParameterException {
       stock.put("001", 160);
       stock.put("004", 200);
       WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
       HashMap<String,Integer> inventory = new HashMap<String,Integer>();
       inventory = instance.getInventory();
       assertEquals(inventory.get("001"), instance.getInventory().get("001"));
    }

    /**
     * Test of getContainers method, of class WarehouseImpl.
     */
    @Test
    public void testGetContainers() throws InvalidParameterException {
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        assertEquals(instance.getContainers().size(),0);
    }

    /**
     * Test of getNumberofContainers method, of class WarehouseImpl.
     */
    @Test
    public void testGetNumberofContainers() throws InvalidParameterException {
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        assertEquals(instance.getNumberofContainers(), 7);
    }

    /**
     * Test of getSchedule method, of class WarehouseImpl.
     */
    @Test
    public void testGetSchedule() throws InvalidParameterException {
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        assertEquals(instance.getSchedule().size(),0);
    }

    /**
     * Test of bookSchedule method, of class WarehouseImpl.
     */
    @Test
    public void testBookSchedule() throws Exception {
        HashSet<Integer> schedule = new HashSet<Integer>();
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        instance.bookSchedule(7);
        schedule = instance.getSchedule();
        assertTrue(schedule.contains(7));
    }

    /**
     * Test of decreaseInventory method, of class WarehouseImpl.
     */
    @Test
    public void testDecreaseInventory() throws Exception {
        stock.clear();
        stock.put("001", 160);
        stock.put("004", 200);
        WarehouseImpl instance = new WarehouseImpl(warehouseID,numOfContainers,
                                    containersPerDay,stock,containers);
        instance.decreaseInventory("001", 40);
        HashMap<String,Integer> inventory = new HashMap<String,Integer>();
        inventory = instance.getInventory();
        int decreasedSum = inventory.get("001");
        int inventorySum = instance.getInventory().get("001");
        assertEquals(inventorySum,decreasedSum);
    }

}