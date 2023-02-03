package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
public class Delivery { //Delivery Class used to output the delivery.json
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("outcome")
    private String outcome;
    @JsonProperty("costInPence")
    private Integer costInPence;
    public Delivery(String orderNo, OrderOutcome outcome, Integer costInPence){
        this.orderNo = orderNo;
        this.outcome = outcome.toString();
        this.costInPence = costInPence;
    }
}
