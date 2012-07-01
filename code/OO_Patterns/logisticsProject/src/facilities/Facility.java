/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package facilities;

import java.util.HashSet;
import utilities.InvalidParameterException;

/**
 *
 * @author wadetollefson
 */
public interface Facility {

    String getID();

    int getContainersProcessedPerDay();

    HashSet<Integer> getSchedule();

    void bookSchedule(int day)
            throws InvalidParameterException;
}
