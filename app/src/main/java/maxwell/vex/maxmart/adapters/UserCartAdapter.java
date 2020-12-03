package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.Interface.UserCartAction;
import maxwell.vex.maxmart.models.UserCartProduct;

public class UserCartAdapter extends RecyclerView.Adapter<UserCartAdapter.UserCartViewHolder> {

    private List<UserCartProduct> userCartProductList;
    private Context cCtx;
    private UserCartAction userCartAction;
    int quantity=0;

    public UserCartAdapter(List<UserCartProduct> userCartProductList, Context cCtx, UserCartAction userCartAction) {
        this.userCartProductList = userCartProductList;
        this.cCtx = cCtx;
        this.userCartAction = userCartAction;
    }

    @NonNull
    @Override
    public UserCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(cCtx).inflate(R.layout.user_cart_item, null);
        return new UserCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserCartViewHolder holder, int position) {
        final UserCartProduct userCartProduct = userCartProductList.get(position);

        final String productName = userCartProduct.getName();
        String productPrice = userCartProduct.getPrice();
        String productImage = userCartProduct.getImage();
        quantity = userCartProduct.getQuantity();
        String pQuantity = String.valueOf(quantity);

        holder.cartName.setText(productName);
        holder.cartPrice.setText("GH₵ "+ productPrice);
        holder.quantityTv.setText(pQuantity);


        Picasso.with(cCtx).load(productImage)
                .fit().centerInside().into(holder.cartImage);


    }


    @Override
    public int getItemCount() {
        return userCartProductList.size();
    }

    class UserCartViewHolder extends RecyclerView.ViewHolder {

        private ImageView cartImage, cartDeleteBtn;
        private TextView cartName;
        private TextView cartPrice;
        private TextView quantityTv;
        private ImageView addQuantity, removeQuantity;


        public UserCartViewHolder(@NonNull View itemView) {
            super(itemView);

            cartImage = itemView.findViewById(R.id.cart_image);
            cartPrice = itemView.findViewById(R.id.cart_price);
            cartName = itemView.findViewById(R.id.cart_name);
            cartDeleteBtn = itemView.findViewById(R.id.cart_delete);
            addQuantity = itemView.findViewById(R.id.increase);
            removeQuantity = itemView.findViewById(R.id.decrease);
            quantityTv = itemView.findViewById(R.id.quantityTv);


            /// deleting product from cart
            cartDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    userCartAction.deleteCartItem(getAdapterPosition());
                }
            });

            /// increasing product quantity
            addQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userCartAction.increaseQuantity(getAdapterPosition());
                }
            });

            /// decreasing product quantity
            removeQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userCartAction.decreaseQuantity(getAdapterPosition());
                }
            });

        }
    }
}
