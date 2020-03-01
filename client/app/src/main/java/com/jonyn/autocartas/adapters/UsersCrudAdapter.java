package com.jonyn.autocartas.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jonyn.autocartas.R;
import com.jonyn.autocartas.modelos.User;

import java.util.List;

public class UsersCrudAdapter extends RecyclerView.Adapter<UsersCrudAdapter.UserCrudViewHolder> {

    private List<User> users;

    public UsersCrudAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserCrudViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_crud_user, parent, false);

        return new UserCrudViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCrudViewHolder holder, int position) {
        User u = users.get(position);
        holder.bindUser(u);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserCrudViewHolder extends RecyclerView.ViewHolder {

        private TextView tvCUser;
        private TextView tvCNombre;
        private TextView tvCPasswd;
        private TextView tvCWins;
        private TextView tvCTies;
        private TextView tvCLoses;

        UserCrudViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCUser = itemView.findViewById(R.id.tvCUser);
            tvCNombre = itemView.findViewById(R.id.tvCNombre);
            tvCPasswd = itemView.findViewById(R.id.tvCPasswd);
            tvCWins = itemView.findViewById(R.id.tvCWins);
            tvCTies = itemView.findViewById(R.id.tvCTies);
            tvCLoses = itemView.findViewById(R.id.tvCLoses);
        }

        void bindUser(User user) {
            tvCUser.setText(itemView.getContext().getString(R.string.crud_user, user.getUser()));
            tvCNombre.setText(itemView.getContext().getString(R.string.crud_name, user.getNombre()));
            tvCPasswd.setText(itemView.getContext().getString(R.string.crud_passwd, "*************"));
            tvCWins.setText(itemView.getContext().getString(R.string.wins_count, String.valueOf(user.getpGanadas())));
            tvCTies.setText(itemView.getContext().getString(R.string.ties_count, String.valueOf(user.getpEmpatadas())));
            tvCLoses.setText(itemView.getContext().getString(R.string.loses_count, String.valueOf(user.getpPerdidas())));
        }
    }
}

