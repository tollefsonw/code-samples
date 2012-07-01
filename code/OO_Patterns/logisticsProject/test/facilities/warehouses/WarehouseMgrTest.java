/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
public class WarehouseMgrTest {

    public WarehouseMgrTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class WarehouseMgr.
     */
    @Test
    public void testGetInstance() throws Exception {
        assertTrue(WarehouseMgr.getInstance() != null);
    }

    /**
     * Test of isValidWarehouse method, of class WarehouseMgr.
     */
    @Test
    public void testIsValidWarehouse() throws IOException, InvalidParameterException {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        assertTrue(WarehouseMgr.getInstance().isValidWarehouse("WarehouseA"));
    }

    /**
     * Test of getWarehousesInfo method, of class WarehouseMgr.
     */
    @Test
    public void testGetWarehousesInfo() throws IOException, InvalidParameterException {
        HashMap<String,Warehouse> testInfo = new HashMap<String,Warehouse>();
        WarehouseMgr.getInstance().resetWarehouseMgr();
        testInfo = WarehouseMgr.getInstance().getWarehousesInfo();
        assertTrue(testInfo.containsKey("WarehouseA"));
    }

    /**
     * Test of getPreferredWarehousesList method, of class WarehouseMgr.
     */
    @Test
    public void testGetPreferredWarehousesList() throws IOException, InvalidParameterException {
        ArrayList<String> testInfo = new ArrayList<String>();
        WarehouseMgr.getInstance().resetWarehouseMgr();
        testInfo = WarehouseMgr.getInstance().getPreferredWarehousesList();
        assertTrue(testInfo.contains("WarehouseA"));
    }

    /**
     * Test of decreaseWarehouseInventory method, of class WarehouseMgr.
     */
    @Test
    public void testDecreaseWarehouseInventory() throws Exception {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        try{
            WarehouseMgr.getInstance().decreaseWarehouseInventory("WarehouseX", null);
            fail();
        }
        catch (InvalidParameterException ipe)
        {

        }
    }

    /**
     * Test of getContainerManifests method, of class WarehouseMgr.
     */
    @Test
    public void testGetContainerManifests() throws IOException, InvalidParameterException {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        assertEquals(WarehouseMgr.getInstance().getContainerManifests().values().size(),0);
    }

    /**
     * Test of getContainersProcessedPerDay method, of class WarehouseMgr.
     */
    @Test
    public void testGetContainersProcessedPerDay() throws Exception {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        assertEquals(WarehouseMgr.getInstance().
                getContainersProcessedPerDay("WarehouseA"),2);
    }

    /**
     * Test of getWarehouseSchedule method, of class WarehouseMgr.
     */
    @Test
    public void testGetWarehouseSchedule() throws Exception {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        assertEquals(WarehouseMgr.getInstance().getWarehouseSchedule("WarehouseA").size(),0);
    }

    /**
     * Test of setWarehouseSchedule method, of class WarehouseMgr.
     */
    @Test
    public void testSetWarehouseSchedule() throws Exception {
        WarehouseMgr.getInstance().resetWarehouseMgr();
        try{
            WarehouseMgr.getInstance().setWarehouseSchedule("WarehouseX", 7);
        }
        catch(InvalidParameterException ipe){

        }
    }

    /**
     * Test of setContainerAsUnavailable method, of class WarehouseMgr.
     */
    @Test
    public void testSetContainerAsUnavailable() throws Exception {
        ContainerImpl test = new ContainerImpl("WarehouseA1");
        WarehouseMgr.getInstance().resetWarehouseMgr();
        WarehouseMgr.getInstance().setContainerAsUnavailable(test.getID());
        assertFalse(!test.isContainerAvailable());
    }

}