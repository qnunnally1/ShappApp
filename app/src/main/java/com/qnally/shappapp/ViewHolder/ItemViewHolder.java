package com.qnally.shappapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qnally.shappapp.Interface.ItemClickListener;
import com.qnally.shappapp.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView image;
    public TextView title, price;

    private ItemClickListener mItemClickListener;

    public ItemViewHolder(View itemView) {
        super(itemView);

        image = (ImageView)itemView.findViewById(R.id.item_img);
        title = (TextView) itemView.findViewById(R.id.item_title);
        price = (TextView) itemView.findViewById(R.id.item_price);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.OnClick(v, getAdapterPosition(), false);

    }
}