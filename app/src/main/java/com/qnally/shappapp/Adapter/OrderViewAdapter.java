package com.qnally.shappapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.qnally.shappapp.Model.PlacedOrder;
import com.qnally.shappapp.OrderHistory;
import com.qnally.shappapp.OrderHistoryList;
import com.qnally.shappapp.R;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.OrderViewHolder>{

    private Context mContext;
    private List<PlacedOrder> itemData = new ArrayList<>();

    public OrderViewAdapter(Context context, List<PlacedOrder> itemData) {
        mContext = context;
        this.itemData = itemData;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position) {
        final PlacedOrder nitem = itemData.get(position);

        String id = String.valueOf(itemData.get(position).getOrder_id());

        holder.order_id.setText(id);
        holder.order_date.setText(itemData.get(position).getDate());
        holder.order_total.setText(itemData.get(position).getTotal());
        holder.order_status.setText(convertStatus(itemData.get(position).getStatus()));
        holder.order_address.setText(itemData.get(position).getAddress());
        holder.order_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderHistoryList.class);
                intent.putExtra("Total", nitem.getTotal());
                intent.putExtra("Item List", (Serializable)nitem.getItems());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.optionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(mContext, holder.optionDigit);
                popupMenu.inflate(R.menu.order_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){
                            case R.id.view_order :
                                Intent intent = new Intent(mContext, OrderHistoryList.class);
                                intent.putExtra("Total", nitem.getTotal());
                                intent.putExtra("Item List", (Serializable)nitem.getItems());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                                break;
                            case R.id.cance_order:
                                if(nitem.getStatus().equals("0")){
                                    itemData.remove(position);
                                    notifyDataSetChanged();

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Orders").child(String.valueOf(nitem.getOrder_id())).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("Delete", "Notification has been deleted");
                                                    } else {
                                                        Log.d("Delete", "Notification couldn't be deleted");
                                                    }
                                                }
                                            });

                                } else if(nitem.getStatus().equals("1")){
                                    Toast.makeText(mContext, "This order has already been placed.", Toast.LENGTH_SHORT).show();
                                } else if(nitem.getStatus().equals("2")){
                                    Toast.makeText(mContext, "This order has already been shipped.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "This order has already been delivered.", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private String convertStatus(String status) {
        if(status.equals("0"))
            return "Processing";
        else if(status.equals("1"))
            return "Placed";
        else if(status.equals("2"))
            return "Shipped";
        else
            return "Delivered";
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }


    public class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView order_id, order_status, order_address, order_total, order_date;
        private ImageButton optionDigit;
        private CardView order_card;

        public OrderViewHolder(View itemView) {
            super(itemView);

            order_id = (TextView) itemView.findViewById(R.id.order_id);
            order_status = (TextView) itemView.findViewById(R.id.order_status);
            order_address = (TextView) itemView.findViewById(R.id.order_address);
            order_total = (TextView) itemView.findViewById(R.id.order_total);
            order_date = (TextView) itemView.findViewById(R.id.order_date);
            order_card = (CardView) itemView.findViewById(R.id.order_card);
            optionDigit = (ImageButton) itemView.findViewById(R.id.optionDigit);
        }
    }
}
