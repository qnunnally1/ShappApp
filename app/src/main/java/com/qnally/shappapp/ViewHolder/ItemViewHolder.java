package com.qnally.shappapp.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qnally.shappapp.Interface.OnItemClickListener;
import com.qnally.shappapp.R;

public class ItemViewHolder extends RecyclerView.ViewHolder{

    public ImageView image;
    public TextView title, price, description;
    public CardView card;

    public ItemViewHolder(View itemView) {
        super(itemView);

        image = (ImageView)itemView.findViewById(R.id.item_img);
        title = (TextView) itemView.findViewById(R.id.item_title);
        price = (TextView) itemView.findViewById(R.id.item_price);
        description = (TextView) itemView.findViewById(R.id.item_details);
        card = (CardView) itemView.findViewById(R.id.card);
    }
}