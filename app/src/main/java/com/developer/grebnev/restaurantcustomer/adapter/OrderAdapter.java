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
import com.developer.grebnev.restaurantcustomer.dialog.EditingOrderDialog;
import com.developer.grebnev.restaurantcustomer.model.Order;
import com.developer.grebnev.restaurantcustomer.model.Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by Grebnev on 03.12.2016.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    List<Order> orders;
    FragmentManager fragmentManager;
    DBHelper dbHelper;
    RecyclerView recyclerView;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public OrderAdapter(List<Order> orders, FragmentManager fragmentManager, DBHelper dbHelper, RecyclerView recyclerView) {
        this.orders = orders;
        this.fragmentManager = fragmentManager;
        this.dbHelper = dbHelper;
        this.recyclerView = recyclerView;
    }



    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView dateOrder;
        TextView priceOrder;

        public OrderViewHolder(View itemView) {
            super(itemView);
            dateOrder = (TextView) itemView.findViewById(R.id.tv_name);
            priceOrder = (TextView) itemView.findViewById(R.id.tv_sum);
        }
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_order_item, parent, false);
        OrderViewHolder orderViewHolder = new OrderViewHolder(v);
        return orderViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder orderViewHolder, final int position) {
        orderViewHolder.dateOrder.setText(Utils.getFullDate(orders.get(position).getDateOrder()));
        orderViewHolder.priceOrder.setText(String.valueOf(new BigDecimal(orders.get(position).getPriceOrder()*1.2).setScale(2, RoundingMode.UP).doubleValue()));

        final View itemView = orderViewHolder.itemView;

        itemView.setVisibility(View.VISIBLE);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment editingOrderDialog = EditingOrderDialog.newInstance(orders.get(position), fragmentManager, recyclerView);
                editingOrderDialog.show(fragmentManager, "EditOrderDialog");
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dbHelper.removeOrder(orders.get(position).getListMeal());
                recyclerView.setAdapter(new OrderAdapter(dbHelper.query().getOrders(), fragmentManager, dbHelper, recyclerView));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
