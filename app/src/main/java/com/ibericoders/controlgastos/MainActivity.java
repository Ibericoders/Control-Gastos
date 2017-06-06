package com.ibericoders.controlgastos;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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

    public void exportar(View v){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Exportar exportar=new Exportar();
            exportar.execute();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(this,"El acceso al almacenamiento externo es necesario para usar esta función.",Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private class Exportar extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            if(Build.VERSION.SDK_INT>24){
                //Comprobar si se puede acceder al almacenaje externo.
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    //Obtener los datos a guardar en el archivo y el nombre del archivo.
                    ArrayList<Gasto> gastos=ggastos.obtenerTodosGastos();
                    Calendar cal=Calendar.getInstance();
                    String filename="Exportado"+cal.get(Calendar.DAY_OF_MONTH)+"|"+cal.get(Calendar.MONTH)+"|"+cal.get(Calendar.YEAR)+
                            "|"+cal.get(Calendar.HOUR)+"|"+cal.get(Calendar.MINUTE)+".txt";

                    //Crear la carpeta
                    File folder = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS), "Ibericoders");
                    if (!folder.mkdirs()) {
                        folder.mkdirs();
                    }
                    //Crear el archivo
                    File file = new File(folder, filename);
                    //Escribir datos en el archivo
                    try {
                        FileWriter writer = new FileWriter(file);
                        for(int i=0;i<gastos.size();i++){
                            writer.append(gastos.get(i).toString()+"\n");
                        }
                        writer.flush();
                        writer.close();
                    }catch(IOException ex){
                        ex.printStackTrace();
                    }
                    //Enviar el archivo por email

                    //Obtener localizacion
                    File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filename);
                    Uri path = Uri.fromFile(filelocation);
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // Indicar que vas a mandar un email
                    emailIntent .setType("vnd.android.cursor.dir/email");
                    //Recipientes del email
                    String to[] = {"jroldanreal@gmail.com"};
                    emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
                    // Añadir el adjunto
                    emailIntent .putExtra(Intent.EXTRA_STREAM, path);
                    // Asunto del email
                    emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Base de datos exportada");
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //Enviar email
                    startActivityForResult(Intent.createChooser(emailIntent , "Enviar email..."),10);
                }else{
                    Toast.makeText(MainActivity.this,"El almacenamiento externo no es accesible.",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(MainActivity.this,"Esta función sólo está disponible en dispositivos con Android 7 o superior",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, "Base de datos exportada correctamente.", Toast.LENGTH_LONG).show();
        }
    }
}
