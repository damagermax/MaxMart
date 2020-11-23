package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import maxwell.vex.maxmart.admin.AdminLogin;
import maxwell.vex.maxmart.models.UserCartProduct;

public class Home extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView userPic;
    private DatabaseReference userRef;

    private FirebaseUser user;
    private String currentUser;
    private int cartQuantity = 0;
    private TextView gamesTv, fashionTv, cartBadgeTv;

    // editProfileDialog
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private CircleImageView mUserPic;
    private EditText mUserName, mUserAddress;
    private Button mUpdateBtn;
    private AlertDialog dialog;
    private String name, address, downloadImageUrl;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private ImageView search, cartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);


        userPic = findViewById(R.id.home_profile);
        gamesTv = findViewById(R.id.gamesTv);
        fashionTv = findViewById(R.id.fashionTv);
        cartBtn = findViewById(R.id.cartIv);
        search = findViewById(R.id.searchIv);
        cartBadgeTv = findViewById(R.id.cart_badge);
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        /// make cart badge invisible => will make it visible if cart is not empty
        cartBadgeTv.setVisibility(View.GONE);

        /// opening search activity
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchProduct.class));

            }
        });




        /// opening fragments
        gamesTv.setOnClickListener(this);
        fashionTv.setOnClickListener(this);

        /// opening edit profile dialog
        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileDialog();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserGamesFragment()).commit();


        /// getting current user's phone number
        gettingCurrentUser();
        /// get cartQuantity
        gettingCartQuantity();

        /// displaying current user's profile picture on actionbar
        displayCurrentUserInfo();

        /// opening admin panel
        openAdminPortal();

        /// opening cart activity
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), UserCart.class);
                intent.putExtra("cartQuantity",String.valueOf(cartQuantity));/// send cart quantity to cart activity
                startActivity(intent);
            }
        });


    }

    /// get cartQuantity
    private void gettingCartQuantity() {

        /// initializing firebase
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference();
        cartRef.child("Cart list").child(currentUser).child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                /// check if cart is empty
                if (snapshot.exists()) {

                    /// if cart if not empty

                    /// reset  to zero before summing up
                    cartQuantity = 0;

                    /// looping through the database
                    for (DataSnapshot cartDb : snapshot.getChildren()) {


                        /// getting quantity of one product  from database
                        int quantityOfOnProduct = Integer.parseInt(cartDb.child("quantity").getValue().toString());

                        /// getting the total quantity
                        cartQuantity = cartQuantity + quantityOfOnProduct;
                        cartBadgeTv.setText(String.valueOf(cartQuantity));
                        /// make cart badge visible
                        cartBadgeTv.setVisibility(View.VISIBLE);
                    }

                }else {
                    /// if cart is empty
                    /// make cart badge invisible
                    cartBadgeTv.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    /////////////////////////////////////////////////////////

    /// [ Edit Profile Functions - START] ///
    private void editProfileDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Home.this);
        View view = getLayoutInflater().inflate(R.layout.edit_profle_dialog, null);

        mUserPic = view.findViewById(R.id.user_pic_edit);
        mUserName = view.findViewById(R.id.user_name_edit);
        mUserAddress = view.findViewById(R.id.user_address_edit);
        mUpdateBtn = view.findViewById(R.id.user_update);

        displayCurrentUserInfoOnDialog();

        mUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mUserName.getText().toString();
                address = mUserAddress.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(getApplicationContext(), "Please enter your address", Toast.LENGTH_SHORT).show();
                } else storeUserProfilePic();
            }
        });


        mBuilder.setView(view);
        dialog = mBuilder.create();
        dialog.show();
    }

    private void displayCurrentUserInfoOnDialog() {
        DatabaseReference editRef = FirebaseDatabase.getInstance().getReference("User Profile").child(currentUser);
        editRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String user_pic, user_name, user_address;

                if ((snapshot.hasChild("image")) && (snapshot.hasChild("name")) && (snapshot.hasChild("address"))) {

                    user_pic = snapshot.child("image").getValue().toString();
                    user_name = snapshot.child("name").getValue().toString();
                    user_address = snapshot.child("address").getValue().toString();

                    mUserName.setText(user_name);
                    mUserAddress.setText(user_address);
                    Picasso.with(getApplicationContext()).load(user_pic).
                            placeholder(R.drawable.profile).fit().centerCrop()
                            .into(mUserPic);

                } else {
                    user_name = snapshot.child("name").getValue().toString();
                    user_address = snapshot.child("address").getValue().toString();

                    mUserName.setText(user_name);
                    mUserAddress.setText(user_address);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void storeUserProfilePic() {
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (imageUri != null) {
            final StorageReference filepath = storageReference.
                    child("User Profile").child(currentUser + ".JPG");
            final UploadTask uploadTask = filepath.putFile(imageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return filepath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUrl = task.getResult();
                                downloadImageUrl = downloadUrl.toString();
                                saveInfoWithPic();
                            }

                        }
                    });


                }
            });

        } else saveInfoWithoutPic();
    }

    private void saveInfoWithPic() {
        DatabaseReference info = databaseReference.child("User Profile");
        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("address", address);
        user.put("image", downloadImageUrl);

        info.child(currentUser).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


    }

    private void saveInfoWithoutPic() {
        DatabaseReference info = databaseReference.child("User Profile");

        HashMap<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("address", address);

        info.child(currentUser).updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });


    }

    /// [Storing user data - END  ]///
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
            mUserPic.setImageURI(imageUri);
        }
    }
    ////////// [Choosing user photo - END]///////////


    private void openAdminPortal() {
        userPic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                return false;
            }
        });
    }


    private void gettingCurrentUser() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUser = user.getPhoneNumber();
        }
    }

    private void displayCurrentUserInfo() {
        userRef = FirebaseDatabase.getInstance().getReference("User Profile").child(currentUser);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists()) && (snapshot.hasChild("image"))) {
                    String user_pic = snapshot.child("image").getValue().toString();

                    Picasso.with(getApplicationContext()).
                            load(user_pic).placeholder(R.drawable.profile).
                            fit().centerCrop().into(userPic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.gamesTv:
                GameUi();
                fragment = new UserGamesFragment();
                break;
            case R.id.fashionTv:
                FashionUi();
                fragment = new UserFashionFragment();
                break;

        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.replace(R.id.fragment_container, fragment).commit();

    }

    private void FashionUi() {
        gamesTv.setBackgroundResource(R.drawable.tablayout);
        gamesTv.setTextColor(getResources().getColor(R.color.white));

        fashionTv.setBackgroundResource(R.drawable.tadbtn);
        fashionTv.setTextColor(getResources().getColor(R.color.GreyDark));


    }

    private void GameUi() {
        fashionTv.setBackgroundResource(R.drawable.tablayout);
        fashionTv.setTextColor(getResources().getColor(R.color.white));

        gamesTv.setBackgroundResource(R.drawable.tadbtn);
        gamesTv.setTextColor(getResources().getColor(R.color.GreyDark));

    }

    /// [ Menu ] ///
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}