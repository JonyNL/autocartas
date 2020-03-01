package com.jonyn.autocartas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonyn.autocartas.R;
import com.jonyn.autocartas.modelos.Coche;

import java.util.ArrayList;

public class CochesCrudAdapter extends RecyclerView.Adapter<CochesCrudAdapter.CocheCrudViewHolder> {

    private ArrayList<Coche> coches;
    private Context context;

    public CochesCrudAdapter(ArrayList<Coche> coches, Context context) {
        this.coches = coches;
        this.context = context;
    }

    @NonNull
    @Override
    public CochesCrudAdapter.CocheCrudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_crud_coche, parent,false);

        CochesCrudAdapter.CocheCrudViewHolder cvh = new CochesCrudAdapter.CocheCrudViewHolder(itemView, context);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull CochesCrudAdapter.CocheCrudViewHolder holder, int position) {
        Coche c = coches.get(position);
        holder.bindCoche(c);
    }

    @Override
    public int getItemCount() {
        return coches.size();
    }

    class CocheCrudViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView ivCoche;
        private TextView tvCId;
        private TextView tvCModelo;
        private TextView tvCPais;
        private TextView tvCMotor;
        private TextView tvCCilindros;
        private TextView tvCPotencia;
        private TextView tvCRevXmin;
        private TextView tvCVelocidad;
        private TextView tvCConsumo;

        CocheCrudViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            ivCoche = itemView.findViewById(R.id.ivCCoche);
            tvCId = itemView.findViewById(R.id.tvCId);
            tvCModelo = itemView.findViewById(R.id.tvCModelo);
            tvCPais = itemView.findViewById(R.id.tvCPais);
            tvCMotor = itemView.findViewById(R.id.tvCMotor);
            tvCCilindros = itemView.findViewById(R.id.tvCCilindros);
            tvCPotencia = itemView.findViewById(R.id.tvCPotencia);
            tvCRevXmin = itemView.findViewById(R.id.tvCRevXmin);
            tvCVelocidad = itemView.findViewById(R.id.tvCVelocidad);
            tvCConsumo = itemView.findViewById(R.id.tvCConsumo);
            this.context = context;
        }

        void bindCoche(Coche c) {
            int img = context.getResources().getIdentifier(
                    "_" + c.getId(), "drawable", context.getPackageName());
            if (img != 0) {
                ivCoche.setImageResource(img);
            } else ivCoche.setImageResource(R.drawable._rd);
            tvCId.setText(context.getString(R.string.coche_id, c.getId()));
            tvCModelo.setText(context.getString(R.string.coche_modelo, c.getModelo()));
            tvCPais.setText(context.getString(R.string.coche_pais, c.getPais()));
            tvCMotor.setText(context.getString(R.string.coche_motor, String.valueOf(c.getMotor())));
            tvCCilindros.setText(context.getString(R.string.coche_cilindros, String.valueOf(c.getCilindros())));
            tvCPotencia.setText(context.getString(R.string.coche_potencia, String.valueOf(c.getPotencia())));
            tvCRevXmin.setText(context.getString(R.string.coche_revxmin, String.valueOf(c.getRevXmin())));
            tvCVelocidad.setText(context.getString(R.string.coche_velocidad, String.valueOf(c.getVelocidad())));
            tvCConsumo.setText(context.getString(R.string.coche_consumo, String.valueOf(c.getConsAt100Km())));
        }
    }
}