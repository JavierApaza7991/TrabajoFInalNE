package com.example.javier.trabajofinalne;

import android.content.ContentValues;
import android.content.Intent;
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

public class FormularioCliente extends AppCompatActivity {

    Spinner opciones_std;

    EditText text_codigo, text_nombre, text_ruc, text_zona, text_tipodecliente, text_estado;
    Button boton_agregar, boton_mostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cliente);

        opciones_std = (Spinner) findViewById(R.id.sp01);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_std, android.R.layout.simple_spinner_item);
        opciones_std.setAdapter(adapter);

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        text_ruc = (EditText) findViewById(R.id.text_ruc);
        text_zona = (EditText) findViewById(R.id.text_zona);
        text_tipodecliente = (EditText) findViewById(R.id.text_tipodecliente);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        boton_agregar = (Button) findViewById(R.id.boton_agregar);
        boton_mostrar = (Button) findViewById(R.id.boton_mostrar);

        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente(text_codigo.getText().toString(), text_nombre.getText().toString(), text_ruc.getText().toString(), text_zona.getText().toString(), text_tipodecliente.getText().toString());
            }
        });
        boton_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormularioCliente.this, ListadoClientes.class));
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

    //Método para guardar un registro en base de datos.
    private void guardarCliente (String id, String nombre, String ruc, String zona, String tipCliente) {
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("ID", id);
            c.put("NOMBRE", nombre);
            c.put("RUC", ruc);
            c.put("ZONA", zona);
            c.put("TIPOCLIENTE", tipCliente);
            //c.put("Estado", estado);

            db.insert("CLIENTE", null, c);
            db.close();
            Toast.makeText(this, "Registro insertado."+c.toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
