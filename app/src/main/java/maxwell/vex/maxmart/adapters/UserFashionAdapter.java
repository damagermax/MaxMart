package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.ProductDetail;
import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.models.UserFashion;

public class UserFashionAdapter extends RecyclerView.Adapter<UserFashionAdapter.UserFashionViewHolder> {

    private Context fCtx;
    private List<UserFashion> userFashionList;

    public UserFashionAdapter(Context fCtx, List<UserFashion> userFashionList) {
        this.fCtx = fCtx;
        this.userFashionList = userFashionList;
    }

    @NonNull
    @Override
    public UserFashionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fCtx);
        View view = inflater.inflate(R.layout.user_fashion_item, parent, false);
        return new UserFashionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserFashionViewHolder holder, int position) {
        UserFashion userFashion = userFashionList.get(position);

        /// getting data
        String image = userFashion.getImage();
        String name = userFashion.getName();
        String price = userFashion.getPrice();
        final String productID = userFashion.getProductID();
        final String category = userFashion.getCategory();
        String priceI = "GH₵ " + price;
        ///////////////////////////////////////////////////

        /// setting data to views
        holder.fashionPrice.setText(priceI);
        holder.fashionName.setText(name);
        Picasso.with(fCtx).load(image).fit().centerCrop().into(holder.fashionImage, new Callback() {
            @Override
            public void onSuccess() {

                /// hiding progressBar when data loads successfully
                holder.fashionProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////


        /// passing dara to product detail screen when an item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fCtx, ProductDetail.class);
                intent.putExtra("productID", productID); // pass productID to productDetails activity to display info
                intent.putExtra("category", category); // pass category to display similar products
                fCtx.startActivity(intent);
            }
        });
        /////////////////////////////////////////////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return userFashionList.size();
    }

    public static class UserFashionViewHolder extends RecyclerView.ViewHolder {

        private ImageView fashionImage;
        private TextView fashionName;
        private TextView fashionPrice;
        private ProgressBar fashionProgressBar;

        public UserFashionViewHolder(@NonNull View itemView) {
            super(itemView);

            fashionImage = itemView.findViewById(R.id.fashion_ImageIv);
            fashionName = itemView.findViewById(R.id.fashion_NameTv);
            fashionPrice = itemView.findViewById(R.id.fashion_PriceTv);
            fashionProgressBar = itemView.findViewById(R.id.fashionPg);
        }
    }
}
