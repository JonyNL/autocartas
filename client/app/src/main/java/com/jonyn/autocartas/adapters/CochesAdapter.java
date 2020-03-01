package com.jonyn.autocartas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonyn.autocartas.R;
import com.jonyn.autocartas.modelos.Coche;

import java.util.ArrayList;
import java.util.List;


public class CochesAdapter extends RecyclerView.Adapter<CochesAdapter.CocheViewHolder>
implements View.OnClickListener {

    private ArrayList<Coche> coches;
    private Context context;
    private View.OnClickListener listener;

    public CochesAdapter(ArrayList<Coche> coches, Context context) {
        this.coches = coches;
        this.context = context;
    }

    @NonNull
    @Override
    public CocheViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_cartas, parent,false);

        itemView.setOnClickListener(this);

        CocheViewHolder cvh = new CocheViewHolder(itemView, context);
        return cvh;
    }

    public void updateCoches(List<Coche> coches) {
        this.coches.clear();
        this.coches.addAll(coches);
        this.notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!= null)
            listener.onClick(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CocheViewHolder holder, int position) {
        Coche c = coches.get(position);
        holder.bindCoche(c);
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

    class CocheViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivCoche;
        private Context context;
        CocheViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            ivCoche = itemView.findViewById(R.id.ivCoche);
            this.context = context;
        }

        void bindCoche(Coche c){
            int img = context.getResources().getIdentifier(
                    "_" + c.getId(), "drawable", context.getPackageName());
            if (img != 0) {
                ivCoche.setImageResource(img);
            } else ivCoche.setImageResource(R.drawable._rd);
        }

    }
}
