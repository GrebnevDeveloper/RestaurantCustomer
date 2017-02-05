package com.developer.grebnev.restaurantcustomer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.grebnev.restaurantcustomer.R;
import com.developer.grebnev.restaurantcustomer.adapter.RecipeAdapter;
import com.developer.grebnev.restaurantcustomer.database.DBHelper;
import com.developer.grebnev.restaurantcustomer.model.Product;
import com.developer.grebnev.restaurantcustomer.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 26.10.2016.
 */

public class AddingNewRecipeDialog extends DialogFragment {

    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public AddingNewRecipeDialog(FragmentManager fragmentManager, RecyclerView recyclerView) {
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.adding_recipe_dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_recipe, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.til_dialog_recipe_name);
        final EditText etName = tilName.getEditText();

        final TextInputLayout tvProduct = (TextInputLayout) container.findViewById(R.id.til_tv_products);

        final Spinner spProductForRecipe = (Spinner) container.findViewById(R.id.sp_dialog_products_for_recipe);

        final TextInputLayout tilRecipeQuantity = (TextInputLayout) container.findViewById(R.id.til_dialog_recipe_quantity);
        final EditText etQuantity = tilRecipeQuantity.getEditText();

        final ListView lvProductForRecipe = (ListView) container.findViewById(R.id.lv_products_for_recipe);

        final Button btnAddProductForRecipe = (Button) container.findViewById(R.id.btn_add_product_for_recipe);

        tilName.setHint(getString(R.string.recipe_name));
        tilRecipeQuantity.setHint(getString(R.string.quantity_product_for_recipe));

        final ArrayList<Product> listProductForRecipe = new ArrayList<>();

        ArrayList<String> productName = new ArrayList<>();

        dbHelper = new DBHelper(getActivity());

        for (Product product : dbHelper.query().getProducts()) {
            productName.add(product.getNameProduct());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, productName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spProductForRecipe.setAdapter(adapter);

        btnAddProductForRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProductForRecipe.add(new Product(spProductForRecipe.getSelectedItemPosition(), spProductForRecipe.getSelectedItem().toString(),
                        Double.parseDouble(etQuantity.getText().toString())));
                ArrayList<String> productNameForRecipe = new ArrayList<String>();
                for (Product productForRecipe : listProductForRecipe) {
                    productNameForRecipe.add(productForRecipe.getNameProduct());
                }
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        productNameForRecipe);
                lvProductForRecipe.setAdapter(adapterForLV);
                spProductForRecipe.setSelection(0);
                etQuantity.setText("");
            }
        });

        lvProductForRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvProduct = (TextView) view;
                ArrayList<String> productNameForRecipe = new ArrayList<>();
                for (int i = 0; i < listProductForRecipe.size(); i++) {
                    if (tvProduct.getText().toString().equals(listProductForRecipe.get(i).getNameProduct())) {
                        etQuantity.setText(String.valueOf(listProductForRecipe.get(i).getQuantityProduct()));
                        spProductForRecipe.setSelection(listProductForRecipe.get(i).getKey());
                        listProductForRecipe.remove(i);
                    }
                }
                for (Product product : listProductForRecipe) {
                    productNameForRecipe.add(product.getNameProduct());
                }
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        productNameForRecipe);
                lvProductForRecipe.setAdapter(adapterForLV);
            }
        });

        lvProductForRecipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvProduct = (TextView) view;
                ArrayList<String> productNameForRecipe = new ArrayList<>();
                for (int i = 0; i < listProductForRecipe.size(); i++) {
                    if (tvProduct.getText().toString().equals(listProductForRecipe.get(i).getNameProduct())) {
                        listProductForRecipe.remove(i);
                    }
                }
                for (Product product : listProductForRecipe) {
                    productNameForRecipe.add(product.getNameProduct());
                }
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        productNameForRecipe);
                lvProductForRecipe.setAdapter(adapterForLV);
                return true;
            }
        });

        builder.setView(container);

        final Recipe recipe = new Recipe();

        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recipe.setNameRecipe(etName.getText().toString());
                recipe.setListProductForRecipe(listProductForRecipe);
                dbHelper.saveRecipe(recipe);
                RecipeAdapter recipeAdapter = new RecipeAdapter(dbHelper.query().getRecipes(), fragmentManager, dbHelper, recyclerView);
                recyclerView.setAdapter(recipeAdapter);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        return alertDialog;
    }
}
