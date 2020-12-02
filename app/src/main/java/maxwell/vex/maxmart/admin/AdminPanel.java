package maxwell.vex.maxmart.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import maxwell.vex.maxmart.R;

public class AdminPanel extends AppCompatActivity implements View.OnClickListener {

    private CardView productManagement,orderManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_admin_panel);

        productManagement=findViewById(R.id.product_management);
        orderManagement=findViewById(R.id.order_management);


        productManagement.setOnClickListener(this);
        orderManagement.setOnClickListener(this);

    }
    public void  onClick(View view){
        switch ( view.getId()){


            case R.id.product_management:
                startActivity(new Intent(getApplicationContext(), AdminProductManagement.class));
                break;
            case R.id.order_management:
                startActivity(new Intent(getApplicationContext(), AdminOrderManagement.class));
                break;

        }

    }

}