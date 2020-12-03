package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import maxwell.vex.maxmart.Interface.AdminProductManagementActions;
import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.adapters.AdminProductManagementAdapter;

public class AdminProductManagement extends AppCompatActivity  implements AdminProductManagementActions {

    private DatabaseReference databaseReference;

    private RecyclerView productRecycler;
    private AdminProductManagementAdapter productAdapter;
    private List<maxwell.vex.maxmart.models.AdminProductManagement> productManagementList;

    private ImageView addProduct,backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_product_management);

        addProduct=findViewById(R.id.add_product);
        backBtn=findViewById(R.id.backBtn_pm);

        /// move to add product screen
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminAddProduct.class));
            }
        });

        /// move to previous screen
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// show all products added to database
        showAllProducts();
    }

    private void showAllProducts() {

        productRecycler = findViewById(R.id.product_recyclerview);
        productRecycler.setHasFixedSize(true);

        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        productManagementList = new ArrayList<>();
        productAdapter = new AdminProductManagementAdapter(getApplicationContext(), productManagementList,this);
        productRecycler.setAdapter(productAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productManagementList.clear();
                for (DataSnapshot mSnapshot : snapshot.getChildren()) {
                    maxwell.vex.maxmart.models.AdminProductManagement adminVideoGames = mSnapshot.getValue(maxwell.vex.maxmart.models.AdminProductManagement.class);
                    productManagementList.add(adminVideoGames);

                }
                productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    /// delete product
    @Override
    public void deleteProduct(int position) {
        maxwell.vex.maxmart.models.AdminProductManagement adminProductManagement=productManagementList.get(position);
        final String productID = adminProductManagement.getProductID();


       DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Products");
       final StorageReference storageReference= FirebaseStorage.getInstance().getReference("Categories");
        databaseReference.child(productID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                storageReference.child(productID+".JPG").delete();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toasty.success(getApplicationContext(), "Product deleted successfully",Toast.LENGTH_SHORT).show();

            }
        });


    }
}