package com.developer.grebnev.restaurantcustomer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.developer.grebnev.restaurantcustomer.model.Order;
import com.developer.grebnev.restaurantcustomer.model.Product;
import com.developer.grebnev.restaurantcustomer.model.Recipe;

import java.util.List;

/**
 * Created by Grebnev on 29.10.2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "restaurant_customer_database";

    public static final String PRODUCTS_TABLE = "products_table";
    public static final String RECIPE_TABLE = "recipe_table";
    public static final String ORDER_TABLE = "order_table";

    public static final String UID_PRODUCT = "_id_product";

    public static final String PRODUCTS_TITLE_COLUMN = "product_title";
    public static final String PRODUCTS_QUANTITY_COLUMN = "product_quantity";
    public static final String PRODUCTS_PRICE_COLUMN = "product_price";

    public static final String UID_RECIPE = "_id_recipe";
    public static final String RECIPE_TITLE_COLUMN = "recipe_title";
    public static final String PRODUCT_FOR_RECIPE_COLUMN = "product_for_recipe";
    public static final String PRODUCT_QUANTITY_FOR_RECIPE_COLUMN = "product_quantity_for_recipe";

    public static final String UID_ORDER = "_id_order";
    public static final String ORDER_DATE_COLUMN = "order_date";
    public static final String MEAL_FOR_ORDER_COLUMN = "meal_for_order";
    public static final String PRICE_ORDER_COLUMN = "price_order";


    private static final String PRODUCT_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + PRODUCTS_TABLE + " (" + UID_PRODUCT + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRODUCTS_TITLE_COLUMN + " TEXT NOT NULL, "
            + PRODUCTS_QUANTITY_COLUMN + " LONG, " + PRODUCTS_PRICE_COLUMN + " LONG);";

    private static final String RECIPE_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + RECIPE_TABLE + " (" + UID_RECIPE + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RECIPE_TITLE_COLUMN + " TEXT NOT NULL, "
            + PRODUCT_FOR_RECIPE_COLUMN + " TEXT NOT NULL, " + PRODUCT_QUANTITY_FOR_RECIPE_COLUMN + " LONG);";

    private static final String ORDER_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + ORDER_TABLE + " (" + UID_ORDER + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ORDER_DATE_COLUMN + " LONG NOT NULL, "
            + MEAL_FOR_ORDER_COLUMN + " TEXT NOT NULL, " + PRICE_ORDER_COLUMN + " LONG);";

    private DBQueryManager dbQueryManager;
    private DBUpdateManager dbUpdateManager;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dbQueryManager = new DBQueryManager(getReadableDatabase());
        dbUpdateManager = new DBUpdateManager(getWritableDatabase());
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUCT_TABLE_CREATE_SCRIPT);
        db.execSQL(RECIPE_TABLE_CREATE_SCRIPT);
        db.execSQL(ORDER_TABLE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + PRODUCTS_TABLE);
        db.execSQL("DROP TABLE " + RECIPE_TABLE);
        db.execSQL("DROP TABLE " + ORDER_TABLE);
        onCreate(db);
    }

    public void saveProduct(Product product) {
        ContentValues newValues = new ContentValues();

        newValues.put(PRODUCTS_TITLE_COLUMN, product.getNameProduct());
        newValues.put(PRODUCTS_QUANTITY_COLUMN, product.getQuantityProduct());
        newValues.put(PRODUCTS_PRICE_COLUMN, product.getPriceProduct());

        getWritableDatabase().insert(PRODUCTS_TABLE, null, newValues);
    }

    public void saveRecipe(Recipe recipe) {
        ContentValues newValues = new ContentValues();

        for (Product productForRecipe : recipe.getListProductForRecipe()) {
            newValues.put(RECIPE_TITLE_COLUMN, recipe.getNameRecipe());
            newValues.put(PRODUCT_FOR_RECIPE_COLUMN, productForRecipe.getNameProduct());
            newValues.put(PRODUCT_QUANTITY_FOR_RECIPE_COLUMN, productForRecipe.getQuantityProduct());

            getWritableDatabase().insert(RECIPE_TABLE, null, newValues);
        }
    }

    public void saveOrder(Order order) {
        ContentValues newValues = new ContentValues();

        for (Recipe recipeForOrder : order.getListMeal()) {
            newValues.put(ORDER_DATE_COLUMN, order.getDateOrder());
            newValues.put(MEAL_FOR_ORDER_COLUMN, recipeForOrder.getNameRecipe());
            newValues.put(PRICE_ORDER_COLUMN, recipeForOrder.getPriceMeal());

            getWritableDatabase().insert(ORDER_TABLE, null, newValues);
        }
    }

    public DBQueryManager query() {
        return dbQueryManager;
    }

    public DBUpdateManager update() {
        return dbUpdateManager;
    }

    public void removeProduct(int key) {
        getWritableDatabase().delete(PRODUCTS_TABLE, UID_PRODUCT + "=" + key, null);
    }

    public void removeRecipe(List<Product> recipeForDelete) {
        for (Product product : recipeForDelete) {
            getWritableDatabase().delete(RECIPE_TABLE, UID_RECIPE + "=" + product.getKey(), null);
        }
    }

    public void removeOrder(List<Recipe> orderForDelete) {
        for (Recipe recipe : orderForDelete) {
            getWritableDatabase().delete(ORDER_TABLE, UID_ORDER + "=" + recipe.getKeyRecipe(), null);
        }
    }
}
