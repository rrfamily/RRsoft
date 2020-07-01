package com.example.rrgroup;


import com.example.rrgroup.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Interface.ItemClickListener;
import MenuViewHolder.ProductViewHolder;
import Model.Products;

public class ProductList extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryID="";
    FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter;

    //search Function
    FirebaseRecyclerAdapter<Products, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);


        //Firebase

        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //get intent here
        if(getIntent() != null)
            categoryID = getIntent().getStringExtra("CategoryID");
        if(!categoryID.isEmpty() && categoryID != null)
        {

            loadListProduct(categoryID);

        }


        //search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Search Product");
        loadSuggest(); //to suggest from

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //when user type their text,we will change suggest list

                List<String> suggest = new ArrayList<>();
                    for(String search:suggestList){
                        if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()));
                        suggest.add(search);
                    }
                    materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when search bar is closed Restore original suggest adapter
                if(!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //when search finish show result
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });




    }

    private void startSearch(CharSequence text) {

        Query searchByName = productList.orderByChild("Name").equalTo(text.toString()); //Compare Name

        FirebaseRecyclerOptions<Products> productOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(searchByName, Products.class)
                .build();


        searchAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(productOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

                holder.product_name.setText(model.getName());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(holder.product_image);

                final Products local = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent ProductDetailIntent = new Intent(ProductList.this, ProductDetail.class);
                        ProductDetailIntent.putExtra("productId", searchAdapter.getRef(position).getKey()); // Send Food Id to new Activity
                        startActivity(ProductDetailIntent);

                    }
                });


            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);

                return new ProductViewHolder(itemView);
            }
        };
        searchAdapter.startListening();
        recyclerView.setAdapter(searchAdapter); //set adapter for recycler view is search result

    }

    private void loadSuggest() {

        productList.orderByChild("menuID").equalTo(categoryID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Products item = postSnapshot.getValue(Products.class);
                    suggestList.add(item.getName()); // suggest list
                }

                materialSearchBar.setLastSuggestions(suggestList);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadListProduct(String categoryID) {

        Query searchByName = productList.orderByChild("menuID").equalTo(categoryID);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(searchByName,Products.class).build();


        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);

                return new ProductViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull final ProductViewHolder viewHolder, @SuppressLint("RecyclerView") final int position, @NonNull final Products model) {
                viewHolder.product_name.setText(model.getName());
                // viewHolder.food_price.setText(String.format("$ %s", model.getPrice()));
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.product_image);

                final Products local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {


                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        //Start new activity
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productID", adapter.getRef(position).getKey());
                        startActivity(productDetail);


                    }
                });


            }

        };
        //set adapter
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
       // Log.d("TAG",""+adapter.getItemCount());
    }
}
