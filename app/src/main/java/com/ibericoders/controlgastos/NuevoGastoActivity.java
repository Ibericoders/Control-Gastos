package com.ibericoders.controlgastos;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.database.DataSetObserver;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NuevoGastoActivity extends AppCompatActivity {

    EditText nombre,descripcion,cantidad,fecha;
    Spinner sp;
    GestionGastos ggastos;
    String cat=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_gasto);

        ggastos=new GestionGastos(this);

        //Referencias a objetos
        nombre=(EditText)this.findViewById(R.id.edt_nombreGasto);
        descripcion=(EditText)this.findViewById(R.id.edt_descripcionGasto);
        cantidad=(EditText)this.findViewById(R.id.edt_cantidadGasto);
        fecha=(EditText)this.findViewById(R.id.edt_fechaGasto);
        sp=(Spinner)this.findViewById(R.id.sp_categoriaGasto);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Cat1","Cat2","Cat3","Cat4","Cat5"});
        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cat=parent.getSelectedItem().toString();
                Toast.makeText(NuevoGastoActivity.this,parent.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            @TargetApi(24)
            public void onClick(View v) {

                Calendar cal=Calendar.getInstance();
                //Generar cuadro de dialogo de fecha
                DatePickerDialog dgfecha=new DatePickerDialog(NuevoGastoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //Cada vez que se seleccione una fecha se genera una cadena con los datos de la feccha seleccionada.
                        String fechaselec=view.getDayOfMonth()+"/"+(view.getMonth()+1)+"/"+view.getYear();
                        //Volcamos la cadena de fecha en el TextView
                        fecha.setText(fechaselec);
                    }
                }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));

                dgfecha.show();
            }
        });
    }

    public void cancelar(View v){
        this.finish();
    }
    public void guardar(View v){
        if(nombre.getText().length()>0 && descripcion.getText().length()>0 && cantidad.getText().length()>0 && fecha.getText().length()>0 && cat!=null){
            Gasto g=new Gasto(nombre.getText().toString(),descripcion.getText().toString(),Double.parseDouble(cantidad.getText().toString()),fecha.getText().toString(),cat);
            if(!ggastos.comprobarGasto(g.getNombre())){
                ggastos.guardarNuevoGasto(g);
                Toast.makeText(this, "Gasto introducido correctamente", Toast.LENGTH_LONG).show();
                this.finish();
            }else{
                Toast.makeText(this, "Gasto ya introducido", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Es necesario completar todos los campos", Toast.LENGTH_LONG).show();
        }

    }
}
