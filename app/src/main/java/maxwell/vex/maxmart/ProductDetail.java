package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ProductDetail extends AppCompatActivity implements View.OnClickListener {

    private ImageButton backBtn;
    private ImageView detailImage,likeBtn;
    private TextView detailName, detailPrice, detailDesc;
    private String categoryID, productID;
    private Button addToCart;
    private String currentUser;
    private String productName, productPrice, productImage, productDesc;

    private List<SimilarProducts>similarProductsList;
    private  SimilarProductAdapter similarProductAdapter;
    private RecyclerView similarRecyclerView;


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
        likeBtn=findViewById(R.id.like_product);
        similarRecyclerView=findViewById(R.id.similarRV);




        /// add to cart click
        addToCart.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        /// get product details
        getProductDetails();

        /// get current user
        gettingCurrentUser();

        /// display similar products
        getSimilarProducts();

    }

    /// getting similar products from database
    private void getSimilarProducts() {

        /// getting intent from adapter class
        String category=getIntent().getStringExtra("category");

        /// checking if category is no null
        if (category!=null){

            similarRecyclerView.setHasFixedSize(true);
            similarRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
            similarProductsList= new ArrayList<>();
            similarProductAdapter=new SimilarProductAdapter(this,similarProductsList);
            similarRecyclerView.setAdapter(similarProductAdapter);

            Query similarQuery =FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").equalTo(category);
            similarQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    similarProductsList.clear();
                    if (snapshot.exists()){
                        for (DataSnapshot similar:snapshot.getChildren()){

                            SimilarProducts similarProducts=similar.getValue(SimilarProducts.class);
                            similarProductsList.add(similarProducts);

                        }

                    }
                    similarProductAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }
    //////////////////////////////////////////

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


        // Getting  intent
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");


        /// Checking if data passed is not null before getting data from database
        if (productID != null) {

            DatabaseReference details = FirebaseDatabase.getInstance().getReference().child("Products").child(productID);
            details.addValueEventListener(new ValueEventListener() {
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


    }
    ///////////////////////////////////////

    /// adding product to cart
    private void addProductsToCart() {


        /// check if values are  not empty
        if ((productID != null) && (productName!=null)&&(productPrice!=null)&&(productImage!=null)) {
            /// if values are  not empty

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("name", productName);
            cartMap.put("price", productPrice);
            cartMap.put("image", productImage);
            cartMap.put("quantity", 1);
            cartMap.put("productID", productID);

            /// initialize firebaseDatabase
            DatabaseReference addToCartRef=FirebaseDatabase.getInstance().getReference();
            /// saving items to database (cart list)
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

            addProductsToCart();  /// add product to cart

        }else  if (v.getId()==R.id.backBtn_pd){

            onBackPressed();  ///  move to previous screen
        }
    }


}