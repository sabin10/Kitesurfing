package com.example.sabin.kitesurfing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sabin.kitesurfing.service.Spots;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpotRecycleAdapter extends RecyclerView.Adapter<SpotRecycleAdapter.ViewHolder> {

    private List<Spots.Result> spots;
    private Context context;

    public SpotRecycleAdapter(List<Spots.Result> spots) {
        this.spots = spots;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.spot_item, parent, false);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        String name = spots.get(position).getName();
        String country = spots.get(position).getCountry();
        boolean isFavorite = spots.get(position).isFavorite();

        holder.setNameCountry(name, country);

        holder.isFavoriteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });



    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        }

        public void setNameCountry(String name, String country) {
            nameView.setText(name);
            countryView.setText(country);
        }
    }
}
