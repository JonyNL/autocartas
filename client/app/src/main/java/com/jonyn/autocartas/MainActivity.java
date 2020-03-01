package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jonyn.autocartas.adapters.UsersAdapter;
import com.jonyn.autocartas.modelos.User;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * Enumerado para la gestion de los menus para los metodos CRUD de la base de datos
     * */
    public enum crudSelection {
        USERS, CARS;
    }

    private crudSelection selection;
    private APIService mApiService;
    private Button btnLogin;
    private Button btnStats;
    private EditText etUser;
    private EditText etPasswd;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Autocartas");

        mApiService = APIUtils.getAPIService();

        etUser = findViewById(R.id.etUser);
        etPasswd = findViewById(R.id.etPasswd);

        btnLogin = findViewById(R.id.btnLogin);
        btnStats = findViewById(R.id.btnStats);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = etUser.getText().toString();
                String passwd = etPasswd.getText().toString();
                if (user.length() == 0) {
                    Toast.makeText(MainActivity.this, "Campo user no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else if (passwd.length() == 0) {
                    Toast.makeText(MainActivity.this, "Campo password no puede estar vacio", Toast.LENGTH_SHORT).show();
                } else
                    login(user, passwd);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Metodo que nos loguea en la API
     *
     * @param user: usuario que va a hacer login
     * @param passwd: password del usuario
     * */
    private void login(final String user, final String passwd) {
        final String usr = user;
        mApiService.login(user, passwd).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Intent i = new Intent(MainActivity.this, SesionActivity.class);
                    i.putExtra(SesionActivity.USER, usr);
                    i.putExtra(SesionActivity.ID_SESION, response.body());
                    Toast.makeText(MainActivity.this, "Sesión abierta como: " + user, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                } else if (response.code() == 412) {
                    Toast.makeText(MainActivity.this, "Reabriendo sesión", Toast.LENGTH_SHORT).show();
                    login(user, passwd);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "login: Error al recibir login del API" + t.getMessage());
                Toast.makeText(MainActivity.this, "API: Error en login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.iCrudCoches:
                selection = crudSelection.CARS;
                i = new Intent(MainActivity.this, ActivityCrud.class);
                i.putExtra(ActivityCrud.CRUDSELECTION, selection);
                startActivity(i);
                return true;
            case R.id.iCrudUsers:
                selection = crudSelection.USERS;
                i = new Intent(MainActivity.this, ActivityCrud.class);
                i.putExtra(ActivityCrud.CRUDSELECTION, selection);
                startActivity(i);
                return true;
            case R.id.iUrl:
                i = new Intent(MainActivity.this, ActivityNewUrl.class);
                startActivity(i);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mApiService = APIUtils.getAPIService();
    }
}
