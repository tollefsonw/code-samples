/**
 * This project is an implementation of a breadth-first 
 * search with a map-reduce algorithm that uses Hadoop.
 * @author Wade Tollefson
 * @version 1.0.1
 * @see Node
 */

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

@SuppressWarnings("deprecation")
public class GraphSearch extends Configured implements Tool 
{
  static enum Counter { MORE_WORK };

  public static class MapClass extends MapReduceBase implements
      Mapper<LongWritable, Text, IntWritable, Text> 
  {
	
    public void map(LongWritable key, Text value, OutputCollector<IntWritable, Text> output,
        Reporter reporter) throws IOException 
    {
      Node node = new Node(value.toString());

      if (node.getColor().equalsIgnoreCase("gray")) {
        for (int v : node.getEdges()) {
          Node vnode = new Node(v);
          vnode.setDistance(node.getDistance() + 1);
          vnode.setColor("gray");
          output.collect(new IntWritable(vnode.getId()), vnode.getLine());
        }
        node.setColor("black");
      }
      output.collect(new IntWritable(node.getId()), node.getLine());
    }
  }

  public static class Reduce extends MapReduceBase implements
      Reducer<IntWritable, Text, IntWritable, Text> 
  {
	  
    public void reduce(IntWritable key, Iterator<Text> values,
        OutputCollector<IntWritable, Text> output, Reporter reporter) throws IOException
    {
      List<Integer> edges = null;
      int distance = 999;
      String color = "white";

      while (values.hasNext()) 
      {
        Text value = values.next();

        Node u = new Node(key.get() + "\t" + value.toString());

        if (u.getEdges().size() > 0) {
          edges = u.getEdges();
        }

        if (u.getDistance() < distance) {
          distance = u.getDistance();
        }

        if (u.getColor().equalsIgnoreCase("black")) 
          color = u.getColor();
        else if (u.getColor().equalsIgnoreCase("gray") && !color.equalsIgnoreCase("black"))
          color = u.getColor();
      }
      Node n = new Node(key.get());
      n.setDistance(distance);
      n.setEdges(edges);
      n.setColor(color);
      output.collect(key, new Text(n.getLine()));
      
      if(color.equalsIgnoreCase("gray"))
    	  reporter.incrCounter(Counter.MORE_WORK, 1);
    }
  }

  public int run(String[] args) throws Exception 
  {
    int iterationCount = 0;
    boolean keepGoing = true;
    while (keepGoing) 
    {
      String input;
      if (iterationCount == 0)
        input = "input-graph";
      else
        input = "output-graph-" + iterationCount;

      String output = "output-graph-" + (iterationCount + 1);
      iterationCount++;
      JobConf conf = new JobConf(getConf(), GraphSearch.class);
      conf.setJobName("graphsearch");
      conf.setOutputKeyClass(IntWritable.class);
      conf.setOutputValueClass(Text.class);
      conf.setMapperClass(MapClass.class);
      conf.setReducerClass(Reduce.class);
      FileInputFormat.setInputPaths(conf, new Path(input));
      FileOutputFormat.setOutputPath(conf, new Path(output));
      RunningJob job = JobClient.runJob(conf);
      
      org.apache.hadoop.mapreduce.Counter counter 
      				= job.getCounters().findCounter(GraphSearch.Counter.MORE_WORK);
      if(counter.getValue() == 0)
    	  keepGoing = false;
    }
    return 0;
  }

  public static void main(String[] args) throws Exception 
  { ToolRunner.run(new Configuration(), new GraphSearch(), args); }
}
