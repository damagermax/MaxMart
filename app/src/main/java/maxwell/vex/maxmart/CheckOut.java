package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import maxwell.vex.maxmart.adapters.UserCartAdapter;
import maxwell.vex.maxmart.models.UserCartProduct;

public class CheckOut extends AppCompatActivity {

    private String currentUser;
    private EditText nameET, phoneET, addressET;
    private RadioGroup delivery_Method, payment_Method;
    private String name, phone, address, deliveryMethod, paymentMethod, date, orderId;

    private Button confirmOrderBtn;

    private int total = 0, deliveryFees = 10;
    private TextView totalTv, subtotalTv, deliveryFeesTv;
    private String subtotal;
    private List<UserCartProduct> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        /// Hooks
        nameET = findViewById(R.id.check_out_name);
        addressET = findViewById(R.id.check_out_address);
        phoneET = findViewById(R.id.check_out_phone);
        delivery_Method = findViewById(R.id.radioGroup_DM);
        payment_Method = findViewById(R.id.radioGroupPay);
        confirmOrderBtn = findViewById(R.id.confirm_order);
        totalTv = findViewById(R.id.total_Tv);
        deliveryFeesTv = findViewById(R.id.deliveryFees_Tv);
        subtotalTv = findViewById(R.id.subTotal_Tv);

        // [] get delivery fees from database

