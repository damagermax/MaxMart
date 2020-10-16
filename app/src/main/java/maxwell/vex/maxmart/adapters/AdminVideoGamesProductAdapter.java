package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.modules.AdminVideoGames;

public class AdminVideoGamesProductAdapter extends RecyclerView.Adapter<AdminVideoGamesProductAdapter.AdminProductViewHolder> {


    private Context mCtx;
    private   List<AdminVideoGames> adminVideoGamesList;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    public AdminVideoGamesProductAdapter(Context mCtx, List<AdminVideoGames> adminVideoGamesList) {
        this.mCtx = mCtx;
        this.adminVideoGamesList = adminVideoGamesList;

    }

    @NonNull
    @Override
    public AdminProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(mCtx);
        View view=inflater.inflate(R.layout.admin_videoa_game_item,parent,false);
        return new AdminProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductViewHolder holder, int position) {
        AdminVideoGames adminVideoGames = adminVideoGamesList.get(position);
        holder.productName.setText(adminVideoGames.getName());
        holder.productPrice.setText(adminVideoGames.getPrice());
        holder.productCategory.setText(adminVideoGames.getCategory());
        holder.productID.setText(adminVideoGames.getProductID());

        String imageUrl = adminVideoGames.getImage();
        final String product_ID= adminVideoGames.getProductID();

        Picasso.with(mCtx).load(imageUrl)
                .fit().centerCrop().into(holder.productImage);


        holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("Categories").child("Video Games");
                storageReference= FirebaseStorage.getInstance().getReference("Categories");
                databaseReference.child(product_ID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storageReference.child(product_ID+".JPG").delete();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mCtx,"Product deleted successfully",
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return adminVideoGamesList.size();
    }

    public class AdminProductViewHolder extends RecyclerView.ViewHolder{


        private ImageButton deleteProduct;
        private TextView productPrice;
        private TextView productName;
        private TextView productCategory;
        private TextView productID;
        private ImageView productImage;


        public AdminProductViewHolder(@NonNull View itemView ) {
            super(itemView);
            productName=itemView.findViewById(R.id.product_name);
            productCategory=itemView.findViewById(R.id.product_category);
            productPrice=itemView.findViewById(R.id.product_price);
            productImage=itemView.findViewById(R.id.product_image);
            productID=itemView.findViewById(R.id.product_date);
            deleteProduct=itemView.findViewById(R.id.product_delete);





        }
    }
}
