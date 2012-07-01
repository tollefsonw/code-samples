
package edu.se452;

import java.io.Serializable;

/**
 *
 * @author wadetollefson
 */
public class HoundBean implements Serializable{

    private static final long serialVersionUID = -8360344691931165807L;

    private String name;
    private String gender;
    private String age;
    private String weight;

    public HoundBean()
    {
        name = "";
        gender = "";
        age = "";
        weight = "";
    }

    public HoundBean(String n, String g, String a, String w)
    {
        name = n;
        gender = g;
        age = a;
        weight = w;
    }

    public void setName(String n){
        name = n;
    }

    public void setGender(String g){
        gender = g;
    }

    public void setAge (String a){
        age = a;
    }

    public void setWeight(String w){
        weight = w;
    }

    public String getName(){
        return this.name;
    }

    public String getGender(){
        return this.gender;
    }

    public String getAge(){
        return this.age;
    }

    public String getWeight(){
        return this.weight;
    }
}
