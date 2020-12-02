package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.adapters.AllProductAdapter;
import maxwell.vex.maxmart.models.AllProducts;
import maxwell.vex.maxmart.models.UserSearchProduct;

public class SearchProduct extends AppCompatActivity {


    /// listView variables
    private ListView listView;
    private ImageView backBtn;
    private EditText searchInput;
    private List<UserSearchProduct> searchProductList;
    private ArrayAdapter<UserSearchProduct> searchAdapter;
    /// recyclerView variables
    private RecyclerView searchRv;
    private AllProductAdapter allProductAdapter;
    private List<AllProducts> allProductsList;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /// Hooks
        listView = findViewById(R.id.search_outPut);
        backBtn = findViewById(R.id.cancel_search);
        searchInput = findViewById(R.id.searchEt);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        searchText = searchInput.getText().toString();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserSearchProduct userSearchProduct = searchProductList.get(position);
            }
        });

        /// load  all products  from database to recyclerView
        loadingAllProduct();

        /// search data
        searchForProduct();

    }

    private void searchForProduct() {
        searchProductList = new ArrayList<>();
        searchAdapter = new ArrayAdapter<UserSearchProduct>
                (this, android.R.layout.simple_list_item_1, searchProductList);

        Query search = FirebaseDatabase.getInstance().getReference("Products")
                .orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");
        search.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /// load  data from database to recyclerView
    private void loadingAllProduct() {
        searchRv = findViewById(R.id.searchRv);
        searchRv.setHasFixedSize(true);
        searchRv.setLayoutManager(new GridLayoutManager(this, 2));
        allProductsList = new ArrayList<>();
        allProductAdapter = new AllProductAdapter(allProductsList, this);
        searchRv.setAdapter(allProductAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allProductsList.clear();
                for (DataSnapshot DB1 : snapshot.getChildren()) {

                    AllProducts allProducts = DB1.getValue(AllProducts.class);
                    allProductsList.add(allProducts);


                }
                allProductAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    ////////////////////////////////////////////
}