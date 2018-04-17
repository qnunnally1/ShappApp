package com.qnally.shappapp;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.qnally.shappapp.Adapter.CartAdapter;
import com.qnally.shappapp.Adapter.OrderViewListAdapter;
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Model.PlacedOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryList extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Order> items = new ArrayList<>();
    private CartAdapter adapter;
    private CoordinatorLayout rootlayout;

    android.support.v7.widget.Toolbar mToolbar;
    TextView totalPrice, orderCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_list);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.order_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Order Items");

        /*mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_cart);
        rootlayout = (CoordinatorLayout) findViewById(R.id.order_root);
        totalPrice = (TextView) findViewById(R.id.order_total);*/

        mRecyclerView = (RecyclerView)findViewById(R.id.order_recycler_view_cart);
        rootlayout = (CoordinatorLayout) findViewById(R.id.root);
        totalPrice = (TextView) findViewById(R.id.order_total);
        orderCount = (TextView) findViewById(R.id.order_count);

        Intent intent = getIntent();
        items = (ArrayList<Order>) getIntent().getExtras().getSerializable("Item List");
        totalPrice.setText(intent.getStringExtra("Total"));
        //String number = String.valueOf(items.size());
        orderCount.setText(String.valueOf(items.size()));

        adapter = new CartAdapter(this, items);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
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
            startActivity(new Intent(OrderHistoryList.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(OrderHistoryList.this, Homepage.class));
        } else if(id == R.id.action_search) {
            startActivity(new Intent(OrderHistoryList.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
