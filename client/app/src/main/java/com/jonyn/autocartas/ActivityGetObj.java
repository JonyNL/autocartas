package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jonyn.autocartas.modelos.Coche;
import com.jonyn.autocartas.modelos.User;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityGetObj extends AppCompatActivity {

    private APIService mApiService;
    private MainActivity.crudSelection selection;

    private LinearLayout llCoche;
    private LinearLayout llUser;

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

    private TextView tvCUser;
    private TextView tvCNombre;
    private TextView tvCPasswd;
    private TextView tvCWins;
    private TextView tvCTies;
    private TextView tvCLoses;

    private TextView tvSelect;
    private Spinner spObjects;

    List<String> usrs;
    List<String> coches;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_obj);

        mApiService = APIUtils.getAPIService();

        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(ActivityCrud.CRUDSELECTION);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(selection.toString());
        setSupportActionBar(toolbar);

        spObjects = findViewById(R.id.spObjects);
        tvSelect = findViewById(R.id.tvSelect);

        // dependiendo de sobre que tabla se realice la consula, buscan unos campos u otros
        if (selection == MainActivity.crudSelection.USERS) {
            tvSelect.setText("User: ");
            tvCUser = findViewById(R.id.tvCUser);
            tvCNombre = findViewById(R.id.tvCNombre);
            tvCPasswd = findViewById(R.id.tvCPasswd);
            tvCWins = findViewById(R.id.tvCWins);
            tvCTies = findViewById(R.id.tvCTies);
            tvCLoses = findViewById(R.id.tvCLoses);
            llUser = findViewById(R.id.llUser);
            usrs = new ArrayList<>();
            readUsers();
        } else {
            tvSelect.setText("ID Coche: ");
            ivCoche = findViewById(R.id.ivCCoche);
            tvCId = findViewById(R.id.tvCId);
            tvCModelo = findViewById(R.id.tvCModelo);
            tvCPais = findViewById(R.id.tvCPais);
            tvCMotor = findViewById(R.id.tvCMotor);
            tvCCilindros = findViewById(R.id.tvCCilindros);
            tvCPotencia = findViewById(R.id.tvCPotencia);
            tvCRevXmin = findViewById(R.id.tvCRevXmin);
            tvCVelocidad = findViewById(R.id.tvCVelocidad);
            tvCConsumo = findViewById(R.id.tvCConsumo);
            llCoche = findViewById(R.id.llCoche);
            coches = new ArrayList<>();
            readCoches();
        }

        spObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // dependiendo de sobre que tabla se realice la consula, se busca un coche o un usuario
                if (selection == MainActivity.crudSelection.USERS) {
                    readUser(usrs.get(position));
                } else {
                    readCoche(coches.get(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                            ActivityGetObj.this, android.R.layout.simple_spinner_item, usrs);

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
     * Metodo que recibe un usuario del API y lo muestra en pantalla
     *
     * @param user: usuario a mostrar
     * */
    private void readUser(String user) {
        mApiService.readUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    tvCUser.setText(getString(R.string.crud_user, response.body().getUser()));
                    tvCNombre.setText(getString(R.string.crud_name, response.body().getNombre()));
                    tvCPasswd.setText(getString(R.string.crud_passwd, response.body().getPasswd()));
                    tvCWins.setText(getString(R.string.wins_count, String.valueOf(response.body().getpGanadas())));
                    tvCTies.setText(getString(R.string.ties_count, String.valueOf(response.body().getpEmpatadas())));
                    tvCLoses.setText(getString(R.string.loses_count, String.valueOf(response.body().getpPerdidas())));
                    llUser.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "Error al leer usuario del API. " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que recibe listado de usuarios del API y los asigna a una lista
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
                            ActivityGetObj.this, android.R.layout.simple_spinner_item, coches);

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

    /**
     * Metodo que recibe un coche del API y lo muestra en pantalla
     *
     * @param idCoche: id del coche a buscar
     * */
    private void readCoche(String idCoche) {
        mApiService.readCoche(idCoche).enqueue(new Callback<Coche>() {
            @Override
            public void onResponse(Call<Coche> call, Response<Coche> response) {
                if (response.isSuccessful()) {
                    Coche c = response.body();
                    int img = getResources().getIdentifier(
                            "_" + c.getId(), "drawable", getPackageName());
                    if (img != 0) {
                        ivCoche.setImageResource(img);
                    } else ivCoche.setImageResource(R.drawable._rd);
                    tvCId.setText(getString(R.string.coche_id, c.getId()));
                    tvCModelo.setText(getString(R.string.coche_modelo, c.getModelo()));
                    tvCPais.setText(getString(R.string.coche_pais, c.getPais()));
                    tvCMotor.setText(getString(R.string.coche_motor, String.valueOf(c.getMotor())));
                    tvCCilindros.setText(getString(R.string.coche_cilindros, String.valueOf(c.getCilindros())));
                    tvCPotencia.setText(getString(R.string.coche_potencia, String.valueOf(c.getPotencia())));
                    tvCRevXmin.setText(getString(R.string.coche_revxmin, String.valueOf(c.getRevXmin())));
                    tvCVelocidad.setText(getString(R.string.coche_velocidad, String.valueOf(c.getVelocidad())));
                    tvCConsumo.setText(getString(R.string.coche_consumo, String.valueOf(c.getConsAt100Km())));
                    llCoche.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Coche> call, Throwable t) {
                Log.i(TAG, "Error al leer coche del API. " + t.getMessage());
            }
        });
    }
}
