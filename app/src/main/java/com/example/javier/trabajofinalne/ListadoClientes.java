package com.example.javier.trabajofinalne;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoClientes extends AppCompatActivity {

    ListView listView;
    ArrayList <String> listadoClientes;

    //cargar el listado cuando se entre a esta ventana.
    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarListaClientes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_listado);

        listView = (ListView) findViewById(R.id.listView_clientes);
        cargarListaClientes();

        //Pasar los datos de la lista a la ventana modificar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Probar la posición de cada elemento del listView
                //Toast.makeText(ListadoClientes.this, "Posición:"+position, Toast.LENGTH_SHORT).show();
                Toast.makeText(ListadoClientes.this, listadoClientes.get(position), Toast.LENGTH_SHORT).show();
                String clave = listadoClientes.get(position).split("   ")[0];
                String nombre = listadoClientes.get(position).split("   ")[1];
                String ruc = listadoClientes.get(position).split("   ")[2];
                String zona = listadoClientes.get(position).split("   ")[3];
                String tipCliente = listadoClientes.get(position).split("   ")[4];
                String estado = listadoClientes.get(position).split("   ")[5];

                Intent intent = new Intent(ListadoClientes.this, ModificarCliente.class);
                intent.putExtra("ID", clave);
                intent.putExtra("NOMBRE", nombre);
                intent.putExtra("RUC", ruc);
                intent.putExtra("ZONA", zona);
                intent.putExtra("TIPOCLIENTE", tipCliente);
                intent.putExtra("ESTADO", estado);
                startActivity(intent);
            }
        });

        /*boton_nuevo_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListadoClientes.this, FormularioCliente.class));startActivity(new Intent(ListadoClientes.this, FormularioCliente.class));
            }
        });*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListadoClientes.this, FormularioCliente.class));
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

    private void cargarListaClientes(){
        listadoClientes = ListaClientes();
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoClientes);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> ListaClientes() {
        ArrayList <String> datos = new ArrayList<String>();
        BaseHelper helper = new BaseHelper(this, "Demo", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select ID, NOMBRE, RUC, ZONA, TIPOCLIENTE, ESTADO from Cliente";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                String linea = c.getString(0)+"   "+c.getString(1)+"   "+c.getString(2)+"   "+c.getString(3)+"   "+c.getString(4)+"   "+c.getString(5);
                datos.add(linea);
            } while (c.moveToNext());
        }
        db.close();
        return datos;
    }
}
