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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> users;

    public UsersAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem_user, parent, false);

        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User u = users.get(position);
        holder.bindUser(u);

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUser;
        private TextView tvWins;
        private TextView tvTies;
        private TextView tvLoses;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);
            tvWins = itemView.findViewById(R.id.tvWins);
            tvTies = itemView.findViewById(R.id.tvTies);
            tvLoses = itemView.findViewById(R.id.tvLoses);
        }

        void bindUser(User user) {
            tvUser.setText(user.getUser());
            tvWins.setText(String.valueOf(user.getpGanadas()));
            tvTies.setText(String.valueOf(user.getpEmpatadas()));
            tvLoses.setText(String.valueOf(user.getpPerdidas()));
        }

    }
}
