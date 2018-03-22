package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.qnally.shappapp.Interface.OnItemClickListener;
import com.qnally.shappapp.ItemPage;
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
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
        final Item item = itemData.get(position);
        final String image = itemData.get(position).getImage();

        Glide.with(mContext)
                .load(image)
                .into(holder.image);

        holder.title.setText(itemData.get(position).getName());
        holder.price.setText(itemData.get(position).getPrice());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ItemPage.class);

                intent.putExtra("Image", itemData.get(position).getImage());
                intent.putExtra("Name", itemData.get(position).getName());
                intent.putExtra("Price", itemData.get(position).getPrice());
                intent.putExtra("Description", itemData.get(position).getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }


    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, price;
        CardView mCardView;
        Button add;

        public ListViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.list_title);
            price = (TextView) itemView.findViewById(R.id.list_price);
            //add = (Button) itemView.findViewById(R.id.list_add2c);
            mCardView = (CardView) itemView.findViewById(R.id.card);
        }
    }

}
