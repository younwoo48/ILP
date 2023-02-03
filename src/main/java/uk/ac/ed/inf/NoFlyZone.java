package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoFlyZone {
    @JsonProperty("name")
    private String name;
    @JsonProperty("coordinates")
    private Double[][] coordinates;

    /**
     * Changes the instance of this NoFlyZone to an Area instance
     * @return
     */
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
