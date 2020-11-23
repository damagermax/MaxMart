package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.adapters.AdminVideoGamesProductAdapter;
import maxwell.vex.maxmart.models.AdminVideoGames;

public class AdminVideoGamesProducts extends AppCompatActivity  {

    private DatabaseReference databaseReference;

    private RecyclerView productRecycler;
    private AdminVideoGamesProductAdapter productAdapter;
    private List<AdminVideoGames> adminVideoGamesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_video_games_products);

        showAdminProducts();
    }

    private void showAdminProducts() {

        productRecycler = findViewById(R.id.product_recyclerview);
        productRecycler.setHasFixedSize(true);

        productRecycler.setLayoutManager(new LinearLayoutManager(this));
        adminVideoGamesList = new ArrayList<>();
        productAdapter = new AdminVideoGamesProductAdapter(getApplicationContext(), adminVideoGamesList);
        productRecycler.setAdapter(productAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Categories").child("Video Games");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminVideoGamesList.clear();
                for (DataSnapshot mSnapshot : snapshot.getChildren()) {
                    AdminVideoGames adminVideoGames = mSnapshot.getValue(AdminVideoGames.class);
                    adminVideoGamesList.add(adminVideoGames);

                }
                productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



}