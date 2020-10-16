package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Registery extends AppCompatActivity {

    // Variables
    private ImageView userPhoto;
    private Button shopButton;
    private EditText userName, userNickname, userAddress;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageTask uploadTask;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private String name, nickname, address, downloadImageUrl,phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registery);

        // Hooks
        userPhoto = findViewById(R.id.user_photo);
        userName = findViewById(R.id.user_name);
        userNickname = findViewById(R.id.user_nickname);
        userAddress = findViewById(R.id.user_address);
        shopButton = findViewById(R.id.shopBtn);


        // [Getting phoneNumber]
        Intent intent=getIntent();
        phoneNumber=intent.getStringExtra("fullNumber");


        // Initialing firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        // [Choosing user photo - START]
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // [Storing user data -START]
        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateUserData();
            }
        });
    }

    //////// [Storing user data - validatingUserData function  ]//////////
    private void validateUserData() {
        name = userName.getText().toString();
        nickname = userNickname.getText().toString();
        address = userAddress.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getApplicationContext(), "Please enter your nickname", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(address)) {
            Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
        } else storeUserProfilePic();

    }
    private void storeUserProfilePic() {

        if (imageUri!=null){
          final   StorageReference filepath = storageReference.
                    child("User Profile").child(phoneNumber+".JPG");
           final UploadTask uploadTask =filepath.putFile( imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()){
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                Uri downloadUrl =task.getResult();
                                downloadImageUrl=downloadUrl.toString();
                                saveInfoWithPic();
                            }

                        }
                    });



                }
            });

        }
        else saveInfoWithoutPic();

    }

    private void saveInfoWithPic() {
        DatabaseReference info = databaseReference.child("User Profile");
        HashMap<String,Object> user =new HashMap<>();
        user.put("name",name);
        user.put("nickname",nickname);
        user.put("phone",phoneNumber);
        user.put("address",address);
        user.put("image",downloadImageUrl);

        info.child(phoneNumber).setValue(user);

        Intent intent=new Intent(getApplicationContext(),Home.class);
        intent.putExtra("phoneNumber",phoneNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    private void saveInfoWithoutPic() {
        DatabaseReference info = databaseReference.child("User Profile");

        HashMap<String,Object> user =new HashMap<>();
        user.put("name",name);
        user.put("nickname",nickname);
        user.put("phone",phoneNumber);
        user.put("address",address);

        info.child(phoneNumber).setValue(user);

        Intent intent=new Intent(getApplicationContext(),Home.class);
        intent.putExtra("phoneNumber",phoneNumber);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    //////// [Storing user data - END  ]//////////


    // /////////[Choosing user photo- openGallery function ]///////////
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==
                RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            userPhoto.setImageURI(imageUri);
        }
    }
    ////////// [Choosing user photo - END]///////////



}