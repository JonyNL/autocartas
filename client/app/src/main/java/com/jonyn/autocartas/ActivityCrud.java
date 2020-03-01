package com.jonyn.autocartas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCrud extends AppCompatActivity {
    public static final String CRUDSELECTION = "com.jonyn.autocartas.CRUDSELECTION";

    private MainActivity.crudSelection selection;
    private Button btnGetObjects;
    private Button btnGetObject;
    private Button btnCreateObject;
    private Button btnEditObject;
    private Button btnDeleteObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        Toolbar toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        selection = (MainActivity.crudSelection) i.getSerializableExtra(CRUDSELECTION);

        toolbar.setTitle("CRUD " + selection.toString());
        setSupportActionBar(toolbar);

        btnGetObjects = findViewById(R.id.btnGetObjects);
        btnGetObject = findViewById(R.id.btnGetObject);
        btnCreateObject = findViewById(R.id.btnCreateObject);
        btnEditObject = findViewById(R.id.btnEditObject);
        btnDeleteObject = findViewById(R.id.btnDeleteObject);

        if (selection == MainActivity.crudSelection.CARS) {
            btnGetObjects.setText(R.string.coches_list);
            btnGetObject.setText(R.string.coche_info);
            btnCreateObject.setText(R.string.create_coche);
            btnEditObject.setText(R.string.edit_coche);
            btnDeleteObject.setText(R.string.delete_coche);
        } else {
            btnGetObjects.setText(R.string.users_list);
            btnGetObject.setText(R.string.user_info);
            btnCreateObject.setText(R.string.create_user);
            btnEditObject.setText(R.string.edit_user);
            btnDeleteObject.setText(R.string.delete_user);
        }
        btnGetObjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCrud.this, ActivityGetObjects.class);
                i.putExtra(CRUDSELECTION, selection);
                startActivity(i);
            }
        });
        btnGetObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCrud.this, ActivityGetObj.class);
                i.putExtra(CRUDSELECTION, selection);
                startActivity(i);
            }
        });

        btnCreateObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCrud.this, ActivityCreateObj.class);
                i.putExtra(CRUDSELECTION, selection);
                startActivity(i);
            }
        });

        btnEditObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCrud.this, ActivityEditObj.class);
                i.putExtra(CRUDSELECTION, selection);
                startActivity(i);
            }
        });

        btnDeleteObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivityCrud.this, ActivityDeleteObj.class);
                i.putExtra(CRUDSELECTION, selection);
                startActivity(i);
            }
        });
    }
}
