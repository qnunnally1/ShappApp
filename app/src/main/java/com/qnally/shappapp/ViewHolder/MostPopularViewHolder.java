package com.qnally.shappapp.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.qnally.shappapp.R;

public class MostPopularViewHolder extends RecyclerView.ViewHolder {

    public ImageView mImageView;

    public MostPopularViewHolder(View itemView) {
        super(itemView);

        mImageView = (ImageView) itemView.findViewById(R.id.image_rec);
    }
}
