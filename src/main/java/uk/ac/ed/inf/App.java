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
    public ArrayList<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.East,Direction.EastNorthEast,Direction.NorthEast,Direction.NorthNorthEast,Direction.North,Direction.NorthNorthWest,Direction.NorthWest,Direction.WestNorthWest,
            Direction.West, Direction.WestSouthWest, Direction.SouthWest,Direction.SouthSouthWest,Direction.South,Direction.SouthSouthEast,Direction.SouthEast,Direction.EastSouthEast,Direction.East));

    private ArrayList<LngLat> reconstruct(HashMap<LngLat,LngLat> from, LngLat current){
        ArrayList<LngLat> path = new ArrayList<LngLat>(Arrays.asList(current));
        while(from.containsKey(current)){
            current = from.get(current);
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    public ArrayList<LngLat> findDirection (LngLat start, LngLat finish){

        ArrayList<LngLat> open = new ArrayList<LngLat>();
        open.add(start);
        HashMap<LngLat, LngLat> from = new HashMap<LngLat, LngLat>();
        HashMap<LngLat, Double> gScore = new HashMap<LngLat, Double>();
        gScore.put(start, 0.0);
        HashMap<LngLat, Double> fScore = new HashMap<LngLat, Double>();
        fScore.put(start, finish.distanceTo(start));
        while (!open.isEmpty()) {
            LngLat current = open.get(0);
            double least = fScore.get(current);
            for (LngLat node : open) {
                if (fScore.get(node) < least) {
                    current = node;
                    least = fScore.get(node);
                }
            }
            if (current.closeTo(finish)) {
                return reconstruct(from,current);
            }
            open.remove(current);
            for(Direction d: directions){
                double provisional_gScore = gScore.get(current)+LngLat.MOVE_DISTANCE;
                LngLat neighbor = current.nextPosition(d);
                if(gScore.containsKey(neighbor)){
                    if(provisional_gScore<gScore.get(neighbor)){
                        from.put(neighbor,current);
                        gScore.put(neighbor,provisional_gScore);
                        fScore.put(neighbor,provisional_gScore+neighbor.distanceTo(finish));
                        if(!open.contains(neighbor)){
                            open.add(neighbor);
                        }
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws MalformedURLException {

        System.out.printf();
    }


}
