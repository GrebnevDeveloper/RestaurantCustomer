package com.developer.grebnev.restaurantcustomer.adapter;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.grebnev.restaurantcustomer.R;
import com.developer.grebnev.restaurantcustomer.database.DBHelper;
import com.developer.grebnev.restaurantcustomer.dialog.EditingRecipeDialog;
import com.developer.grebnev.restaurantcustomer.model.Product;
import com.developer.grebnev.restaurantcustomer.model.Recipe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 01.12.2016.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    List<Recipe> recipes;
    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public RecipeAdapter(List<Recipe> recipes, FragmentManager fragmentManager, DBHelper dbHelper, RecyclerView recyclerView) {
        this.recipes = recipes;
        this.fragmentManager = fragmentManager;
        this.dbHelper = dbHelper;
        this.recyclerView = recyclerView;
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView nameRecipe;
        TextView quantityMeal;
        TextView costPrice;
        TextView sumMeal;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            nameRecipe = (TextView) itemView.findViewById(R.id.tv_name);
            quantityMeal = (TextView) itemView.findViewById(R.id.tv_quantity);
            costPrice = (TextView) itemView.findViewById(R.id.tv_price);
            sumMeal = (TextView) itemView.findViewById(R.id.tv_sum);
        }
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_recipe_item, parent, false);
        RecipeViewHolder recipeViewHolder = new RecipeViewHolder(v);
        return recipeViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder recipeViewHolder, final int position) {
        recipeViewHolder.nameRecipe.setText(recipes.get(position).getNameRecipe());
        ArrayList<Product> productsForRecipe = new ArrayList<>();
        productsForRecipe.addAll(recipes.get(position).getListProductForRecipe());
        int minQuantityMeal = 10000;
        for (Product productForRecipe : productsForRecipe) {
            for (Product product : dbHelper.query().getProducts()) {
                if (product.getNameProduct().equals(productForRecipe.getNameProduct())) {
                    int minQuantityBuff = (int) (product.getQuantityProduct() / productForRecipe.getQuantityProduct());
                    if (minQuantityBuff < minQuantityMeal) {
                        minQuantityMeal = minQuantityBuff;
                    }
                }
            }
        }
        recipeViewHolder.quantityMeal.setText(String.valueOf(minQuantityMeal));
        recipeViewHolder.costPrice.setText(String.valueOf(new BigDecimal(recipes.get(position).getPriceMeal()).setScale(2, RoundingMode.UP).doubleValue()));
        recipeViewHolder.sumMeal.setText(String.valueOf(new BigDecimal(recipes.get(position).getPriceMeal()*1.2).setScale(2, RoundingMode.UP).doubleValue()));

        final View itemView = recipeViewHolder.itemView;

        itemView.setVisibility(View.VISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment editingRecipeDialog = EditingRecipeDialog.newInstance(recipes.get(position), fragmentManager, recyclerView);
                editingRecipeDialog.show(fragmentManager, "EditRecipeDialog");
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbHelper.removeRecipe(recipes.get(position).getListProductForRecipe());
                recyclerView.setAdapter(new RecipeAdapter(dbHelper.query().getRecipes(), fragmentManager, dbHelper, recyclerView));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
