package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.Model.SuggestedItems;
import com.qnally.shappapp.R;

import java.util.ArrayList;
import java.util.List;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<SuggestedItems> load = new ArrayList<>();
    private Context mContext;

    public SuggestedAdapter(Context context, List<SuggestedItems> imageUrls) {
        mContext = context;
        load = imageUrls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called.");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called.");

        SuggestedItems image = load.get(position);

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
