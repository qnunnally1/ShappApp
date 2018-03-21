package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MostPopularAdapter extends RecyclerView.Adapter<MostPopularAdapter.ViewHolder>{

    private List<MostPopularItems> load = new ArrayList<>();
    private Context mContext;

    public MostPopularAdapter(Context context, List<MostPopularItems> imageUrls) {
        mContext = context;
        load = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MostPopularItems image = load.get(position);

        Glide.with(mContext)
                .load(image.getImage())
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return load.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);

            mImageView = view.findViewById(R.id.image_rec);
        }
    }
}
