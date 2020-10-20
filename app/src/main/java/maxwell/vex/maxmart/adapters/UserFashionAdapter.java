package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.modules.UserFashion;

public class UserFashionAdapter extends RecyclerView.Adapter<UserFashionAdapter.UserFashionViewHolder> {

    private Context fCtx;
    private List<UserFashion>userFashionList;

    public UserFashionAdapter(Context fCtx, List<UserFashion> userFashionList) {
        this.fCtx = fCtx;
        this.userFashionList = userFashionList;
    }

    @NonNull
    @Override
    public UserFashionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(fCtx);
        View view = inflater.inflate(R.layout.user_fashion_item,parent,false);
        return new UserFashionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserFashionViewHolder holder, int position) {
        UserFashion userFashion = userFashionList.get(position);

        String image=userFashion.getImage();
        String name=userFashion.getName();
        String price =userFashion.getPrice();
        String productID=userFashion.getProductID();
        String priceI="GH₵ "+price;

        holder.fashionPrice.setText(priceI);
        holder.fashionName.setText(name);
        Picasso.with(fCtx).load(image).fit().centerCrop().into(holder.fashionImage);

    }

    @Override
    public int getItemCount() {
        return userFashionList.size();
    }

    public static class UserFashionViewHolder extends  RecyclerView.ViewHolder{

        private ImageView fashionImage;
        private TextView fashionName;
        private  TextView fashionPrice;

        public UserFashionViewHolder(@NonNull View itemView) {
            super(itemView);

            fashionImage=itemView .findViewById(R.id.fashion_ImageIv);
            fashionName= itemView.findViewById(R.id.fashion_NameTv);
            fashionPrice= itemView.findViewById(R.id.fashion_PriceTv);
        }
    }
}
