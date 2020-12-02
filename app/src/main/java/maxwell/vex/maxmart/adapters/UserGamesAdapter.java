package maxwell.vex.maxmart.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.util.List;

import maxwell.vex.maxmart.ProductDetail;
import maxwell.vex.maxmart.R;
import maxwell.vex.maxmart.models.UserGames;

public class UserGamesAdapter extends RecyclerView.Adapter<UserGamesAdapter.UserGamesViewHolder> {

    private Context gCtx;
    private List<UserGames> userGamesList;


    public UserGamesAdapter(Context gCtx, List<UserGames> userGamesList) {
        this.gCtx = gCtx;
        this.userGamesList = userGamesList;
    }

    @NonNull
    @Override
    public UserGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(gCtx);
        View view = inflater.inflate(R.layout.user_games_item, null);
        return new UserGamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserGamesViewHolder holder, int position) {
        /// getting the position of an item
        UserGames userGames = userGamesList.get(position);
        //////////////////////////////////////////////////

        /// getting data
        String image = userGames.getImage();
        String name = userGames.getName();
        String price = userGames.getPrice();
        final String productID = userGames.getProductID();
        final String category = userGames.getCategory();
        String priceI = "GH₵ " + price;
        /////////////////////////////////////////////

        /// setting data to view
        holder.gamePrice.setText(priceI);
        holder.gameName.setText(name);
        Picasso.with(gCtx).load(image)
                .fit().centerCrop().into(holder.gameImage, new Callback() {
            @Override
            public void onSuccess() {
                /// hiding progressBar
                holder.gamesProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {

            }
        });
        /////////////////////////////////////////////////////////////////////////

        /// passing data to to product detail activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gCtx, ProductDetail.class);
                intent.putExtra("productID", productID);
                gCtx.startActivity(intent);


            }
        });
        ////////////////////////////////////////////////

    }

    @Override
    public int getItemCount() {
        return userGamesList.size();
    }

    public static class UserGamesViewHolder extends RecyclerView.ViewHolder {


        private ImageView gameImage;
        private TextView gamePrice;
        private TextView gameName;
        private ProgressBar gamesProgressBar;

        public UserGamesViewHolder(@NonNull View itemView) {
            super(itemView);
            gameImage = itemView.findViewById(R.id.gameImageIv);
            gameName = itemView.findViewById(R.id.gameNameTv);
            gamePrice = itemView.findViewById(R.id.gamePriceTv);
            gamesProgressBar=itemView.findViewById(R.id.gamePg);
        }
    }
}
