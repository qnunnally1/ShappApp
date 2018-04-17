package com.qnally.shappapp;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Notifications.CartNotification;

import java.util.ArrayList;
import java.util.List;

public class ItemPage extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference, reference2;

    TextView title, price, details;
    ImageView image;
    Button add;
    Toolbar mToolbar;
    Intent intent;
    ElegantNumberButton quantity;

    String rec_intent, item_img, item_title, item_price, item_details = "";
    List<String> suggestedSearch = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        image = (ImageView)findViewById(R.id.image1);
        title = (TextView)findViewById(R.id.item_title);
        price = (TextView)findViewById(R.id.item_price);
        details = (TextView)findViewById(R.id.item_details);
        add = (Button)findViewById(R.id.add_to_cart);
        quantity = (ElegantNumberButton) findViewById(R.id.quantity);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Products");
        reference2 = database.getReference("All Products");

        intent = getIntent();
        if(intent != null) {
            String image = intent.getExtras().getString("Image");
            Glide.with(this).load(image).into((ImageView) findViewById(R.id.image1));
            title.setText(intent.getExtras().getString("Name"));
            price.setText(intent.getExtras().getString("Price"));
            details.setText(intent.getExtras().getString("Description"));
        }

        item_img = intent.getExtras().getString("Image");
        item_title = intent.getExtras().getString("Name");
        item_price = intent.getExtras().getString("Price");
        item_details = intent.getExtras().getString("Description");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Item item = new Item( item_img, item_title, item_price, item_details, "0");
                new Database(getBaseContext()).addToCart(new Order(
                        intent.getExtras().getString("Name"),
                        quantity.getNumber().toString(),
                        intent.getExtras().getString("Price"),
                        intent.getExtras().getString("Image")
                ));

                Toast.makeText(ItemPage.this, "Added to Cart", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(ItemPage.this, cart_list.class));
            return true;
        } else if (id == R.id.action_home) {
            startActivity(new Intent(ItemPage.this, Homepage.class));
        } else if (id == R.id.action_search) {
            startActivity(new Intent(ItemPage.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
