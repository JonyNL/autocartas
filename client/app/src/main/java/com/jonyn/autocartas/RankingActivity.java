package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.jonyn.autocartas.adapters.UsersAdapter;
import com.jonyn.autocartas.modelos.User;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RankingActivity extends AppCompatActivity {
    private APIService mApiService;
    private RecyclerView rvRanking;
    private UsersAdapter adapter;
    private List<User> users;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ranking");
        setSupportActionBar(toolbar);

        mApiService = APIUtils.getAPIService();

        users = new ArrayList<>();

        rvRanking = findViewById(R.id.rvRanking);
        rvRanking.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        adapter = new UsersAdapter(users);
        rvRanking.setAdapter(adapter);

        getStats();
    }

    /**
     * Metodo que llama al API para recibir los usuarios ordenados por estadisticas
     * */
    private void getStats() {
        mApiService.getStats().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                Log.i(TAG, "got Response");
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response OK");
                    users.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else Log.i(TAG, "Response NOT OK");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(TAG, "Error al recibir el ranking del API." + t.getMessage());
            }
        });
    }
}
