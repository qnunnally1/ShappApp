package com.qnally.shappapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Adapter.CartAdapter;
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Helper.Common;
import com.qnally.shappapp.Helper.DialogHelper;
import com.qnally.shappapp.Helper.RecyclerItemTouchHelper;
import com.qnally.shappapp.Helper.RecyclerItemTouchHelperListener;
import com.qnally.shappapp.Model.Customer;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Model.PlacedOrder;
import com.qnally.shappapp.Remote.ItemRequest;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class cart_list extends AppCompatActivity implements RecyclerItemTouchHelperListener, DialogHelper.DialogHelperListener {

    private RecyclerView mRecyclerView;
    private List<Order> list = new ArrayList<>();
    private List<Item> cart = new ArrayList<>();
    private CartAdapter adapter;
    private CoordinatorLayout rootlayout;

    android.support.v7.widget.Toolbar mToolbar;
    TextView totalPrice;
    Button placeOrderbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Cart");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view_cart);
        rootlayout = (CoordinatorLayout) findViewById(R.id.root);
        totalPrice = (TextView) findViewById(R.id.total);
        placeOrderbtn = (Button) findViewById(R.id.completeOrder);

        ItemTouchHelper.SimpleCallback item
                = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(item).attachToRecyclerView(mRecyclerView);

        placeOrderbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String price = totalPrice.getText().toString();
                if(!price.equals("$0.00")) {
                    if (com.qnally.shappapp.Common.Common.current != null) {
                        Intent topay = new Intent(cart_list.this, RegistrationPayment.class);
                        topay.putExtra("First Name", com.qnally.shappapp.Common.Common.current.getFirst_name());
                        topay.putExtra("Last Name", com.qnally.shappapp.Common.Common.current.getLast_name());
                        topay.putExtra("Email", com.qnally.shappapp.Common.Common.current.getEmail());
                        topay.putExtra("Total", totalPrice.getText().toString());
                        topay.putExtra("Order", (Serializable) list);
                        startActivity(topay);
                    } else {
                        //showAlert();
                        openDialog();
                    }
                } else{
                    Toast.makeText(cart_list.this,"You have no items in your cart",Toast.LENGTH_LONG).show();
                }
            }
        });

        loadItems();
    }

    private void openDialog() {
        DialogHelper dh = new DialogHelper();
        dh.show(getSupportFragmentManager(), "Login Dialog");
    }

    @Override
    public void tryCredentials(final String username, final String password) {
        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("Customer");

        final ProgressDialog mDialog = new ProgressDialog(cart_list.this);
        mDialog.show();

        table_customer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String email2string = username.replace(".com","");

                //check if customer exists in database
                if(dataSnapshot.child(email2string).exists()) {

                    //get customer info
                    mDialog.dismiss();
                    Customer cust = dataSnapshot.child(email2string).getValue(Customer.class);
                    if (cust != null) {
                        if (cust.getPassword().equals(password)) {
                            Intent topayment = new Intent(getApplicationContext(), RegistrationPayment.class);
                            com.qnally.shappapp.Common.Common.current = cust;
                            topayment.putExtra("First Name", com.qnally.shappapp.Common.Common.current.getFirst_name());
                            topayment.putExtra("Last Name", com.qnally.shappapp.Common.Common.current.getLast_name());
                            topayment.putExtra("Email", com.qnally.shappapp.Common.Common.current.getEmail());
                            topayment.putExtra("Total", totalPrice.getText().toString());
                            startActivity(topayment);
                            finish();
                        } else {
                            Toast.makeText(cart_list.this, "Failed Sign In", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else{
                    mDialog.dismiss();
                    Toast.makeText(cart_list.this, "Credentials Not Recognized", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadItems() {
        list = new Database(this).getCarts();
        adapter = new CartAdapter(this, list);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        //calculate total price
        double total = 0;
        for(Order order: list){
            String price = order.getPrice();
            if(price.contains(",")) {
                price = order.getPrice().replace("$", "").replace(",", "");
            } else {
                price = order.getPrice().replace("$", "");
            }

            total+=(Double.parseDouble(price)) * (Integer.parseInt(order.getQuantity()));

            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            totalPrice.setText(fmt.format(total));

        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartAdapter.CartViewHolder) {
            String name = list.get(viewHolder.getAdapterPosition()).getProductName();
            final Order deletedItem = list.get(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deletedItem.getProductName());

            //Update total
            //calculate total price
            double total = 0;
            List<Order> upd_list = new Database(getBaseContext()).getCarts();
            for (Order item : upd_list) {
                String price = item.getPrice().replace("$", "").replace(",", "");

                total += (Double.parseDouble(price)) * (Integer.parseInt(item.getQuantity()));
            }
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            totalPrice.setText(fmt.format(total));

            Snackbar snackbar = Snackbar.make(rootlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                adapter.restoreItem(deletedItem, deleteIndex);
                new Database(getBaseContext()).addToCart(deletedItem);

                //calculate total price
                double total = 0;
                for (Order order : list) {
                    String price = order.getPrice().replace("$", "");

                    total += (Double.parseDouble(price)) * (Integer.parseInt(order.getQuantity()));
                }
                    Locale locale = new Locale("en", "US");
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    totalPrice.setText(fmt.format(total));

                }
            });

            snackbar.setActionTextColor(getResources().getColor(R.color.primary_bg));
            snackbar.show();

        }
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
            startActivity(new Intent(cart_list.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(cart_list.this, Homepage.class));
        } else if(id == R.id.action_search) {
            startActivity(new Intent(cart_list.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
