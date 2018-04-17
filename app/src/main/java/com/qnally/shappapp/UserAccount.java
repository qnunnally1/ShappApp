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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Adapter.MostPopularAdapter;
import com.qnally.shappapp.Adapter.OrderViewAdapter;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.Model.PlacedOrder;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserAccount extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView usr_name, settings_title, settings_details, orders_title, noorder_details;
    ImageView user_image;
    Toolbar mToolbar;
    Button changeinfo;

    private OrderViewAdapter adapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager lm;

    List<PlacedOrder> po = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        mToolbar = (Toolbar) findViewById(R.id.user_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_image = (ImageView)findViewById(R.id.user_photo);
        usr_name = (TextView)findViewById(R.id.user_name);
        settings_title = (TextView)findViewById(R.id.acct_settings_text);
        settings_details = (TextView) findViewById(R.id.user_details);
        noorder_details = (TextView) findViewById(R.id.noorder_text);
        orders_title = (TextView)findViewById(R.id.acct_orders_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.order_scroll);
        changeinfo = (Button)findViewById(R.id.change_info_btn);

        database = FirebaseDatabase.getInstance();

        reference = database.getReference("Orders");

        String name = Common.current.getFirst_name() + " " + Common.current.getLast_name();
        usr_name.setText(name);
        String email = Common.current.getEmail();

        if ((Common.current.getShipping_Address() != null) && (Common.current.getBilling_Address() != null && Common.current.getPayment() != null)) {
            String saddress = Common.current.getShipping_Address().getFullAddress();
            String baddress = Common.current.getBilling_Address().getFullAddress();
            String paymentnum = Common.current.getPayment().getCreditNumber();
            StringBuilder creditnum = new StringBuilder();

            if (Common.current.getPayment().getCardType().equals("AMEX")) {
                for (int i = 0; i < paymentnum.length(); i++) {
                    if (i == 4 || i == 11) {
                        creditnum.append(" ");
                        continue;
                    }

                    if (i >= 12) {
                        creditnum.append(paymentnum.charAt(i));
                        continue;
                    }

                    creditnum.append("*");
                }
            } else {
                for (int i = 0; i < paymentnum.length(); i++) {
                    if (i == 4 || i == 9 || i == 14) {
                        creditnum.append(" ");
                        continue;
                    }

                    if (i >= 15) {
                        creditnum.append(paymentnum.charAt(i));
                        continue;
                    }

                    creditnum.append("*");
                }
            }
            settings_details.setText("Name: " + name + "\nEmail: " + email + "\nShipping Address: " + saddress
                    + "\nBilling Address: " + baddress + "\nPayment: " + creditnum);
        } else {
            settings_details.setText("Name: " + name + "\nEmail: " + email + "\nShipping Address: N/A" + "\nBilling Address: N/A" + "\nPayment: N/A");
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.order_scroll);
        lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(lm);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post : dataSnapshot.getChildren()){
                    PlacedOrder one = post.getValue(PlacedOrder.class);

                    long yourmilliseconds = one.getOrder_id();
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    Date resultdate = new Date(yourmilliseconds);

                    DateTime timePlaced = new DateTime(resultdate);
                    DateTime now = DateTime.now();
                    Minutes min = Minutes.minutesBetween(now, timePlaced);
                    int minToInt = min.getMinutes();
                    Math.abs(minToInt);

                    int days = one.getDaysToShip();

                    int minInDay = 0;

                    switch(days){
                        case 3 : minInDay = 4320;
                            break;
                        case 4 : minInDay = 5760;
                            break;
                        case 5 : minInDay = 7200;
                    }

                    if(minToInt >= minInDay){
                        post.getRef().child("status").setValue("3");
                    } else if(minToInt >= 1440){
                        post.getRef().child("status").setValue("2");
                    } else if (minToInt >= 1){
                        post.getRef().child("status").setValue("1");
                    } else {
                        post.getRef().child("status").setValue("0");
                    }

                    if(one.getEmail().equals(Common.current.getEmail().concat(".com"))){
                        po.add(one);
                    }

                }
                adapter = new OrderViewAdapter(getApplicationContext(), po);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserAccount.this, InfoModify.class));
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
            startActivity(new Intent(UserAccount.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(UserAccount.this, Homepage.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
