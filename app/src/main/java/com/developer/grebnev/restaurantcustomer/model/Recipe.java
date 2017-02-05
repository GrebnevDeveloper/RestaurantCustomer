package com.developer.grebnev.restaurantcustomer.model;

import java.util.List;

/**
 * Created by Grebnev on 26.10.2016.
 */

public class Recipe {
    private String nameRecipe;
    private int keyRecipe;
    private List<Product> listProductForRecipe;
    private double priceMeal;

    public Recipe() {

    }

    public Recipe(String nameRecipe, int keyRecipe) {
        this.nameRecipe = nameRecipe;
        this.keyRecipe = keyRecipe;
    }

    public Recipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public Recipe(String nameRecipe, List<Product> listProductForRecipe, double priceMeal) {
        this.nameRecipe = nameRecipe;
        this.listProductForRecipe = listProductForRecipe;
        this.priceMeal = priceMeal;
    }

    public String getNameRecipe() {
        return nameRecipe;
    }

    public void setNameRecipe(String nameRecipe) {
        this.nameRecipe = nameRecipe;
    }

    public List<Product> getListProductForRecipe() {
        return listProductForRecipe;
    }

    public void setListProductForRecipe(List<Product> listProductForRecipe) {
        this.listProductForRecipe = listProductForRecipe;
    }

    public double getPriceMeal() {
        return priceMeal;
    }

    public void setPriceMeal(double priceMeal) {
        this.priceMeal = priceMeal;
    }

    public int getKeyRecipe() {
        return keyRecipe;
    }

    public void setKeyRecipe(int keyRecipe) {
        this.keyRecipe = keyRecipe;
    }
}
