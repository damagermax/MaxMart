package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.adapters.UserCartAdapter;
import maxwell.vex.maxmart.modules.UserCartProduct;

public class UserCart extends AppCompatActivity implements RecyclerViewItemClick {

    private List<UserCartProduct> cartList;
    private UserCartAdapter cartAdapter;
    private RecyclerView cartRecycler;
    private DatabaseReference cartRef;

    private String currentUser;
    private ImageButton backBtn;
    private Button checkoutBtn;


    private TextView totalPriceIV;
    private int totalPrice = 0;
    private int priceOfOneProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_cart);

        /// Hooking views
        cartRecycler = findViewById(R.id.cart_recycler);
        backBtn = findViewById(R.id.backBtn_cart);
        checkoutBtn = findViewById(R.id.cart_checkOutBtn);
        totalPriceIV = findViewById(R.id.cart_totalPrice);


        /// onBackPressed
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// getting current user's phone number function call
        gettingCurrentUser();

        /// displaying cart item function call
        displayingCartItems();

    }

    /// getting current user's phone number function
    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getPhoneNumber();

        }
    }

    /// displaying cart item function
    private void displayingCartItems() {

        /// initializing array
        cartList = new ArrayList<>();

        /// setting up recyclerView
        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        /// initializing adapter
        cartAdapter = new UserCartAdapter(cartList, getApplication(), this);

        /// setting adapter to recyclerView
        cartRecycler.setAdapter(cartAdapter);


        /// initializing firebase
        cartRef = FirebaseDatabase.getInstance().getReference();
        cartRef.child("Cart list").child("User View").child(currentUser).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();

                /// looping through the database
                for (DataSnapshot cartDb : snapshot.getChildren()) {

                    /// getting the database value to userCartProduct model
                    UserCartProduct userCartProduct = cartDb.getValue(UserCartProduct.class);
                    if (userCartProduct != null) {

                        /// getting product price to calculate the total price
                        priceOfOneProduct = (Integer.parseInt(userCartProduct.getPrice()));

                        /// summing up the price of products in cart list
                        totalPrice = totalPrice + priceOfOneProduct;
                        String price = String.valueOf(totalPrice);
                        totalPriceIV.setText(price);
                    }
                    cartList.add(userCartProduct);
                }
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    /// delete interface
    @Override
    public void deleteCartItem(int position) {

        /// resetting total price  when an item is removed from cart
        totalPrice = 0;

        /// getting the position of the item removed
        final UserCartProduct userCartProduct = cartList.get(position);
        /// getting the ID of the item clicked
        final String productID = userCartProduct.getProductID();

        /// deleting item from cart
        final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference();
        cartRef.child("Cart list").child("User View").child(currentUser).child("Products").
                child(productID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                cartRef.child("Cart list").child("Admin View").child(currentUser).child("Products").child(productID).removeValue();

            }
        });


    }


}