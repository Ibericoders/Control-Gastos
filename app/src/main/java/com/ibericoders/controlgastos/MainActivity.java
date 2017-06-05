package com.ibericoders.controlgastos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView gastos;
    ArrayList<Gasto> datos;
    GestionGastos ggastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Mostrar datos en ListView
        gastos=(ListView)this.findViewById(R.id.listagastos);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Obtener datos
        ggastos=new GestionGastos(this);
        datos=ggastos.obtenerTodosGastos();

        //Mostrar datos en ListView
        ListadoAdapter adapter=new ListadoAdapter(this,datos);
        gastos.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        //Obtener datos
        ggastos=new GestionGastos(this);
        datos=ggastos.obtenerTodosGastos();

        //Mostrar datos en ListView
        ListadoAdapter adapter=new ListadoAdapter(this,datos);
        gastos.setAdapter(adapter);
    }

    public void nuevo(View v){
        Intent intent=new Intent(this,NuevoGastoActivity.class);
        this.startActivity(intent);
    }
}
