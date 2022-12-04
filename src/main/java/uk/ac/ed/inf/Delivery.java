package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Delivery {
    @JsonProperty("orderNo")
    public String orderNo;
    @JsonProperty("outcome")
    public String outcome;
    @JsonProperty("costInPence")
    public Integer costInPence;
    public Delivery(String orderNo, OrderOutcome outcome, Integer costInPence){
        this.orderNo = orderNo;
        this.outcome = outcome.toString();
        this.costInPence = costInPence;
    }
}
