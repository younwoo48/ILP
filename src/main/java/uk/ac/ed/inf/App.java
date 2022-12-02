package uk.ac.ed.inf;


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

    private static ArrayList<LngLat> reconstruct(HashMap<LngLat,LngLat> from, LngLat current){
        ArrayList<LngLat> path = new ArrayList<LngLat>(Arrays.asList(current));
        while(from.containsKey(current)){
            current = from.get(current);
            path.add(current);
        }
        Collections.reverse(path);
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
        ArrayList<LngLat> open = new ArrayList<LngLat>();
        open.add(start);
        HashMap<LngLat, LngLat> from = new HashMap<LngLat, LngLat>();
        HashMap<LngLat, Double> gScore = new HashMap<LngLat, Double>();
        gScore.put(start, 0.0);
        HashMap<LngLat, Double> fScore = new HashMap<LngLat, Double>();
        fScore.put(start, finish.distanceTo(start));
        while (!open.isEmpty()) {
            LngLat current = open.get(0);
            double least = get(fScore,current);
            for (LngLat node : open) {
                if (fScore.get(node) < least) {
                    current = node;
                    least = fScore.get(node);
                }
            }
            System.out.println(current.lng);
            if (current.closeTo(finish)) {
                return reconstruct(from,current);
            }
            open.remove(current);
            for(Direction d: directions){
                double provisional_gScore = gScore.get(current)+LngLat.MOVE_DISTANCE;
                LngLat neighbor = current.nextPosition(d);
                if(containsHash(gScore,neighbor)){
                    if(provisional_gScore<get(gScore,neighbor)){
                        from.put(neighbor,current);
                        gScore = put(gScore,neighbor,provisional_gScore);
                        fScore = put(fScore,neighbor,provisional_gScore+neighbor.distanceTo(finish));
                        if(!containsArray(open,neighbor)){
                            open.add(neighbor);
                        }
                    }
                }
                else{
                    from.put(neighbor,current);
                    gScore = put(gScore,neighbor,provisional_gScore);
                    fScore = put(fScore,neighbor,provisional_gScore+neighbor.distanceTo(finish));
                    if(!containsArray(open,neighbor)){
                        open.add(neighbor);
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws MalformedURLException {
        Restaurant[] restaurants =  Restaurant.getRestaurantsFromRestServer("https://ilp-rest.azurewebsites.net");
        LngLat start = new LngLat(restaurants[0].longitude, restaurants[0].latitude);
        LngLat end = new LngLat(restaurants[1].longitude, restaurants[1].latitude);
        System.out.println(end.lng);
        System.out.println(end.lat);
        ArrayList<LngLat> path = findDirection(start,end);
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


    }


}
