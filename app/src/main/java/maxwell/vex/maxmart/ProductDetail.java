package maxwell.vex.maxmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    private ImageButton backBtn;
    private ImageView detailImage;
    private TextView detailName,detailPrice,detailDesc;
    private  String productID;
    private  DatabaseReference detailRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_detail);

        backBtn=findViewById(R.id.backBtn_pd);
        detailName=findViewById(R.id.detail_name);
        detailPrice=findViewById(R.id.detail_price);
        detailDesc=findViewById(R.id.detail_desc);
        detailImage=findViewById(R.id.detail_imageTv);


        Intent intent=getIntent();
        productID=intent.getStringExtra("ID");

        detailRef= FirebaseDatabase.getInstance().getReference("Categories").child("Video Games").child(productID);
        detailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String productName=snapshot.child("name").getValue(String.class);
                    String productPrice=snapshot.child("price").getValue(String.class);
                    String productImage=snapshot.child("image").getValue(String.class);
                    String productDesc=snapshot.child("description").getValue(String.class);


                    detailDesc.setText(productDesc);
                    detailName.setText(productName);
                    detailPrice.setText(productPrice);
                    Picasso.with(getApplicationContext()).load(productImage).fit().centerInside()
                            .into(detailImage);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}