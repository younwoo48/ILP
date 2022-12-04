package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.jackson.LngLatAltSerializer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class IlpRestClient
{
    public URL baseUrl;
    public IlpRestClient(URL baseUrl) {
        this.baseUrl = baseUrl;
    }
    public URL getBaseUrl() {return baseUrl;}


    public void download(String fromEndpoint, String toFilename){
        URL finalURL = null;

        try{
            finalURL=new URL(baseUrl.toString() + fromEndpoint);
        } catch (MalformedURLException e) {
            System.err.println("URL in invalid : " + baseUrl + fromEndpoint);
            System.exit(2);
        }
        try (BufferedInputStream in = new BufferedInputStream(finalURL.openStream());
             FileOutputStream fileOutputStream =
                     new FileOutputStream(fromEndpoint, false)){
            var dataBuffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer,0,1024)) != -1){
                fileOutputStream.write(dataBuffer,0,bytesRead);
            }

            System.out.println("File was written at: " + toFilename);
        } catch(IOException e){
            System.err.format("Error loading file: %s from %s -> %s", fromEndpoint, finalURL,e);
        }
    }
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

    public void recordDrone(ArrayList<Node> positions, String date) throws IOException {

        ArrayList<LngLatAlt> geo_position = new ArrayList<LngLatAlt>();
        for (Node position: positions){

            geo_position.add(new LngLatAlt(position.lnglat.lng, position.lnglat.lat));
        }
        LineString lines = new LineString(geo_position.toArray(new LngLatAlt[0]));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("drone-"+date+".geojson"),lines);

    }
    public void recordDelivery(Order[] orders, String date) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
        for(Order order:orders){
            deliveries.add(new Delivery(order.orderNo, order.orderStatus, order.pizzaPrice));
        }
        mapper.writeValue(new File("deliveries-"+date+".json"),deliveries);

    }
    public void recordFlightPath(ArrayList<FlightPath> flightPaths, String date) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(new File("flightpath-"+date+".json"),flightPaths);

    }
}
