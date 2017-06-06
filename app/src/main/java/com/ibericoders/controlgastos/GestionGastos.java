package com.ibericoders.controlgastos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Jorge R on 04/06/2017.
 */

public class GestionGastos {

    private Ayudante helper;
    private SQLiteDatabase db;
    public GestionGastos(Context ctx){
        //Creación objeto ayudante y obtención de la base de datos
        helper=new Ayudante(ctx,"datos",1);
        db=helper.getWritableDatabase();
    }

    public void guardarNuevoGasto(Gasto g){
        if(!comprobarGasto(g.getNombre())){
            String sql="insert into gastos (nombre,descripcion,cantidad,fecha) ";
            sql+="values ('"+g.getNombre()+"','"+g.getDescripcion()+"',"+g.getCantidad()+",'"+g.getFecha()+"')";
            db.execSQL(sql);
        }
    }

    public boolean comprobarGasto(String nombre){
        boolean res=false;
        String sql="select * from gastos where nombre='"+nombre+"'";
        Cursor c= db.rawQuery(sql,null);
        while(c.moveToNext()){
            res=true;
        }
        c.close();
        return res;
    }

    public ArrayList<Gasto> obtenerTodosGastos(){
        String sql="select * from gastos";
        Cursor c= db.rawQuery(sql,null);
        ArrayList<Gasto>gastos=new ArrayList<>();
        while(c.moveToNext()){
            Gasto g=new Gasto(c.getString(1),c.getString(2),Double.parseDouble(c.getString(3)),c.getString(4));
            gastos.add(g);
        }
        c.close();
        return gastos;
    }

    public Gasto obtenerGasto(String nombre){
        String sql="select * from gastos where nombre='"+nombre+"'";
        Cursor c= db.rawQuery(sql,null);
        Gasto g=null;
        while(c.moveToNext()){
            g=new Gasto(c.getString(1),c.getString(2),Double.parseDouble(c.getString(3)),c.getString(4));
        }
        c.close();
        return g;
    }

    public void borrarGasto(String nombre){
        String sql="delete * from participantes where nombre='"+nombre+"'";
        db.execSQL(sql);
    }
}
