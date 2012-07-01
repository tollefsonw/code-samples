/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities.warehouses;

import java.util.HashMap;
import utilities.InvalidParameterException;


/**
 *
 * @author wadetollefson
 */
public interface Container {

    String getID();

    double getLength();

    double getHeight();

    double getWidth();

    double getMaxWeight();

    double getCurrentWeight();

    double getMaxVolume();

    double getCurrentAvailableVolume();

    void reduceAvailableVolume(double volume) throws InvalidParameterException;

    void addWeight(double weight) throws InvalidParameterException;

    boolean isContainerAvailable();

    void setContainerInUse(boolean available);

    void addItem(String id, int qnty) throws InvalidParameterException;

    HashMap<String,Integer> getContainerContents();

    String containerToString();
}
