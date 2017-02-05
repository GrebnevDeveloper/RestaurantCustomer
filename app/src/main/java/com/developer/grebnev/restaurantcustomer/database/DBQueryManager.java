package com.developer.grebnev.restaurantcustomer.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developer.grebnev.restaurantcustomer.model.Order;
import com.developer.grebnev.restaurantcustomer.model.Product;
import com.developer.grebnev.restaurantcustomer.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 29.10.2016.
 */

public class DBQueryManager {

    private SQLiteDatabase database;

    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();

        Cursor c = database.query(DBHelper.PRODUCTS_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DBHelper.PRODUCTS_TITLE_COLUMN));
                double quantity = c.getDouble(c.getColumnIndex(DBHelper.PRODUCTS_QUANTITY_COLUMN));
                double price = c.getDouble(c.getColumnIndex(DBHelper.PRODUCTS_PRICE_COLUMN));
                int key = c.getInt(c.getColumnIndex(DBHelper.UID_PRODUCT));

                Product product = new Product(title, quantity, price, key);
                products.add(product);
            } while (c.moveToNext());
        }
        c.close();
        return products;
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        List<Recipe> recipesBuff = new ArrayList<>();
        List<Product> listProductForRecipe;

        Cursor c = database.query(DBHelper.RECIPE_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                listProductForRecipe = new ArrayList<>();
                String title = c.getString(c.getColumnIndex(DBHelper.RECIPE_TITLE_COLUMN));
                String titleProduct = c.getString(c.getColumnIndex(DBHelper.PRODUCT_FOR_RECIPE_COLUMN));
                double quantityProductForRecipe = c.getDouble(c.getColumnIndex(DBHelper.PRODUCT_QUANTITY_FOR_RECIPE_COLUMN));
                int key = c.getInt(c.getColumnIndex(DBHelper.UID_RECIPE));
                listProductForRecipe.add(new Product(key, titleProduct, quantityProductForRecipe));
                double priceMeal = 0.0;
                for (Product product : getProducts()) {
                    if (titleProduct.equals(product.getNameProduct())) {
                        priceMeal = quantityProductForRecipe * product.getPriceProduct();
                    }
                }
                Recipe recipe = new Recipe(title, listProductForRecipe, priceMeal);
                recipesBuff.add(recipe);
            } while (c.moveToNext());
        }
        c.close();

        for (int i = 0; i < recipesBuff.size(); i++) {
            boolean isRepeatRecipe = false;
            String title = recipesBuff.get(i).getNameRecipe();
            for (Recipe recipeOut : recipes) {
                if (title.equals(recipeOut.getNameRecipe())) {
                    isRepeatRecipe = true;
                }
            }
            if (!isRepeatRecipe) {
                double priceMeal = 0.0;
                listProductForRecipe = new ArrayList<>();
                for (int j = i; j < recipesBuff.size(); j++) {
                    if (title.equals(recipesBuff.get(j).getNameRecipe())) {
                        for (Product product : recipesBuff.get(j).getListProductForRecipe()) {
                            listProductForRecipe.add(product);
                            for (Product productOfStock : getProducts()) {
                                if (productOfStock.getNameProduct().equals(product.getNameProduct())) {
                                    priceMeal = priceMeal + product.getQuantityProduct() * productOfStock.getPriceProduct();
                                }
                            }

                        }
                    }
                }
                Recipe recipe = new Recipe(title, listProductForRecipe, priceMeal);
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        List<Order> ordersBuff = new ArrayList<>();
        List<Recipe> listRecipeForOrder;

        Cursor c = database.query(DBHelper.ORDER_TABLE, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                listRecipeForOrder = new ArrayList<>();
                long dateOrder = c.getLong(c.getColumnIndex(DBHelper.ORDER_DATE_COLUMN));
                String titleRecipe = c.getString(c.getColumnIndex(DBHelper.MEAL_FOR_ORDER_COLUMN));
                int key = c.getInt(c.getColumnIndex(DBHelper.UID_ORDER));
                listRecipeForOrder.add(new Recipe(titleRecipe, key));
                double priceOrder = 0.0;
                for (Recipe recipe : getRecipes()) {
                    if (titleRecipe.equals(recipe.getNameRecipe())) {
                        priceOrder = recipe.getPriceMeal();
                    }
                }
                Order order = new Order(dateOrder, listRecipeForOrder, priceOrder);
                ordersBuff.add(order);
            } while (c.moveToNext());
        }
        c.close();

        for (int i = 0; i < ordersBuff.size(); i++) {
            boolean isRepeatOrder = false;
            long dateOrder = ordersBuff.get(i).getDateOrder();
            for (Order orderOut : orders) {
                if (dateOrder == orderOut.getDateOrder()) {
                    isRepeatOrder = true;
                }
            }
            if (!isRepeatOrder) {
                double priceOrder = 0.0;
                listRecipeForOrder = new ArrayList<>();
                for (int j = i; j < ordersBuff.size(); j++) {
                    if (dateOrder == ordersBuff.get(j).getDateOrder()) {
                        for (Recipe recipe : ordersBuff.get(j).getListMeal()) {
                            listRecipeForOrder.add(recipe);
                            for (Recipe recipeOfStock : getRecipes()) {
                                if (recipeOfStock.getNameRecipe().equals(recipe.getNameRecipe())) {
                                    priceOrder = priceOrder + recipeOfStock.getPriceMeal();
                                }
                            }

                        }
                    }
                }
                Order order = new Order(dateOrder, listRecipeForOrder, priceOrder);
                orders.add(order);
            }
        }
        return orders;
    }
}
