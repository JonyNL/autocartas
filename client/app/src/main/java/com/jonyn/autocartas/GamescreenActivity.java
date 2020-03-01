package com.jonyn.autocartas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.fanlayoutmanager.FanLayoutManager;
import com.cleveroad.fanlayoutmanager.FanLayoutManagerSettings;
import com.jonyn.autocartas.Iiterfaces.ICocheSeleccionado;
import com.jonyn.autocartas.adapters.CochesAdapter;
import com.jonyn.autocartas.modelos.Coche;
import com.jonyn.autocartas.modelos.ResPartida;
import com.jonyn.autocartas.remote.APIService;
import com.jonyn.autocartas.remote.APIUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GamescreenActivity extends AppCompatActivity implements ICocheSeleccionado {

    private final Handler mHandler = new Handler();
    /**Runnable que gestiona el comportamiento de la interfaz del sistema*/
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideSystemUI();
        }
    };

    /**Runnable que gestiona la llamada a ready() para disponer de tiempo para ver el resultado de la ronda*/
    private final Runnable sendReady = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(GamescreenActivity.this, "Player 2 Ready", Toast.LENGTH_SHORT).show();
            ready(resPartida.getIdSesion());
        }
    };

    public static final String RESPARTIDA = "com.jonyn.autocartas.RESPARTIDA";
    private TextView tvWins;
    private TextView tvTies;
    private TextView tvLoses;
    private TextView tvFeature;
    private TextView tvRonda;
    private ImageView ivCocheP1;
    private ImageView ivCocheP2;

    private CochesAdapter adapter;
    private RecyclerView rvCoches;
    private FanLayoutManager fanLayoutManager;
    private FrameLayout frameLayout;
    private ArrayList<Coche> coches;
    private ResPartida resPartida;
    private String idCoche;
    private Button bClose;

    private int wins;
    private int ties;
    private int loses;
    private boolean p2Preparing;

    boolean firstPlayer;
    public final String TAG = this.getClass().getSimpleName();
    private APIService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        hideSystemUI();

        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            delayedHide();
                        }
                    }
                });


        mApiService = APIUtils.getAPIService();

        Intent i = getIntent();
        resPartida = (ResPartida) i.getSerializableExtra(RESPARTIDA);

        coches = resPartida.getManoP1();
        wins = 0;
        ties = 0;
        loses = 0;
        p2Preparing = false;

        tvWins = findViewById(R.id.tvWins);
        tvTies = findViewById(R.id.tvTies);
        tvLoses = findViewById(R.id.tvLoses);
        tvRonda = findViewById(R.id.tvRonda);
        tvFeature = findViewById(R.id.tvFeature);
        ivCocheP1 = findViewById(R.id.ivCocheP1);
        ivCocheP2 = findViewById(R.id.ivCocheP2);
        bClose = findViewById(R.id.bClose);

        tvWins.setText(getString(R.string.wins_count, String.valueOf(resPartida.getvPlayer())));
        tvTies.setText(getString(R.string.ties_count, String.valueOf(resPartida.getEmpates())));
        tvLoses.setText(getString(R.string.loses_count, String.valueOf(resPartida.getvCpu())));
        tvRonda.setText(getString(R.string.round, String.valueOf(resPartida.getRonda())));
        String feature = resPartida.getCaracteristica();
        if (feature != null)
            tvFeature.setText(feature);

        String idCocheP2 = resPartida.getPlayP2id();
        if (idCocheP2 != null) {
            int img = this.getResources().getIdentifier(
                    "_" + idCocheP2, "drawable", this.getPackageName());
            if (img != 0) {
                ivCocheP2.setImageResource(img);
            } else ivCocheP2.setImageResource(R.drawable._rd);
        }
        rvCoches = findViewById(R.id.rvCoches);
        adapter = new CochesAdapter(coches, this);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fanLayoutManager.getSelectedItemPosition() == rvCoches.getChildAdapterPosition(v))
                    onCocheSeleccionado(coches.get(rvCoches.getChildAdapterPosition(v)));
                else fanLayoutManager.switchItem(rvCoches, rvCoches.getChildAdapterPosition(v));
            }
        });

        rvCoches.setAdapter(adapter);
        fanLayoutManager = new FanLayoutManager(this,
                FanLayoutManagerSettings.newBuilder(this)
                        .withFanRadius(true)
                        .build());
        rvCoches.setLayoutManager(fanLayoutManager);

        frameLayout = findViewById(R.id.flFullscreenLayout);

        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSystemUI();
            }
        });
    }

    @Override
    public void onCocheSeleccionado(Coche c) {
        idCoche = c.getId();
        if (resPartida.isPlayerFirst())
            showAlertDialog();
        else {
            if (!p2Preparing)
                playCard(resPartida.getIdSesion(), idCoche, resPartida.getCaracteristica());
        }
    }

    /**
     * Metodo que selecciona al azar al usuario que empieza el juego (no utilizado)
     *
     * @param idSesion: id de la sesion para seleccionar
     * */
    private void raffle(String idSesion) {
        mApiService.makeRaffle(idSesion).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    if (Integer.parseInt(response.body()) == 0)
                        firstPlayer = true;
                    else {
                        firstPlayer = false;
                        ready(GamescreenActivity.this.resPartida.getIdSesion());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "Error al realizar raffle: " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que llama al API para avisar que el usario esta listo
     *
     * @param idSesion: id de la sesion que avisa
     * */
    private void ready(String idSesion) {
        mApiService.ready(idSesion).enqueue(new Callback<ResPartida>() {
            @Override
            public void onResponse(Call<ResPartida> call, Response<ResPartida> response) {
                if (response.isSuccessful()) {
                    resPartida = response.body();
                    tvFeature.setText(resPartida.getCaracteristica());
                    ivCocheP2.setImageResource(R.drawable._rd);
                    p2Preparing = false;
                }
            }

            @Override
            public void onFailure(Call<ResPartida> call, Throwable t) {
                Log.i(TAG, "Error en el metodo ready en API: " + t.getMessage());
            }
        });
    }

    /**
     * Metodo que llama al API para realizar una jugada
     *
     * @param idSesion: id de la sesion que realiza la jugada
     * @param idCoche: id del coche que juega el usuario
     * @param caracteristica: caracteristica a la que se juega la ronda
     * */
    private void playCard(String idSesion, String idCoche, String caracteristica) {
        tvFeature.setText(caracteristica);
        mApiService.playCard(idSesion, idCoche, caracteristica).enqueue(new Callback<ResPartida>() {
            @Override
            public void onResponse(Call<ResPartida> call, Response<ResPartida> response) {
                if (response.isSuccessful()) {
                    resPartida = response.body();
                    coches = resPartida.getManoP1();
                    adapter.updateCoches(coches);
                    fanLayoutManager.onItemsRemoved(rvCoches, 0, coches.size());
                    if (resPartida.getvPlayer() > wins) {
                        wins++;
                        tvFeature.setText(R.string.you_win);
                    } else if (resPartida.getvCpu() > loses) {
                        loses++;
                        tvFeature.setText(R.string.you_lose);
                    } else {
                        ties++;
                        tvFeature.setText(R.string.you_got_tie);
                    }
                    tvWins.setText(getString(R.string.wins_count, String.valueOf(wins)));
                    tvTies.setText(getString(R.string.ties_count, String.valueOf(ties)));
                    tvLoses.setText(getString(R.string.loses_count, String.valueOf(loses)));
                    int imgRes = GamescreenActivity.this.getResources().getIdentifier(
                            "_" + resPartida.getPlayP1id(), "drawable", GamescreenActivity.this.getPackageName());
                    ivCocheP1.setImageResource(imgRes);
                    imgRes = GamescreenActivity.this.getResources().getIdentifier(
                            "_" + resPartida.getPlayP2id(), "drawable", GamescreenActivity.this.getPackageName());
                    ivCocheP2.setImageResource(imgRes);
                    if (resPartida.getRonda() <= 6) {
                        tvRonda.setText(getString(R.string.round, String.valueOf(resPartida.getRonda())));
                        if (!resPartida.isPlayerFirst()) {
                            Toast.makeText(GamescreenActivity.this, "Player 2 Starts", Toast.LENGTH_SHORT).show();
                            p2Preparing = true;
                            delayedReady();
                        } else {
                            Toast.makeText(GamescreenActivity.this, "Player 1 Starts", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        frameLayout.removeView(rvCoches);
                        bClose.setVisibility(View.VISIBLE);
                        if (wins > loses)
                            tvRonda.setText(R.string.you_win);
                        else if (wins < loses)
                            tvRonda.setText(R.string.you_lose);
                        else
                            tvRonda.setText(R.string.you_got_tie);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResPartida> call, Throwable t) {
                Log.i(TAG, "Error al jugar carta: " + t.getMessage());
            }
        });
    }

    /**
     * Muestra un dialogo para seleccionar la caracteristica a jugar cuando le toca al jugador
     * */
    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(GamescreenActivity.this);
        alertDialog.setTitle("Categoria");
        final String[] items = {"MOTOR", "CILINDROS", "POTENCIA", "REVXMIN", "VELOCIDAD", "CONSUMO/100km"};
        int checkedItem = 1;

        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        playCard(resPartida.getIdSesion(), idCoche, items[0]);
                        dialog.dismiss();
                        break;
                    case 1:
                        playCard(resPartida.getIdSesion(), idCoche, items[1]);
                        dialog.dismiss();
                        break;
                    case 2:
                        playCard(resPartida.getIdSesion(), idCoche, items[2]);
                        dialog.dismiss();
                        break;
                    case 3:
                        playCard(resPartida.getIdSesion(), idCoche, items[3]);
                        dialog.dismiss();
                        break;
                    case 4:
                        playCard(resPartida.getIdSesion(), idCoche, items[4]);
                        dialog.dismiss();
                        break;
                    case 5:
                        playCard(resPartida.getIdSesion(), idCoche, "COSTE100KM");
                        dialog.dismiss();
                        break;
                }
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }

    /**
     * Metodo para ocultar la UI del dispositivo
     *
     * */
    private void hideSystemUI() {
        int uiOptions;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    // Hide the nav bar and status bar
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    /**
     * Ejecuta el Runnable que oculta la UI con un delay de 2000 milisegundos
     *
     * */
    private void delayedHide() {
        mHandler.removeCallbacks(mHideRunnable);
        mHandler.postDelayed(mHideRunnable, 2000);
    }

    /**
     * Ejecuta el Runnable que llama a la API a traves de ready() con un delay de 2000 milisegundos
     *
     * */
    private void delayedReady() {
        mHandler.removeCallbacks(sendReady);
        mHandler.postDelayed(sendReady, 8000);
        Toast.makeText(this, "Player 2 preparing...", Toast.LENGTH_SHORT).show();
    }
}
