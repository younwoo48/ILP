package uk.ac.ed.inf;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public ArrayList<Direction> findDirection(LngLat start,LngLat finish){
        ArrayList<LngLat> open = new ArrayList<LngLat>();
        open.add(start);
        HashMap<LngLat,Double> from = new HashMap<LngLat,Double>();
        HashMap<LngLat,Double> gScore = new HashMap<LngLat,Double>();
        gScore.put(start,0.0);
        HashMap<LngLat,Double> hScore = new HashMap<LngLat,Double>();
        hScore.put(start,finish.distanceTo(start));
        while(open.size()>0){
            LngLat current = open.get(0);
            double least = gScore.get(current)+hScore.get(current);
            for(LngLat node:open){
                if(node
            }
        }
    }


}
