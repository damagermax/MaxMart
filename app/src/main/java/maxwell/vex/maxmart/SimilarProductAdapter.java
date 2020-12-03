package maxwell.vex.maxmart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarProductAdapter extends RecyclerView.Adapter<SimilarProductAdapter.SimilarViewHolder> {

    private Context sCtx;
    private List<SimilarProducts>similarProductsList;

    public SimilarProductAdapter(Context sCtx, List<SimilarProducts> similarProductsList) {
        this.sCtx = sCtx;
        this.similarProductsList = similarProductsList;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(sCtx);
        View view = inflater.inflate(R.layout.similar_item,null);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {

        SimilarProducts similarProducts=similarProductsList.get(position);
        holder.price.setText(similarProducts.getPrice());
        holder.name.setText(similarProducts.getName());

        Picasso.with(sCtx).load(similarProducts.getImage()).fit().centerCrop().into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return similarProductsList.size();
    }

    class  SimilarViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView name;
        private TextView price;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.similar_name);
            price=itemView.findViewById(R.id.similar_price);
            image=itemView.findViewById(R.id.similar_image);
        }
    }
}
