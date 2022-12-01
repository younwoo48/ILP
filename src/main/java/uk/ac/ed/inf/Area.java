package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Area {
    private Point[] points;
    public Area(Point[] points){
        this.points = points;
    }
    public Point[] getPoints() {
        return points;
    }
}
