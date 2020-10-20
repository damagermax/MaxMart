package maxwell.vex.maxmart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.adapters.UserFashionAdapter;
import maxwell.vex.maxmart.modules.UserFashion;


public class UserFashionFragment extends Fragment {
    private List<UserFashion> userFashionList;
    private UserFashionAdapter userFashionAdapter;
    private RecyclerView fashionRecycler;
    private DatabaseReference fashionRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_user_fashion, container, false);
       fashionRecycler=view.findViewById(R.id.fashion_frag_user);
        loadFashion();
        return view;
    }
    private void loadFashion() {
        userFashionList = new ArrayList<>();
        fashionRecycler.setHasFixedSize(true);
        fashionRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        userFashionAdapter = new UserFashionAdapter(getContext(), userFashionList);
        fashionRecycler.setAdapter(userFashionAdapter);

        fashionRef = FirebaseDatabase.getInstance().getReference("Categories").child("Fashion");
        fashionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFashionList.clear();
                for (DataSnapshot fs : snapshot.getChildren()) {
                    UserFashion userFashion = fs.getValue(UserFashion.class);
                    userFashionList.add(userFashion);
                }
                userFashionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}