package uk.ac.ed.inf;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class App {

    private static int turns = 0; //Steps the drone took
    private final static int BATTERY = 2000; //Steps the drone can take every day
    //List of Available Directions for travel
    private static ArrayList<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.East, Direction.EastNorthEast, Direction.NorthEast, Direction.NorthNorthEast, Direction.North, Direction.NorthNorthWest, Direction.NorthWest, Direction.WestNorthWest,
            Direction.West, Direction.WestSouthWest, Direction.SouthWest, Direction.SouthSouthWest, Direction.South, Direction.SouthSouthEast, Direction.SouthEast, Direction.EastSouthEast, Direction.East));
    private static NoFlyZone[] no_fly_zones; //The Array of the no fly zones
    private static ArrayList<Area> no_fly_areas = new ArrayList<Area>(); //The list of the no fly zones as Area type
    private static LngLat appleton = new LngLat(-3.186874, 55.944494); // Coordinates of Appleton Tower


    /**
     * Generate no_fly_areas() in Area type
     */
    static void noFlyAreas() {
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL("https://ilp-rest.azurewebsites.net"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        no_fly_zones = restClient.deserialize("/noFlyZones", NoFlyZone[].class);
        for (NoFlyZone no_fly_zone : no_fly_zones) {
            no_fly_areas.add(no_fly_zone.getArea());
        }

    }

    /**
     * @param current,neighbor The LngLat value of the two points in stake
     *                         Divides the line between two points into sections and checks if each point is not inside the NoFlyZones
     * @return boolean
     */
    static boolean isAllowed(LngLat current, LngLat neighbor) {
        double lng_diff = neighbor.lng - current.lng;
        double lat_diff = neighbor.lat - current.lat;
        int sections = 20;
        for (Area no_fly_area : no_fly_areas) {

            for (int i = 1; i <= sections; i++) {
                LngLat point = new LngLat((current.lng + (lng_diff * i) / sections), (current.lat + (lat_diff * i) / sections));
                if (point.inArea(no_fly_area)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @param goal,start The Node value of the start and end of the constructed path
     *                   Uses the Node's ComeFrom method to find which back track the path
     * @return path of list of Nodes
     */
    static ArrayList<Node> reconstruct(Node goal, Node start) {
        ArrayList<Node> path = new ArrayList<Node>();
        while (!goal.equals(start)) {
            path.add(goal);
            goal = goal.comeFrom;
        }
        path.add(goal);
        Collections.reverse(path); //Reverses Path to make it go from start -> end
        return path;
    }

    /**
     * @param start,finish The LngLat value of the start and finish destination
     *                     Uses A* Search to find an optimal path from start to finish
     *                     Uses two lists, open and visited, the function goes through the open list in the order of fScore and adds to visited.
     *                     Then goes through all 16 directions to see if nodes with better fScores exist.
     * @return The Path found
     */
    static ArrayList<Node> findDirection(LngLat start, LngLat finish) {
        ArrayList<Node> open = new ArrayList<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        Node start_node = new Node(start, finish);
        open.add(start_node);
        while (!open.isEmpty()) {
            Node current = open.get(0);
            double least = current.fScore;
            for (Node node : open) {
                if (node.fScore < least) {
                    current = node;
                    least = node.fScore;
                }
            }
            if (current.lnglat.closeTo(finish)) {
                return reconstruct(current, start_node);
            }
            visited.add(current);
            open.remove(current);
            for (Direction d : directions) {
                Node neighbor = new Node(current, d, finish);
                if (isAllowed(current.lnglat, neighbor.lnglat)) {
                    boolean visit = false;
                    for (Node visits : visited) {
                        if (visits.isSameTo(neighbor)) {
                            if (visits.fScore > neighbor.fScore) {
                                visits.gScore = neighbor.gScore;
                            }
                            visit = true;
                            break;
                        }
                    }
                    if (!visit) {
                        open.add(neighbor);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Main function of the app
     * Validates the orders, then finds a path for the drone to follow
     * Finally creating 3 different files for the flight path, drone movement, and the deliveries
     */
    public static void main(String[] args) throws IOException, InvalidPizzaCombinationException, InvalidOrderException {
        String date = args[0];
        String defaultEndpoint = null;
        if(args.length == 1){
            defaultEndpoint = "https://ilp-rest.azurewebsites.net";
        }
        else{
            defaultEndpoint = args[1];
        }
        noFlyAreas();
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL(defaultEndpoint));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<FlightPath> flightPath = new ArrayList<FlightPath>(); //List to store the flight paths, gets emptied every day
        ArrayList<Node> totalPath = new ArrayList<Node>(); //List that stores the path in nodes
        Order[] orders = restClient.deserialize("/orders/" + date, Order[].class);
        for (Order order : orders) {
            if (order.getDeliveryCost() != 0) {
                ArrayList<Node> thisPath = new ArrayList<Node>();
                thisPath.addAll(findDirection(appleton, order.restaurant.getLngLat()));
                thisPath.add(new Node(order.restaurant.getLngLat(), appleton));
                thisPath.addAll(findDirection(order.restaurant.getLngLat(), appleton));
                if (turns + thisPath.size() <= BATTERY) {
                    order.orderDelivered();
                    totalPath.addAll(thisPath);
                    int tick = turns;
                    for (Node node : thisPath) {
                        flightPath.add(new FlightPath(order.orderNo, node.comeFrom.lnglat.lng, node.comeFrom.lnglat.lat, node.direction, node.lnglat.lng, node.lnglat.lat, tick));
                        tick += 1;
                    }
                    turns = turns + thisPath.size() + 2;
                }
            }
        }
        restClient.recordDrone(totalPath, date);
        restClient.recordDelivery(orders, date);
        restClient.recordFlightPath(flightPath, date);
        turns = 0;
        flightPath.clear();
        totalPath.clear();

    }


}



