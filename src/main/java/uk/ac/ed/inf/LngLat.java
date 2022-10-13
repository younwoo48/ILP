package uk.ac.ed.inf;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.Math;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Math.min;

public class LngLat {
    public double lng;
    public double lat;
    public LngLat(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }

    /**
     *
     * @param x,y The coordinate of the point to check
     * @param x1,y1,x2,y2 The coordinates of the two points making the edge
     *
     * @return True if the right vertical line that passes through x,y meet the edge.
     */
    private boolean passEdge(double x, double y, double x1, double y1, double x2, double y2){
        double boty = Math.min(y1,y2);
        double topy = Math.max(y1,y2);
        if(y<=boty || y>=topy){
            return false;
        }
        if(x2 == x1){
            if(x1>x) {
                return true;
            }
            return false;
        }
        double a = (y2-y1)/(x2-x1);
        double b = y1 - a*x1;
        double p = (y-b)/a;
        if(p>x){
            return true;
        }
        return false;
    }

    /**
     * inCentralArea
        true if the point is in Central Area else false. Uses Basic Ray tracing algorithm to see if the point is in the Polygon.
        @return Boolean type
     */
    public boolean inCentralArea(){

        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/centralArea");
            ObjectMapper mapper = new ObjectMapper();
            Point[] corners = mapper.readValue(url, Point[].class);
            int n = corners.length;
            if(n >= 3) {
                int edge_passes = 0;
                for(int i=0; i< n; i++) {
                    if (passEdge(this.lng, this.lat, corners[i].longitude, corners[i].latitude, corners[(i + 1) % n].longitude, corners[(i + 1) % n].latitude)) {
                        edge_passes++;
                    }
                }
                if(edge_passes%2==1){
                    return true;
                }
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * distanceTo
        @param point LngLat object
        Finds the pythagorean distance between this object and the given object
        @return the distance in double type
     */
    public double distanceTo(LngLat point){
        return Math.sqrt(Math.pow((lng - point.lng),2)+Math.pow((lat - point.lat),2)); // Pythagorean distance equation
    }

    /**
     * closeTo
        @param point LngLat object
        true if the given point is only 0.00015 degrees away from this point
        @return Boolean type
     */
    public boolean closeTo(LngLat point){
        if(distanceTo(point)<0.00015){
            return true;
        }
        return false;
    }

    /**
     * closeTo
        @param angle double type value that is the angle of travel
        Finds the next position of drone after traveling to the given angle
        @return LngLat position
     */
    public LngLat nextPosition(double angle){

        double next_lng = lng + 0.00015*Math.cos(Math.toRadians(angle));
        double next_lat = lat + 0.00015*Math.sin(Math.toRadians(angle));
        LngLat next_position = new LngLat(next_lng,next_lat);
        return next_position;
    }

}
