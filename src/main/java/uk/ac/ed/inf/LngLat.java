package uk.ac.ed.inf;
import java.lang.Math;

public class LngLat {
    public double lng;
    public double lat;
    public LngLat(double lng, double lat){
        this.lng = lng;
        this.lat = lat;
    }
    public boolean inCentralArea(){
        if(55.942617<lat && lat<55.946233){
            if(-3.192473<lng && lng<-3.184319){
                return true;
            }
        }
        return false;
    }
    public double distanceTo(LngLat point){
        return Math.sqrt(Math.pow((lng - point.lng),2)+Math.pow((lat - point.lat),2));
        // Pythagorean distance equation
    }
    public boolean closeTo(LngLat point){
        if(distanceTo(point)<0.00015){
            return true;
        }
        return false;
    }
    public LngLat nextPosition(double angle){

        double next_lng = lng + 0.00015*Math.cos(Math.toRadians(angle));
        double next_lat = lat + 0.00015*Math.sin(Math.toRadians(angle));
        LngLat next_position = new LngLat(next_lng,next_lat);
        return next_position;
    }

}
