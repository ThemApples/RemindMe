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
    private OnItemClickListener ll;

    public static class LocationViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView imageV;
        public TextView t1l;
        public TextView t2t;

        public LocationViewHolder(@NonNull View itemView, final OnItemClickListener listene) {
            super(itemView);

            imageV = itemView.findViewById(R.id.imageView);
            t1l = itemView.findViewById(R.id.line1);
            t2t = itemView.findViewById(R.id.line2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listene != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listene.onItemClick(position);
                        }
                    }
                }
            });
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
       LocationViewHolder lvh = new LocationViewHolder(v,ll);
       return  lvh;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationItem ci = oal.get(position);

        holder.imageV.setImageResource(ci.getmImageResource());
        holder.t1l.setText(ci.getTextTop());
        holder.t2t.setText(ci.getTextBottom());
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listen)
    {
        ll = listen;
    }


    @Override
    public int getItemCount() {
        return oal.size();
    }
}
