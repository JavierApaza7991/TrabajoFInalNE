package com.example.javier.trabajofinalne;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ModificarZona extends AppCompatActivity {

    Spinner opciones_std;

    EditText text_codigo, text_nombre, text_estado;
    Button boton_modificar, boton_eliminar;

    String codigo, nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_zona);

        opciones_std = (Spinner) findViewById(R.id.sp01);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_std, android.R.layout.simple_spinner_item);
        opciones_std.setAdapter(adapter);

        //Se obtiene los datos enviados cuando se hace click en la lista.
        Bundle b = getIntent().getExtras();
        if (b != null) {
            codigo = b.getString("ID");
            nombre = b.getString("NOMBRE");
        }

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        //Colocar los campos enviados enel formulario de modificar
        text_codigo.setText(codigo);
        text_codigo.setEnabled(false);
        text_nombre.setText(nombre);

        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_eliminar = (Button) findViewById(R.id.boton_eliminar);

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarZona(text_codigo.getText().toString(), text_nombre.getText().toString());
                onBackPressed();
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarZona(codigo);
                onBackPressed();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void modificarZona (String id, String nombre) {
        BaseHelper helper = new BaseHelper(this, "Demo3", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "update ZONA set ID='" + id +"',NOMBRE='" + nombre + "' where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Modificación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void eliminarZona (String id) {
        BaseHelper helper = new BaseHelper(this, "Demo3", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "delete from ZONA where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Eliminación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
