package com.qnally.shappapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class AllCategories extends AppCompatActivity {

    ImageButton elec, cloth, vg, books, house, ent;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        elec = (ImageButton) findViewById(R.id.elec_cat);
        cloth = (ImageButton) findViewById(R.id.cloth_cat);
        vg = (ImageButton) findViewById(R.id.vg_cat);
        books = (ImageButton) findViewById(R.id.books_cat);
        house = (ImageButton) findViewById(R.id.hous_cat);
        ent = (ImageButton) findViewById(R.id.ent_cat);

        //electronics
        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent elecList = new Intent(AllCategories.this, ItemList.class);
                elecList.putExtra("CategoryId", "Electronics");
                startActivity(elecList);
            }
        });

        //clothing
        cloth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent clothList = new Intent(AllCategories.this, ItemList.class);
                clothList.putExtra("CategoryId", "Clothing");
                startActivity(clothList);
            }
        });

        //books
        books.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bksList = new Intent(AllCategories.this, ItemList.class);
                bksList.putExtra("CategoryId", "Books");
                startActivity(bksList);
            }
        });

        //video games
        vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vgList = new Intent(AllCategories.this, ItemList.class);
                vgList.putExtra("CategoryId", "Video Games");
                startActivity(vgList);
            }
        });

        //video games
        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vgList = new Intent(AllCategories.this, ItemList.class);
                vgList.putExtra("CategoryId", "Household");
                startActivity(vgList);
            }
        });

        //video games
        ent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vgList = new Intent(AllCategories.this, ItemList.class);
                vgList.putExtra("CategoryId", "Entertainment");
                startActivity(vgList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(AllCategories.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(AllCategories.this, Homepage.class));
        } else if(id == R.id.action_search) {
            startActivity(new Intent(AllCategories.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        /*// Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.ic_badge);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        CartCount.setBadgeCount(this, icon, notificationCountCart);

        this.menu = menu;*/

        return true;
    }
}
