package uk.ac.ed.inf;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MalformedURLException {
        LngLat t1 = new LngLat(23,67.6);
        LngLat t2 = new LngLat(-3.19,55.945);
        LngLat t3 = new LngLat(-4,55.943);
        LngLat t4 = new LngLat(-3.19,67.6);
        System.out.println(t1.inCentralArea());
        System.out.println(t2.inCentralArea());
        System.out.println(t3.inCentralArea());
        System.out.println(t4.inCentralArea());



        Restaurant [] participants = Restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net"));
        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/orders");
            ObjectMapper mapper = new ObjectMapper();
            Order[] orders = mapper.readValue(url, Order[].class);
            if(orders.length !=0 ) {
                System.out.println("The server responded with the order list");
                for(Order order: orders){
                    System.out.println(order.getDeliveryCost());
                }
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
