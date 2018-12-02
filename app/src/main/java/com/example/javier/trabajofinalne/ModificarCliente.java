package com.example.javier.trabajofinalne;

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

public class ModificarCliente extends AppCompatActivity {

    Spinner opciones_std, opciones_zonas, opciones_tipo_cliente;

    EditText text_codigo, text_nombre, text_ruc;
    Button boton_modificar, boton_cancelar;

    String codigo, nombre, ruc, zona, tipoCliente, estado, text_zona, text_tipodecliente, text_estado;

    ArrayList <String> listaZonas;
    ArrayList <String>  listaTipoClientes;
    ArrayList <String>  listaEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_cliente);

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

        //Se obtiene los datos enviados cuando se hace click en la lista.
        Bundle b = getIntent().getExtras();
        if (b != null) {
            codigo = b.getString("ID");
            nombre = b.getString("NOMBRE");
            ruc = b.getString("RUC");
            zona = b.getString("ZONA");
            tipoCliente = b.getString("TIPOCLIENTE");
            estado = b.getString("ESTADO");
        }

        text_codigo = (EditText) findViewById(R.id.text_codigo);
        text_nombre = (EditText) findViewById(R.id.text_nombre);
        text_ruc = (EditText) findViewById(R.id.text_ruc);
        //text_estado = (EditText) ((Spinner) findViewById(R.id.sp01)).getOnItemSelectedListener();

        //Colocar los campos enviados enel formulario de modificar
        text_codigo.setText(codigo);
        text_codigo.setEnabled(false);
        text_nombre.setText(nombre);
        text_ruc.setText(ruc);
        opciones_zonas.setSelection(buscarPosición(listaZonas, zona));
        opciones_tipo_cliente.setSelection(buscarPosición(listaTipoClientes, tipoCliente));
        opciones_std.setSelection(buscarPosición(listaEstados, estado));

        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificarCliente(text_codigo.getText().toString(), text_nombre.getText().toString(), text_ruc.getText().toString(), text_zona, text_tipodecliente, text_estado);
                onBackPressed();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModificarCliente.this, ListadoClientes.class));
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

    private void modificarCliente (String id, String nombre, String ruc, String zona, String tipCliente, String estado) {
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            String sql = "update CLIENTE set ID='" + id +"',NOMBRE='" + nombre + "',RUC='" + ruc + "',ZONA='" + zona + "',TIPOCLIENTE='" + tipCliente + "',ESTADO='" + estado + "' where ID="+id;
            db.execSQL(sql);
            db .close();

            Toast.makeText(this, "Modificación correcta.", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
