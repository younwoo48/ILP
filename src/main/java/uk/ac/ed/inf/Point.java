package uk.ac.ed.inf;


import com.fasterxml.jackson.annotation.JsonProperty;

/*
Class to parse with the JSON
 */
public class Point {
    @JsonProperty("name")
    public String name;
    @JsonProperty("longitude")
    public double longitude;
    @JsonProperty("latitude")
    public double latitude;
}
