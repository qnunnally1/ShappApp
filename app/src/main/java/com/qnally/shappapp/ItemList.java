package com.qnally.shappapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.qnally.shappapp.Interface.ItemClickListener;
import com.qnally.shappapp.Model.Item;
import com.qnally.shappapp.Model.MostPopularItems;
import com.qnally.shappapp.ViewHolder.ItemViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemList extends AppCompatActivity{

    List<Item> mItems = mItems = new ArrayList<>();

    RecyclerView list;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Toolbar mToolbar;

    FirebaseDatabase database;
    DatabaseReference itemList;

    String categoryId="";


    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.homepage_2);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");

        initCollapsingToolbar(categoryId);

        //Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference(categoryId);

        list = (RecyclerView) findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        list.setLayoutManager(mLayoutManager);
        list.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(50), true));
        list.setItemAnimator(new DefaultItemAnimator());

        //list = (RecyclerView) findViewById(R.id.recycler_view);
        //layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //list.setLayoutManager(new GridLayoutManager(this, 2));


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
