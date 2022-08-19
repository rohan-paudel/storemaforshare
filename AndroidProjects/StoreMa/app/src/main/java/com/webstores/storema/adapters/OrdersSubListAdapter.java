package com.webstores.storema.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webstores.storema.R;
import com.webstores.storema.models.ItemsModel;
import com.webstores.storema.models.OrdersSubListModel;

import java.util.ArrayList;

public class OrdersSubListAdapter extends RecyclerView.Adapter<OrdersSubListAdapter.ViewHolder>{

    private ArrayList<OrdersSubListModel> ordersSubListModelArrayList;

    public OrdersSubListAdapter() {
        ordersSubListModelArrayList = new ArrayList<>();
    }


    public void clearData(){
        ordersSubListModelArrayList.clear();
    }


    public void updateItemsList(OrdersSubListModel itemsModel) {
        this.ordersSubListModelArrayList.add(itemsModel);
        this.notifyDataSetChanged();
    }

    private ViewGroup parent;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_sub_orders_list, parent, false);
        return new OrdersSubListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrdersSubListModel model = ordersSubListModelArrayList.get(position);


        holder.orderName.setText(model.getStringOrderName());
        holder.orderPrice.setText("Price: "+model.getStringOrderPrice());
        holder.orderQuantity.setText("QTY: "+model.getStringOrderQuantity());
        Glide.with(parent.getContext())
                .load(model.getStringOrderImage())
                .into(holder.imgItemLogo);


    }

    @Override
    public int getItemCount() {
        return ordersSubListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imgItemLogo;
        private TextView orderName;
        private TextView orderPrice;
        private TextView orderQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgItemLogo = itemView.findViewById(R.id.img_item_logo);
            orderQuantity = itemView.findViewById(R.id.order_quantity);
            orderPrice = itemView.findViewById(R.id.order_price);
            orderName = itemView.findViewById(R.id.order_name);


        }
    }
}
