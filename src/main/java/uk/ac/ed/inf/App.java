package uk.ac.ed.inf;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Hello world!
 */
public class App {
    private static int turns = 0;
    private final static int BATTERY = 2000;
    private static ArrayList<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.East,Direction.EastNorthEast,Direction.NorthEast,Direction.NorthNorthEast,Direction.North,Direction.NorthNorthWest,Direction.NorthWest,Direction.WestNorthWest,
            Direction.West, Direction.WestSouthWest, Direction.SouthWest,Direction.SouthSouthWest,Direction.South,Direction.SouthSouthEast,Direction.SouthEast,Direction.EastSouthEast,Direction.East));
    private static NoFlyZone[] no_fly_zones;
    private static ArrayList<Area> no_fly_areas = new ArrayList<Area>();
    private static LngLat appleton = new LngLat(-3.186874,55.944494);
    private static ArrayList<Node> totalPath = new ArrayList<Node>();


    private static void noFlyAreas(){
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL("https://ilp-rest.azurewebsites.net"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        no_fly_zones = restClient.deserialize("/noFlyZones",NoFlyZone[].class);
        for(NoFlyZone no_fly_zone: no_fly_zones){
            no_fly_areas.add(no_fly_zone.getArea());
        }

    }
    private static boolean isAllowed(LngLat current, LngLat neighbor){
        double lng_diff = neighbor.lng- current.lng;
        double lat_diff = neighbor.lat - current.lat;
        for(Area no_fly_area:no_fly_areas){

            for(int i=1;i<=5;i++){
                LngLat point = new LngLat((current.lng+(lng_diff*i)/5),(current.lat+(lat_diff*i)/5));
                if(point.inArea(no_fly_area)){
                    return false;
                }
            }
        }
        return true;
    }



    private static ArrayList<Node> reconstruct(Node goal, Node start){
        ArrayList<Node> path = new ArrayList<Node>();
        while(!goal.equals(start)){
            path.add(goal);
            goal = goal.comeFrom;
        }
        path.add(goal);
        Collections.reverse(path);
        return path;
    }


    public static ArrayList<Node> findDirection (LngLat start, LngLat finish){
        ArrayList<Node> open = new ArrayList<Node>();
        ArrayList<Node> visited = new ArrayList<Node>();
        Node start_node = new Node(start,finish);
        open.add(start_node);
        while (!open.isEmpty()) {
            Node current = open.get(0);
            double least = current.fScore;
            for(Node node: open){
                if(node.fScore<least){
                    current = node;
                    least = node.fScore;
                }
            }
            if (current.lnglat.closeTo(finish)) {
                return reconstruct(current,start_node);
            }
            visited.add(current);
            open.remove(current);
            for(Direction d: directions){
                Node neighbor = new Node(current, d, finish);
                if(isAllowed(current.lnglat,neighbor.lnglat)) {
                    boolean visit = false;
                    for(Node visits : visited){
                        if(visits.isSameTo(neighbor)){
                            if(visits.fScore>neighbor.fScore){
                                visits.changeG(neighbor.gScore);
                            }
                            visit = true;
                            break;
                        }
                    }
                    if(!visit){
                        open.add(neighbor);
                    }
                }
            }
        }
        return null;
    }
    public static void main(String[] args) throws IOException, InvalidPizzaCombinationException, InvalidOrderException {
        Restaurant[] restaurants =  Restaurant.getRestaurantsFromRestServer("https://ilp-rest.azurewebsites.net");
        noFlyAreas();
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL("https://ilp-rest.azurewebsites.net"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        for(int i=1;i<=12;i++){
            LocalDate date = LocalDate.of(2023,1,i);
            String date_string = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).toString();
            Order[] orders = restClient.deserialize("/orders/"+date_string,Order[].class);
            ArrayList<FlightPath> flight_path = new ArrayList<FlightPath>();
            for(Order order: orders){
                if(order.getDeliveryCost()!=0){
                    ArrayList<Node> thisPath = new ArrayList<Node>();
                    thisPath.addAll(findDirection(appleton,order.restaurant.getLngLat()));
                    thisPath.add(new Node(order.restaurant.getLngLat(),appleton));
                    thisPath.addAll(findDirection(order.restaurant.getLngLat(),appleton));
                    if(turns+thisPath.size()<=BATTERY){
                        order.orderDelivered();
                        totalPath.addAll(thisPath);
                        int tick = turns;
                        for(Node node: thisPath){
                            flight_path.add(new FlightPath(order.orderNo,node.comeFrom.lnglat.lng,node.comeFrom.lnglat.lat,node.direction,node.lnglat.lng,node.lnglat.lat,tick));
                        tick +=1;
                        }
                        turns = turns + thisPath.size()+2;
                    }
                }
            }
            restClient.recordDrone(totalPath, date_string);
            restClient.recordDelivery(orders, date_string);
            restClient.recordFlightPath(flight_path,date_string);
            turns = 0;
        }



    }


}
