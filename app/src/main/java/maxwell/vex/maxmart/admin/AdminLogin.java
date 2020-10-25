package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import maxwell.vex.maxmart.R;

public class AdminLogin extends AppCompatActivity {


    private Button submit;
    private ImageButton backBtn;
    private EditText admin_Name, admin_Password;
    private String name, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_login);

        submit = findViewById(R.id.admin_submit);
        admin_Name = findViewById(R.id.admin_name);
        admin_Password = findViewById(R.id.admin_password);
        backBtn = findViewById(R.id.backBtn_login);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatingField();
            }
        });
    }

    private void validatingField() {
        name = admin_Name.getText().toString();
        password = admin_Password.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        } else validatingUserNameAndPassword(name, password);


    }

    private void validatingUserNameAndPassword(final String name, final String password) {
        final DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference().child("Admins Profile");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {

                    String dbName = ds.child("name").getValue().toString();
                    String dbPassword = ds.child("password").getValue().toString();


                    if ((dbName.equals(name)) && (dbPassword.equals(password))) {

                        startActivity(new Intent(getApplicationContext(), AdminPanel.class));
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}