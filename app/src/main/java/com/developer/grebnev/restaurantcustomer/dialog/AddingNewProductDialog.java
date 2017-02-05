package com.developer.grebnev.restaurantcustomer.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.developer.grebnev.restaurantcustomer.R;
import com.developer.grebnev.restaurantcustomer.adapter.ProductAdapter;
import com.developer.grebnev.restaurantcustomer.database.DBHelper;
import com.developer.grebnev.restaurantcustomer.model.Product;

/**
 * Created by Grebnev on 23.10.2016.
 */

public class AddingNewProductDialog extends DialogFragment {

    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public AddingNewProductDialog(FragmentManager fragmentManager, RecyclerView recyclerView) {
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.adding_product_dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View container = inflater.inflate(R.layout.dialog_add_product, null);

        final TextInputLayout tilName = (TextInputLayout) container.findViewById(R.id.til_dialog_product_name);
        final EditText etName = tilName.getEditText();

        TextInputLayout tilQuantity = (TextInputLayout) container.findViewById(R.id.til_dialog_product_quantity);
        final EditText etQuantity = tilQuantity.getEditText();

        final TextInputLayout tilPrice = (TextInputLayout) container.findViewById(R.id.til_dialog_product_price);
        final EditText etPrice = tilPrice.getEditText();

        tilName.setHint(getResources().getString(R.string.product_name));
        tilQuantity.setHint(getResources().getString(R.string.product_quantity));
        tilPrice.setHint(getResources().getString(R.string.product_price));

        builder.setView(container);

        final Product product = new Product();
        dbHelper = new DBHelper(getActivity());

        builder.setPositiveButton(getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                product.setNameProduct(etName.getText().toString());
                product.setQuantityProduct(Double.parseDouble(etQuantity.getText().toString()));
                product.setPriceProduct(Double.parseDouble(etPrice.getText().toString()));
                dbHelper.saveProduct(product);
                ProductAdapter productAdapter = new ProductAdapter(dbHelper.query().getProducts(), fragmentManager, dbHelper, recyclerView);
                recyclerView.setAdapter(productAdapter);
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
