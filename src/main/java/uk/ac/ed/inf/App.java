package uk.ac.ed.inf;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Hello world!
 */
public class App {
    public static ArrayList<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.East,Direction.EastNorthEast,Direction.NorthEast,Direction.NorthNorthEast,Direction.North,Direction.NorthNorthWest,Direction.NorthWest,Direction.WestNorthWest,
            Direction.West, Direction.WestSouthWest, Direction.SouthWest,Direction.SouthSouthWest,Direction.South,Direction.SouthSouthEast,Direction.SouthEast,Direction.EastSouthEast,Direction.East));
    public static NoFlyZone[] no_fly_areas;
    private static void noFlyAreas(){
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL("https://ilp-rest.azurewebsites.net"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        no_fly_areas = restClient.deserialize("/noFlyZones",NoFlyZone[].class);

    }
    private static boolean isAllowed(LngLat current, LngLat neighbor){
        for(NoFlyZone noFlyZone:no_fly_areas){
            if(noFlyZone.intersects(current,neighbor)){
                return false;
            }
        }
        return true;
    }



    private static ArrayList<LngLat> reconstruct(Node goal, Node start){
        ArrayList<LngLat> path = new ArrayList<LngLat>();
        while(!goal.equals(start)){
            path.add(goal.lnglat);
            goal = goal.comeFrom;
        }
        return path;
    }
    public static boolean containsHash(HashMap<LngLat,Double> map, LngLat target){
        for(LngLat node: map.keySet()){
            if(node.veryClose(target)){
                return true;
            }
        }
        return false;
    }
    public static boolean containsArray(ArrayList<LngLat> map, LngLat target){
        for(LngLat node: map){
            if(node.veryClose(target)){
                return true;
            }
        }
        return false;
    }
    public static HashMap<LngLat,Double> put(HashMap<LngLat,Double> map, LngLat target, double value){
        for(LngLat node: map.keySet()){
            if(node.veryClose(target)){
                map.put(node,value);
                return map;
            }
        }
        map.put(target,value);
        return map;
    }
    public static Double get(HashMap<LngLat,Double> map, LngLat target){
        for(LngLat node: map.keySet()){
            if(node.veryClose(target)){
                return map.get(node);
            }
        }
        return null;
    }


    public static ArrayList<LngLat> findDirection (LngLat start, LngLat finish){
        ArrayList<Node> open = new ArrayList<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        Node start_node = new Node(start,finish);
        open.add(start_node);

        while (!open.isEmpty()) {
            Node current = open.get(0);
            double least = current.fScore;
            for(Node node: open){
                if(node.fScore<least){
                    current = node;
                    least = node.fScore;
                }
            }
            System.out.println(current.lnglat.lng);
            if (current.lnglat.closeTo(finish)) {
                return reconstruct(current,start_node);
            }
            open.remove(current);
            for(Direction d: directions){
                Node neighbor = new Node(current, d, finish);
                if(isAllowed(current.lnglat,neighbor.lnglat)) {
                    open.add(neighbor);
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws MalformedURLException, JsonProcessingException {
        Restaurant[] restaurants =  Restaurant.getRestaurantsFromRestServer("https://ilp-rest.azurewebsites.net");
        LngLat start = new LngLat(restaurants[1].longitude, restaurants[1].latitude);
        LngLat end = new LngLat(restaurants[2].longitude, restaurants[2].latitude);
        System.out.println(end.lng);
        System.out.println(end.lat);
        noFlyAreas();
        /**ArrayList<LngLat> path = findDirection(start,end);
        for(LngLat node:path){
            System.out.println(node.lng);
            System.out.println(node.lat);
        }
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL("https://ilp-rest.azurewebsites.net"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        restClient.createFlightPath(path);
        System.out.println(no_fly_areas[0].coordinates[0][0]);
        */
        NoFlyZone
    }


}
