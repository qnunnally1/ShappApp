package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.Interface.ItemClickListener;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.R;

import java.util.List;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ListViewHolder>{

    private Context mContext;
    private List<Item>  itemData;

    public ListRecyclerAdapter(Context context, List<Item> itemData) {
        mContext = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        String image = itemData.get(position).getImage();

        Glide.with(mContext)
                .load(image)
                .into(holder.image);

        holder.title.setText(itemData.get(position).getName());
        holder.price.setText(itemData.get(position).getPrice());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, price;
        ItemClickListener mItemClickListener;
        CardView mCardView;

        public ListViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.item_title);
            price = (TextView) itemView.findViewById(R.id.item_price);
            mCardView = (CardView) itemView.findViewById(R.id.card);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.OnClick(v, getAdapterPosition(), false);
        }
    }

}
