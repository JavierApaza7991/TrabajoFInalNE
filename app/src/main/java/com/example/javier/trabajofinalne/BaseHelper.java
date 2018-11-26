package com.example.javier.trabajofinalne;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseHelper extends SQLiteOpenHelper {

    String tablaCliente = "CREATE TABLE CLIENTE(ID TEXT PRIMARY KEY, NOMBRE TEXT, RUC TEXT, ZONA TEXT, TIPOCLIENTE TEXT, ESTADO TEXT)";
    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaCliente);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table tablaCliente");
        db.execSQL(tablaCliente);
    }
}
