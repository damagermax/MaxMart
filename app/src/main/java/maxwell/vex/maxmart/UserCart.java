package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import maxwell.vex.maxmart.Interface.UserCartAction;
import maxwell.vex.maxmart.adapters.UserCartAdapter;
import maxwell.vex.maxmart.models.UserCartProduct;

public class UserCart extends AppCompatActivity implements UserCartAction {

    private List<UserCartProduct> cartList;
    private UserCartAdapter cartAdapter;
    private RecyclerView cartRecycler;
    private DatabaseReference cartRef;

    private String currentUser;
    private ImageButton backBtn;
    private Button checkoutBtn;


    private TextView totalPriceIV, cartBadgeTv;
    private int totalPrice = 0;
    private int productQuantity = 0;
    private int priceOfOneProduct;
    private int cartQuantity = 0;

    private AlertDialog removeDialog;

    private RelativeLayout cartIsEmptyMsg;
    private LinearLayout totalAndCheckoutContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_cart);

        /// Hooking views
        cartRecycler = findViewById(R.id.cart_recycler);
        backBtn = findViewById(R.id.backBtn_cart);
        checkoutBtn = findViewById(R.id.cart_checkOutBtn);
        totalPriceIV = findViewById(R.id.cart_totalPrice);
        cartIsEmptyMsg = findViewById(R.id.empty_cart);
        totalAndCheckoutContainer = findViewById(R.id.total_price_container);
        cartBadgeTv = findViewById(R.id.cart_badgeTV);
        /////////////////////////////////////////////////

        /// make cartBadge invisible => will make it visible if cart iss not empty
        cartBadgeTv.setVisibility(View.GONE);


        // make total and checkOut button invisible
        totalAndCheckoutContainer.setVisibility(View.GONE);


        /// onBackPressed
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// move to shipment details screen
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), CheckOut.class);
                intent.putExtra("total_price", String.valueOf(totalPrice));
                intent.putExtra("quantity", String.valueOf(cartQuantity));
                startActivity(intent);


            }
        });

        /// getting current user's phone number function call
        /// will only show if cart is not empty
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
    /////////////////////////////////////////////////

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
        cartRef.child("Cart list").child(currentUser).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartList.clear(); /// clear list before add
                cartQuantity = 0; /// reset cartQuantity before sum up

                /// check if cart if empty
                if (snapshot.exists()) {
                    /// if cart is not empty
                    /// looping through the database
                    for (DataSnapshot cartDb : snapshot.getChildren()) {

                        /// getting the database value to userCartProduct model
                        UserCartProduct userCartProduct = cartDb.getValue(UserCartProduct.class);

                        /// getting cart total quantity
                        cartQuantity = cartQuantity + userCartProduct.getQuantity();
                        cartBadgeTv.setText(String.valueOf(cartQuantity));
                        cartBadgeTv.setVisibility(View.VISIBLE); /// make cart badge visible

                        /// getting product price to calculate the total price
                        priceOfOneProduct = (Integer.parseInt(userCartProduct.getPrice())) * (userCartProduct.getQuantity());

                        /// summing up the price of products in cart list
                        totalPrice = totalPrice + priceOfOneProduct;
                        String price = String.valueOf(totalPrice);
                        totalPriceIV.setText("GH₵ " + price);

                        cartList.add(userCartProduct);

                    }

                    /// make total and checkOut button visible
                    totalAndCheckoutContainer.setVisibility(View.VISIBLE);


                } else {

                    /// if cart is empty
                    cartIsEmptyMsg.setVisibility(View.VISIBLE);// leave a message
                    totalAndCheckoutContainer.setVisibility(View.GONE);// make total and checkOut button invisible
                    cartBadgeTv.setVisibility(View.GONE); /// make cart badge invisible

                }

                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    ////////////////////////////////////////




    /// Cart interface classes
    @Override
    public void deleteCartItem(int position) {
        totalPrice = 0; /// resetting total price  when an item is removed from cart

        /// getting the ID of the item clicked
        final UserCartProduct userCartProduct = cartList.get(position);
        final String productID = userCartProduct.getProductID();
        String productName = userCartProduct.getName();

        /// popUp a dialog
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UserCart.this);
        alertDialogBuilder.setTitle("Remove Item");
        alertDialogBuilder.setMessage("Are you sure you want to remove " + productName + " from your cart ?");
        alertDialogBuilder.setCancelable(false);


        /// confirm item removal
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                removeDialog.dismiss();


                /// deleting item from cart if yes is clicked
                final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference();
                cartRef.child("Cart list").child(currentUser).child("Products").
                        child(productID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toasty.success(getApplicationContext(),"Item removed successfully",Toast.LENGTH_SHORT).show();


                    }
                });

            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeDialog.dismiss();

            }
        });

        removeDialog = alertDialogBuilder.create();
        removeDialog.show();
        removeDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        removeDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));


    }

    @Override
    public void increaseQuantity(int position) {
        /// resetting total price  when there is a change in item quantity
        totalPrice = 0;

        /// getting the quantity
        final UserCartProduct userCartProduct = cartList.get(position);
        productQuantity = userCartProduct.getQuantity();
        String productID = userCartProduct.getProductID();

        /// limiting the quantity of items a user can order
        /// only increase if quantity is less than 5
        if (productQuantity < 5) {
            productQuantity++;

            int qty = productQuantity--;
            saveQuantity(qty, productID);
        }
    }

    @Override
    public void decreaseQuantity(int position) {

        /// resetting total price  when there is a change in item quantity
        totalPrice = 0;

        /// getting the quantity
        final UserCartProduct userCartProduct = cartList.get(position);
        productQuantity = userCartProduct.getQuantity();
        String productID = userCartProduct.getProductID();

        /// only decrease quantity if quantity is greater than 1
        if (productQuantity > 1) {
            productQuantity--;
            int qty = productQuantity--;
            saveQuantity(qty, productID);
        }

    }
    //////////////////////////////////////////////

    /// Saving new quantity to database
    public void saveQuantity(int qty, final String productID) {

        final HashMap<String, Object> hashMap = new HashMap();
        hashMap.put("quantity", qty);

        /// save new quantity
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cart list");
        databaseReference.child(currentUser).child("Products").child(productID).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                    }
                });

    }
    ///////////////////////////////////////////////////////////////


}