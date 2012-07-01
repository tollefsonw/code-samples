/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

/**
 *
 * @author wadetollefson
 */
public interface Item {

    public String getItemID();

    public String getDescription();

    public double getLength();

    public double getHeight();

    public double getWidth();

    public double getWeight();

    public int getQtyPerPack();

    public String itemToString();

}
