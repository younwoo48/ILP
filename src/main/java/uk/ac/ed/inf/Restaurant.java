package uk.ac.ed.inf;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Restaurant {
    @JsonProperty("menu")
    public Menu[] menu;
    @JsonProperty("name")
    public String name;
    @JsonProperty("longitude")
    public double longitude;
    @JsonProperty("latitude")
    public double latitude;

    /**
     * containsMenu
     * @param possible_menu the menus to check
     * Checks if all the possible_menu is actually available in this restaurant
     * @return true if the restaurant has the menu, false if else.
     */
    public boolean containsMenu(Menu possible_menu){
        for(Menu option: this.menu){
            if(option.equals(possible_menu)){
                return true;
            }
        }
        return false;
    }
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
}
