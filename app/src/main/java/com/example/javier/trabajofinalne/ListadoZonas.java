package com.example.javier.trabajofinalne;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoZonas extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listadoZonas;

    //cargar el listado cuando se entre a esta ventana.
    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarListaZonas();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_zona_listado);

        listView = (ListView) findViewById(R.id.listView_clientes);
        cargarListaZonas();

        //Pasar los datos de la lista a la ventana modificar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Probar la posición de cada elemento del listView
                //Toast.makeText(ListadoClientes.this, "Posición:"+position, Toast.LENGTH_SHORT).show();
                Toast.makeText(ListadoZonas.this, listadoZonas.get(position), Toast.LENGTH_SHORT).show();
                String clave = listadoZonas.get(position).split("   ")[0];
                String nombre = listadoZonas.get(position).split("   ")[1];

                Intent intent = new Intent(ListadoZonas.this, ModificarZona.class);
                intent.putExtra("ID", clave);
                intent.putExtra("NOMBRE", nombre);

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
                startActivity(new Intent(ListadoZonas.this, FormularioZona.class));
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

    private void cargarListaZonas(){
        listadoZonas = ListaZonas();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listadoZonas);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> ListaZonas() {
        ArrayList <String> datos = new ArrayList<String>();
        BaseHelper helper = new BaseHelper(this, "Demo3", null, 1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select ID, NOMBRE from ZONA";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                String linea = c.getString(0)+"   "+c.getString(1);
                datos.add(linea);
            } while (c.moveToNext());
        }
        db.close();
        return datos;
    }
}
