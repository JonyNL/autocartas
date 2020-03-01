package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jonyn.autocartas.modelos.ResPartida;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SesionActivity extends AppCompatActivity {

    APIService mApiService;
    public static final String ID_SESION = "com.jonyn.autocartas.ID_SESION";
    public static final String USER = "com.jonyn.autocartas.USER";
    private String user;
    private String idSesion;
    private Button btnNewGame;
    private Button btnResetGame;
    private Button btnLogOut;
    public final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sesion);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Sesion");
        setSupportActionBar(toolbar);

        mApiService = APIUtils.getAPIService();

        Intent i = getIntent();
        user = i.getStringExtra(USER);
        idSesion = i.getStringExtra(ID_SESION);

        btnNewGame = findViewById(R.id.btnNewGame);
        btnResetGame = findViewById(R.id.btnReset);
        btnLogOut = findViewById(R.id.btnLogOut);

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewGame(idSesion);
            }
        });

        btnResetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getResetGame(idSesion);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(user, idSesion);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Si volvemos a la activity anterior pulsando el boton atras, cerramos la sesion
        logout(user, idSesion);
    }

    /**
     * Metodo que cierra la sesion en la API
     *
     * @param user: usuario de la sesion
     * @param idSesion: id de la sesion a cerrar
     * */
    private void logout(String user, String idSesion) {
        mApiService.logout(user, idSesion).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response logout OK: " + response.body());
                    Toast.makeText(SesionActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                    SesionActivity.this.finish();
                } else {
                    Log.i(TAG, "Response logout NOT OK: " + response.message());
                    Toast.makeText(SesionActivity.this, "API: Error en la sesion", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Error al intentar cerrar sesion en la API: "+t.getMessage());
                Toast.makeText(SesionActivity.this, "API: Error al cerrar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Metodo para reasignar una partida en la API
     *
     * @param idSesion: id de la sesion que resetea la sesion
     * */
    private void getResetGame(String idSesion) {
        mApiService.getResetGame(idSesion).enqueue(new Callback<ResPartida>() {
            @Override
            public void onResponse(Call<ResPartida> call, Response<ResPartida> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response resetGame OK: " + response.body().getIdSesion());
                    Toast.makeText(SesionActivity.this, "Partida restablecida correctamente", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SesionActivity.this, GamescreenActivity.class);
                    i.putExtra(GamescreenActivity.RESPARTIDA, response.body());
                    startActivity(i);
                } else {
                    Log.i(TAG, "Response resetGame NOT OK: " + response.message());
                    Toast.makeText(SesionActivity.this, "Error al restablecer partida", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResPartida> call, Throwable t) {
                Log.i(TAG, "API: Error al restablecer la partida: " + t.getMessage());
            }
        });
    }

    /**
     * Metodo para recibir una partida de la API
     *
     * @param idSesion: id de la sesion que recibe la partida
     * */
    private void getNewGame(String idSesion) {
        mApiService.getNewGame(idSesion).enqueue(new Callback<ResPartida>() {
            @Override
            public void onResponse(Call<ResPartida> call, Response<ResPartida> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "Response newGame OK: " + response.body().getIdSesion());
                    Intent i = new Intent(SesionActivity.this, GamescreenActivity.class);
                    i.putExtra(GamescreenActivity.RESPARTIDA, response.body());
                    startActivity(i);
                } else {
                    Log.i(TAG, "Response newGame NOT OK: " + response.message());
                    Toast.makeText(SesionActivity.this, "Error al crear nueva partida", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ResPartida> call, Throwable t) {
                Log.i(TAG, "Error al recibir partida: " + t.getMessage());
                Toast.makeText(SesionActivity.this, "Error al recibir partida en la API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
