package com.ibericoders.controlgastos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jorge on 04/06/2017.
 */

public class Ayudante extends SQLiteOpenHelper{

    public Ayudante(Context ctx, String nombre, int version){
        //Recibimos los datos y los envía a SQLiteOpenHelper para empezar el proceso de creación de la BD
        super(ctx,nombre,null,version);
    }

    public Ayudante(Context ctx, String nombre){
        //Recibimos los datos y los envía a SQLiteOpenHelper para empezar el proceso de creación de la BD
        super(ctx,nombre,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se le llama para construir las tablas de la base de datos.
        //Instruccion SQL de creacion de la tabla de gastos.
        String sqlCrearTabla1="create table gastos (_id integer primary key autoincrement,";
        sqlCrearTabla1+="nombre text,descripcion text,cantidad double,fecha text,categoria text)";
        //Ejecutar la instrucción 1.
        db.execSQL(sqlCrearTabla1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Instrucciones sobre qué hacer cuando se actualice la version de la base de datos.

    }
}
