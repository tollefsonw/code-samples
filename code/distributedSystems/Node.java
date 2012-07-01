/**
 * This project is an implementation of a breadth-first 
 * search with a map-reduce algorithm that uses Hadoop.
 * @author Wade Tollefson
 * @version 1.0.1
 * @see GraphSearch
 */

import java.util.*;

import org.apache.hadoop.io.Text;

public class Node 
{
  private final int id;
  private int distance;
  private List<Integer> edges = new ArrayList<Integer>();
  private String color = "white";

  public Node(String str) {

    String[] map = str.split("\t");
    String key = map[0];
    String value = map[1];

    String[] tokens = value.split("\\|");

    this.id = Integer.parseInt(key);

    for (String s : tokens[0].split(",")) {
      if (s.length() > 0) {
        edges.add(Integer.parseInt(s));
      }
    }
    
    if (tokens[1].equals("999")) {
      this.distance = 999;
    } else {
      this.distance = Integer.parseInt(tokens[1]);
    }  
    this.color = tokens[2];
  }

  public Node(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  public int getDistance() {
    return this.distance;
  }

  public void setDistance(int distance) {
    this.distance = distance;
  }

  public String getColor() {
    return this.color;
  }

  public void setColor(String color) {
    this.color = color;
  }
  
  public List<Integer> getEdges() {
    return this.edges;
  }

  public void setEdges(List<Integer> edges) {
    this.edges = edges;
  }

  public Text getLine() {
    StringBuffer s = new StringBuffer();
    
    for (int v : edges) {
      s.append(v).append(",");
    }
    s.append("|");

    if (this.distance < 999) {
      s.append(this.distance).append("|");
    } else {
      s.append("999").append("|");
    }
    s.append(color);
    return new Text(s.toString());
  }
}
