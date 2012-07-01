/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.ports;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import utilities.ErrorCheckUtility;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class PortMgr {

    private static PortMgr instance;
    private HashMap<String,Port> portList = new HashMap<String,Port>();
    

	private PortMgr() throws IOException
	{
            loadPorts();
	}

	public static PortMgr getInstance() throws IOException
	{
            if (instance == null)
                instance = new PortMgr();
            return instance;
	}

	private void loadPorts() throws IOException
	{
            String portInfo = "ports.txt";
            ErrorCheckUtility errorCheck = new ErrorCheckUtility();
            BufferedWriter fstream = new BufferedWriter(new FileWriter("log.txt",true));
            BufferedReader input =  new BufferedReader(new FileReader(portInfo));
            if (input == null)
            {
                fstream.write("Error in Class 'PortMgr'"
                                + "in Method 'loadPorts' - Port Information Does Not Exist "
                                + "Check File 'ports.txt'\r\n");
            }
            String port;
            while ((port = input.readLine()) != null)
            {
                StringTokenizer st = new StringTokenizer(port);
                if (inputPortDataIsValid(st.countTokens()))
                {
                    String portID = st.nextToken();
                    String portType = st.nextToken();
                    String temp1 = st.nextToken();
                    if (errorCheck.isIntNumber(temp1))
                    {
                    int containersPerDay = Integer.parseInt(temp1);
                    try{
                    PortImpl newPort = new PortImpl(portID,portType,containersPerDay);
                    portList.put(portID, newPort);
                        }
                    catch(InvalidParameterException ipe)
                    {
                        fstream.write(ipe.getMessage()+"\r\n");
                    }
                    }
                    else
                    {
                        fstream.write("Error in Class 'PortMgr'"
                                + "in Method 'loadPorts' - Invalid Data: Order Data Parameters "
                                + "Containers Per Day Must Be Numeric\r\n");
                    }
                }
                else
                {
                    fstream.write("Error in Class 'PortMgr'"
                                + "in Method 'loadItems' - Invalid Port: "
                                + "Incorrect Port Information Format\r\n");
                }
            }
	}

        public int getContainersProcessedPerDay(String portID)
        {
            int containersPerDay =
                    portList.get(portID).getContainersProcessedPerDay();
            return containersPerDay;
        }

        public HashSet<Integer> getPortSchedule(String portID) throws InvalidParameterException
        {
            if (isValidPort(portID))
            {
               return portList.get(portID).getSchedule();
            }
            throw new InvalidParameterException("Error in Class 'PortMgr'"
                                + "in Method 'getPortSchedule' - Port ID Is Not Valid:"
                                + " Was, " + portID);
        }

        public void setPortSchedule(String portID, int day)
                throws InvalidParameterException
        {
            if (isValidPort(portID))
            {
                portList.get(portID).bookSchedule(day);
            }
            throw new InvalidParameterException("Error in Class 'PortMgr'"
                                + "in Method 'setPortSchedule' - Port ID Is Not Valid:"
                                + " Was, " + portID);        }

	private boolean inputPortDataIsValid(int fields)
        {
            if (fields != 3)
                return false;
            return true;
        }

	private boolean isValidPort(String id)
	{
            return portList.containsKey(id);
	}

        public void printPorts()
	{
		for (Port port: portList.values())
			System.out.println(port.portToString());
	}

}
