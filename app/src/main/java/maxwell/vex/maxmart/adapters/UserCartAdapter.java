package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.modules.UserCartProduct;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.UserCartViewHolder> {

    private List<UserCartProduct> userCartProductList;
    private Context cCtx;
    private String currentUser;

    public UserCartAdapter(List<UserCartProduct> userCartProductList, Context cCtx) {
        this.userCartProductList = userCartProductList;
        this.cCtx = cCtx;
    }


    @NonNull
    @Override
    public UserCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(cCtx).inflate(R.layout.user_cart_item,null);
        return new UserCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCartViewHolder holder, int position) {
        final UserCartProduct userCartProduct = userCartProductList.get(position);

        final String productName= userCartProduct.getName();
        String productPrice= userCartProduct.getPrice();
        String productImage= userCartProduct.getImage();
        final String productID=userCartProduct.getProductID();

        holder.cartName.setText(productName);
        holder.cartPrice.setText(productPrice);

        Picasso.with(cCtx).load(productImage)
                .fit().centerInside().into(holder.cartImage);

        holder.cartDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gettingCurrentUser();

                final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference();
                cartRef.child("Cart list").child("User View").child(currentUser).child("Products").
                        child(productID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        cartRef.child("Cart list").child("Admin View").child(currentUser).child("Products").child(productID).removeValue();

                                Toast.makeText(cCtx,productName+" removed from cart successfully",Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });


    }
     /// getting currentUser
    private void gettingCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            currentUser=user.getPhoneNumber();

        }
    }

    @Override
    public int getItemCount() {
        return userCartProductList.size();
    }

    public static class UserCartViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartImage, cartDeleteBtn;
        private TextView cartName;
        private TextView cartPrice;


        public UserCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImage=itemView.findViewById(R.id.cart_image);
            cartPrice=itemView.findViewById(R.id.cart_price);
            cartName=itemView.findViewById(R.id.cart_name);
            cartDeleteBtn=itemView.findViewById(R.id.cart_delete);

        }
    }
}
