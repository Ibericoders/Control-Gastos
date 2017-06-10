package com.ibericoders.controlgastos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView gastos;
    ArrayList<Gasto> datos;
    GestionGastos ggastos;
    CardView exportar;
    EditText em_exp;
    TextView filtro,tvfab2,tvfab3;
    FloatingActionButton fab1,fab2,fab3;
    int CHOOSE_FILE_REQUESTCODE=15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Referencias a objetos
        gastos=(ListView)this.findViewById(R.id.listagastos);
        exportar=(CardView)this.findViewById(R.id.cv_exportar);
        em_exp=(EditText)this.findViewById(R.id.edt_emailExportar);
        filtro=(TextView)this.findViewById(R.id.tv_filtroGasto);
        filtro.setVisibility(View.GONE);
        fab1=(FloatingActionButton)this.findViewById(R.id.fabPrincipal);
        fab2=(FloatingActionButton)this.findViewById(R.id.fabsub1);
        fab2.setVisibility(View.GONE);
        fab3=(FloatingActionButton)this.findViewById(R.id.fabsub2);
        fab3.setVisibility(View.GONE);
        tvfab2=(TextView)this.findViewById(R.id.tv_fabsub1);
        tvfab2.setVisibility(View.GONE);
        tvfab3=(TextView)this.findViewById(R.id.tv_fabsub2);
        tvfab3.setVisibility(View.GONE);
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

        //Ocultar tarjeta exportar
        exportar.setVisibility(View.GONE);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.filtrar:
                showDialogMenu();
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        showDialogMenu();
                        Log.d("Dialog used.","here");
                    }
                }; break;
            case R.id.importar:
                importar();
                break;
            case R.id.exportar:
                empezarProceso();
                break;
        }
        return true;
    }

    public void showDialogMenu(){
        final String[] categorias=new String[]{"Cat1","Cat2","Cat3","Cat4","Cat5"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona la categoría de filtro");
        builder.setItems(categorias, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Activar textview con nombre categoria
                filtro.setText("Filtro activado: "+categorias[which]);
                filtro.setVisibility(View.VISIBLE);
                //Cambiar adapter del listview
                //Obtener datos
                ggastos=new GestionGastos(MainActivity.this);
                datos=ggastos.obtenerGastosCategoria(categorias[which]);

                //Mostrar datos en ListView
                ListadoAdapter adapter=new ListadoAdapter(MainActivity.this,datos);
                gastos.setAdapter(adapter);
            }
        });
        builder.show();
    }

    public void nuevo(View v){
        Intent intent=new Intent(this,NuevoGastoActivity.class);
        this.startActivity(intent);
    }

    public void desplegar(View v){
        //Filtro de pulsaciones(Abrir/Cerrar)
        if(fab2.getVisibility()==View.GONE){
            //Activar dos subbotones/textos
            fab2.setVisibility(View.VISIBLE);
            fab3.setVisibility(View.VISIBLE);
            tvfab2.setVisibility(View.VISIBLE);
            tvfab3.setVisibility(View.VISIBLE);
            //Cambiar imagen boton principal
            fab1.setImageResource(R.drawable.ic_cerrar);
            //Oscurecer pantalla
        }else if(fab2.getVisibility()==View.VISIBLE){
            //Volver al inicio
            //Desactivar dos subbotones/textos
            fab2.setVisibility(View.GONE);
            fab3.setVisibility(View.GONE);
            tvfab2.setVisibility(View.GONE);
            tvfab3.setVisibility(View.GONE);
            //Cambiar imagen boton principal
            fab1.setImageResource(R.drawable.ic_anadir);

        }

    }

    public void empezarProceso(){
        exportar.setVisibility(View.VISIBLE);
    }
    public void exportar(View v){

        String email=em_exp.getText().toString();
        if(email.contains("@") && email.endsWith(".com")){
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                Exportar exportar=new Exportar();
                exportar.execute(email);
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
            }
        }else {
            Toast.makeText(this, "Email no valido", Toast.LENGTH_LONG).show();
            em_exp.setText("");
        }
    }

    public void importar(){
        //Crear intent para selección de archivo
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        Intent i = Intent.createChooser(intent, "Archivo");
        startActivityForResult(i, CHOOSE_FILE_REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Recibir la información de vuelta del Explorador de Archivos.
        if(requestCode==CHOOSE_FILE_REQUESTCODE && resultCode==RESULT_OK){
            //Obtener ruta del archivo
            String[] paths=data.getData().getLastPathSegment().split("[:]");
            String path=paths[1];

            Toast.makeText(this,path,Toast.LENGTH_LONG).show();

            //Leer el archivo y procesar los gastos

            //Uri uri=Uri.parse(path);
            File archivo=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),path);
            try {
                BufferedReader bf = new BufferedReader(new FileReader(archivo));
                String linea;
                int totalGastos=0;
                int gastosAnadidos=0;
                while ((linea = bf.readLine()) != null) {
                    totalGastos++;
                    String[] g = linea.split("[|]");
                    Gasto gasto = new Gasto(g[0], g[1], Double.parseDouble(g[2]), g[3],g[4]);
                    if(!ggastos.comprobarGasto(gasto.getNombre())){
                        ggastos.guardarNuevoGasto(gasto);
                        gastosAnadidos++;
                    }
                }
                Toast.makeText(this,"Gastos encontrados: "+totalGastos+". Gastos añadidos: "+gastosAnadidos+".",Toast.LENGTH_LONG).show();

                //Mostrar datos en ListView
                ListadoAdapter adapter=new ListadoAdapter(this,datos);
                gastos.setAdapter(adapter);

            }catch(IOException ex){
                ex.printStackTrace();
                Toast.makeText(this,"Archivo no legible o no encontrado",Toast.LENGTH_LONG).show();
            }
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
            }
        }
    }

    private class Exportar extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
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
                    File filelocation = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/Ibericoders/", filename);
                    Uri path = Uri.fromFile(filelocation);
                    // Indicar que vas a mandar un email
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent .setType("vnd.android.cursor.dir/email");
                    //Recipientes del email
                    String to[] = {"ibericoders@gmail.com",params[0]};
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
