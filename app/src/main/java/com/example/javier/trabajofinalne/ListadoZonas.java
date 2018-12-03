package com.example.javier.trabajofinalne;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

public class ListadoZonas extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listadoZonas;
    Button boton_nueva_zona, boton_modificar, boton_eliminar, boton_cancelar;
    boolean botones_validos;

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

        botones_validos = false;

        boton_nueva_zona = (Button) findViewById(R.id.boton_nueva_zona);
        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_eliminar = (Button) findViewById(R.id.boton_eliminar);
        boton_cancelar = (Button) findViewById(R.id.boton_cancelar);
        listView = (ListView) findViewById(R.id.listView_clientes);
        cargarListaZonas();

        final Intent intent = new Intent(ListadoZonas.this, ModificarZona.class);

        //Pasar los datos de la lista a la ventana modificar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Probar la posición de cada elemento del listView
                //Toast.makeText(ListadoClientes.this, "Posición:"+position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ListadoZonas.this, listadoZonas.get(position), Toast.LENGTH_SHORT).show();
                String clave = listadoZonas.get(position).split("   ")[0];
                String nombre = listadoZonas.get(position).split("   ")[1];
                String estado = listadoZonas.get(position).split("   ")[2];

                intent.putExtra("ID", clave);
                intent.putExtra("NOMBRE", nombre);
                intent.putExtra("ESTADO", estado);
                //startActivity(intent);
                botones_validos = true;

                //Cambiar de color cuando se selecciona un elemento del listView
                for (int i=0; i<listadoZonas.size(); i++) {
                    if (i == position) parent.getChildAt(i).setBackgroundColor(Color.rgb(82, 190, 128));
                    else parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
            }
        });

        boton_nueva_zona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botones_validos = false;
                startActivity(new Intent(ListadoZonas.this, FormularioZona.class));
            }
        });

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botones_validos) {
                    Toast.makeText(ListadoZonas.this, intent.getExtras().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    botones_validos = false;
                } else Toast.makeText(ListadoZonas.this, "Tiene que seleccionar una Zona para modificar.", Toast.LENGTH_SHORT).show();
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botones_validos) {

                    AlertDialog.Builder men = new AlertDialog.Builder((ListadoZonas.this));
                    men.setTitle("Eliminar Zona");
                    men.setMessage("¿Esta segur@ de eliminar esta Zona?");
                    men.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    men.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminarZona(intent.getExtras().getString("ID"));
                            //onBackPressed();
                            onPostResume();
                            botones_validos = false;
                        }
                    });
                    men.show();

                } else Toast.makeText(ListadoZonas.this, "Tiene que seleccionar una Zona para eliminar.", Toast.LENGTH_SHORT).show();
            }
        });

        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListadoZonas.this, MainActivity.class));
            }
        });

        // Ícono de + para agregar nueva Zona
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListadoZonas.this, FormularioZona.class));
            }
        });*/

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
        String sql = "select ID, NOMBRE, ESTADO from ZONA";
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                String linea = c.getString(0)+"   "+c.getString(1)+"   "+c.getString(2);
                datos.add(linea);
            } while (c.moveToNext());
        }
        db.close();
        return datos;
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
