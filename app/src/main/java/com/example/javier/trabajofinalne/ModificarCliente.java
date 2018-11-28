package com.example.javier.trabajofinalne;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ModificarCliente extends AppCompatActivity {

    EditText text_codigo, text_nombre, text_ruc, text_zona, text_tipodecliente, text_estado;
    Button boton_modificar, boton_eliminar;

    String codigo, nombre, ruc, zona, tipoCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cliente);

        //Se obtiene los datos enviados cuando se hace click en la lista.
        Bundle b = getIntent().getExtras();
        if (b != null) {
            codigo = b.getString("ID");
            nombre = b.getString("NOMBRE");
            ruc = b.getString("RUC");
            zona = b.getString("ZONA");
            tipoCliente = b.getString("TIPOCLIENTE");
        }

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        text_ruc = (EditText) findViewById(R.id.text_ruc);
        text_zona = (EditText) findViewById(R.id.text_zona);
        text_tipodecliente = (EditText) findViewById(R.id.text_tipodecliente);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        //Colocar los campos enviados enel formulario de modificar
        text_codigo.setText(codigo);
        text_nombre.setText(nombre);
        text_ruc.setText(ruc);
        text_zona.setText(zona);
        text_tipodecliente.setText(tipoCliente);

        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_eliminar = (Button) findViewById(R.id.boton_eliminar);

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarCliente(text_codigo.getText().toString(), text_nombre.getText().toString(), text_ruc.getText().toString(), text_zona.getText().toString(), text_tipodecliente.getText().toString());
                onBackPressed();
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarCliente(codigo);
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

    private void modificarCliente (String id, String nombre, String ruc, String zona, String tipCliente) {
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "update CLIENTE set ID='" + id +"',NOMBRE='" + nombre + "',RUC='" + ruc + "',ZONA='" + zona + "',TIPOCLIENTE='" + tipCliente + "' where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Modificación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void eliminarCliente (String id) {
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "delete from CLIENTE where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Eliminación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
