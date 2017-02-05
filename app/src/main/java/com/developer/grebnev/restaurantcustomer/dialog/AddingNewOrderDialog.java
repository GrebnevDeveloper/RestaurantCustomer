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
import java.util.Date;

/**
 * Created by Grebnev on 29.10.2016.
 */

public class AddingNewOrderDialog extends DialogFragment {

    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public AddingNewOrderDialog(FragmentManager fragmentManager, RecyclerView recyclerView) {
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.adding_order_dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_order, null);

        final Spinner spMealForOrder = (Spinner) container.findViewById(R.id.sp_dialog_meal_for_order);

        final ListView lvMealForOrder = (ListView) container.findViewById(R.id.lv_meal_for_order);

        final Button btnAddMealForOrder = (Button) container.findViewById(R.id.btn_add_meal_for_order);

        final ArrayList<Recipe> listMealForOrder = new ArrayList<>();

        ArrayList<String> recipeName = new ArrayList<>();

        dbHelper = new DBHelper(getActivity());

        for (Recipe recipe : dbHelper.query().getRecipes()) {
            recipeName.add(recipe.getNameRecipe());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, recipeName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMealForOrder.setAdapter(adapter);

        final ArrayList<String> mealNameForOrder = new ArrayList<String>();

        btnAddMealForOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mealNameForOrder.add(spMealForOrder.getSelectedItem().toString());
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        mealNameForOrder);
                lvMealForOrder.setAdapter(adapterForLV);
                spMealForOrder.setSelection(0);
            }
        });

        lvMealForOrder.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvProduct = (TextView) view;
                for (int i = 0; i < mealNameForOrder.size(); i++) {
                    if (tvProduct.getText().toString().equals(mealNameForOrder.get(i))) {
                        mealNameForOrder.remove(i);
                    }
                }
                ArrayAdapter<String> adapterForLV = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        mealNameForOrder);
                lvMealForOrder.setAdapter(adapterForLV);
                return true;
            }
        });

        builder.setView(container);

        final Order order = new Order();

        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Date dateOrder = new Date();
                order.setDateOrder(dateOrder.getTime());
                ArrayList<Recipe> mealsForOrder = new ArrayList<>();
                for (String nameMealForOrder : mealNameForOrder) {
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
