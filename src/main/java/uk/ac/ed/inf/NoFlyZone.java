package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class NoFlyZone {
    @JsonProperty("name")
    public String name;
    @JsonProperty("coordinates")
    public Double[][] coordinates;

    public Area getArea(){
        Point[] points = new Point[coordinates.length];
        int i = 0;
        for(Double[] coordinate: coordinates){
            points[i] = new Point(coordinate[0],coordinate[1]);
            i++;
        }
        Area area = new Area(points);
        return area;
    }
}
