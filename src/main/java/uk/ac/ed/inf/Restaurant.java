package uk.ac.ed.inf;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import com.fasterxml.jackson.annotation.JsonProperty;



public class Restaurant {
    @JsonProperty("menu")
    private Menu[] menu;
    @JsonProperty("name")
    private String name;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("latitude")
    private double latitude;

    public Menu[] getMenu(){
        return menu;
    }
    /**
     * getRestaurantsFromRestServer
     * @param serverBaseAddress the URL address of the REST server
     * Obtains all the participating restaurants from the REST server as JSON and deserializes it to Restaurant objects
     * @exception MalformedURLException
     * @exception IOException
     * @return The restaurants that are participating, null if an Exception was found
     */
    public static Restaurant[] getRestaurantsFromRestServer(String serverBaseAddress)
    {
        IlpRestClient restClient = null;
        try {
            restClient = new IlpRestClient(new URL(serverBaseAddress));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return restClient.deserialize("/restaurants", Restaurant[].class);
    }
    public LngLat getLngLat(){ //Return the LngLat of this Restaurant
        return new LngLat(longitude,latitude);
    }
}
