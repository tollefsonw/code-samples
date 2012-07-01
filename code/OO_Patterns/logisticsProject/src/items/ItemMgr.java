/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import utilities.ErrorCheckUtility;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class ItemMgr {

    private static ItemMgr instance;
    private HashMap<String, Item> catalog = new HashMap<String, Item>();

	private ItemMgr() throws IOException
	{
            loadItems();
	}

	public static ItemMgr getInstance() throws IOException
	{
            if (instance == null)
                instance = new ItemMgr();
            return instance;
	}

	private void loadItems() throws IOException
	{
            String items = "items.txt";
            ErrorCheckUtility errorCheck = new ErrorCheckUtility();
            BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
            BufferedReader input =  new BufferedReader(new FileReader(items));
            if (input == null)
            {
                fstream.write("Error in Class 'ItemMgr'"
                                + "in Method 'laodItems' - Item Information Does Not Exist "
                                + "Check File 'items.txt'\r\n");
            }
            String item;
            while ((item = input.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(item);
                int numOfFields = st.countTokens();
                if (isValidItem(numOfFields))
                {
                String itemID = st.nextToken();
                String description = st.nextToken();
                String temp1 = st.nextToken();
                String temp2 = st.nextToken();
                String temp3 = st.nextToken();
                String temp4 = st.nextToken();
                String temp5 = st.nextToken();
                if (errorCheck.isDoubleNumber(temp1) && errorCheck.isDoubleNumber(temp2) &&
                        errorCheck.isDoubleNumber(temp3) && errorCheck.isDoubleNumber(temp4) &&
                        errorCheck.isIntNumber(temp5))
                {
                double length = Double.parseDouble(temp1);
                double width = Double.parseDouble(temp2);
                double height = Double.parseDouble(temp3);
                double weight = Double.parseDouble(temp4);
                int quantityPerPack = Integer.parseInt(temp5);
                try{
                ItemImpl newItem = new ItemImpl(itemID, description, length,
                        width, height, weight, quantityPerPack);
                catalog.put(newItem.getItemID(), newItem);
                    }
                catch(InvalidParameterException ipe)
                {
                    fstream.write(ipe.getMessage()+"\r\n");
                }
                }
                else
                {
                    fstream.write("Error in Class 'ItemMgr'"
                                + "in Method 'loadItems' - Invalid Data: Item Data Parameters "
                                + "Must Be Numeric\r\n");
                }
                }
                else
                {
                    String error = "Error in Class 'ItemMgr'"
                                + "in Method 'loadItems' - Invalid Data Format: "
                                + "Must Have Seven Fields\r\n";
                    fstream.write(error);
                    fstream.flush();
                }
            }
	}

	public boolean isValidItem(int numOfFields)
        {
            if (numOfFields != 7)
                return false;
            return true;
        }

	public boolean isItem(String id)
	{
            return catalog.containsKey(id);
	}

	public ItemDTO getItem(String id) throws InvalidParameterException
	{
		if (!isItem(id))
			throw new InvalidParameterException("Error in Class 'ItemMgr'"
                                + "in Method 'getItem' - Invalid Paramter: " + id);
		Item DTO = catalog.get(id);
		return new ItemDTO(DTO.getItemID(), DTO.getDescription(), DTO.getLength(),
                        DTO.getWidth(), DTO.getHeight(),
                        DTO.getWeight(), DTO.getQtyPerPack());
	}

        public void printCatalog()
	{
		for (Item item: catalog.values())
			System.out.println(item.itemToString());
	}

}
