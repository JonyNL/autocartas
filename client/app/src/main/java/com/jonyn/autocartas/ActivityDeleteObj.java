package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jonyn.autocartas.modelos.Coche;
import com.jonyn.autocartas.modelos.User;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityDeleteObj extends AppCompatActivity {

    private APIService mApiService;
    private MainActivity.crudSelection selection;

    private TextView tvSelect;
    private Spinner spObjects;
    private Button btnDeleteObj;

    private List<String> usrs;
    private List<String> coches;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        mApiService = APIUtils.getAPIService();

        Toolbar toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(ActivityCrud.CRUDSELECTION);

        spObjects = findViewById(R.id.spObjects);
        tvSelect = findViewById(R.id.tvSelect);
        btnDeleteObj = findViewById(R.id.btnDeleteObj);

        usrs = new ArrayList<>();
        coches = new ArrayList<>();

        if (selection == MainActivity.crudSelection.USERS) {
            toolbar.setTitle(getString(R.string.delete_user).toUpperCase());
            btnDeleteObj.setText(getString(R.string.delete_user));
            tvSelect.setText("User: ");
            readUsers();
        } else {
            toolbar.setTitle(getString(R.string.delete_coche).toUpperCase());
            btnDeleteObj.setText(getString(R.string.delete_coche));
            tvSelect.setText("ID Coche: ");
            readCoches();
        }
        setSupportActionBar(toolbar);

        btnDeleteObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection == MainActivity.crudSelection.USERS) {
                    Log.i(TAG, (String) spObjects.getSelectedItem());
                    deleteUser((String) spObjects.getSelectedItem());
                } else {
                    deleteCoche((String) spObjects.getSelectedItem());
                }
            }
        });
    }

    /**
     * Metodo que borra un coche de la base de datos desde el API
     *
     * @param idCoche: id del coche a borrar
     * */
    private void deleteCoche(String idCoche) {
        mApiService.deleteCoche(idCoche).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityDeleteObj.this,
                            "Coche eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityDeleteObj.this,
                            "Error al eliminar coche del API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Metodo que borra un usuario de la base de datos desde el API
     *
     * @param user: usuario a borrar
     * */
    private void deleteUser(String user) {
        mApiService.deleteUser(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityDeleteObj.this,
                            "Usuario eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ActivityDeleteObj.this,
                            "Error al eliminar usuario del API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
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
                    for (User usr : response.body()) {
                        usrs.add(usr.getUser());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ActivityDeleteObj.this, android.R.layout.simple_spinner_item, usrs);
                    spObjects.setAdapter(adapter);
                    Log.i(TAG, response.body().toString());
                }
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
                    for (Coche c : response.body()) {
                        coches.add(c.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ActivityDeleteObj.this, android.R.layout.simple_spinner_item, coches);

                    spObjects.setAdapter(adapter);

                    Log.i(TAG, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Coche>> call, Throwable t) {
                Log.i(TAG, "Error al leer coches del API. " + t.getMessage());
            }
        });
    }

}
