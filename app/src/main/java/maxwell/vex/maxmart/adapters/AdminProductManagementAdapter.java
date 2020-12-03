package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.Interface.AdminProductManagementActions;
import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.models.AdminProductManagement;

public class AdminProductManagementAdapter extends RecyclerView.Adapter<AdminProductManagementAdapter.AdminProductViewHolder> {


    private Context mCtx;
    private List<AdminProductManagement> adminVideoGamesList;
    private AdminProductManagementActions adminProductManagementActions;

    public AdminProductManagementAdapter(Context mCtx, List<AdminProductManagement> adminVideoGamesList, AdminProductManagementActions adminProductManagementActions) {
        this.mCtx = mCtx;
        this.adminVideoGamesList = adminVideoGamesList;
        this.adminProductManagementActions = adminProductManagementActions;
    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.admin_product_management_item, parent, false);
        return new AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        AdminProductManagement adminVideoGames = adminVideoGamesList.get(position);
        holder.productName.setText(adminVideoGames.getName());
        holder.productPrice.setText(adminVideoGames.getPrice());
        holder.productCategory.setText(adminVideoGames.getCategory());
        holder.productID.setText(adminVideoGames.getProductID());

        String imageUrl = adminVideoGames.getImage();
        final String product_ID = adminVideoGames.getProductID();

        Picasso.with(mCtx).load(imageUrl)
                .fit().centerCrop().into(holder.productImage);


        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


    }

    @Override
    public int getItemCount() {
        return adminVideoGamesList.size();
    }

    public class AdminProductViewHolder extends RecyclerView.ViewHolder {


        private ImageButton deleteProduct;
        private TextView productPrice;
        private TextView productName;
        private TextView productCategory;
        private TextView productID;
        private ImageView productImage;


        public AdminProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productCategory = itemView.findViewById(R.id.product_category);
            productPrice = itemView.findViewById(R.id.product_price);
            productImage = itemView.findViewById(R.id.product_image);
            productID = itemView.findViewById(R.id.product_date);
            deleteProduct = itemView.findViewById(R.id.product_delete);


            /// delete product
            deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adminProductManagementActions.deleteProduct(getAdapterPosition());
                }
            });
        }
    }
}
