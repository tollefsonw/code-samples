/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.ports;

import java.util.HashSet;
import java.util.Iterator;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class PortImpl implements Port{

    private String portID;
    private String portType;
    private int containersProcessedPerDay;
    private HashSet<Integer> schedule = new HashSet<Integer>();

    public PortImpl(String id, String type, int perDay) throws InvalidParameterException
    {
        setPortID(id);
        setPortType(type);
        setContainersProcessedPerDay(perDay);
    }

    public String getID()
    {
        return portID;
    }

    public String getPortType()
    {
        return portType;
    }

    public int getContainersProcessedPerDay()
    {
        return containersProcessedPerDay;
    }

    public HashSet<Integer> getSchedule() 
    {
        HashSet<Integer> transfer = new HashSet<Integer>();
        Iterator<Integer> it = schedule.iterator();
        while (it.hasNext())
            transfer.add(it.next());
        return transfer;
    }
    
    public void bookSchedule(int day) throws InvalidParameterException
    {
        if (day <= 0)
           throw new InvalidParameterException("Error in Class 'PortImpl'"
                                + "in Method 'bookSchedule' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + day);
        schedule.add(day);
    }

    private void setPortID(String id) throws InvalidParameterException
    {
        if (id == null)
            throw new InvalidParameterException("Error in Class 'PortImpl'"
                                + "in Method 'setPortID' - Invalid Paramter:NULL");
        portID = id;
    }

    private void setPortType(String type) throws InvalidParameterException
    {
        if (type == null)
            throw new InvalidParameterException("Error in Class 'PortImpl'"
                                + "in Method 'setPortType' - Invalid Paramter:NULL");
        portType = type;
    }

    private void setContainersProcessedPerDay(int perDay) throws InvalidParameterException
    {
        if (perDay <= 0)
           throw new InvalidParameterException("Error in Class 'PortImpl'"
                                + "in Method 'setContainersProcessedPerDay' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + perDay);
        containersProcessedPerDay = perDay;
    }

    public String portToString()
    {
        String port = "Port ID: " + portID + "\n"
                + "Port Type: " + portType + "\n"
                + "Containers Processed Per Day: " + containersProcessedPerDay + "\n";
        return port;
    }
}
