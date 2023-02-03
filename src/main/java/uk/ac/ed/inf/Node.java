package uk.ac.ed.inf;

public class Node { //Node class that stores the necessary data to be used in the A* search at App
    public LngLat lnglat;
    public double gScore;
    public double hScore;
    public double fScore;
    public Double direction;
    public Node comeFrom;

    /**
     * Constructor of Node
     * @param comeFrom
     * @param d
     * @param finish
     * Has 3 parameters and used to record which node it came from and where it's headed
     */
    public Node(Node comeFrom, Direction d, LngLat finish){
        this.lnglat = comeFrom.lnglat.nextPosition(d);
        this.gScore = comeFrom.gScore+LngLat.MOVE_DISTANCE;
        this.hScore = lnglat.distanceTo(finish);
        this.fScore = (this.gScore)*0.8+this.hScore;
        this.direction = d.angle();
        this.comeFrom = comeFrom;

    }

    /**
     * Constructor for Nodes with no direction
     * @param lnglat
     * @param finish
     */
    public Node(LngLat lnglat, LngLat finish){
        gScore = 0;

        this.lnglat = lnglat;
        this.hScore = lnglat.distanceTo(finish);
        this.fScore = this.gScore+this.hScore;
        this.direction = null;
        comeFrom = this;
    }

    /**
     *
     * @param node
     * Checks if this node and the given parameter are on the same coordinates
     * @return boolean
     */
    public boolean isSameTo(Node node){
        if(lnglat.veryClose(node.lnglat)){
            return true;
        }
        return false;
    }
}
