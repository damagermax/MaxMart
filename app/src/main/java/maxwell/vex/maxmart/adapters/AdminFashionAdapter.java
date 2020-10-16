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
import maxwell.vex.maxmart.modules.AdminFashion;

public class AdminFashionAdapter extends RecyclerView.Adapter<AdminFashionAdapter.AdminFashionViewHolder> {

    private Context fCtx;
    private List<AdminFashion>fashionList;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;


    public AdminFashionAdapter(Context fCtx, List<AdminFashion> fashionList) {
        this.fCtx = fCtx;
        this.fashionList = fashionList;
    }

    @NonNull
    @Override
    public AdminFashionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(fCtx);
        View view=inflater.inflate(R.layout.admin_fashion_item,null);
        return new AdminFashionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFashionViewHolder holder, int position) {
        AdminFashion adminFashion=fashionList.get(position);
        holder.fashionName.setText(adminFashion.getName());
        holder.fashionPrice.setText(adminFashion.getPrice());
        holder.fashionCategory.setText(adminFashion.getCategory());
        holder.fashionDate.setText(adminFashion.getProductID());
        Picasso.with(fCtx).load(adminFashion.getImage()).fit().centerCrop().into(holder.fashionImage);

        final String product_ID= adminFashion.getProductID();

        holder.fashionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference= FirebaseDatabase.getInstance().getReference("Categories").child("Fashion");
                storageReference= FirebaseStorage.getInstance().getReference("Categories");
                databaseReference.child(product_ID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        storageReference.child(product_ID+".JPG").delete();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(fCtx,"Product deleted successfully",
                                Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return fashionList.size();
    }

    public  static class  AdminFashionViewHolder extends  RecyclerView.ViewHolder{


        private ImageView fashionImage;
        private ImageButton fashionDelete;
        private TextView fashionName;
        private TextView fashionPrice;
        private TextView fashionDate;
        private TextView fashionCategory;


        public AdminFashionViewHolder(@NonNull View itemView) {
            super(itemView);

            fashionName =itemView.findViewById(R.id.admin_fashion_name);
            fashionPrice =itemView.findViewById(R.id.admin_fashion_price);
            fashionCategory =itemView.findViewById(R.id.admin_fashion_category);
            fashionDate =itemView.findViewById(R.id.admin_fashion_date);
            fashionImage =itemView.findViewById(R.id.admin_fashion_image);
            fashionDelete =itemView.findViewById(R.id.fashion_delete);


        }
    }
}
