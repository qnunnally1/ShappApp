package com.qnally.shappapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qnally.shappapp.Interface.ItemClickListener;
import com.qnally.shappapp.R;

public class MostPopularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView mImageView;

    private ItemClickListener mItemClickListener;

    public MostPopularViewHolder(View itemView) {
        super(itemView);

        mImageView = (ImageView) itemView.findViewById(R.id.image_rec);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.OnClick(v,getAdapterPosition(),false);
    }
}
