package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightPath { //Flight path class to parse with the flightpath.json file
    @JsonProperty("orderNo")
    public String orderNo;
    @JsonProperty("fromLongitude")
    public Double fromLongitude;
    @JsonProperty("fromLatitude")
    public Double fromLatitude;
    @JsonProperty("angle")
    public Double angle;
    @JsonProperty("toLongitude")
    public Double toLongitude;
    @JsonProperty("toLatitude")
    public Double toLatitude;
    @JsonProperty("ticksSinceStartOfCalculation")
    public Integer ticksSinceStartOfCalculation;

    public FlightPath(String orderNo, Double fromLongitude, Double fromLatitude, Double angle, Double toLongitude, Double toLatitude, Integer ticksSinceStartOfCalculation){
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
        this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }
}
