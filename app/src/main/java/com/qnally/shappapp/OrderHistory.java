package com.qnally.shappapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Adapter.CartAdapter;
import com.qnally.shappapp.Adapter.ListRecyclerAdapter;
import com.qnally.shappapp.Adapter.OrderViewAdapter;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Model.PlacedOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderHistory extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager lm;
    private OrderViewAdapter adapter;

    android.support.v7.widget.Toolbar mToolbar;

    List<PlacedOrder> po = new ArrayList<>();

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Order History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Orders");


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_cart);
        mRecyclerView.setHasFixedSize(true);
        lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);

        mReference.child("Orders").orderByChild("email").equalTo(Common.current.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren()){
                    PlacedOrder one = post.getValue(PlacedOrder.class);
                    po.add(one);
                }

                Collections.sort(po);

                adapter = new OrderViewAdapter(getApplicationContext(), po);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(OrderHistory.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(OrderHistory.this, Homepage.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
