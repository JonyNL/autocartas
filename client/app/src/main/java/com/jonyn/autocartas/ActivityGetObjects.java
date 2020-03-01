package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.jonyn.autocartas.adapters.CochesCrudAdapter;
import com.jonyn.autocartas.adapters.UsersCrudAdapter;
import com.jonyn.autocartas.modelos.Coche;
import com.jonyn.autocartas.modelos.User;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityGetObjects extends AppCompatActivity {

    private RecyclerView rvListObjects;
    private CochesCrudAdapter adapterCoches;
    private UsersCrudAdapter adapterUsers;

    private APIService mApiService;
    private MainActivity.crudSelection selection;
    private List objects;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_objects);

        mApiService = APIUtils.getAPIService();

        // Accedes a la tabla sobre la que se hace la consulta desde los Extras del Intent
        // y la asignas a una variable local
        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(ActivityCrud.CRUDSELECTION);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(selection.toString());



        rvListObjects = findViewById(R.id.rvListObjects);
        rvListObjects.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));

        // Comprueba si la llamada va a ser sobre la tabla Users o sobre la tabla Coches
        if (selection == MainActivity.crudSelection.USERS){
            objects = new ArrayList<User>();
            adapterUsers = new UsersCrudAdapter(objects);
            rvListObjects.setAdapter(adapterUsers);
            readUsers();
        } else {
            objects = new ArrayList<Coche>();
            adapterCoches = new CochesCrudAdapter((ArrayList<Coche>) objects, this);
            rvListObjects.setAdapter(adapterCoches);
            readCoches();
        }


    }

    /**
     * Metodo que recibe listado de usuarios del API y los asigna a una lista
     *
     * */
    private void readUsers() {
        Log.i(TAG, "readUsers:called");
        mApiService.readUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    objects.clear();
                    objects.addAll(response.body());

                    Toast.makeText(ActivityGetObjects.this, "Todo OK", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, response.body().toString());
                    adapterUsers.notifyDataSetChanged();
                } else Toast.makeText(ActivityGetObjects.this, "NOT OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i(TAG, "Error al leer usuarios del API. " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que recibe listado de coches del API y los asigna a una lista
     *
     * */
    private void readCoches() {
        Log.i(TAG, "readUsers:called");
        mApiService.readCoches().enqueue(new Callback<List<Coche>>() {
            @Override
            public void onResponse(Call<List<Coche>> call, Response<List<Coche>> response) {
                if (response.isSuccessful()) {
                    objects.clear();
                    objects.addAll(response.body());

                    Toast.makeText(ActivityGetObjects.this, "Todo OK", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, response.body().toString());
                    adapterCoches.notifyDataSetChanged();
                } else Toast.makeText(ActivityGetObjects.this, "NOT OK", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Coche>> call, Throwable t) {
                Log.i(TAG, "Error al leer usuarios del API. " + t.getMessage());
            }
        });
    }
}
