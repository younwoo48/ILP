package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Area {
    @JsonProperty("name")
    public String name;
    @JsonProperty("coordinates")
    public Point[] coordinates;

    public Area(Point[] coordinates){this.coordinates = coordinates;}

    public Point[] getPoints() {
        return coordinates;
    }

}
