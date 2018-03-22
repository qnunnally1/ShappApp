package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.ItemPage;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MostPopularItems image = load.get(position);
        final String photo = load.get(position).getImage();

        Glide.with(mContext)
                .load(image.getImage())
                .into(holder.mImageView);

        holder.title.setText(load.get(position).getName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemPage.class);

                intent.putExtra("Image", load.get(position).getImage());
                intent.putExtra("Name", load.get(position).getName());
                intent.putExtra("Price", load.get(position).getPrice());
                intent.putExtra("Description", load.get(position).getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return load.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView title, price;
        CardView card;

        public ViewHolder(View view) {
            super(view);

            mImageView = view.findViewById(R.id.image_rec);
            title = (TextView) itemView.findViewById(R.id.horizontal_title);
            price = (TextView) itemView.findViewById(R.id.item_price);
            card = (CardView) itemView.findViewById(R.id.hor_card);
        }
    }
}
