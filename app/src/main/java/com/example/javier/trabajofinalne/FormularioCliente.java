package com.example.javier.trabajofinalne;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

public class FormularioCliente extends AppCompatActivity {

    Spinner opciones_std, opciones_zonas, opciones_tipo_cliente;

    EditText text_codigo, text_nombre, text_ruc;
    Button boton_agregar, boton_mostrar, boton_cancelar;
    String text_zona, text_tipodecliente, text_estado;

    ArrayList <String> listaZonas;
    ArrayList <String>  listaTipoClientes;
    ArrayList <String>  listaEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_cliente);

        //Conexión a la Base de Datos para obtener la lista de zonas y de tipo_clientes
        BaseHelper helper = new BaseHelper(this, "Demo3", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select ID, NOMBRE, ESTADO from ZONA";
        Cursor c = db.rawQuery(sql, null);

        listaZonas = new ArrayList<String>();
        while (c.moveToNext()) {
            if (c.getString(2).equals("Activo"))
                listaZonas.add(c.getString(1));
        }
        db.close();

        BaseHelper helper2 = new BaseHelper(this, "Demo2", null, 1);
        SQLiteDatabase db2 = helper2.getReadableDatabase();
        String sql2 = "select ID, NOMBRE, ESTADO from TIPOCLIENTE";
        Cursor c2 = db2.rawQuery(sql2, null);

        listaTipoClientes = new ArrayList<String>();
        while (c2.moveToNext()) {
            if (c2.getString(2).equals("Activo"))
                listaTipoClientes.add(c2.getString(1));
        }
        db2.close();

        //Cargando infomación de la base de datos a los spinners
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

        opciones_zonas = (Spinner) findViewById(R.id.text_zona);
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaZonas);
        opciones_zonas.setAdapter(adapter2);

        //optener la opcion del spinner seleccionado
        opciones_zonas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_zona = listaZonas.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        opciones_tipo_cliente = (Spinner) findViewById(R.id.text_tipodecliente);
        ArrayAdapter<CharSequence> adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listaTipoClientes);
        opciones_tipo_cliente.setAdapter(adapter3);

        //optener la opcion del spinner seleccionado
        opciones_tipo_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text_tipodecliente = listaTipoClientes.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        //Obtener que número de código sigue
        BaseHelper helper3 = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db3 = helper3.getReadableDatabase();
        String sql3 = "select ID, NOMBRE from Cliente";
        Cursor c3 = db3.rawQuery(sql3, null);

        Integer cod = 1;
        String value;
        while (c3.moveToNext()) {
            value = c3.getString(0);
            cod = Integer.valueOf(value)+1;
        }
        db3.close();

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_codigo.setText(String.valueOf(cod));
        text_codigo.setEnabled(false);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        text_ruc = (EditText) findViewById(R.id.text_ruc);
        //text_zona = (Spinner) findViewById(R.id.text_zona);
        //text_tipodecliente = (EditText) findViewById(R.id.text_tipodecliente);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        boton_agregar = (Button) findViewById(R.id.boton_agregar);
        //boton_mostrar = (Button) findViewById(R.id.boton_mostrar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        boton_agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCliente(text_codigo.getText().toString(), text_nombre.getText().toString(), text_ruc.getText().toString(), text_zona, text_tipodecliente, text_estado);
                startActivity(new Intent(FormularioCliente.this, ListadoClientes.class));
            }
        });
        /*boton_mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FormularioCliente.this, ListadoClientes.class));
            }
        });*/
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
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
    private void guardarCliente (String id, String nombre, String ruc, String zona, String tipCliente, String estado) {
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            ContentValues c = new ContentValues();
            c.put("ID", id);
            c.put("NOMBRE", nombre);
            c.put("RUC", ruc);
            c.put("ZONA", zona);
            c.put("TIPOCLIENTE", tipCliente);
            c.put("ESTADO", estado);
            //c.put("Estado", estado);

            db.insert("CLIENTE", null, c);
            db.close();
            Toast.makeText(this, "Registro insertado."+c.toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
