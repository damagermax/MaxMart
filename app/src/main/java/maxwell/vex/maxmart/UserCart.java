package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserCart extends AppCompatActivity {

    private List<UserCartProduct> cartList;
    private UserCartAdapter cartAdapter;
    private RecyclerView cartRecycler;
    private DatabaseReference cartRef;

    private String currentUser;
    private ImageButton backBtn;
    private Button checkoutBtn;


    private TextView totalPriceIV;
    private int totalPrice=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_cart);

        cartRecycler=findViewById(R.id.cart_recycler);
        backBtn=findViewById(R.id.backBtn_cart);
        checkoutBtn=findViewById(R.id.cart_checkOutBtn);
        totalPriceIV=findViewById(R.id.cart_totalPrice);







        /// onBackPressed
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// getting current user's phone number
        gettingCurrentUser();

        /// displaying cart items
        displayingCartItems();
    }

    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser=user.getPhoneNumber();

        }
    }

    private void displayingCartItems() {
        cartList=new ArrayList<>();

        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter=new UserCartAdapter(cartList,getApplication());
        cartRecycler.setAdapter(cartAdapter);


        cartRef= FirebaseDatabase.getInstance().getReference();
        cartRef.child("Cart list").child("User View").child(currentUser).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot cartDb:snapshot.getChildren()){
                    UserCartProduct userCartProduct=cartDb.getValue(UserCartProduct.class);
                    if (userCartProduct != null) {
                        int priceOfOneProduct =(Integer.parseInt(userCartProduct.getPrice()));

                        totalPrice=totalPrice+priceOfOneProduct;
                        String mPrice=(String.valueOf(totalPrice));
                        totalPriceIV.setText("GH₵ "+mPrice);

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
}