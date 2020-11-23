package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.adapters.SearchAdapter;
import maxwell.vex.maxmart.models.SearchProducts;

public class SearchProduct extends AppCompatActivity {

    private RecyclerView searchRv;
    private ImageButton backBtn;
    private EditText searchInput;

    private SearchAdapter searchAdapter;
    private List<SearchProducts>searchProductsList;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);



        backBtn=findViewById(R.id.searchBackBtn);
        searchInput=findViewById(R.id.searchEt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /// getting data from firebase
        gettingKey();

    }





    private void gettingKey() {
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Categories");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                   String key=dataSnapshot.getRef().getKey().toString();
                    //Toast.makeText(getApplicationContext(),key,Toast.LENGTH_SHORT).show();
                    loadingData(key);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void loadingData(String key) {
        searchRv=findViewById(R.id.searchRv);
        searchRv.setHasFixedSize(true);
        searchRv.setLayoutManager(new GridLayoutManager(this,2));
        searchProductsList=new ArrayList<>();
        searchAdapter=new SearchAdapter(searchProductsList, this);
        searchRv.setAdapter(searchAdapter);

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Categories").child(key);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchProductsList.clear();
                for (DataSnapshot DB1:snapshot.getChildren()){
                    String key=DB1.getRef().getKey().toString();
                    SearchProducts searchProducts=DB1.getValue(SearchProducts.class);
                    searchProductsList.add(searchProducts);



                }
                searchAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}