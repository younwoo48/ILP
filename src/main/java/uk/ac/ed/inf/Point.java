package uk.ac.ed.inf;


import com.fasterxml.jackson.annotation.JsonProperty;

/*
Class to parse with the JSON
 */
public class Point { //Used to create the border for the areas including the noFlyZones and to parse with the centralArea
    @JsonProperty("name")
    private String name;
    @JsonProperty("longitude")
    public double longitude;
    @JsonProperty("latitude")
    public double latitude;
    public Point(double lng, double lat){
        this.longitude = lng;
        this.latitude = lat;
    }
}
