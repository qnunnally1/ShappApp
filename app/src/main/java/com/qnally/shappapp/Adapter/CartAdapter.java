package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.ItemPage;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter  extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private Context mContext;
    private List<Order>  itemData = new ArrayList<>();

    public CartAdapter(Context context, List<Order> itemData) {
        mContext = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        final Order item = itemData.get(position);
        final String image = itemData.get(position).getImage();

        Glide.with(mContext)
                .load(image)
                .into(holder.image);

        holder.name.setText(itemData.get(position).getProductName());
        holder.quantity.setText("Qty: " + itemData.get(position).getQuantity());

        String preprice = itemData.get(position).getPrice();
        String parseprice = preprice.replace("$","").replace(",","");

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        double _price = (Double.parseDouble(parseprice)) * (Integer.parseInt(itemData.get(position).getQuantity()));
        holder.price.setText(fmt.format(_price));

    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public Order getItem(int pos){
        return itemData.get(pos);
    }

    public void removeItem(int pos){
        itemData.remove(pos);
        notifyItemRemoved(pos);
    }

    public void restoreItem(Order mitem, int pos){
        itemData.add(pos, mitem);
        notifyItemInserted(pos);
    }


    public class CartViewHolder extends RecyclerView.ViewHolder{
        public TextView name, price, quantity;
        public ImageView image;
        public RelativeLayout background, foreground;

        public void setName(TextView name) {
            this.name = name;
        }

        public CartViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.cart_name);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            quantity = (TextView) itemView.findViewById(R.id.cart_quantity);
            image = (ImageView) itemView.findViewById(R.id.cart_image);
            background = (RelativeLayout) itemView.findViewById(R.id.view_background);
            foreground = (RelativeLayout) itemView.findViewById(R.id.view_foreground);
        }
    }
}
