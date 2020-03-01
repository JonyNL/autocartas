package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jonyn.autocartas.remote.APIUtils;
/**
 * Asigna una nueva URL al Servicio del API (Activity Actualmente no funcional)
 *
 *
 * */
public class ActivityNewUrl extends AppCompatActivity {

    private EditText etIp;
    private EditText etPort;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_url);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("NEW URL");
        setSupportActionBar(toolbar);

        etIp = findViewById(R.id.etIp);
        etPort = findViewById(R.id.etPort);

        fab = findViewById(R.id.fabChangeUrl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIUtils.setIp(etIp.getText().toString());
                APIUtils.setPort(etPort.getText().toString());
                if (APIUtils.removeInstance()) {
                    Toast.makeText(ActivityNewUrl.this, "URL modificada", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }
}
