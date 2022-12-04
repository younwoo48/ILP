package uk.ac.ed.inf;

public class Node {
    public LngLat lnglat;
    public double gScore;
    public double hScore;
    public double fScore;
    public Double direction;
    public Node comeFrom;
    public Node(Node comeFrom, Direction d, LngLat finish){
        this.lnglat = comeFrom.lnglat.nextPosition(d);
        this.gScore = comeFrom.gScore+LngLat.MOVE_DISTANCE;
        this.hScore = lnglat.distanceTo(finish);
        this.fScore = (this.gScore)*0.8+this.hScore;
        this.direction = d.angle();
        this.comeFrom = comeFrom;

    }
    public Node(LngLat lnglat, LngLat finish){
        gScore = 0;

        this.lnglat = lnglat;
        this.hScore = lnglat.distanceTo(finish);
        this.fScore = this.gScore+this.hScore;
        this.direction = null;
        comeFrom = this;
    }
    public boolean isSameTo(Node node){
        if(lnglat.veryClose(node.lnglat)){
            return true;
        }
        return false;
    }
    public void changeG(double newG){
        this.gScore = newG;
        this.fScore = (this.gScore)*0.8+this.hScore;
    }
}
