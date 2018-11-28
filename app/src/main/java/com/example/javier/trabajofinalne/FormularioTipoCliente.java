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

public class FormularioTipoCliente extends AppCompatActivity {

    Spinner opciones_std;

    EditText text_codigo, text_nombre, text_estado;
    Button boton_agregar, boton_mostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_tipo_cliente);

        opciones_std = (Spinner) findViewById(R.id.sp01);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_std, android.R.layout.simple_spinner_item);
        opciones_std.setAdapter(adapter);

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        boton_agregar = (Button) findViewById(R.id.boton_agregar);
        boton_mostrar = (Button) findViewById(R.id.boton_mostrar);

        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarTipoCliente(text_codigo.getText().toString(), text_nombre.getText().toString());
            }
        });
        boton_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormularioTipoCliente.this, ListadoTipoClientes.class));
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
    private void guardarTipoCliente (String id, String nombre) {
        BaseHelper helper = new BaseHelper(this, "Demo2", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("ID", id);
            c.put("NOMBRE", nombre);
            //c.put("Estado", estado);

            db.insert("TIPOCLIENTE", null, c);
            db.close();
            Toast.makeText(this, "Registro insertado."+c.toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
