package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.Math;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Math.min;

public class LngLat {
    public double lng;
    public double lat;
    public static final double TOLERANCE = 0.00015;
    public static final double MOVE_DISTANCE = 0.00015;

    public LngLat(@JsonProperty("longitude") double lng, @JsonProperty("latitude") double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * @param x,y         The coordinate of the point to check
     * @param x1,y1,x2,y2 The coordinates of the two points making the edge
     * @return True if the right vertical line that passes through x,y meet the edge.
     */
    private boolean passEdge(double x, double y, double x1, double y1, double x2, double y2) {
        double boty = Math.min(y1, y2);
        double topy = Math.max(y1, y2);
        if (y <= boty || y >= topy) {
            return false;
        }
        if (x2 == x1) {
            if (x1 > x) {
                return true;
            }
            return false;
        }
        double a = (y2 - y1) / (x2 - x1);
        double b = y1 - a * x1;
        double p = (y - b) / a;
        if (p > x) {
            return true;
        }
        return false;
    }

    /**
     * inCentralArea
     * true if the point is in Central Area else false. Uses Basic Ray tracing algorithm to see if the point is in the Polygon.
     *
     * @return Boolean type
     */
    public boolean inCentralArea() {
        var corners = CentralArea.getInstance("").getArea().getPoints();
        int n = corners.length;
        if (n >= 3) {
            int edge_passes = 0;
            for (int i = 0; i < n; i++) {
                if (passEdge(this.lng, this.lat, corners[i].longitude, corners[i].latitude, corners[(i + 1) % n].longitude, corners[(i + 1) % n].latitude)) {
                    edge_passes++;
                }
            }
            if (edge_passes % 2 == 1) {
                return true;
            }
        }


        return false;
    }

    /**
     * distanceTo
     *
     * @param point LngLat object
     *              Finds the pythagorean distance between this object and the given object
     * @return the distance in double type
     */
    public double distanceTo(LngLat point) {
        return Math.sqrt(Math.pow((lng - point.lng), 2) + Math.pow((lat - point.lat), 2)); // Pythagorean distance equation
    }

    /**
     * closeTo
     *
     * @param point LngLat object
     *              true if the given point is only 0.00015 degrees away from this point
     * @return Boolean type
     */
    public boolean closeTo(LngLat point) {
        if (distanceTo(point) < TOLERANCE) {
            return true;
        }
        return false;
    }

    /**
     * closeTo
     *
     * @param d Direction type value that is the angle of travel
     *          Finds the next position of drone after traveling to the given angle
     * @return LngLat position
     */
    public LngLat nextPosition(Direction d) {
        if (d == null) {
            return this;
        }
        double next_lng = lng + MOVE_DISTANCE * Math.cos(Math.toRadians(d.angle()));
        double next_lat = lat + MOVE_DISTANCE * Math.sin(Math.toRadians(d.angle()));
        LngLat next_position = new LngLat(next_lng, next_lat);
        return next_position;
    }
    public LngLat previousPosition(Direction d) {
        if (d == null) {
            return this;
        }
        double previous_lng = lng - MOVE_DISTANCE * Math.cos(Math.toRadians(d.angle()));
        double previous_lat = lat - MOVE_DISTANCE * Math.sin(Math.toRadians(d.angle()));
        LngLat previous_position = new LngLat(previous_lng, previous_lat);
        return previous_position;
    }

}
