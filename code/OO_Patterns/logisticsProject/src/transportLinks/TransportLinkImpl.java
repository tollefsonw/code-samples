/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transportLinks;

import utilities.InvalidParameterException;


/**
 *
 * @author wadetollefson
 */
public class TransportLinkImpl implements TransportLink{

    private String source; 
    private String destination;
    private String transitLinkType;
    private int travelTime;

    public TransportLinkImpl(String src, String dest, String type, int time)
            throws InvalidParameterException
    {
        setSource(src);
        setDestination(dest);
        setLinkType(type);
        setTravelTime(time);
    }

    public String getSource() 
    {
        return source;
    }

    public String getDestination() 
    {
        return destination;
    }

    public String getLinkType()
    {
        return transitLinkType;
    }

    public int getTravelTime()
    {
        return travelTime;
    }

    private void setSource(String src) throws InvalidParameterException
    {
        if (src == null)
            throw new InvalidParameterException("Error in Class 'TransportLinkImpl'"
                                + "in Method 'setSource' - Invalid Paramter:NULL");
        source = src;
    }

    private void setDestination(String dest) throws InvalidParameterException
    {
        if (dest == null)
            throw new InvalidParameterException("Error in Class 'TransportLinkImpl'"
                                + "in Method 'setDestination' - Invalid Paramter:NULL");
        destination = dest;
    }

    private void setLinkType(String type) throws InvalidParameterException
    {
        if (type == null)
            throw new InvalidParameterException("Error in Class 'TransportLinkImpl'"
                                + "in Method 'setLinkType' - Invalid Paramter:NULL");
        transitLinkType = type;
    }

    private void setTravelTime(int time) throws InvalidParameterException
    {
        if (time <= 0)
           throw new InvalidParameterException("Error in Class 'TransportLinkImpl'"
                                + "in Method 'setTravelTime' - Invalid Paramter:"
                                + "Must Be Greater Than Zero, But Was " + time);
        travelTime = time;
    }

    public String transportLinksToString()
    {
         String link = "Transport Source: " + source + "\n"
                + "Transport Destination: " + destination + "\n"
                + "Transport Link Type: " + transitLinkType + "\n"
                + "Transport Time in Days: " + travelTime + "\n";
        return link;
    }
}
