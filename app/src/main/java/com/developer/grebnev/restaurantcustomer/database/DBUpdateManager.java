package com.developer.grebnev.restaurantcustomer.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.developer.grebnev.restaurantcustomer.model.Product;

/**
 * Created by Grebnev on 29.10.2016.
 */

public class DBUpdateManager {
    SQLiteDatabase database;

    DBUpdateManager(SQLiteDatabase database) {
        this.database = database;
    }

    public void titleProduct(String title, int key) {
        updateProduct(DBHelper.PRODUCTS_TITLE_COLUMN, title ,key);
    }

    public void quantityProduct(double quantity, int key) {
        updateProduct(DBHelper.PRODUCTS_QUANTITY_COLUMN, quantity, key);
    }

    public void priceProduct(double price, int key) {
        updateProduct(DBHelper.PRODUCTS_PRICE_COLUMN, price, key);
    }

    public void product(Product product) {
        titleProduct(product.getNameProduct(), product.getKey());
        quantityProduct(product.getQuantityProduct(), product.getKey());
        priceProduct(product.getPriceProduct(), product.getKey());
    }

    private void updateProduct(String column, String value, int key) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelper.PRODUCTS_TABLE, cv, DBHelper.UID_PRODUCT + "=" + key, null);
    }

    private void updateProduct(String column, double value, int key) {
        ContentValues cv = new ContentValues();
        cv.put(column, value);
        database.update(DBHelper.PRODUCTS_TABLE, cv, DBHelper.UID_PRODUCT + "=" + key, null);
    }
}
