package maxwell.vex.maxmart.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import maxwell.vex.maxmart.R;

public class AdminPanel extends AppCompatActivity implements View.OnClickListener {

    private TextView addProduct,fashion,games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        addProduct=findViewById(R.id.add_product);
        fashion=findViewById(R.id.fashion);
        games=findViewById(R.id.games);


        addProduct.setOnClickListener(this);
        fashion.setOnClickListener(this);
        games.setOnClickListener(this);
    }
    public void  onClick(View view){
        switch ( view.getId()){
            case R.id.add_product:
                startActivity(new Intent(getApplicationContext(),AdminAddProduct.class));
                break;

            case R.id.games:
                startActivity(new Intent(getApplicationContext(), AdminVideoGamesProducts.class));
                break;
            case R.id.fashion:
                startActivity(new Intent(getApplicationContext(),AdminFashionProduct.class));

        }

    }

}