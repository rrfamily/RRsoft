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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

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


    }

    private void loadListProduct(String categoryID) {

        Query searchByName = productList.orderByChild("MenuID").equalTo(categoryID);

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(searchByName,Products.class).build();


        adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {


            public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item, parent, false);

                return new ProductViewHolder(itemView);
            }

            ///////////////
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
                       /* Intent foodDetailIntent = new Intent(ProductList.this, Products.class);
                        foodDetailIntent.putExtra("MenuID", adapter.getRef(position).getKey());
                        startActivity(foodDetailIntent);*/
                       Toast.makeText(ProductList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                    }
                });


            }


            /////////////////

          /*  protected void populateViewHolder(ProductViewHolder viewHolder,Products model, int position){

            viewHolder.product_name.setText(model.getName());
            Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.product_image);

            final Products local = model;
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                   Toast.makeText(ProductList.this,""+local.getName(),Toast.LENGTH_SHORT).show();

                }
            });

            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {

               /* ProductViewHolder.product_Name.setText(model.getName());
                Picasso.with().load(model.getImage()).into(ProductViewHolder.product_image);

                ProductViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        // Start activity of food details
                        Intent foodDetails = new Intent(ProductList.this, FoodDetails.class);
                        foodDetails.putExtra("FoodId", adapter.getRef(position).getKey()); //send FoodId to new Activity
                        startActivity(foodDetails);
                    }
                });*/

        };

          /*  @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
                return new ProductViewHolder(view);
            }*/

        //};

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //set adapter
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
       // Log.d("TAG",""+adapter.getItemCount());
    }
}
