package maxwell.vex.maxmart.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import maxwell.vex.maxmart.R;

public class AdminAddProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText addProductName, addProductPrice, addProductDescription;
    private ImageView addProductImage;
    private Button uploadBtn;
    private CheckBox checkBox;
    private ImageButton backBtn;
    private String productName, productPrice, productDescription,productID;
    private  String saveCurrentDate,saveCurrentTime;

    /// [ Image From Gallery] ///
    private static final  int PICK_IMAGE_REQUEST = 1;
    private Uri productUri;
    private String downloadUrl;

    /// [ Spinner ] ///
    private Spinner spinner;
    private String item;
    private  String[] categories= {"Select Category","Video Games","Fashion"};

    /// [ Firebase ] ///
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private StorageTask mUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);


        /// [ Hooks ] ///
        addProductName = findViewById(R.id.admin_product_name);
        addProductPrice = findViewById(R.id.admin_product_price);
        addProductDescription = findViewById(R.id.admin_product_description);
        addProductImage = findViewById(R.id.admin_product_image);
        spinner =findViewById(R.id.admin_product_spinner);
        uploadBtn =findViewById(R.id.admin_product_upload);
        checkBox= findViewById(R.id.admin_verify_upload);
        backBtn=findViewById(R.id.backBtn_add);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        /// [ Initializing Firebase ] ///
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");
        storageReference = FirebaseStorage.getInstance().getReference("Categories");

        ///  [ Getting Input From Fields ] ///
        productName = addProductName.getText().toString();
        productPrice = addProductPrice.getText().toString();
        productDescription = addProductDescription.getText().toString();

        /// [ Spinner Functions ] ///
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,categories);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateTextFields(item);
            }
        });

        /// [ Getting Image From Gallery - START ] ///
        addProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromGallery();
            }
        });

    }

    private void validateTextFields(String item) {
        ///  [ Getting Input From Fields ] ///
        productName = addProductName.getText().toString();
        productPrice = addProductPrice.getText().toString();
        productDescription = addProductDescription.getText().toString();


        if (TextUtils.isEmpty(productName)){

            Toast.makeText(getApplicationContext(),"please enter product name ",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productPrice)){

            Toast.makeText(getApplicationContext(),"please enter product price ",Toast.LENGTH_SHORT).show();
        }
        else if (item.equals("Select Category")){

            Toast.makeText(getApplicationContext(),"please select category ",Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(productDescription)){

            Toast.makeText(getApplicationContext(),"please enter product description ",Toast.LENGTH_SHORT).show();
        }
        else if (productUri==null){

            Toast.makeText(getApplicationContext(),"Please select product image",Toast.LENGTH_SHORT).show();
        }



        else if (mUpload!=null && mUpload.isInProgress() ){

            Toast.makeText(getApplicationContext(),"Upload in progress...",Toast.LENGTH_SHORT).show();
        }
        else if (!checkBox.isChecked()){

            Toast.makeText(getApplicationContext(),"Please verify upload",Toast.LENGTH_SHORT).show();

        }else storeProductImage();



    }
    private void storeProductImage() {


        Calendar calendar= Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd,yyyy  ");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime =currentTime.format(calendar.getTime());

        productID=saveCurrentDate+saveCurrentTime;

       final StorageReference filepath =storageReference.child( productID+".JPG");
        final UploadTask uploadTask=filepath.putFile(productUri);
       mUpload= uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }

                        return  filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl1=task.getResult();
                            downloadUrl=downloadUrl1.toString();
                            saveProductToFireBase(item);
                        }

                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(getApplicationContext() ,e.getMessage(),Toast.LENGTH_SHORT).show();

           }
       });


    }
    private void saveProductToFireBase(String item) {
        HashMap<String,Object> productMap= new HashMap<>();
        productMap.put("name",productName);
        productMap.put("price",productPrice);
        productMap.put("image",downloadUrl);
        productMap.put("category",item);
        productMap.put("description",productDescription);
        productMap.put("productID",productID);

        databaseReference.child(productID).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toasty.success(getApplicationContext(),"Product added successfully",Toast.LENGTH_SHORT).show();
                    checkBox.setChecked(false);
                }else {
                    Toasty.error(getApplicationContext(),"Product not saved",Toast.LENGTH_SHORT).show();


                }

            }
        });


    }


    /// [ Getting Image From Gallery - START ] ///
    private void getImageFromGallery() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT );
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode ==
                RESULT_OK && data != null && data.getData() != null) {
            productUri = data.getData();
            addProductImage.setImageURI(productUri);
        }
    }
            /// [ Getting Image From Gallery - END] ///



         /////////// [ Spinner AdapterViewOnSelectListener - START ]  ////////////
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         item = spinner.getSelectedItem().toString();

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(), "Please select a category" , Toast.LENGTH_SHORT).show();

    }
         /////////// [ Spinner AdapterViewOnSelectListener - END ]  ////////////
}