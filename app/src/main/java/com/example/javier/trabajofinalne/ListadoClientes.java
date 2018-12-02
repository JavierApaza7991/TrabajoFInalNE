package com.example.javier.trabajofinalne;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

public class ListadoClientes extends AppCompatActivity {

    ListView listView;
    ArrayList <String> listadoClientes;
    Button boton_modificar, boton_eliminar;
    boolean botones_validos;

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

        botones_validos = false;

        boton_modificar = (Button) findViewById(R.id.boton_modificar);
        boton_eliminar = (Button) findViewById(R.id.boton_eliminar);
        listView = (ListView) findViewById(R.id.listView_clientes);
        cargarListaClientes();

        final Intent intent = new Intent(ListadoClientes.this, ModificarCliente.class);

        //Pasar los datos de la lista a la ventana modificar
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Probar la posición de cada elemento del listView
                //Toast.makeText(ListadoClientes.this, "Posición:"+position, Toast.LENGTH_SHORT).show();
                //Toast.makeText(ListadoClientes.this, listadoClientes.get(position), Toast.LENGTH_SHORT).show();
                String clave = listadoClientes.get(position).split("   ")[0];
                String nombre = listadoClientes.get(position).split("   ")[1];
                String ruc = listadoClientes.get(position).split("   ")[2];
                String zona = listadoClientes.get(position).split("   ")[3];
                String tipCliente = listadoClientes.get(position).split("   ")[4];
                String estado = listadoClientes.get(position).split("   ")[5];

                intent.putExtra("ID", clave);
                intent.putExtra("NOMBRE", nombre);
                intent.putExtra("RUC", ruc);
                intent.putExtra("ZONA", zona);
                intent.putExtra("TIPOCLIENTE", tipCliente);
                intent.putExtra("ESTADO", estado);
                //startActivity(intent);
                botones_validos = true;

                //Cambiar de color cuando se selecciona un elemento del listView
                for (int i=0; i<listadoClientes.size(); i++) {
                    if (i == position) parent.getChildAt(i).setBackgroundColor(Color.rgb(82, 190, 128));
                    else parent.getChildAt(i).setBackgroundColor(Color.WHITE);
                }
            }
        });

        /*boton_nuevo_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListadoClientes.this, FormularioCliente.class));startActivity(new Intent(ListadoClientes.this, FormularioCliente.class));
            }
        });*/

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botones_validos) {
                    Toast.makeText(ListadoClientes.this, intent.getExtras().toString(), Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    botones_validos = false;
                } else Toast.makeText(ListadoClientes.this, "Tiene que seleccionar un Cliente para modificar.", Toast.LENGTH_SHORT).show();
            }
        });

        boton_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (botones_validos) {

                    AlertDialog.Builder men = new AlertDialog.Builder((ListadoClientes.this));
                    men.setTitle("Eliminar Cliente");
                    men.setMessage("¿Esta segur@ de eliminar este Cliente?");
                    men.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    men.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            eliminarCliente(intent.getExtras().getString("ID"));
                            //onBackPressed();
                            onPostResume();
                            botones_validos = false;
                        }
                    });
                    men.show();

                } else Toast.makeText(ListadoClientes.this, "Tiene que seleccionar un Cliente para eliminar.", Toast.LENGTH_SHORT).show();
            }
        });

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
