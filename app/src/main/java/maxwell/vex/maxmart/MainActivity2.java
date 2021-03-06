package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class MainActivity2 extends AppCompatActivity {
    private ImageButton nextButton;
    private EditText phoneNumber;
    private CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);


        phoneNumber = findViewById(R.id.phone_input);
        ccp = findViewById(R.id.ccp);
        nextButton = findViewById(R.id.nextBtn);


        ccp.registerCarrierNumberEditText(phoneNumber);


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OTP.class);
                intent.putExtra("fullNumber", ccp.getFullNumberWithPlus().replace("", ""));

                if (TextUtils.isEmpty(phoneNumber.getText()))
                    Toast.makeText(getApplicationContext(), "Please enter your phome number", Toast.LENGTH_SHORT).show();
                else startActivity(intent);


            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(),Home.class));
            finish();


        }





    }
}