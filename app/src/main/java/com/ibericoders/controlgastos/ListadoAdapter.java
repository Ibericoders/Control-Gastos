package com.ibericoders.controlgastos;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class ListadoAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<Gasto> datos;
    private LayoutInflater lf;

    public ListadoAdapter(Context ctx, ArrayList<Gasto> datos){
        this.ctx=ctx;
        this.datos=datos;
        lf=LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return datos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //Generamos un objeto View a partir de la plantilla creada para la fila.
        convertView=lf.inflate(R.layout.tarjeta_gasto,null);

        //Nombre Gasto
        TextView tvNombre=(TextView)convertView.findViewById(R.id.tv_nombreGasto);
        tvNombre.setText(datos.get(position).getNombre());

        //Descripcion Gasto
        TextView tvDescripcion=(TextView)convertView.findViewById(R.id.tv_descripcionGasto);
        tvDescripcion.setText(datos.get(position).getDescripcion());

        //Cantidad Gasto
        TextView tvCantidad=(TextView)convertView.findViewById(R.id.tv_cantidadGasto);
        tvCantidad.setText(String.valueOf(datos.get(position).getCantidad()));

        //Fecha Gasto
        TextView tvFecha=(TextView)convertView.findViewById(R.id.tv_fechaGasto);
        tvFecha.setText(datos.get(position).getFecha());

        //Devolver el view de la fila
        return convertView;
    }
}
