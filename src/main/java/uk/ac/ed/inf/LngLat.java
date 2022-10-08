package uk.ac.ed.inf;
import java.lang.Math;

public class LngLat {
    public double lng;
    public double lat;
    public LngLat(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }
    /**
     * inCentralArea
        true if the point is in Central Area else false
        @return Boolean type
     */
    public boolean inCentralArea(){
        if(55.942617<lat && lat<55.946233){
            if(-3.192473<lng && lng<-3.184319){
                return true;
            }
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