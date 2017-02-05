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
import com.developer.grebnev.restaurantcustomer.dialog.EditingProductDialog;
import com.developer.grebnev.restaurantcustomer.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Grebnev on 20.11.2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    List<Product> products;
    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    public ProductAdapter(List<Product> products, FragmentManager fragmentManager, DBHelper dbHelper, RecyclerView recyclerView) {
        this.dbHelper = dbHelper;
        this.products = products;
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameProduct;
        TextView quantityProduct;
        TextView priceProduct;
        TextView sumProduct;

        public ProductViewHolder(View itemView) {
            super(itemView);
            nameProduct = (TextView) itemView.findViewById(R.id.tv_name);
            quantityProduct = (TextView) itemView.findViewById(R.id.tv_quantity);
            priceProduct = (TextView) itemView.findViewById(R.id.tv_price);
            sumProduct = (TextView) itemView.findViewById(R.id.tv_sum);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_product_item, parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(v);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, final int position) {
        productViewHolder.nameProduct.setText(products.get(position).getNameProduct());
        productViewHolder.quantityProduct.setText(String.valueOf(products.get(position).getQuantityProduct()));
        productViewHolder.priceProduct.setText(String.valueOf(products.get(position).getPriceProduct()));
        productViewHolder.sumProduct.setText(String.valueOf(new BigDecimal(products.get(position).getQuantityProduct()
                * products.get(position).getPriceProduct()).setScale(2, RoundingMode.UP).doubleValue()));
        final View itemView = productViewHolder.itemView;

        itemView.setVisibility(View.VISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment editingProductDialog = EditingProductDialog.newInstance(products.get(position), fragmentManager, recyclerView);
                editingProductDialog.show(fragmentManager, "EditProductDialog");
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbHelper.removeProduct(products.get(position).getKey());
                recyclerView.setAdapter(new ProductAdapter(dbHelper.query().getProducts(), fragmentManager, dbHelper, recyclerView));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
