package com.qnally.shappapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Adapter.MostPopularAdapter;
import com.qnally.shappapp.Adapter.SuggestedAdapter;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.Model.SuggestedItems;
import com.qnally.shappapp.Notifications.CartNotification;

import java.util.ArrayList;
import java.util.List;


public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView fullName;
    private List<MostPopularItems> mpItems = new ArrayList<>();
    private List<SuggestedItems> sItems = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference most_pop, suggested, category;

    RecyclerView firstRecyclerView, secondRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layman1, layman2;

    ImageButton elec, clothing, books, videogames;
    Button add2c, seemore;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        most_pop = database.getReference("MostPopularItems");
        suggested = database.getReference("SuggestedItems");
        category = database.getReference("Category");

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set-up the NavigationView and its listener
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set Name for user
        if(Common.current != null) {
            View headerView = navigationView.getHeaderView(0);
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.create_acct_btn).setVisible(false);
            String userFullName = "Hi " + Common.current.getFirst_name() + "!";
            fullName = (TextView) headerView.findViewById(R.id.fullName);
            fullName.setText(userFullName);
        }else{
            View headerView = navigationView.getHeaderView(0);
            navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_acctinfo).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_categories).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_help).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);
            String userFullName = "Hi Shapp Shopper!";
            fullName = (TextView) headerView.findViewById(R.id.fullName);
            fullName.setText(userFullName);
        }

        //set up category buttons
        elec = (ImageButton) findViewById(R.id.elecbtn);
        clothing = (ImageButton) findViewById(R.id.clothbtn);
        books = (ImageButton) findViewById(R.id.bookbtn);
        videogames = (ImageButton) findViewById(R.id.vgbtn);
        add2c = (Button) findViewById(R.id.button);
        seemore = (Button) findViewById(R.id.seemore);

        //electronics
        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent elecList = new Intent(Homepage.this, ItemList.class);
                elecList.putExtra("CategoryId", "Electronics");
                startActivity(elecList);
            }
        });

        //clothing
        clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clothList = new Intent(Homepage.this, ItemList.class);
                clothList.putExtra("CategoryId", "Clothing");
                startActivity(clothList);
            }
        });

        //books
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bksList = new Intent(Homepage.this, ItemList.class);
                bksList.putExtra("CategoryId", "Books");
                startActivity(bksList);
            }
        });

        //video games
        videogames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vgList = new Intent(Homepage.this, ItemList.class);
                vgList.putExtra("CategoryId", "Video Games");
                startActivity(vgList);
            }
        });

        //see more categories
        seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vgList = new Intent(Homepage.this, AllCategories.class);
                startActivity(vgList);
            }
        });

        add2c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this, cart_list.class));
            }
        });


        //set up first horizontal row
        firstRecyclerView = (RecyclerView) findViewById(R.id.firstscrollsnap);
        layman1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(layman1);

        most_pop.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot post : dataSnapshot.getChildren()){
                    MostPopularItems one = post.getValue(MostPopularItems.class);
                    mpItems.add(one);
                }
                mAdapter = new MostPopularAdapter(getApplicationContext(), mpItems);

                firstRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //set up second horizontal row
        secondRecyclerView = (RecyclerView) findViewById(R.id.secscrollsnap);
        layman2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        secondRecyclerView.setLayoutManager(layman2);
        suggested.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot post: dataSnapshot.getChildren()){
                    SuggestedItems one = post.getValue(SuggestedItems.class);
                    sItems.add(one);
                }
                mAdapter = new SuggestedAdapter(getApplicationContext(), sItems);
                secondRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Intent goToHomepage= new Intent("com.qnally.shappapp.Homepage");
            startActivity(goToHomepage);

        } else if (id == R.id.nav_acctinfo) {

        } else if (id == R.id.nav_categories) {
            //Intent goToCategories= new Intent("com.qnally.shappapp.ItemList");
            //startActivity(goToCategories);

        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_logout) {
            Common.current=null;
            Intent goToHomepage= new Intent("com.qnally.shappapp.Homepage");
            startActivity(goToHomepage);
            Toast.makeText(Homepage.this,"You have been logged out",Toast.LENGTH_LONG).show();

        } else if (id == R.id.nav_login) {
            //Intent goToLoginPage= new Intent();
            startActivity(new Intent(Homepage.this, Login.class));

        } else if (id == R.id.create_acct_btn) {
            Intent goToRegistrationPage= new Intent("com.qnally.shappapp.RegistrationPersonalInfo");
            startActivity(goToRegistrationPage);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.button);
        CartNotification.setAddToCart(Homepage.this, item,notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }*/

    /*
    Navigation drawer will be closed on pressing back button while it
    is open, else we delegate to its super class.
    */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
