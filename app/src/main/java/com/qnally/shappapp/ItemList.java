package com.qnally.shappapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Adapter.ListRecyclerAdapter;
import com.qnally.shappapp.Adapter.MostPopularAdapter;
//import com.qnally.shappapp.Interface.ItemClickListener;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Interface.OnItemClickListener;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.Notifications.CartCount;
import com.qnally.shappapp.Notifications.CartNotification;
import com.qnally.shappapp.ViewHolder.ItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity{

    List<Item> mItems = mItems = new ArrayList<>();

    RecyclerView list;
    RecyclerView.Adapter mAdapter;
    //ListRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Toolbar mToolbar;
    //Button add;

    FirebaseDatabase database;
    DatabaseReference itemList;

    String categoryId="";

    //public static int notificationCountCart = 0;


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.homepage_2);
        new FetchCountTask().execute();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        initCollapsingToolbar(categoryId);

        //Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference(categoryId);

        //add = (Button) findViewById(R.id.list_add2c);
        list = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        list.setLayoutManager(mLayoutManager);
        list.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(50), true));
        list.setItemAnimator(new DefaultItemAnimator());

        itemList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot post : dataSnapshot.getChildren()){
                    Item one = post.getValue(Item.class);
                    mItems.add(one);
                }

                mAdapter = new ListRecyclerAdapter(getApplicationContext(), mItems);
                list.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = mItems.get
                //Item item = new Item( item_img, item_title, item_price, item_details, "0");
                Common.currentItems.add(item);
            }
        });*/

        switch(categoryId){
            case "Electronics":
                try {
                    Glide.with(this).load(R.drawable.elec_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Video Games":
                try {
                    Glide.with(this).load(R.drawable.vg_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Clothing":
                try {
                    Glide.with(this).load(R.drawable.cloth_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Household":
                try {
                    Glide.with(this).load(R.drawable.household_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Entertainment":
                try {
                    Glide.with(this).load(R.drawable.ent_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "Books":
                try {
                    Glide.with(this).load(R.drawable.books_notitle).into((ImageView) findViewById(R.id.backdrop));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void initCollapsingToolbar(final String categoryId) {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        TextView text = (TextView) findViewById(R.id.Categorytxt);
        text.setText(categoryId);
        text.setTextColor(Color.BLACK);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(categoryId);
                    collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
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

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Get the notifications MenuItem and
        // its LayerDrawable (layer-list)
        MenuItem item = menu.findItem(R.id.action_cart);
        CartNotification.setAddToCart(ItemList.this, item, notificationCountCart);
        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(ItemList.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(ItemList.this, Homepage.class));
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    Updates the count of notifications in the ActionBar.
     */
    private void updateNotificationsBadge(int count) {
        //notificationCountCart = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    /*
    Sample AsyncTask to fetch the notifications count
    */
    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            return 5;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }
}
