/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package logistics;

import facilities.ports.PortMgr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import transportLinks.TransportLink;
import transportLinks.TransportLinkMgr;
import utilities.InvalidParameterException;
import facilities.warehouses.WarehouseMgr;

/**
 *
 * @author wadetollefson
 */
public class DeliveryMgr {

    private static DeliveryMgr instance;
    private ArrayList<ArrayList<TransportLink>> travelPaths = new ArrayList<ArrayList<TransportLink>>();

    private DeliveryMgr(){}

	public static DeliveryMgr getInstance()
	{
            if (instance == null)
                instance = new DeliveryMgr();
            return instance;
	}

        public HashMap<String,Integer> shipOrder(String source,
                    String destination, int numberOfContainers)
                        throws IOException, InvalidParameterException
        {
            travelPaths.clear();
            ArrayList<TransportLink> path = new ArrayList<TransportLink>();
            findTravelPaths(source,destination,path);
            HashMap<ArrayList<TransportLink>,Integer> route = 
                    findShortestTravelPath(travelPaths,numberOfContainers);
            bookFacilities(route,numberOfContainers);
            HashMap<String,Integer> pathAndTravelTime = getPathAndTravelTime(route,destination);
            return pathAndTravelTime;
        }

	private void findTravelPaths(String source, String destination, ArrayList<TransportLink> path)
                throws IOException, InvalidParameterException
        {
            ArrayList<TransportLink> links =
                    TransportLinkMgr.getInstance().getTransportLinksFromSource(source);
            for (TransportLink link : links)
            {
                path.add(link);
                if (link.getDestination().equalsIgnoreCase(destination))
                    travelPaths.add((ArrayList<TransportLink>) path.clone());
                else
                {
                    findTravelPaths(link.getDestination(),destination,path);
                    path.remove(link);
                }
            }

        }

        private HashMap<ArrayList<TransportLink>,Integer>
                findShortestTravelPath(ArrayList<ArrayList<TransportLink>> paths,
                                            int numberOfContainers)
                                                throws IOException, InvalidParameterException
        {
            int totalTravelTime = 0;
            ArrayList<TransportLink> bestpath = null;
            Iterator<ArrayList<TransportLink>> it = paths.iterator();
            while(it.hasNext())
            {
                int travelTime = 0;
                ArrayList<TransportLink> path = it.next();
                for (TransportLink link : path)
                    travelTime += link.getTravelTime() + 
                                calculateProcessingTime(link.getSource(),numberOfContainers);
                if ((totalTravelTime == 0) || (travelTime < totalTravelTime)) //what about equal
                {
                    totalTravelTime = travelTime;
                    bestpath = path;
                }

            }
            HashMap<ArrayList<TransportLink>,Integer> route =
                    new HashMap<ArrayList<TransportLink>,Integer>();
            route.put(bestpath, totalTravelTime);
            return route;
        }

        private int calculateProcessingTime(String location, int numberOfContainers)
                throws IOException,  InvalidParameterException
        {
           if (WarehouseMgr.getInstance().isValidWarehouse(location))
           {
               int containersPerDay =
                       WarehouseMgr.getInstance().getContainersProcessedPerDay(location);
               int daysToProcess = (int)Math.ceil(numberOfContainers / containersPerDay);
               HashSet<Integer> schedule = WarehouseMgr.getInstance().getWarehouseSchedule(location);
               int day = 1;
               while (daysToProcess > 0)
               {
                   if (!schedule.contains(day))
                   {
                        daysToProcess--;
                        day++;
                   }
                   day++;
               }
               return day;
           }
           else
           {

               int containersPerDay =
                       PortMgr.getInstance().getContainersProcessedPerDay(location);
               int daysToProcess = (int)Math.ceil(numberOfContainers / containersPerDay);
               HashSet<Integer> schedule = PortMgr.getInstance().getPortSchedule(location);
               int day = 1;
               while (daysToProcess > 0)
               {
                   if (!schedule.contains(day))
                   {
                        daysToProcess--;
                        day++;
                   }
                   day++;
               }
               return day;
           }
        }

        private void bookFacilities(HashMap<ArrayList<TransportLink>,Integer> shortestPath,
                                            int numberOfContainers)
                                                   throws IOException, InvalidParameterException
        {
            ArrayList<TransportLink> route = new ArrayList<TransportLink>();
            Set<ArrayList<TransportLink>> info = shortestPath.keySet();
            Iterator<ArrayList<TransportLink>> it = info.iterator();
            route = it.next();
            for (TransportLink link : route)
            {
                String location = link.getSource();
                if (WarehouseMgr.getInstance().isValidWarehouse(location))
                {
                    int containersPerDay =
                       WarehouseMgr.getInstance().getContainersProcessedPerDay(location);
                    int daysToProcess = (int)Math.ceil(numberOfContainers / containersPerDay);
                    HashSet<Integer> schedule = WarehouseMgr.getInstance().getWarehouseSchedule(location);
                    int day = 1;
                    while (daysToProcess > 0)
                    {
                        if (!schedule.contains(day))
                        {
                            WarehouseMgr.getInstance().setWarehouseSchedule(location,day);
                            daysToProcess--;
                            day++;
                        }
                        day++;
                    }
                }
                else
                {
                    int containersPerDay =
                       PortMgr.getInstance().getContainersProcessedPerDay(location);
                    int daysToProcess = (int)Math.ceil(numberOfContainers / containersPerDay);
                    HashSet<Integer> schedule = PortMgr.getInstance().getPortSchedule(location);
                    int day = 1;
                    while (daysToProcess > 0)
                    {
                        if (!schedule.contains(day))
                        {
                            PortMgr.getInstance().setPortSchedule(location, day);
                            daysToProcess--;
                            day++;
                        }
                        day++;
                    }
                }
            }
        }

        private HashMap<String,Integer> getPathAndTravelTime
                    (HashMap<ArrayList<TransportLink>,Integer> path,
                            String destination)
        {
            String course = "";
            ArrayList<TransportLink> route = new ArrayList<TransportLink>();
            Set<ArrayList<TransportLink>> info = path.keySet();
            Iterator<ArrayList<TransportLink>> it = info.iterator();
            route = it.next();
            for (TransportLink link : route)
            {
                course += link.getSource()+"--";
            }
            course += destination;
            int travelTime = path.get(route);
            HashMap<String,Integer> pathAndTravelTime = new HashMap<String,Integer>();
            pathAndTravelTime.put(course, travelTime);
            return pathAndTravelTime;
        }

        //private calculateTravelCosts - here or orderMgr

}
