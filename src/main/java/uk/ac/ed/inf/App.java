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
