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
import maxwell.vex.maxmart.models.AllProducts;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.SearchViewHolder> {

    private List<AllProducts>searchProductList;
    private Context sCtx;

    public AllProductAdapter(List<AllProducts> searchProductList, Context sCtx) {
        this.searchProductList = searchProductList;
        this.sCtx = sCtx;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(sCtx);
        View view =inflater.inflate(R.layout.search_item,null);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchViewHolder holder, int position) {
        final AllProducts allProducts =searchProductList.get(position);
        /// setting data
        holder.name.setText(allProducts.getName());
        holder.price.setText(allProducts.getPrice());
        Picasso.with(sCtx).load(allProducts.getImage()).fit().centerCrop().into(holder.image, new Callback() {
            @Override
            public void onSuccess() {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {

            }
        });

        /// passing dara to product detail screen when an item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(sCtx, ProductDetail.class);
                intent.putExtra("productID", allProducts.getProductID()); // pass productID to productDetails activity to display info
                sCtx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return searchProductList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {


        private ImageView image;
        private TextView name;
        private  TextView price;
        private ProgressBar progressBar;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.searchIv);
            progressBar=itemView.findViewById(R.id.searchPg);
            name=itemView.findViewById(R.id.searchNameTv);
            price=itemView.findViewById(R.id.searchPriceTv);
        }
    }
}
