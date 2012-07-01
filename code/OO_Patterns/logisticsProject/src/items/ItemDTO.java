/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package items;

/**
 *
 * @author wadetollefson
 */
public class ItemDTO {

    public String itemID;
    public String description;
    public double length;
    public double width;
    public double height;
    public double weight;
    public int quantityPerPack;

    public ItemDTO(String id, String desc, double len, double wid, double hgt,
                        double wgt, int qtyPerPack)
    {
        itemID = id;
        description = desc;
        height = hgt;
        width = wid;
        length = len;
        weight = wgt;
        quantityPerPack = qtyPerPack;
    }
}
