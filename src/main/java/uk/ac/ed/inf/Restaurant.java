package uk.ac.ed.inf;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Restaurant {
    public Menu[] menu;
    public String name;
    public double longitude;
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
    public static Restaurant[] getRestaurantsFromRestServer(URL serverBaseAddress)
    {
        try {
            URL url = new URL(serverBaseAddress + "/restaurants");
            ObjectMapper mapper = new ObjectMapper();
            Restaurant[] restaurants = mapper.readValue(url, Restaurant[].class);
            if(restaurants.length !=0 ) {
                System.out.println("The server responded with the restaurant list");
                return restaurants;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
        e.printStackTrace();
        }
        return null;
    }
}
