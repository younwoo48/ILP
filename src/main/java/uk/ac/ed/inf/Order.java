package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Order {
    private static final int FIXED_ORDER_CHARGE= 100;
    @JsonProperty("orderNo")
    public String orderNo;
    @JsonProperty("orderDate")
    public String orderDate;
    @JsonProperty("customer")
    public String customer;
    @JsonProperty("creditCardNumber")
    public String creditCardNumber;
    @JsonProperty("creditCardExpiry")
    public String creditCardExpiry;
    @JsonProperty("cvv")
    public String cvv;
    @JsonProperty("priceTotalInPence")
    public int priceTotalInPence;
    @JsonProperty("orderItems")
    public String[] orderItems;

    /**
     * checkMenusWithRestaurant
     * @param menus List of Menu
     * @param restaurant Restaurant type
     * True if the given menus are all inside the restaurant's menu, else return false
     * @return Boolean
     */
    private boolean checkMenusWithRestaurant(Menu[] menus, Restaurant restaurant){
        for(Menu menu:menus){
            if(!restaurant.containsMenu(menu)){
                return false;
            }
        }
        return true;
    }
    /**
     * getDeliveryCost
     * Returns the delivery cost of the order
     * @exception Exception InvalidPizzaCount if more than 4 orders were ordered
     * @exception Exception InvalidPizzaCombinationMultipleSuppliers if the orders are not all from the same restaurant
     * @exception MalformedURLException
     * @exception IOException
     * @return 1 if it's a valid order, else 0
     */
    public int getDeliveryCost() throws InvalidOrderException, InvalidPizzaCombinationException{
        int cost = FIXED_ORDER_CHARGE;
        Restaurant [] participants = Restaurant.getRestaurantsFromRestServer("https://ilp-rest.azurewebsites.net");
        if(orderItems.length>=5) {
            throw new InvalidOrderException("Number of Pizza is incorrect");
        }
        for(Restaurant participant:participants){
            for(Menu menu: participant.getMenu()){
                if(menu.equals(orderItems[0])){
                    if(!checkMenusWithRestaurant(participant.getMenu(), participant)){
                        throw new InvalidPizzaCombinationException();
                    }
                    else{
                        cost = cost + priceTotalInPence;
                    }
                }
            }
        }
        if(cost == FIXED_ORDER_CHARGE){
            throw new InvalidPizzaCombinationException();
        }

        return cost;
    }
}
