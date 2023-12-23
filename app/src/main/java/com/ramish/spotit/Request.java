package com.ramish.spotit;
public class Request {

    public static Object Method;
    String requestId;
    String itemId;
    String customerId;
    Double hours;

    public Request() {}

    public Request(String requestId, String itemId, String customerId, Double hours) {
        this.requestId = requestId;
        this.itemId = itemId;
        this.customerId = customerId;
        this.hours = hours;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
