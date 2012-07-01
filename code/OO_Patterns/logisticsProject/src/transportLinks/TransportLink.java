/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transportLinks;

/**
 *
 * @author wadetollefson
 */
public interface TransportLink {

    String getSource();  

    String getDestination(); 

    String getLinkType();

    int getTravelTime();

    String transportLinksToString();

}

