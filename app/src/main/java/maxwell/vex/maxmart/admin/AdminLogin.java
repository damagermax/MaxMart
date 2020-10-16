package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private EditText admin_Name, admin_Password;
     private String phone, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        submit = findViewById(R.id.admin_submit);
        admin_Name = findViewById(R.id.admin_name);
        admin_Password = findViewById(R.id.admin_password);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatingField();
            }
        });
    }

    private void validatingField() {
        phone = admin_Name.getText().toString();
        password = admin_Password.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getApplicationContext(), "Please enter your phone number", Toast.LENGTH_SHORT).show();

       /// } else if (TextUtils.isEmpty(password)) {
            ///Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
        } else validatingUserNameAndPassword();


    }

    private void validatingUserNameAndPassword() {


        DatabaseReference mRef;
        mRef = FirebaseDatabase.getInstance().getReference("Admins Profile");

        Query checkAdmins = mRef.orderByChild("phone").equalTo(phone);
        checkAdmins.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    startActivity( new Intent(getApplicationContext(),AdminPanel.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}