package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backBtn;
    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDesc;
    private String categoryID, productID;
    private TextView call, addToCart;
    private String currentUser;
    private String productName, productPrice, productImage, productDesc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);

        backBtn = findViewById(R.id.backBtn_pd);
        detailName = findViewById(R.id.detail_name);
        detailPrice = findViewById(R.id.detail_price);
        detailDesc = findViewById(R.id.detail_desc);
        detailImage = findViewById(R.id.detail_imageTv);
        addToCart = findViewById(R.id.add_to_cart);
        call = findViewById(R.id.call);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Getting  intent
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");
        categoryID = intent.getStringExtra("categoryID");

        /// add to cart click
        addToCart.setOnClickListener(this);

        /// Checking if data passed is not null before getting data from database
        if ((categoryID != null) && (productID != null)) {

            /// get product details
            getProductDetails();

        }

        /// get current user
        gettingCurrentUser();

    }

    /// getting current user with phone number
    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getPhoneNumber();

        }
    }
    //////////////////////////////////////////

    /// getting  product details to display
    private void getProductDetails() {
        DatabaseReference fashionRef = FirebaseDatabase.getInstance().getReference().child("Categories").child(categoryID).child(productID);
        fashionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                /// check if database is not empty
                if (snapshot.exists()) {
                    /// if database is not empty get data

                    productName = snapshot.child("name").getValue(String.class);
                    productPrice = snapshot.child("price").getValue(String.class);
                    productImage = snapshot.child("image").getValue(String.class);
                    productDesc = snapshot.child("description").getValue(String.class);

                    ///set data to views
                    detailDesc.setText(productDesc);
                    detailName.setText(productName);
                    detailPrice.setText("GH₵ " + productPrice);
                    Picasso.with(getApplicationContext()).load(productImage).fit().centerInside()
                            .into(detailImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    ///////////////////////////////////////

    /// adding product to cart
    private void addProductsToCart() {


        /// check if productId is not empty
        if (productID != null) {
            /// if productID is no empty

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("name", productName);
            cartMap.put("price", productPrice);
            cartMap.put("image", productImage);
            cartMap.put("quantity", 1);
            cartMap.put("productID", productID);

            /// initialize firebaseDatabase
            DatabaseReference addToCartRef=FirebaseDatabase.getInstance().getReference();

            addToCartRef.child("Cart list").child(currentUser).child("Products")
                    .child(productID).updateChildren(cartMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toasty.success(getApplicationContext(), productName + " added to cart", Toast.LENGTH_SHORT).show();

                        }
                    });
        }
    }
    ///////////////////////////////////////



    @Override
    public void onClick(View v) {

        /// get ids and compare
        if (v.getId() == R.id.add_to_cart) {
            /// add product to cart
            addProductsToCart();
        }
    }


}