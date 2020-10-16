package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import maxwell.vex.maxmart.admin.AdminLogin;

public class Home extends AppCompatActivity {


    private TextView userNickName;
    private CircleImageView user_Pic;
    private DatabaseReference userRef;
    private Button go;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        userNickName = findViewById(R.id.home_user_name);
        user_Pic = findViewById(R.id.home_profile);
        Intent intent=getIntent();


        user=FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("User Profile").child(user.getPhoneNumber());

        gettingUserProfileToDisplay();

        go = findViewById(R.id.admin_go);
        go.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                return false;
            }
        });


    }

    private void gettingUserProfileToDisplay() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if ((snapshot.exists())&&(snapshot.hasChild("nickname"))&&(snapshot.hasChild("image"))){
                    String userName ="Hi, "+snapshot.child("nickname").getValue().toString();
                    String userPic = snapshot.child("image").getValue().toString();
                    userNickName.setText(userName);
                    Picasso.with(getApplicationContext())
                            .load(userPic).
                            fit().centerCrop()
                            .into(user_Pic);

                }
                else {
                    String userName ="Hi, "+snapshot.child("nickname").getValue().toString();
                    userNickName.setText(userName);

                }







            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}