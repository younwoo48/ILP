package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class IlpRestClient //Class to get live data from the Ilp REST Server or to create necessary files
{
    public URL baseUrl;
    public IlpRestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
    public URL getBaseUrl() {return baseUrl;}


    /**
     *
     * @param fromEndpoint The link to the JSON that's being reached
     * @param klass Class to parse to
     * @return The instance of the class
     */
    public <T> T deserialize(String fromEndpoint, Class<T> klass){
        URL finalURL = null;
        T response = null;

        try{
            finalURL = new URL(baseUrl.toString() + fromEndpoint);
        } catch (MalformedURLException e){
            System.err.println("URL is invalid : "+baseUrl+fromEndpoint);
            System.exit(2);
        }
        try{
            response = new ObjectMapper().readValue(finalURL, klass);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /**
     *
     * @param positions
     * @param date
     * @throws IOException
     * Creates the drone.geojson file using the Nodes data from App
     */
    public void recordDrone(ArrayList<Node> positions, String date) throws IOException {
        ArrayList<LngLatAlt> geo_position = new ArrayList<LngLatAlt>();
        for (Node position: positions){
            geo_position.add(new LngLatAlt(position.lnglat.lng, position.lnglat.lat));
        }
        LineString lines = new LineString(geo_position.toArray(new LngLatAlt[0]));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("resultfiles/drone-"+date+".geojson"),lines);
    }

    /**
     *
     * @param orders
     * @param date
     * @throws IOException
     * Creates the deliveries.json file using the orders given from the App
     */
    public void recordDelivery(Order[] orders, String date) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
        for(Order order:orders){
            deliveries.add(new Delivery(order.orderNo, order.orderStatus, order.pizzaPrice));
        }
        mapper.writeValue(new File("resultfiles/deliveries-"+date+".json"),deliveries);
    }

    /**
     *
     * @param flightPaths
     * @param date
     * @throws IOException
     * Creates the flightpath.json file using the parsed data from App
     */
    public void recordFlightPath(ArrayList<FlightPath> flightPaths, String date) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("resultfiles/flightpath-"+date+".json"),flightPaths);
    }
}
