package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCreateObj extends AppCompatActivity {

    private APIService mApiService;

    private MainActivity.crudSelection selection;

    private LinearLayout llNewUser;
    private ScrollView svNewCoche;

    private EditText etUser;
    private EditText etName;
    private EditText etPasswd;

    private EditText etIdCoche;
    private EditText etModelo;
    private EditText etPais;
    private EditText etMotor;
    private EditText etCilindros;
    private EditText etPotencia;
    private EditText etRevXmin;
    private EditText etVelocidad;
    private EditText etConsumo;
    private Button btnSaveObject;

    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_obj);

        mApiService = APIUtils.getAPIService();

        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(ActivityCrud.CRUDSELECTION);

        Toolbar toolbar = findViewById(R.id.toolbar);

        btnSaveObject = findViewById(R.id.btnSaveObject);
        svNewCoche = findViewById(R.id.svNewCoche);
        llNewUser = findViewById(R.id.llNewUser);

        if (selection == MainActivity.crudSelection.USERS) {
            toolbar.setTitle(getString(R.string.create_user).toUpperCase());
            etUser = findViewById(R.id.etUser);
            etName = findViewById(R.id.etName);
            etPasswd = findViewById(R.id.etPasswd);
            llNewUser.setVisibility(View.VISIBLE);
            btnSaveObject.setText(getString(R.string.save, getString(R.string.user)));
        } else {
            toolbar.setTitle(getString(R.string.create_coche).toUpperCase());
            etIdCoche = findViewById(R.id.etIdCoche);
            etModelo = findViewById(R.id.etModelo);
            etPais = findViewById(R.id.etPais);
            etMotor = findViewById(R.id.etMotor);
            etCilindros = findViewById(R.id.etCilindros);
            etPotencia = findViewById(R.id.etPotencia);
            etRevXmin = findViewById(R.id.etRevXmin);
            etVelocidad = findViewById(R.id.etVelocidad);
            etConsumo = findViewById(R.id.etConsumo);
            svNewCoche.setVisibility(View.VISIBLE);
            btnSaveObject.setText(getString(R.string.save, getString(R.string.coche)));
        }

        setSupportActionBar(toolbar);

        btnSaveObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection == MainActivity.crudSelection.USERS) {
                    createUser(etUser.getText().toString(), etName.getText().toString(),
                            etPasswd.getText().toString());
                } else {
                    createCoche(etIdCoche.getText().toString(),
                            etModelo.getText().toString(), etPais.getText().toString(),
                            Integer.parseInt(etMotor.getText().toString()),
                            Integer.parseInt(etCilindros.getText().toString()),
                            Integer.parseInt(etPotencia.getText().toString()),
                            Integer.parseInt(etRevXmin.getText().toString()),
                            Integer.parseInt(etVelocidad.getText().toString()),
                            Integer.parseInt(etConsumo.getText().toString()));
                }
            }
        });
    }

    /**
     * Metodo que crea un coche en la base de datos desde el API
     *
     * @param id: id del coche a crear
     * @param modelo: modelo del coche a crear
     * @param pais: pais del coche a crear
     * @param motor: motor del coche a crear
     * @param cilindros: cilindros del coche a crear
     * @param potencia: potencia del coche a crear
     * @param revxmin: revxmin del coche a crear
     * @param velocidad: velocidad del coche a crear
     * @param consAt100km: consAt100km del coche a crear
     * */
    private void createCoche(String id, String modelo, String pais, int motor, int cilindros,
                             int potencia, int revxmin, int velocidad, int consAt100km) {
        mApiService.createCoche(id, modelo, pais, motor, cilindros, potencia, revxmin, velocidad,
                consAt100km).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityCreateObj.this,
                            "Coche creado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 406) {
                    Toast.makeText(ActivityCreateObj.this,
                            "ID ya registrado en Base de Datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Error al crear coche en el API. " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que crea un usuario en la base de datos desde el API
     *
     * @param user: user del usuario a crear
     * @param nombre: nombre del usuario a crear
     * @param passwd: password del usuario a crear
     * */
    private void createUser(String user, String nombre, String passwd) {
        mApiService.createUser(user, nombre, passwd).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ActivityCreateObj.this,
                            "Usuario creado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (response.code() == 406) {
                    Toast.makeText(ActivityCreateObj.this,
                            "Usuario ya registrado en Base de Datos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Error al crear usuario en el API. " + t.getMessage());
            }
        });
    }
}
