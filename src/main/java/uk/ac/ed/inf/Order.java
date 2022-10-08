package uk.ac.ed.inf;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Order {
    public String orderNo;
    public String orderDate;
    public String customer;
    public String creditCardNumber;
    public String creditCardExpiry;
    public String cvv;
    public int priceTotalInPence;
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
    public int getDeliveryCost(){
        try{
            Restaurant [] participants = Restaurant.getRestaurantsFromRestServer(new URL("https://ilp-rest.azurewebsites.net"));
            try{
                if(orderItems.length>=5) throw new Exception(String.valueOf(OrderOutcome.InvalidPizzaCount));
            }
            catch (Exception e){
                System.out.println(e);
                return 0;
            }
            try{
                for(Restaurant participant:participants){
                    for(Menu menu: participant.getMenu()){
                        if(menu.equals(orderItems[0])){
                            if(!checkMenusWithRestaurant(participant.getMenu(), participant)){
                                throw new Exception(String.valueOf(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers));
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                System.out.println(e);
                return 0;
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
