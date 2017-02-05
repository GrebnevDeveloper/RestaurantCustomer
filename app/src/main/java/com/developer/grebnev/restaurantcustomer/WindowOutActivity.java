package com.developer.grebnev.restaurantcustomer;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.developer.grebnev.restaurantcustomer.adapter.OrderAdapter;
import com.developer.grebnev.restaurantcustomer.adapter.ProductAdapter;
import com.developer.grebnev.restaurantcustomer.adapter.RecipeAdapter;
import com.developer.grebnev.restaurantcustomer.database.DBHelper;
import com.developer.grebnev.restaurantcustomer.dialog.AddingNewOrderDialog;
import com.developer.grebnev.restaurantcustomer.dialog.AddingNewProductDialog;
import com.developer.grebnev.restaurantcustomer.dialog.AddingNewRecipeDialog;

/**
 * Created by Grebnev on 22.10.2016.
 */

public class WindowOutActivity extends AppCompatActivity {

    FragmentManager fragmentManager;
    RecyclerView rv;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_window_out);

        rv = (RecyclerView)findViewById(R.id.rv_item);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        fragmentManager = getFragmentManager();

        final Intent intent = getIntent();
        if (intent.getStringExtra("model").equals(getString(R.string.products))) {
            setTitle(R.string.products);
            dbHelper = new DBHelper(this);
            ProductAdapter productAdapter = new ProductAdapter(dbHelper.query().getProducts(), fragmentManager, dbHelper, rv);
            rv.setAdapter(productAdapter);
            rv.invalidate();
        }
        if (intent.getStringExtra("model").equals(getString(R.string.recipes))) {
            setTitle(R.string.recipes);
            dbHelper = new DBHelper(this);
            RecipeAdapter recipeAdapter = new RecipeAdapter(dbHelper.query().getRecipes(), fragmentManager, dbHelper, rv);
            rv.setAdapter(recipeAdapter);
            rv.getAdapter().notifyDataSetChanged();
            rv.invalidate();
        }
        if (intent.getStringExtra("model").equals(getString(R.string.orders))) {
            setTitle(R.string.orders);
            dbHelper = new DBHelper(this);
            OrderAdapter orderAdapter = new OrderAdapter(dbHelper.query().getOrders(), fragmentManager, dbHelper, rv);
            rv.setAdapter(orderAdapter);
            rv.invalidate();
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent.getStringExtra("model").equals(getString(R.string.products))) {
                    DialogFragment addingNewProductDialog = new AddingNewProductDialog(fragmentManager, rv);
                    addingNewProductDialog.show(fragmentManager, "AddingNewProductDialog");
                }
                if (intent.getStringExtra("model").equals(getString(R.string.recipes))) {
                    DialogFragment addingNewRecipeDialog = new AddingNewRecipeDialog(fragmentManager, rv);
                    addingNewRecipeDialog.show(fragmentManager, "AddingNewRecipeDialog");
                }
                if (intent.getStringExtra("model").equals(getString(R.string.orders))) {
                    DialogFragment addingNewOrderDialog = new AddingNewOrderDialog(fragmentManager, rv);
                    addingNewOrderDialog.show(fragmentManager, "AddingNewOrderDialog");
                }
            }
        });
    }
}