        ///
        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();/// validating inputs

            }
        });


        /// getting data from Intent (total price)
        subtotal = getIntent().getStringExtra("total_price");


        /// setting data
        assert subtotal != null;
        total = Integer.parseInt(subtotal);
        totalTv.setText("GH₵ " + String.valueOf(total));
        deliveryFeesTv.setText("GH₵ " + String.valueOf(deliveryFees));
        subtotalTv.setText("GH₵ " + subtotal);


        /// get current user phone number
        gettingCurrentUser();

        /// get cartItems from dataBase to a list (cartList)/// get cartItems from dataBase
        gettingCartItems();
    }

    /// get cartItems from dataBase to a list (cartList)
    private void gettingCartItems() {

        /// initializing array
        cartList = new ArrayList<>();

        /// initializing firebase
      DatabaseReference  cartRef = FirebaseDatabase.getInstance().getReference();
        cartRef.child("Cart list").child(currentUser).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartList.clear(); /// clear list before add

                /// check if cart if empty
                if (snapshot.exists()) {
                    /// if cart is not empty
                    /// looping through the database
                    for (DataSnapshot cartDb : snapshot.getChildren()) {

                        /// getting the database value to userCartProduct model
                        UserCartProduct userCartProduct = cartDb.getValue(UserCartProduct.class);

                        /// add to list
                        cartList.add(userCartProduct);

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /////////////////////////////////////////////////////////

    /// validating fields
    private void validation() {

        /// get checked radioButton and returns -1 if no button is checked
        int deliveryMethodSelected = delivery_Method.getCheckedRadioButtonId();
        int paymentMethodSelected = payment_Method.getCheckedRadioButtonId();

        /// Hooks
        RadioButton radioButton_deliveryMethod = findViewById(deliveryMethodSelected);
        RadioButton radioButton_paymentMethod = findViewById(paymentMethodSelected);

        /// get inputs
        name = nameET.getText().toString();
        phone = phoneET.getText().toString();
        address = addressET.getText().toString();

        /// checking if radio button is selected before getting value
        if (paymentMethodSelected != -1 && deliveryMethodSelected != -1) {
            deliveryMethod = radioButton_deliveryMethod.getText().toString();
            paymentMethod = radioButton_paymentMethod.getText().toString();
        }


        if (TextUtils.isEmpty(name)) {
            Toasty.warning(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phone)) {
            Toasty.warning(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();

        } else if (phone.length() != 10) {
            Toasty.warning(getApplicationContext(), "Please check your phone number", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(address)) {
            Toasty.warning(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();

        } else if (deliveryMethodSelected == -1) {
            Toasty.warning(getApplicationContext(), "Please select your delivery method", Toast.LENGTH_SHORT).show();

        } else if (paymentMethodSelected == -1) {
            Toasty.warning(getApplicationContext(), "Please select your payment method", Toast.LENGTH_SHORT).show();

            /// Check payment method selected
        } else if (payment_Method.getCheckedRadioButtonId() == R.id.payOnDelivery) {

            /// if payment method is cash on delivery
            /// save order details to database
            saveOrderDetails();
        }else if (payment_Method.getCheckedRadioButtonId()==R.id.momo){
            /*
            / payment method is Mobile money
            / get mobile number and validate it (check if number is registered for momo(MTN) )
            / TODO rewrite code using Service
            */
            Toasty.warning(getApplicationContext(),"work in progress",Toast.LENGTH_SHORT).show();
        }

    }

    /// save order details to database
    private void saveOrderDetails() {



        /// get time in miles for orderID
        orderId = "" + System.currentTimeMillis();


        /// get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-mm-yyyy");
        date = currentDate.format(calendar.getTime());


        /// order details map
        final HashMap<String, Object> details = new HashMap<>();
        details.put("orderID", orderId);
        details.put("orderBy", name);
        details.put("orderCost", String.valueOf(total));
        details.put("orderStatus", "In progress"); /// In progress/Completed/Cancelled
        details.put("address", address);
        details.put("phone", phone);
        details.put("userID", currentUser);
        details.put("date", date);
        details.put("deliveryMethod", deliveryMethod);
        details.put("paymentMethod", paymentMethod);




        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child("User View").child(currentUser);
        orderRef.child(orderId).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                /// save the same details for admin
                DatabaseReference saveToAdmin = FirebaseDatabase.getInstance().getReference("Orders").child("Admin View").child(currentUser);
                saveToAdmin.child(orderId).setValue(details).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        /// save order items to dataBase
                        saveOrderItems();


                    }
                });
            }
        });
    }

    /// save order items to dataBase
    private void saveOrderItems() {

        String itemName, itemPrice, itemImage, itemProductID;
        int itemQuantity;

        /// loop through the list for all item
        for (int i=0;i <cartList.size();i++){
            itemName=cartList.get(i).getName();
            itemImage=cartList.get(i).getImage();
            itemPrice=cartList.get(i).getPrice();
            itemProductID=cartList.get(i).getProductID();
            itemQuantity=cartList.get(i).getQuantity();


            /// order items map
            final HashMap<String, Object> items = new HashMap<>();
            items.put("name", itemName);
            items.put("price", itemPrice);
            items.put("image", itemImage);
            items.put("productID", itemProductID);
            items.put("quantity", String.valueOf(itemQuantity));


            /// save items for user
            final DatabaseReference userItemRef = FirebaseDatabase.getInstance().getReference("Orders").child("User View").child(currentUser).child(orderId).child("Items");
            final String finalItemProductID = itemProductID;
            userItemRef.child(itemProductID).setValue(items).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    /// save items for admin
                    DatabaseReference adminItemRef = FirebaseDatabase.getInstance().getReference();
                    adminItemRef.child("Orders").child("Admin View").child(currentUser).child(orderId).child("Items").child(finalItemProductID).setValue(items)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    /// empty cart after saving order items
                                    emptyCart();

                                }
                            });


                }
            });

        }





    }

    /// empty cart after saving order items
    private void emptyCart() {
        DatabaseReference emptyCartRef = FirebaseDatabase.getInstance().getReference().child("Cart list");
        emptyCartRef.child(currentUser).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    Toasty.success(getApplicationContext(), "Order placed successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CheckOut.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);




                }

            }
        });

    }
    ///////////////////////////////

    /// getting current user's phone number function
    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getPhoneNumber();

        }
    }
    /////////////////////////////////////////////////

    /// calculating total price according to the delivery method selected (onClick method)
    public void DeliveryMethodCalculations(View view) {

        if (view.getId() == R.id.delivery) {

            total = 0; /// reset total

            ///calculating total with delivery fees
            total = deliveryFees + Integer.parseInt(subtotal);
            totalTv.setText("GH₵ " + String.valueOf(total));


        } else if (view.getId() == R.id.pickup) {

            total = 0; /// reset total

            ///calculating total without delivery fees
            total = Integer.parseInt(subtotal);
            totalTv.setText("GH₵ " + String.valueOf(total));
        }

    }
    /////////////////////////////////////////////////////////////////////
}