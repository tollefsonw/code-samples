/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package manifests;

import java.util.StringTokenizer;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public class ContainerManifest {
    
    private String containerManifestID;
    private String containerManifestInfo = "";
    
    public ContainerManifest(String containerID) throws InvalidParameterException
    {
        setContainerManifestID(containerID);
    }

    public String getContainerManifestID()
    {
        return containerManifestID;
    }

    public String getContainerManifestInfo()
    {
        return containerManifestInfo;
    }
    
    public void addContainerManifestInfo(String info) throws InvalidParameterException
    {
        if (info == null)
            throw new InvalidParameterException("Error in Class 'ContainerManifest'"
                                + "in Method 'setItemID' - Invalid Paramter:NULL");
         containerManifestInfo += info + " ";
    }

    private void setContainerManifestID(String containerID) throws InvalidParameterException
    {
        if (containerID == null)
            throw new InvalidParameterException("Error in Class 'ContainerManifest'"
                                + "in Method 'setContainerManifestID' - Invalid Paramter:NULL");
        containerManifestID = containerID;
    }
    
    public String containerManifestToString()
    {
        StringTokenizer st = new StringTokenizer(containerManifestInfo);
        String info =  "Containers: " + containerManifestID + "\n" +
               "Item  " + "QtyPerPack  " +   "NumberOfPacks\n";
        while (st.hasMoreTokens())
        {
            for (int i=1; i<=3; i++)
            {
                info += st.nextToken() + "         ";
            }
            info += "\n";
        }
        return info;
    }
}
