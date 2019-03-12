package com.example.sabin.kitesurfing;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.GetData;
import com.example.sabin.kitesurfing.service.RetrofitClient;
import com.example.sabin.kitesurfing.service.SpotFavoriteResult;
import com.example.sabin.kitesurfing.service.SpotId;
import com.example.sabin.kitesurfing.service.Spots;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpotRecycleAdapter extends RecyclerView.Adapter<SpotRecycleAdapter.ViewHolder> {

    private List<Spots.Result> spots;
    private Context context;
    private static String accesToken;

    public static String getAccesToken() {
        return accesToken;
    }

    public SpotRecycleAdapter(List<Spots.Result> spots, String accesToken) {
        this.spots = spots;
        this.accesToken = accesToken;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);

        String name = spots.get(position).getName();
        String country = spots.get(position).getCountry();
        holder.setNameCountry(name, country);

        final String spotId = spots.get(position).getId();
        final boolean isFavorite = spots.get(position).isFavorite();
        holder.setFavoriteIcon(isFavorite);


        holder.isFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetData service = RetrofitClient.getRetrofitInstance().create(GetData.class);

                if (spots.get(position).isFavorite() == false) {
                    // cand spotul nu e la favorite si se da click pe el
                    // => se baga la favorite

                    holder.setFavoriteIcon(true);
                    spots.get(position).setFavorite(true);

                    Call<SpotFavoriteResult> callAddToFavorites = service.addSpotToFavorites(accesToken, new SpotId(spotId));
                    callAddToFavorites.enqueue(new Callback<SpotFavoriteResult>() {
                        @Override
                        public void onResponse(Call<SpotFavoriteResult> call, Response<SpotFavoriteResult> response) {
                            Log.i("AddFavorite", "Succes");
                        }

                        @Override
                        public void onFailure(Call<SpotFavoriteResult> call, Throwable t) {
                            Log.i("AddFavorite", "Failure");
                        }
                    });
                } else {
                    //spotul e la favorite si se da click pe el
                    // => se sterge de la favorite

                    holder.setFavoriteIcon(false);
                    spots.get(position).setFavorite(false);

                    Call<SpotFavoriteResult> callRemoveFromFavorites = service.removeSpotFromFavorites(accesToken, new SpotId(spotId));
                    callRemoveFromFavorites.enqueue(new Callback<SpotFavoriteResult>() {
                        @Override
                        public void onResponse(Call<SpotFavoriteResult> call, Response<SpotFavoriteResult> response) {
                            Log.i("RemoveFavorite", "Succes");
                        }

                        @Override
                        public void onFailure(Call<SpotFavoriteResult> call, Throwable t) {
                            Log.i("RemoveFavorite", "Failure");
                        }
                    });
                }
                notifyDataSetChanged();
            }
        });







    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public static final String SPOT_ID = "spotId";
        public static final String IS_FAVORITE = "isFavorite";
        private View mView;
        private TextView nameView;
        private TextView countryView;
        private ImageView isFavoriteView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            nameView = mView.findViewById(R.id.spot_name);
            countryView = mView.findViewById(R.id.spot_country);
            isFavoriteView = mView.findViewById(R.id.spot_favorite);

            mView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //cand se apasa pe un item
            String spotId = spots.get(getAdapterPosition()).getId();
            boolean isFavorite = spots.get(getAdapterPosition()).isFavorite();
            //Toast.makeText(context, "APASAREEE" + spotId, Toast.LENGTH_SHORT).show();
            goToDetails(spotId, isFavorite);
        }

        public void setNameCountry(String name, String country) {
            nameView.setText(name);
            countryView.setText(country);
        }

        public void setFavoriteIcon(boolean isFavorite) {
            if (isFavorite) {
                isFavoriteView.setImageResource(R.mipmap.staron);
            } else {
                isFavoriteView.setImageResource(R.mipmap.staroff);
            }
        }

        public void goToDetails(String spotId, boolean isFavorite) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(SPOT_ID, spotId);
            intent.putExtra(IS_FAVORITE, isFavorite);
            context.startActivity(intent);
        }

    }
}
