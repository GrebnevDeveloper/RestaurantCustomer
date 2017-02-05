package com.developer.grebnev.restaurantcustomer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.developer.grebnev.restaurantcustomer.R;
import com.developer.grebnev.restaurantcustomer.adapter.OrderAdapter;
import com.developer.grebnev.restaurantcustomer.database.DBHelper;
import com.developer.grebnev.restaurantcustomer.model.Order;
import com.developer.grebnev.restaurantcustomer.model.Recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grebnev on 03.12.2016.
 */

public class EditingOrderDialog extends DialogFragment {
    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public EditingOrderDialog(FragmentManager fragmentManager, RecyclerView recyclerView) {
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    public static EditingOrderDialog newInstance(Order order, FragmentManager fragmentManager, RecyclerView recyclerView) {
        EditingOrderDialog editingOrderDialog = new EditingOrderDialog(fragmentManager, recyclerView);

        String[] nameRecipe = new String[order.getListMeal().size()];
        int[] keyRecipe = new int[order.getListMeal().size()];

        for (int i = 0; i < order.getListMeal().size(); i++) {
            nameRecipe[i] = order.getListMeal().get(i).getNameRecipe();
            keyRecipe[i] = order.getListMeal().get(i).getKeyRecipe();
        }

        Bundle args = new Bundle();
        args.putLong("dateOrder", order.getDateOrder());
        args.putStringArray("nameMealForOrder", nameRecipe);
        args.putIntArray("keyRecipe", keyRecipe);
        args.putDouble("priceOrder", order.getPriceOrder());

        editingOrderDialog.setArguments(args);
        return editingOrderDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();

        final List<Recipe> listRecipeForOrder = new ArrayList<>();
        long dateOrder = args.getLong("dateOrder");

        String[] nameRecipe = args.getStringArray("nameMealForOrder");
        int[] keyRecipe = args.getIntArray("keyRecipe");
        for (int i = 0; i < nameRecipe.length; i++) {
            listRecipeForOrder.add(new Recipe(nameRecipe[i], keyRecipe[i]));
        }
        double priceOrder = args.getDouble("priceOrder", 0);

        final Order order = new Order(dateOrder, listRecipeForOrder, priceOrder);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.editing_order_dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_order, null);

        final Spinner spMealForOrder = (Spinner) container.findViewById(R.id.sp_dialog_meal_for_order);

        final ListView lvMealForOrder = (ListView) container.findViewById(R.id.lv_meal_for_order);

        final Button btnAddMealForOrder = (Button) container.findViewById(R.id.btn_add_meal_for_order);

        final ArrayList<Recipe> listMealForOrder = new ArrayList<>();

        final ArrayList<String> recipeName = new ArrayList<>();

        dbHelper = new DBHelper(getActivity());

        for (Recipe recipe : dbHelper.query().getRecipes()) {
            recipeName.add(recipe.getNameRecipe());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, recipeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMealForOrder.setAdapter(adapter);

        listMealForOrder.addAll(order.getListMeal());
        final ArrayList<String> recipeNameForOrder = new ArrayList<String>();
        for (Recipe recipeForOrder : listMealForOrder) {
            recipeNameForOrder.add(recipeForOrder.getNameRecipe());
        }
        ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                recipeNameForOrder);

        lvMealForOrder.setAdapter(adapterForLV);

        btnAddMealForOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeNameForOrder.add(spMealForOrder.getSelectedItem().toString());
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        recipeNameForOrder);
                lvMealForOrder.setAdapter(adapterForLV);
                spMealForOrder.setSelection(0);
            }
        });

        lvMealForOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvProduct = (TextView) view;
                for (int i = 0; i < recipeNameForOrder.size(); i++) {
                    if (tvProduct.getText().toString().equals(recipeNameForOrder.get(i))) {
                        recipeNameForOrder.remove(i);
                    }
                }
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        recipeNameForOrder);
                lvMealForOrder.setAdapter(adapterForLV);
                return true;
            }
        });

        builder.setView(container);

        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.removeOrder(listRecipeForOrder);
                ArrayList<Recipe> mealsForOrder = new ArrayList<>();
                for (String nameMealForOrder : recipeNameForOrder) {
                    for (Recipe recipe : dbHelper.query().getRecipes()) {
                        if (nameMealForOrder.equals(recipe.getNameRecipe())) {
                            mealsForOrder.add(recipe);
                        }
                    }
                }
                order.setListMeal(mealsForOrder);
                dbHelper.saveOrder(order);
                OrderAdapter orderAdapter = new OrderAdapter(dbHelper.query().getOrders(), fragmentManager, dbHelper, recyclerView);
                recyclerView.setAdapter(orderAdapter);
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
