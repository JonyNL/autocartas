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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

public class ActivityEditObj extends AppCompatActivity {

    private APIService mApiService;

    private MainActivity.crudSelection selection;

    private TextView tvSelect;
    private Spinner spObjects;

    private LinearLayout llEditUser;
    private ScrollView svEditCoche;

    private String[] camposUser, camposCoche;
    private EditText etUser;
    private EditText etName;
    private EditText etPasswd;
    private EditText etWins;
    private EditText etTies;
    private EditText etLoses;

    private List<String> usrs;
    private List<String> coches;

    private EditText etIdCoche;
    private EditText etModelo;
    private EditText etPais;
    private EditText etMotor;
    private EditText etCilindros;
    private EditText etPotencia;
    private EditText etRevXmin;
    private EditText etVelocidad;
    private EditText etConsumo;
    private Button btnEditObject;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_obj);

        mApiService = APIUtils.getAPIService();

        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(ActivityCrud.CRUDSELECTION);

        Toolbar toolbar = findViewById(R.id.toolbar);

        spObjects = findViewById(R.id.spObjects);
        tvSelect = findViewById(R.id.tvSelect);
        btnEditObject = findViewById(R.id.btnEditObject);
        svEditCoche = findViewById(R.id.svEditCoche);
        llEditUser = findViewById(R.id.llEditUser);

        if (selection == MainActivity.crudSelection.USERS) {
            tvSelect.setText("User: ");
            toolbar.setTitle(getString(R.string.edit_user).toUpperCase());
            etUser = findViewById(R.id.etUser);
            etName = findViewById(R.id.etName);
            etPasswd = findViewById(R.id.etPasswd);
            etWins = findViewById(R.id.etWins);
            etTies = findViewById(R.id.etTies);
            etLoses = findViewById(R.id.etLoses);
            btnEditObject.setText(getString(R.string.edit_user));
            usrs = new ArrayList<>();
            readUsers();
        } else {
            tvSelect.setText("ID Coche: ");
            toolbar.setTitle(getString(R.string.edit_coche).toUpperCase());
            etIdCoche = findViewById(R.id.etIdCoche);
            etModelo = findViewById(R.id.etModelo);
            etPais = findViewById(R.id.etPais);
            etMotor = findViewById(R.id.etMotor);
            etCilindros = findViewById(R.id.etCilindros);
            etPotencia = findViewById(R.id.etPotencia);
            etRevXmin = findViewById(R.id.etRevXmin);
            etVelocidad = findViewById(R.id.etVelocidad);
            etConsumo = findViewById(R.id.etConsumo);
            btnEditObject.setText(getString(R.string.edit_coche));
            coches = new ArrayList<>();
            readCoches();
        }

        setSupportActionBar(toolbar);

        btnEditObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //en caso de haber seleccionado un usuario, se comprueba los campos editados y
                // se llama al API para realizar las ediciones correspondientes
                if (selection == MainActivity.crudSelection.USERS) {
                    if (etName.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "nombre", etName.getText().toString());
                        etName.setText("");
                    }
                    if (etWins.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "pGanadas", etWins.getText().toString());
                        etWins.setText("");
                    }
                    if (etTies.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "pEmpatadas", etTies.getText().toString());
                        etTies.setText("");
                    }
                    if (etLoses.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "pPerdidas", etLoses.getText().toString());
                        etLoses.setText("");
                    }
                    if (etPasswd.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "passwd", etPasswd.getText().toString());
                        etPasswd.setText("");
                    }
                    if (etUser.getText().toString().length() > 0) {
                        editUser((String) spObjects.getSelectedItem(), "user", etUser.getText().toString());
                        etUser.setText("");
                    }
                } else {
                    //en caso contrario, se hace lo propio sobre los campos de coche
                    if (etModelo.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "modelo", etModelo.getText().toString());
                        etModelo.setText("");
                    }
                    if (etPais.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "pais", etPais.getText().toString());
                        etPais.setText("");
                    }
                    if (etMotor.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "motor", etMotor.getText().toString());
                        etMotor.setText("");
                    }
                    if (etCilindros.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "cilindros", etCilindros.getText().toString());
                        etCilindros.setText("");
                    }
                    if (etPotencia.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "potencia", etPotencia.getText().toString());
                        etPotencia.setText("");
                    }
                    if (etRevXmin.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "revxmin", etRevXmin.getText().toString());
                        etRevXmin.setText("");
                    }
                    if (etVelocidad.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "velocidad", etVelocidad.getText().toString());
                        etVelocidad.setText("");
                    }
                    if (etConsumo.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "consAt100km", etConsumo.getText().toString());
                        etConsumo.setText("");
                    }
                    if (etIdCoche.getText().toString().length() > 0) {
                        editCoche((String) spObjects.getSelectedItem(), "id", etIdCoche.getText().toString());
                        etIdCoche.setText("");
                    }

                }
            }
        });

        spObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
     * Metodo que envia una peticion de editar un coche al API
     *
     * @param idCoche:  id del coche que se va a editar
     * @param attr:     atributo o campo del coche a editar
     * @param newvalue: nuevo valor a asignar al campo
     */
    private void editCoche(String idCoche, String attr, String newvalue) {
        final String attrib = attr;
        mApiService.editCoche(idCoche, attr, newvalue).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityEditObj.this,
                            "Campo " + attrib + " editado correctamente", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 406) {
                    Toast.makeText(ActivityEditObj.this,
                            "El campo " + attrib + " es unico y el valor ya está asignado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ActivityEditObj.this,
                            "No se pudo editar el campo " + attrib, Toast.LENGTH_SHORT).show();

                if (attrib.equals("id")) {
                    Toast.makeText(ActivityEditObj.this, "Campos editados.", Toast.LENGTH_SHORT).show();
                    readCoches();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Metodo que envia una peticion de editar un usuario al API
     *
     * @param user:     usuario que se va a editar
     * @param attr:     atributo o campo del usuario a editar
     * @param newvalue: nuevo valor a asignar al campo
     */
    private void editUser(String user, final String attr, String newvalue) {
        final String attrib = attr;
        mApiService.editUser(user, attr, newvalue).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityEditObj.this,
                            "Campo " + attrib + " editado correctamente", Toast.LENGTH_SHORT).show();
                } else if (response.code() == 406) {
                    Toast.makeText(ActivityEditObj.this,
                            "El campo " + attrib + " es unico y el valor ya está asignado", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(ActivityEditObj.this,
                            "No se pudo editar el campo " + attrib, Toast.LENGTH_SHORT).show();

                if (attrib.equals("user")) {
                    Toast.makeText(ActivityEditObj.this, "Campos editados.", Toast.LENGTH_SHORT).show();
                    readUsers();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    /**
     * Metodo que recibe listado de usuarios del API y los asigna a una lista
     */
    private void readUsers() {
        Log.i(TAG, "readUsers:called");
        mApiService.readUsers().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    usrs.clear();
                    for (User usr : response.body()) {
                        usrs.add(usr.getUser());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ActivityEditObj.this, android.R.layout.simple_spinner_item, usrs);

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
     */
    private void readUser(String user) {
        mApiService.readUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    etUser.setHint(getString(R.string.crud_user, response.body().getUser()));
                    etName.setHint(getString(R.string.crud_name, response.body().getNombre()));
                    etPasswd.setHint(getString(R.string.crud_passwd, response.body().getPasswd()));
                    etWins.setHint(getString(R.string.wins_count, String.valueOf(response.body().getpGanadas())));
                    etTies.setHint(getString(R.string.ties_count, String.valueOf(response.body().getpEmpatadas())));
                    etLoses.setHint(getString(R.string.loses_count, String.valueOf(response.body().getpPerdidas())));
                    llEditUser.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i(TAG, "Error al leer usuario del API. " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que recibe listado de coches del API y los asigna a una lista
     */
    private void readCoches() {
        Log.i(TAG, "readUsers:called");
        mApiService.readCoches().enqueue(new Callback<List<Coche>>() {
            @Override
            public void onResponse(Call<List<Coche>> call, Response<List<Coche>> response) {
                if (response.isSuccessful()) {
                    coches.clear();
                    for (Coche c : response.body()) {
                        coches.add(c.getId());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            ActivityEditObj.this, android.R.layout.simple_spinner_item, coches);

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
     */
    private void readCoche(String idCoche) {
        mApiService.readCoche(idCoche).enqueue(new Callback<Coche>() {
            @Override
            public void onResponse(Call<Coche> call, Response<Coche> response) {
                if (response.isSuccessful()) {
                    Coche c = response.body();
                    etIdCoche.setHint(getString(R.string.coche_id, c.getId()));
                    etModelo.setHint(getString(R.string.coche_modelo, c.getModelo()));
                    etPais.setHint(getString(R.string.coche_pais, c.getPais()));
                    etMotor.setHint(getString(R.string.coche_motor, String.valueOf(c.getMotor())));
                    etCilindros.setHint(getString(R.string.coche_cilindros, String.valueOf(c.getCilindros())));
                    etPotencia.setHint(getString(R.string.coche_potencia, String.valueOf(c.getPotencia())));
                    etRevXmin.setHint(getString(R.string.coche_revxmin, String.valueOf(c.getRevXmin())));
                    etVelocidad.setHint(getString(R.string.coche_velocidad, String.valueOf(c.getVelocidad())));
                    etConsumo.setHint(getString(R.string.coche_consumo, String.valueOf(c.getConsAt100Km())));
                    svEditCoche.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Coche> call, Throwable t) {
                Log.i(TAG, "Error al leer coche del API. " + t.getMessage());
            }
        });
    }
}
