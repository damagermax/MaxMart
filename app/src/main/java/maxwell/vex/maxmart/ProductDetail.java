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

public class ProductDetail extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView detailImage;
    private TextView detailName, detailPrice, detailDesc;
    private String gamesID, fashionID;
    private String games, fashion;
    private TextView call, addToCart;
    private String currentUser;
    private String productName, productPrice, productImage, productDesc;
    private DatabaseReference addToCartRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        Intent intent = getIntent();
        // Getting games intent
        gamesID = intent.getStringExtra("gamesID");
        games = intent.getStringExtra("category");

        // Getting fashion intent
        fashionID = intent.getStringExtra("fashionID");
        fashion = intent.getStringExtra("category");

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductToCart();
            }
        });


        /// Checking if data passed is not null
        if ((games != null) && (games.equals("Video Games"))) {
            getGames();

        } else {

            if ((fashion != null) && (fashion.equals("Fashion"))) {
                getFashion();

            }

        }

        gettingCurrentUser();

    }
    /// getting current user with phone number
    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getPhoneNumber();

        }
    }
    /// adding products to cart
    private void addProductToCart() {

        addToCartRef = FirebaseDatabase.getInstance().getReference();

        if (fashionID != null) {
            addFashionProductsToCart();

        } else if (gamesID!=null){
            addGamesProductsToCart();

        }


    }
    /// adding games product to cart
    private void addGamesProductsToCart() {
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("name", productName);
        cartMap.put("price", productPrice);
        cartMap.put("image", productImage);
        cartMap.put("productID", gamesID);

        addToCartRef.child("Cart list").child("User View").child(currentUser).child("Products")
                .child(gamesID).updateChildren(cartMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToCartRef.child("Cart list").child("Admin View").child(currentUser).child("Products")
                                .child(gamesID).updateChildren(cartMap);
                        Toast.makeText(getApplicationContext(),productName+" added to cart",Toast.LENGTH_SHORT).show();

                    }
                });
    }
    /// adding fashion product to cart
    private void addFashionProductsToCart() {
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("name", productName);
        cartMap.put("price", productPrice);
        cartMap.put("image", productImage);
        cartMap.put("productID", fashionID);

        addToCartRef.child("Cart list").child("User View").child(currentUser).child("Products")
                .child(fashionID).updateChildren(cartMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToCartRef.child("Cart list").child("Admin View").child(currentUser).child("Products")
                                .child(fashionID).updateChildren(cartMap);
                        Toast.makeText(getApplicationContext(),productName+" added to cart",Toast.LENGTH_SHORT).show();

                    }
                });
    }

    /// getting fashion products to display
    private void getFashion() {
        DatabaseReference fashionRef = FirebaseDatabase.getInstance().getReference().child("Categories").child(fashion).child(fashionID);
        fashionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productName = snapshot.child("name").getValue(String.class);
                    productPrice =  snapshot.child("price").getValue(String.class);
                    productImage = snapshot.child("image").getValue(String.class);
                    productDesc = snapshot.child("description").getValue(String.class);


                    detailDesc.setText(productDesc);
                    detailName.setText(productName);
                    detailPrice.setText("GH₵ " + productPrice);
                    Picasso.with(getApplicationContext()).load(productImage).fit().centerInside()
                            .into(detailImage);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /// getting games products to display
    private void getGames() {
        DatabaseReference gamesRef = FirebaseDatabase.getInstance().getReference().child("Categories").child(games).child(gamesID);
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    productName = snapshot.child("name").getValue(String.class);
                    productPrice =  snapshot.child("price").getValue(String.class);
                    productImage = snapshot.child("image").getValue(String.class);
                    productDesc = snapshot.child("description").getValue(String.class);


                    detailDesc.setText(productDesc);
                    detailName.setText(productName);
                    detailPrice.setText( "GH₵ " +productPrice);
                    Picasso.with(getApplicationContext()).load(productImage).fit().centerInside()
                            .into(detailImage);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}