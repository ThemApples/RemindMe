package com.example.remindme;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private ArrayList<LocationItem> oal;

    public static class LocationViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageV;
        public TextView t1l;
        public TextView t2t;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            imageV = itemView.findViewById(R.id.imageView);
            t1l = itemView.findViewById(R.id.line1);
            t2t = itemView.findViewById(R.id.line2);
        }
    }

    public LocationAdapter(ArrayList<LocationItem> al)
    {
        this.oal = al;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations,parent,false);
       LocationViewHolder lvh = new LocationViewHolder(v);
       return  lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationItem ci = oal.get(position);

        holder.imageV.setImageResource(ci.getmImageResource());
        holder.t1l.setText(ci.getTextTop());
        holder.t2t.setText(ci.getTextBottom());
    }

    @Override
    public int getItemCount() {
        return oal.size();
    }
}
