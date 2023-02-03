package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static uk.ac.ed.inf.App.*;


public class AppTest {
    private LngLat appleton_tower = new LngLat(-3.186874, 55.944494);
    private LngLat sodeberg = new LngLat(-3.1940, 55.9439);
    private LngLat civerinos = new LngLat(-3.1913,55.9455);
    private LngLat sora_lella = new LngLat(	-3.2025,55.9433);
    private LngLat dominoes = new LngLat(	-3.1839, 55.9445);
    //Unit Tests
    @Test
    public void testIsAllowed() {
        LngLat current = new LngLat(-3.186874, 55.944494);
        LngLat neighbor = new LngLat(-3.186874, 55.944494);

        noFlyAreas();
        assertTrue(App.isAllowed(current, neighbor));

        LngLat noFlyPointStart = new LngLat(-3.189, 55.9437);
        LngLat noFlyPointEnd = new LngLat(-3.1895, 55.94375);
        assertFalse(App.isAllowed(noFlyPointStart, noFlyPointEnd));
    }

    @Test
    public void testFindDirection() throws IOException {
        ArrayList<LngLatAlt> geo_position = new ArrayList<LngLatAlt>();
        for(int i=1;i<=5;i++){
            ArrayList<Node> go = App.findDirection(appleton_tower, dominoes);
            ArrayList<Node> back  = App.findDirection(dominoes, appleton_tower);
            for (Node position: go){
                geo_position.add(new LngLatAlt(position.lnglat.lng, position.lnglat.lat));
            }
            for (Node position: back){
                geo_position.add(new LngLatAlt(position.lnglat.lng, position.lnglat.lat));
            }

        }
        LineString lines = new LineString(geo_position.toArray(new LngLatAlt[0]));
        ObjectMapper mapper = new ObjectMapper();
        File theDir = new File("testfiles");
        if (!theDir.exists()){
            theDir.mkdirs();
        }
        mapper.writeValue(new File("testfiles/appleton_test.geojson"),lines);
    }
    /**
    @Test
    public void testOrder() throws InvalidPizzaCombinationException, InvalidOrderException {
        Order credit_wrong = new Order("2023-01-01","123412341234123","07/24","922",2400, new String[]{"Super Cheese", "All Shrooms"});
        int cost = credit_wrong.getDeliveryCost();
        assertTrue(cost==0);
        Order expiry_wrong = new Order("2023-01-01","1234123412341234","07/20","922",2400, new String[]{"Super Cheese", "All Shrooms"});
        cost = expiry_wrong.getDeliveryCost();
        assertTrue(cost==0);
        Order cvv_wrong = new Order("2023-01-01","1234123412341234","07/24","92245",2400, new String[]{"Super Cheese", "All Shrooms"});
        cost = cvv_wrong.getDeliveryCost();
        assertTrue(cost==0);
        Order price_wrong = new Order("2023-01-01","1234123412341234","07/24","922",24000, new String[]{"Super Cheese", "All Shrooms"});
        cost = price_wrong.getDeliveryCost();
        assertTrue(cost==0);
        Order menu_wrong = new Order("2023-01-01","1234123412341234","07/24","922",2400, new String[]{"Super Cheese", "fdfdfd"});
        cost = menu_wrong.getDeliveryCost();
        assertTrue(cost==0);
    }
     **/
    @Test
    public void testDay() throws InvalidPizzaCombinationException, InvalidOrderException, MalformedURLException {

        String defaultEndpoint = "https://ilp-rest.azurewebsites.net";
        noFlyAreas();
        long startTime = System.currentTimeMillis();


        IlpRestClient restClient = new IlpRestClient(new URL(defaultEndpoint));

        int turns = 0;
        int BATTERY = 2000;
        ArrayList<FlightPath> flightPath = new ArrayList<FlightPath>(); //List to store the flight paths, gets emptied every day
        ArrayList<Node> totalPath = new ArrayList<Node>(); //List that stores the path in nodes
        Order[] orders = restClient.deserialize("/orders/2023-01-01" , Order[].class);
        System.out.println(orders);
        for (Order order : orders) {
            if (order.getDeliveryCost() != 0) {
                ArrayList<Node> thisPath = new ArrayList<Node>();
                thisPath.addAll(findDirection(appleton_tower, order.restaurant.getLngLat()));
                thisPath.add(new Node(order.restaurant.getLngLat(), appleton_tower));
                thisPath.addAll(findDirection(order.restaurant.getLngLat(), appleton_tower));
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
        long endTime = System.currentTimeMillis();
        assertTrue(turns<=2000);
        long duration = (endTime - startTime)/1000;
        System.out.println(duration);
        assertTrue(duration<60);
    }

}

