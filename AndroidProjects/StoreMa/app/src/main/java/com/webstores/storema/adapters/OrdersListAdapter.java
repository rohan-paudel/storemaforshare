package com.webstores.storema.adapters;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.webstores.storema.R;
import com.webstores.storema.activities.OrderDetailsActivity;
import com.webstores.storema.models.ItemsModel;
import com.webstores.storema.models.OrdersModel;

import java.util.ArrayList;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {

    private ArrayList<OrdersModel> ordersModelArrayList;

    private ViewGroup parent;


    public OrdersListAdapter() {
        ordersModelArrayList = new ArrayList<>();
    }

    public void clearData(){
        ordersModelArrayList.clear();
    }


    public void updateItemsList(OrdersModel ordersModel) {
        if(ordersModel == null){
            this.notifyDataSetChanged();
        }else {
            this.ordersModelArrayList.add(0,ordersModel);
            this.notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_orders_list, parent, false);
        return new OrdersListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrdersModel model = ordersModelArrayList.get(position);

        holder.textCustomerName.setText(model.getCustomerName());
        holder.textOrderNumber.setText(model.getOrderNumber());
        holder.textDate.setText(model.getDate());


        if(model.getOrderStatus().equals("rejected")){
            holder.textOrderNumber.setTextColor(Color.parseColor("#50cc0000"));
            holder.symbol.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#50cc0000")));
        }
        else if(model.getOrderStatus().equals("new")){

            holder.textOrderNumber.setTextColor(Color.parseColor("#00cc00"));
            holder.symbol.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00cc00")));


        } else if(model.getOrderStatus().equals("delivered")) {
            holder.textOrderNumber.setTextColor(Color.parseColor("#50000000"));
            holder.symbol.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#50000000")));
        }

        else {
            holder.textOrderNumber.setTextColor(Color.parseColor("#0000cc"));
            holder.symbol.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#f00000cc")));
        }

        holder.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent = new Intent(parent.getContext(), OrderDetailsActivity.class);
                intent.putExtra("orderDate", model.getDate());
                intent.putExtra("orderNumber", model.getOrderNumber());
                intent.putExtra("totalOrders", model.getTotalOrders());
                intent.putExtra("ordersPrice", model.getOrdersPrice());
                intent.putExtra("ordersName", model.getOrdersName());
                intent.putExtra("ordersImage", model.getOrdersImage());
                intent.putExtra("ordersQuantity", model.getOrdersQuantity());
                intent.putExtra("deliveryCharges", model.getDeliveryCharge());
                intent.putExtra("customerName", model.getCustomerName());
                intent.putExtra("customerPhone", model.getCustomerPhone());
                intent.putExtra("customerEmail", model.getCustomerEmail());
                intent.putExtra("customerAddress", model.getCustomerAddress());
                intent.putExtra("orderStatus", model.getOrderStatus());
                intent.putExtra("ordersGst", model.getOrdersGst());
                parent.getContext().startActivity(intent);
            }
        });









    }

    @Override
    public int getItemCount() {
        return ordersModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textCustomerName, textDate, textOrderNumber;
        private MaterialCardView orderButton;
        private View symbol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textCustomerName = itemView.findViewById(R.id.customer_name);
            textDate = itemView.findViewById(R.id.date);
            textOrderNumber = itemView.findViewById(R.id.order_number);
            orderButton = itemView.findViewById(R.id.order_button);
            symbol = itemView.findViewById(R.id.symbol);



        }
    }

}
