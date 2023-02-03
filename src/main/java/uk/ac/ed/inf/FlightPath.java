package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlightPath { //Flight path class to parse with the flightpath.json file
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("fromLongitude")
    private Double fromLongitude;
    @JsonProperty("fromLatitude")
    private Double fromLatitude;
    @JsonProperty("angle")
    private Double angle;
    @JsonProperty("toLongitude")
    private Double toLongitude;
    @JsonProperty("toLatitude")
    private Double toLatitude;
    @JsonProperty("ticksSinceStartOfCalculation")
    private Integer ticksSinceStartOfCalculation;

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
