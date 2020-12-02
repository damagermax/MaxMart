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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import maxwell.vex.maxmart.adapters.UserGamesAdapter;
import maxwell.vex.maxmart.models.UserGames;


public class UserGamesFragment extends Fragment {


    private RecyclerView gamesRecycler;
    private List<UserGames> userGamesList;
    private UserGamesAdapter userGamesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_games, container, false);
        gamesRecycler=view.findViewById(R.id.games_frag_user);
        loadGames();

        return view;
    }

    private void loadGames() {



        userGamesList = new ArrayList<>();
        userGamesAdapter = new UserGamesAdapter(getContext(), userGamesList);
        gamesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gamesRecycler.setHasFixedSize(true);
        gamesRecycler.setAdapter(userGamesAdapter);



      Query gamesRef = FirebaseDatabase.getInstance().getReference("Products").orderByChild("category").equalTo("Video Games");
        gamesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userGamesList.clear();

                for (DataSnapshot games : snapshot.getChildren()) {
                    UserGames userGames = games.getValue(UserGames.class);
                    userGamesList.add(userGames);

                }


                userGamesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}