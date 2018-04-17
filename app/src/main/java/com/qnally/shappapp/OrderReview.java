package com.qnally.shappapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.qnally.shappapp.Adapter.CartAdapter;
import com.qnally.shappapp.Model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderReview extends AppCompatActivity {

    Toolbar mToolbar;
    TextView order_id, order_date, total, count, arrival;
    Intent intent;

    private List<Order> items = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        mToolbar = (Toolbar)findViewById(R.id.review_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Order Receipt");

        order_id = (TextView)findViewById(R.id.order_rev_id);
        order_date = (TextView)findViewById(R.id.order_rev_date);
        total = (TextView)findViewById(R.id.order_rev_total);
        count = (TextView)findViewById(R.id.order_rev_count);
        arrival = (TextView)findViewById(R.id.arrival);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_order_rev_list);

        if(getIntent() != null){
            intent = getIntent();
        }

        items = (ArrayList<Order>) getIntent().getExtras().getSerializable("Order");

        order_id.setText(intent.getExtras().getString("Id"));
        order_date.setText(intent.getStringExtra("Date"));
        total.setText(intent.getStringExtra("Total"));
        count.setText(String.valueOf(items.size()));
        arrival.setText(intent.getExtras().getString("Arrival"));

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
            startActivity(new Intent(OrderReview.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(OrderReview.this, Homepage.class));
        } else if(id == R.id.action_search) {
            startActivity(new Intent(OrderReview.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
