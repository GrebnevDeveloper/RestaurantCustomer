package com.developer.grebnev.restaurantcustomer.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Grebnev on 26.10.2016.
 */

public class Order {
    private long dateOrder;
    private List<Recipe> listMeal;
    private double priceOrder;

    public Order() {
    }

    public Order(long dateOrder, List<Recipe> listMeal, Double priceOrder) {
        this.dateOrder = dateOrder;
        this.listMeal = listMeal;
        this.priceOrder = priceOrder;
    }

    public long getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(long dateOrder) {
        this.dateOrder = dateOrder;
    }

    public List<Recipe> getListMeal() {
        return listMeal;
    }

    public void setListMeal(List<Recipe> listMeal) {
        this.listMeal = listMeal;
    }

    public Double getPriceOrder() {
        return priceOrder;
    }

    public void setPriceOrder(Double priceOrder) {
        this.priceOrder = priceOrder;
    }
}
