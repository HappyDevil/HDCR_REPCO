package com.repandco.repco.entities;

import com.google.gson.annotations.SerializedName;

public class StripeJobPost extends JobPost{

    @SerializedName("commission")
    private Integer commission;
    @SerializedName("currency")
    private String currency;
    @SerializedName("price")
    private Integer price;
    @SerializedName("commission")
    public Integer getCommission() {
        return commission;
    }

    @SerializedName("commission")
    public void setCommission(Integer commission) {
        this.commission = commission;
    }

    @SerializedName("currency")
    public String getCurrency() {
        return currency;
    }

    @SerializedName("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @SerializedName("price")
    public Integer getPrice() {
        return price;
    }

    @SerializedName("price")
    public void setPrice(Integer price) {
        this.price = price;
    }

}