package com.qnally.shappapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.qnally.shappapp.Interface.OnItemClickListener;
import com.qnally.shappapp.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class SearchedList extends AppCompatActivity {

    List<String> suggestedSearch = new ArrayList<>();
    MaterialSearchBar mSearchBar;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mManager;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference, allProducts;
    FirebaseRecyclerAdapter<Item, ListViewHolder2> adapter;
    FirebaseRecyclerAdapter<Item, ListViewHolder2> searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_list);

        mDatabase = FirebaseDatabase.getInstance();
        allProducts = mDatabase.getReference("All Products");

        mSearchBar = (MaterialSearchBar) findViewById(R.id.search_searchbar);

        loadList();

        mRecyclerView = (RecyclerView) findViewById(R.id.searchRecycler);
        mManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(50), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mSearchBar.setCardViewElevation(5);
        mSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<>();
                for(String search : suggestedSearch) {
                    if(search.toLowerCase().contains(mSearchBar.getText().toLowerCase())){
                        suggest.add(search);
                    }
                }
                mSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){
                    //mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        loadAllFoods();

    }

    private void loadAllFoods() {
        Query searchbyname = allProducts;

        FirebaseRecyclerOptions<Item> itemOptions = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(searchbyname, Item.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<Item, ListViewHolder2>(itemOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ListViewHolder2 holder, int position, @NonNull Item model) {
                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .into(holder.image);
                holder.title.setText(model.getName());
                holder.price.setText(model.getPrice());

                final Item local = model;
                holder.setItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(SearchedList.this, ItemPage.class);
                        /*intent.putExtra("Product", searchAdapter.getRef(position).getKey());
                        startActivity(intent);*/

                        intent.putExtra("Image", local.getImage());
                        intent.putExtra("Name", local.getName());
                        intent.putExtra("Price", local.getPrice());
                        intent.putExtra("Description", local.getDescription());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //mContext.startActivity(intent);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ListViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ListViewHolder2(view);
            }
        };
        searchAdapter.startListening();
        mRecyclerView.setAdapter(searchAdapter);
    }

    private void startSearch(CharSequence text) {
        Query searchbyname = allProducts.orderByChild("name").equalTo(text.toString());

        FirebaseRecyclerOptions<Item> itemOptions = new FirebaseRecyclerOptions.Builder<Item>()
                .setQuery(searchbyname, Item.class)
                .build();

        searchAdapter = new FirebaseRecyclerAdapter<Item, ListViewHolder2>(itemOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ListViewHolder2 holder, int position, @NonNull Item model) {
                Glide.with(getBaseContext())
                        .load(model.getImage())
                        .into(holder.image);
                holder.title.setText(model.getName());
                holder.price.setText(model.getPrice());

                final Item local = model;

                holder.mCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SearchedList.this, ItemPage.class);

                        intent.putExtra("Image", local.getImage());
                        intent.putExtra("Name", local.getName());
                        intent.putExtra("Price", local.getPrice());
                        intent.putExtra("Description", local.getDescription());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ListViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new ListViewHolder2(view);
            }
        };
        searchAdapter.startListening();
        mRecyclerView.setAdapter(searchAdapter);
    }

    private void loadList() {
        allProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Item item = postSnapshot.getValue(Item.class);
                    suggestedSearch.add(item.getName());
                }
                mSearchBar.setLastSuggestions(suggestedSearch);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        if(adapter != null)
            adapter.stopListening();
        if(searchAdapter != null)
            searchAdapter.stopListening();
        super.onStop();
    }

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

    public class ListViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, price;
        CardView mCardView;

        private OnItemClickListener mItemClickListener;

        public void setItemClickListener(OnItemClickListener itemClickListener) {
            mItemClickListener = itemClickListener;
        }

        public ListViewHolder2(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.item_img);
            title = (TextView) itemView.findViewById(R.id.list_title);
            price = (TextView) itemView.findViewById(R.id.list_price);
            mCardView = (CardView) itemView.findViewById(R.id.card);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onClick(v, getAdapterPosition());
        }
    }
}
