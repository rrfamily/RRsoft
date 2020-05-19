package com.example.rrgroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import Database.Database;
import Model.Order;
import Model.Products;

public class ProductDetail extends AppCompatActivity {

    TextView product_name, product_price, product_description;
    ImageView product_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;


    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;

    Products currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);


        //Firebase
        database = FirebaseDatabase.getInstance();
        products = database.getReference("Products");

        //Init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currentProduct.getName(),
                        numberButton.getNumber(),
                        currentProduct.getPrice(),
                        currentProduct.getDiscount()



                ));

                Toast.makeText(ProductDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();

            }
        });

        product_description = (TextView)findViewById(R.id.product_description);
        product_name = (TextView)findViewById(R.id.product_name);
        product_price = (TextView)findViewById(R.id.product_price);
        product_image = (ImageView)findViewById(R.id.img_product);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);


        //Get Product ID from intent
        if(getIntent() != null)
            productId = getIntent().getStringExtra("ProductID");
        if(!productId.isEmpty()){

            getDetailProduct(productId);
        }

    }

    private void getDetailProduct(String productId) {

        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentProduct = dataSnapshot.getValue(Products.class);

                //Set Image
                Picasso.with(getBaseContext()).load(currentProduct.getImage()).into(product_image);

                collapsingToolbarLayout.setTitle(currentProduct.getName());

                product_price.setText(currentProduct.getPrice());

                product_name.setText(currentProduct.getName());

                product_description.setText(currentProduct.getDescription());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {



            }
        });

    }
}
