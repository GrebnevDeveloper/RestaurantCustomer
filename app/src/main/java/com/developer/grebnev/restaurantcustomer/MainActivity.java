package com.developer.grebnev.restaurantcustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvProducts;
    private TextView tvRecipes;
    private TextView tvOrders;
    private ImageView ivProducts;
    private ImageView ivRecipes;
    private ImageView ivOrders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvProducts = (TextView) findViewById(R.id.tv_products);
        tvProducts.setOnClickListener(this);

        tvRecipes = (TextView) findViewById(R.id.tv_recipes);
        tvRecipes.setOnClickListener(this);

        tvOrders = (TextView) findViewById(R.id.tv_orders);
        tvOrders.setOnClickListener(this);

        ivProducts = (ImageView) findViewById(R.id.products);
        ivProducts.setOnClickListener(this);

        ivRecipes = (ImageView) findViewById(R.id.recipes);
        ivRecipes.setOnClickListener(this);

        ivOrders = (ImageView) findViewById(R.id.orders);
        ivOrders.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_products:
                Intent intentProducts = new Intent(MainActivity.this, WindowOutActivity.class);
                intentProducts.putExtra("model", tvProducts.getText().toString());
                startActivity(intentProducts);
                break;
            case  R.id.tv_recipes:
                Intent intentRecipes = new Intent(MainActivity.this, WindowOutActivity.class);
                intentRecipes.putExtra("model", tvRecipes.getText().toString());
                startActivity(intentRecipes);
                break;
            case R.id.tv_orders:
                Intent intentOrders = new Intent(MainActivity.this, WindowOutActivity.class);
                intentOrders.putExtra("model", tvOrders.getText().toString());
                startActivity(intentOrders);
                break;
            case R.id.products:
                Intent intentProductsImage = new Intent(MainActivity.this, WindowOutActivity.class);
                intentProductsImage.putExtra("model", tvProducts.getText().toString());
                startActivity(intentProductsImage);
                break;
            case  R.id.recipes:
                Intent intentRecipesImage = new Intent(MainActivity.this, WindowOutActivity.class);
                intentRecipesImage.putExtra("model", tvRecipes.getText().toString());
                startActivity(intentRecipesImage);
                break;
            case R.id.orders:
                Intent intentOrdersImage = new Intent(MainActivity.this, WindowOutActivity.class);
                intentOrdersImage.putExtra("model", tvOrders.getText().toString());
                startActivity(intentOrdersImage);
                break;
        }
    }
}
