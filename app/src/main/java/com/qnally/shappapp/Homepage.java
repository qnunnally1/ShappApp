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
import android.support.v7.widget.SearchView;
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
import com.mancj.materialsearchbar.MaterialSearchBar;
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

    Toolbar mToolbar;

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
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
            Intent goToHomepage = new Intent(Homepage.this, Homepage.class);
            startActivity(goToHomepage);

        } else if (id == R.id.nav_acctinfo) {
            Intent goToAcctSettings = new Intent(Homepage.this, UserAccount.class);
            startActivity(goToAcctSettings);
        } else if (id == R.id.nav_categories) {
            Intent goToCategories = new Intent(Homepage.this, AllCategories.class);
            startActivity(goToCategories);

        } else if (id == R.id.nav_help) {
            Intent goToHelpPage = new Intent(Homepage.this, HelpPage.class);
            startActivity(goToHelpPage);

        } else if (id == R.id.nav_logout) {
            Common.current=null;
            Intent logout = new Intent(Homepage.this, Homepage.class);
            logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logout);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(Homepage.this, cart_list.class));
            return true;
        } else if (id == R.id.action_search) {
            startActivity(new Intent(Homepage.this, SearchedList.class));
        } else if (id == R.id.action_usr_acct) {
            if(Common.current != null) {
                startActivity(new Intent(Homepage.this, UserAccount.class));
            }
            else{
                Toast.makeText(Homepage.this,"You must be logged in to view account settings.",Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

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
