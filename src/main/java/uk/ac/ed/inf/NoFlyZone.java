package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
public class NoFlyZone {
    @JsonProperty("name")
    public String name;
    @JsonProperty("coordinates")
    public Double[][] coordinates;
    private boolean doesIntersect(LngLat p, LngLat q, double x1, double y1, double x2, double y2) {
        double x3 = p.lng;
        double x4 = q.lng;
        double y3 = p.lat;
        double y4 = p.lat;
        if (x2 == x1){
            if((x3>=x2 && x2>=x4) || (x4>x2 && x2>x3)){
                return true;
            }
            return false;
        }
        if (x3 == x4){
            if((x1>=x3 && x3>=x2) || (x2>x3 && x3>x1)){
                return true;
            }
            return false;
        }
        double a1 = (y2-y1)/(x2-x1);
        double b1 = y1 - ((y2-y1)*x1)/(x2-x1);
        double a2 = (y4-y3)/(x4-x3);
        double b2 = y3 - ((y4-y3)*x3)/(x4-x3);
        if(a1 == a2){
            return false;
        }
        double x_intersect = -(b1-b2)/(a1-a2);
        double y_intersect = a1*(-((b1-b2)/(a1-a2))+b1);
        if((x3<=x_intersect && x_intersect<=x4) || (x4<=x_intersect && x_intersect<=x3)){
            if((y3<=y_intersect && y_intersect<=y4) || (y4<=y_intersect && y_intersect<=y3)){
                return true;
            }
        }
        return false;
    }
    public boolean intersects(LngLat start, LngLat end) {
        int n = coordinates.length;
        if (n >= 3) {
            int edge_passes = 0;
            for (int i = 0; i < n; i++) {
                if (doesIntersect(start, end, coordinates[i][0], coordinates[i][1], coordinates[(i + 1) % n][0], coordinates[(i + 1) % n][1])){
                    return true;
                }
            }

        }
        return false;
    }
}
