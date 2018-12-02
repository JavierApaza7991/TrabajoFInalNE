package com.example.javier.trabajofinalne;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ModificarTipoCliente extends AppCompatActivity {

    Spinner opciones_std;

    EditText text_codigo, text_nombre;
    Button boton_modificar, boton_cancelar;

    String codigo, nombre, estado;
    String text_estado;

    ArrayList <String>  listaEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_tipo_cliente);

        opciones_std = (Spinner) findViewById(R.id.sp01);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_std, android.R.layout.simple_spinner_item);
        opciones_std.setAdapter(adapter);

        //optener la opcion del spinner seleccionado
        listaEstados = new ArrayList<String>();
        listaEstados.add("Activo");
        listaEstados.add("Inactivo");
        opciones_std.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_estado = listaEstados.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Se obtiene los datos enviados cuando se hace click en la lista.
        Bundle b = getIntent().getExtras();
        if (b != null) {
            codigo = b.getString("ID");
            nombre = b.getString("NOMBRE");
            estado = b.getString("ESTADO");
        }

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        //Colocar los campos enviados enel formulario de modificar
        text_codigo.setText(codigo);
        text_codigo.setEnabled(false);
        text_nombre.setText(nombre);
        opciones_std.setSelection(buscarPosición(listaEstados, estado));

        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarTipoCliente(text_codigo.getText().toString(), text_nombre.getText().toString(), text_estado);
                onBackPressed();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificarTipoCliente.this, ListadoTipoClientes.class));
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    public int buscarPosición (ArrayList<String> array, String str) {
        int pos = 0;
        while (!array.get(pos).equals(str))
            pos++;
        return pos;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void modificarTipoCliente (String id, String nombre, String estado) {
        BaseHelper helper = new BaseHelper(this, "Demo2", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "update TIPOCLIENTE set ID='" + id +"',NOMBRE='" + nombre + "',ESTADO='" + estado + "' where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Modificación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
