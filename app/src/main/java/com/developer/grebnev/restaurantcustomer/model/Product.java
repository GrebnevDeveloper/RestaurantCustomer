package com.developer.grebnev.restaurantcustomer.model;

/**
 * Created by Grebnev on 26.10.2016.
 */

public class Product {
    private String nameProduct;
    private double quantityProduct;
    private double priceProduct;
    private int key;

    public Product() {
    }

    public Product(String nameProductForRecipe, double quantityProductForRecipe) {
        this.nameProduct = nameProductForRecipe;
        this.quantityProduct = quantityProductForRecipe;
    }

    public Product(int id, String nameProductForRecipe, double quantityProductForRecipe) {
        this.key = id;
        this.nameProduct = nameProductForRecipe;
        this.quantityProduct = quantityProductForRecipe;
    }

    public Product(String nameProduct, double quantityProduct, double priceProduct, int key) {
        this.nameProduct = nameProduct;
        this.quantityProduct = quantityProduct;
        this.priceProduct = priceProduct;
        this.key = key;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public double getQuantityProduct() {
        return quantityProduct;
    }

    public void setQuantityProduct(double quantityProduct) {
        this.quantityProduct = quantityProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(double priceProduct) {
        this.priceProduct = priceProduct;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}
