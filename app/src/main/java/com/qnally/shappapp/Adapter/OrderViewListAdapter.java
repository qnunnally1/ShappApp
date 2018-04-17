package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Model.PlacedOrder;
import com.qnally.shappapp.R;

import java.util.ArrayList;
import java.util.List;

public class OrderViewListAdapter extends RecyclerView.Adapter<OrderViewListAdapter.OrderListViewHolder>{

    private Context mContext;
    private List<Order> itemData = new ArrayList<>();

    public OrderViewListAdapter(Context context, List<Order> itemData) {
        mContext = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public OrderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListViewHolder holder, int position) {
        final Order item = itemData.get(position);
        //final String image = itemData.get(position).getItems().get(position).getImage();
        String image = itemData.get(position).getImage();

        Glide.with(mContext)
                .load(image)
                .into(holder.image);

        holder.name.setText(itemData.get(position).getProductName());
        holder.quantity.setText("Qty: " + itemData.get(position).getQuantity());
        holder.price.setText("Total: $" + itemData.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderListViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, quantity;
        public ImageView image;
        public RelativeLayout foreground;

        public void setName(TextView name) {
            this.name = name;
        }

        public OrderListViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.order_item_name);
            price = (TextView) itemView.findViewById(R.id.order_item_price);
            quantity = (TextView) itemView.findViewById(R.id.order_item_quantity);
            image = (ImageView) itemView.findViewById(R.id.order_item_image);
            foreground = (RelativeLayout) itemView.findViewById(R.id.order_view_foreground);
        }
    }
}
