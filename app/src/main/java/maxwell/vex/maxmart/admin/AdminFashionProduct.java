package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.adapters.AdminFashionAdapter;
import maxwell.vex.maxmart.models.AdminFashion;

public class AdminFashionProduct extends AppCompatActivity {

    private List<AdminFashion>fashionList;
    private AdminFashionAdapter fashionAdapter;
    private RecyclerView fashionRecyclerView;
    private DatabaseReference fashionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_fashion_product);

        showFashionProduct();
    }

    private void showFashionProduct() {
        fashionRecyclerView = findViewById(R.id.admin_fashion_recyclerview);
        fashionRecyclerView.setHasFixedSize(true);
        fashionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fashionList=new ArrayList<>();
        fashionAdapter=new AdminFashionAdapter(this,fashionList);
        fashionRecyclerView.setAdapter(fashionAdapter);


        fashionRef = FirebaseDatabase.getInstance().getReference("Categories").child("Fashion");
        fashionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fashionList.clear();
                for (DataSnapshot ds:snapshot.getChildren()){
                    AdminFashion adminFashion=ds.getValue(AdminFashion.class);
                    fashionList.add(adminFashion);
                }
                 fashionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}