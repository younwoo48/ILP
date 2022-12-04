package uk.ac.ed.inf;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.IOException;
import java.net.MalformedURLException;


public class Order {
    private static final int FIXED_ORDER_CHARGE = 100;
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
    @JsonProperty("orderStatus")
    public OrderOutcome orderStatus;
    @JsonProperty("costInPence")
    public int pizzaPrice = 0;

    public Restaurant restaurant=null;


    private OrderOutcome creditCardCheck() {
        if (creditCardNumber.length() != 16) {
            return OrderOutcome.InvalidCardNumber;
        }
        if (creditCardNumber.charAt(0) == '2' || creditCardNumber.indexOf(0) == '5' || creditCardNumber.indexOf(0) == '4') {
            return OrderOutcome.InvalidCardNumber;
        }
        if (cvv.length() != 3) {
            return OrderOutcome.InvalidCvv;
        }
        if (Integer.parseInt(creditCardExpiry.substring(0, 2)) >= 12) {
            return OrderOutcome.InvalidExpiryDate;
        }
        if(Integer.parseInt(creditCardExpiry.substring(3,5))<Integer.parseInt(orderDate.substring(2,4))){
            return OrderOutcome.InvalidExpiryDate;
        }
        if(Integer.parseInt(creditCardExpiry.substring(3,5))==Integer.parseInt(orderDate.substring(2,4))){
            if(Integer.parseInt(orderDate.substring(5,7))>Integer.parseInt(creditCardExpiry.substring(0,2))){
                return OrderOutcome.InvalidExpiryDate;
            }
        }
        if (pizzaPrice + FIXED_ORDER_CHARGE != priceTotalInPence) {
            return OrderOutcome.InvalidTotal;
        }
        return OrderOutcome.AcceptedButNotDelivered;

    }

    public void orderDelivered(){
        orderStatus = OrderOutcome.Delivered;
    }


    /**
     * checkMenusWithRestaurant
     *
     * @param menus List of Menu
     *              True if the given menus are all inside the restaurant's menu, else return false
     * @return Boolean
     */

    private boolean checkMenusWithRestaurant(Menu[] menus) {
        for (String order_item : orderItems) {
            boolean exist = false;
            for (Menu menu : menus) {
                if (order_item.equals(menu.name)) {
                    pizzaPrice += menu.priceInPence;
                    exist = true;
                }
            }
            if (!exist) {
                return false;
            }
        }
        return true;
    }

    /**
     * getDeliveryCost
     * Returns the delivery cost of the order
     *
     * @return the delivery cost if it's a valid order, else 0
     * @throws Exception             InvalidPizzaCount if more than 4 orders were ordered
     * @throws Exception             InvalidPizzaCombinationMultipleSuppliers if the orders are not all from the same restaurant
     * @throws MalformedURLException
     * @throws IOException
     */
    public int getDeliveryCost() throws InvalidOrderException, InvalidPizzaCombinationException {
        int cost = FIXED_ORDER_CHARGE;
        Restaurant[] participants = Restaurant.getRestaurantsFromRestServer("https://ilp-rest.azurewebsites.net");
        if (orderItems == null) {
            this.orderStatus = OrderOutcome.Invalid;
            try {
                throw new InvalidOrderException("Number of Pizza is incorrect");
            } catch (InvalidOrderException e) {
                return 0;
            }

        }
        if (orderItems.length >= 5) {
            this.orderStatus = OrderOutcome.InvalidPizzaCount;
            try {
                throw new InvalidOrderException("Number of Pizza is incorrect");
            } catch (InvalidOrderException e) {
                return 0;
            }

        }
        for (Restaurant participant : participants) {
            for (Menu menu : participant.getMenu()) {
                if (menu.name.equals(orderItems[0])) {
                    this.restaurant = participant;
                    if (!checkMenusWithRestaurant(participant.getMenu())) {
                        this.orderStatus = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
                        try {
                            throw new InvalidPizzaCombinationException();
                        } catch(InvalidPizzaCombinationException e){
                            return 0;
                        }

                    } else {
                        cost = cost + pizzaPrice;
                    }
                }
            }
        }
        if (cost == FIXED_ORDER_CHARGE) {
            this.orderStatus = OrderOutcome.InvalidPizzaNotDefined;
            try {
                throw new InvalidPizzaCombinationException();
            } catch(InvalidPizzaCombinationException e){
                return 0;
            }
        }
        this.orderStatus = creditCardCheck();
        if (this.orderStatus.equals(OrderOutcome.AcceptedButNotDelivered)) {
            pizzaPrice = cost;
            return cost;
        }
        pizzaPrice = 0;
        return 0;
    }
}
