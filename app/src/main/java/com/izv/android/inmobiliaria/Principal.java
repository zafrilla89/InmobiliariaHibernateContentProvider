package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class Principal extends Activity {

    private ArrayList<Inmueble> lista;
    private AdaptadorCursor a;
    private ListView lv;
    private Cursor c;
    private String usuario="", ip="";

    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info =(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int index=info.position;
        if (id == R.id.m_editar) {
            editar(index);
        }else {
            if (id == R.id.m_borrar) {
                lista= sacarlista();
                Inmueble in= lista.get(index);
                borrar(in.getId());
                selectdatos();
                borrarfoto(in);
                a.changeCursor(c);
                lista=sacarlista();
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        iniciarcomponentes();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menucontextual,menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.anadir) {
            return anadir();
        }
        if (id == R.id.usuario) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.guardar);
            LayoutInflater inflater = LayoutInflater.from(this);
            final View vista = inflater.inflate(R.layout.dialogo, null);
            alert.setView(vista);
            final EditText et=(EditText)vista.findViewById(R.id.et);
            final EditText etip=(EditText)vista.findViewById(R.id.ip);
            et.setText(usuario);
            etip.setText(ip);
            alert.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            if (et.getText().toString().compareTo("")==0 || etip.getText().toString().compareTo("")==0){
                                Toast.makeText(Principal.this,"TIenes que escribir un usuario y una ip",Toast.LENGTH_SHORT).show();
                            }else{
                                usuario=et.getText().toString();
                                ip=etip.getText().toString();
                                SharedPreferences pc;
                                pc = PreferenceManager.getDefaultSharedPreferences(
                                        getApplicationContext());
                                SharedPreferences.Editor ed = pc.edit();
                                ed.putString("usuario", usuario);
                                ed.putString("ip", ip);
                                ed.commit();
                            }
                        }
                    });
            alert.setNegativeButton(android.R.string.no,null);
            alert.show();
            return true;
        }
        if (id == R.id.Subir) {
            sacarnosubidos();
            lista = new ArrayList<Inmueble>();
            lista= sacarlista();
            Subir s=new Subir();
            s.execute();
            return true;
        }
        if (id == R.id.bajar) {
            sacarsubidos();
            lista = new ArrayList<Inmueble>();
            lista= sacarlista();
            Bajar b=new Bajar();
            b.execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***********************************************************************/
    /*                                                                     */
    /*                        METODOS AUXILIARES                           */
    /*                                                                     */
    /***********************************************************************/

    public boolean anadir(){
        Intent i = new Intent(this,Alta.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
        finish();
        return true;
    }

    public void borrarfoto(Inmueble in){
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        String id=""+in.getId();
        String[] listaDirectorio = archivo.list();
        for (int x=0;x<listaDirectorio.length;x++){
            String archi=listaDirectorio[x];
            if (archi.indexOf("inmobiliaria_"+id) != -1){
            File f= new File(getExternalFilesDir(null),archi);
                f.delete();
            }
        }
    }

    public boolean editar(int index){
        Intent i = new Intent(this,Editar.class);
        i.putParcelableArrayListExtra("datos", lista);
        i.putExtra("index",index);
        startActivity(i);
        finish();
        return true;
    }

    public static Inmueble getRow(Cursor c) {
        Inmueble in = new Inmueble();
        in.setId(c.getInt(0));
        in.setDireccion(c.getString(1));
        in.setTipo(c.getString(2));
        in.setPrecio(c.getString(3));
        in.setSubido(c.getString(4));
        in.setUsuario(c.getString(5));
        return in;
    }

    public void iniciarcomponentes(){
        SharedPreferences pc;
        pc = PreferenceManager.getDefaultSharedPreferences(
                getApplicationContext());
        usuario = pc.getString("usuario", "");
        ip=pc.getString("ip","");
        selectdatos();
        lista = new ArrayList<Inmueble>();
        lista= sacarlista();
        lv=(ListView)findViewById(R.id.listView);
        a=new AdaptadorCursor(this, c);
        lv.setAdapter(a);
        final Fragmento2 f2 = (Fragmento2) getFragmentManager().findFragmentById(R.id.fragment3);
        final boolean horizontal = f2 != null && f2.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (horizontal) {
                    File archivo = new File(String.valueOf(getExternalFilesDir(null)));
                    ArrayList<Bitmap> lis=new ArrayList<Bitmap>();
                    Adaptador ad = new Adaptador(Principal.this,R.layout.itemfoto,lis);
                    ListView lv= (ListView)findViewById(R.id.lvfotos);
                    lv.setAdapter(ad);
                    f2.ponerimagen(ad,archivo,lista, i,lis);
                } else {
                    Intent in = new Intent(Principal.this, Secundaria.class);
                    in.putExtra("index", i);
                    in.putParcelableArrayListExtra("datos", lista);
                    startActivity(in);
                }
            }
        });
        registerForContextMenu(lv);
    }

    public ArrayList<Inmueble> sacarlista(){
        ArrayList<Inmueble> lista=new ArrayList<Inmueble>();
        c.moveToFirst();
        Inmueble in;
        while (!c.isAfterLast()) {
            in = getRow(c);
            lista.add(in);
            c.moveToNext();
        }
        return lista;
    }

    /***********************************************************************/
    /*                                                                     */
    /*                    METODOS CONTENT PROVIDER                         */
    /*                                                                     */
    /***********************************************************************/

    public void sacarnosubidos(){
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        String[] proyeccion = null;
        String condicion = "subido=?";
        String[] parametros = {"0"};
        String orden = null;
        c= getContentResolver().query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);
    }

    public void sacarsubidos(){
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        String[] proyeccion = null;
        String condicion = "subido=?";
        String[] parametros = {"1"};
        String orden = null;
        c= getContentResolver().query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);
    }

    public void selectdatos() {
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        String[] proyeccion = null;
        String condicion = null;
        String[] parametros = null;
        String orden = null;
        c= getContentResolver().query(
                uri,
                proyeccion,
                condicion,
                parametros,
                orden);
    }

    public void borrar(int index){
        Uri uri= Contrato.TablaInmueble.CONTENT_URI;
        String where= Contrato.TablaInmueble._ID+"=?";
        String[] args= new String[]{index+""};
        int i=getContentResolver().delete(uri,where,args);
    }

    public void insertar (Inmueble in){
        Uri uri=Contrato.TablaInmueble.CONTENT_URI;
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.DIRECCION, in.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, in.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO, in.getPrecio());
        valores.put(Contrato.TablaInmueble.SUBIDO, in.getSubido());
        valores.put(Contrato.TablaInmueble.USUARIO, in.getUsuario());
        Uri urielemento= getContentResolver().insert(uri,valores);
        Cursor cursor=getContentResolver().query(
                urielemento, null, null, null, null);
    }

    /***********************************************************************/
    /*                                                                     */
    /*                          METODOS ASYNCTASK                          */
    /*                                                                     */
    /***********************************************************************/

    class Subir extends AsyncTask<String,Integer,String> {

        public String postFile(String urlPeticion, String nombreParametro, String nombreArchivo) {
            String resultado="";
            int status=0;
            try {
                URL url = new URL(urlPeticion);
                HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
                conexion.setDoOutput(true);
                conexion.setRequestMethod("POST");
                FileBody fileBody = new FileBody(new File(nombreArchivo));
                MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.STRICT);
                multipartEntity.addPart(nombreParametro, fileBody);
                multipartEntity.addPart("nombre", new StringBody("valor"));
                conexion.setRequestProperty("Content-Type", multipartEntity.getContentType().getValue());
                OutputStream out = conexion.getOutputStream();
                try {
                    multipartEntity.writeTo(out);
                } finally {
                    out.close();
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
                String decodedString;
                while ((decodedString = in.readLine()) != null) {
                    resultado+=decodedString+"\n";
                }
                in.close();
                status = conexion.getResponseCode();
            } catch (MalformedURLException ex) {
                return ex.toString();
            } catch (IOException ex) {
                return ex.toString();
            }
            return resultado+"\n"+status;
        }

        @Override
        protected String doInBackground(String... params) {
            String r="";
            try {
            for (int i=0;i<lista.size();i++){
                Inmueble in=lista.get(i);
                URL url = new URL("http://"+ip+"/InmobiliariaHibernate/control?target=inmuebleandroid&op=insert&action=op&tipo="
                +in.getTipo()+"&direccion="+in.getDireccion()+"&precio="+in.getPrecio()+"&usuario="+in.getUsuario());
                BufferedReader b = new BufferedReader
                        (new InputStreamReader
                                (url.openStream()));
                String id = b.readLine();
                id = b.readLine();
                b.close();
                File archivo = new File(String.valueOf(getExternalFilesDir(null)));
                String[] listaDirectorio = archivo.list();
                for (int x=0;x<listaDirectorio.length;x++){
                    String archi=listaDirectorio[x];
                    if (archi.indexOf("inmobiliaria_"+in.getId()) != -1){
                        r=postFile("http://"+ip+"/InmobiliariaHibernate/control?target=fotos&op=insert&action=op&id="+id,"archivo",archivo.getAbsolutePath() + "/" + archi);
                    }
                }
                for (int x=0;x<listaDirectorio.length;x++){
                    String archi=listaDirectorio[x];
                    if (archi.indexOf("inmobiliaria_"+in.getId()) != -1){
                        File f=new File(archivo.getAbsolutePath() + "/" + archi);
                        f.delete();
                    }
                }
                Uri uri= Contrato.TablaInmueble.CONTENT_URI;
                String where= Contrato.TablaInmueble._ID+"=?";
                String[] args= new String[]{in.getId()+""};
                int v=getContentResolver().delete(uri,where,args);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
                Toast.makeText(Principal.this,"Fallo al subir los inmuebles",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Principal.this,"Fallo al subir los inmuebles",Toast.LENGTH_SHORT).show();
            }

            return r;
        }

        @Override
        protected void onPostExecute(String strings) {
            Toast.makeText(Principal.this,"Datos subidos al servidor",Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<Fotos> listafoto;
    private ArrayList<Inmueble> listadescarga;

    class Bajar extends AsyncTask<String,Integer,String> {

        String s;

        @Override
        protected String doInBackground(String... params) {
            for (int i=0;i<lista.size();i++){
                Inmueble in=lista.get(i);
                File archivo = new File(String.valueOf(getExternalFilesDir(null)));
                String[] listaDirectorio = archivo.list();
                for (int x=0;x<listaDirectorio.length;x++){
                    String archi=listaDirectorio[x];
                    if (archi.indexOf("inmobiliaria_"+in.getId()) != -1){
                        File f=new File(archivo.getAbsolutePath() + "/" + archi);
                        f.delete();
                    }
                }
                Uri uri= Contrato.TablaInmueble.CONTENT_URI;
                String where= Contrato.TablaInmueble._ID+"=?";
                String[] args= new String[]{in.getId()+""};
                int v=getContentResolver().delete(uri,where,args);
            }
            String r = ClienteRestFul.get("http://"+ip+"/InmobiliariaHibernate/control?target=inmuebleandroid&op=select&action=view");
            s = ClienteRestFul.get("http://"+ip+"/InmobiliariaHibernate/control?target=fotosandroid&op=select&action=view");
            return r;
        }

        @Override
        protected void onPostExecute(String r) {
            JSONTokener token = new JSONTokener(r);
            try {
                listadescarga=new ArrayList<Inmueble>();
                JSONArray array = new JSONArray(token);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Inmueble in = new Inmueble(object);
                    listadescarga.add(in);
                }
                listafoto=new ArrayList<Fotos>();
                JSONTokener token2 = new JSONTokener(s);
                JSONArray arrayfotos = new JSONArray(token2);
                    for (int x = 0; x < arrayfotos.length(); x++) {
                        JSONObject object = arrayfotos.getJSONObject(x);
                        Fotos fo = new Fotos(object);
                        listafoto.add(fo);
                    }
                BajarFotos bj=new BajarFotos();
                bj.execute();

            } catch (Exception e) {

            }
        }
    }

    class BajarFotos extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... params) {
            String r="";
            lista=new ArrayList<Inmueble>();
            try {
            for (int i=0;i<listadescarga.size();i++){
                Inmueble in=listadescarga.get(i);
                insertar(in);
                selectdatos();
                lista=sacarlista();
                Inmueble inguardado=lista.get(lista.size()-1);
                for (int x = 0; x < listafoto.size(); x++) {
                    Fotos fo=listafoto.get(x);
                    if (fo.getIdinmueble()==in.getId()){
                        URL intlLogoURL = new URL("http://"+ip+"/InmobiliariaHibernate/fotos/"+fo.getNombre());
                        URLConnection urlCon = intlLogoURL.openConnection();
                        Date horaActual=new Date();
                        Calendar today = Calendar.getInstance();
                        String hora=(horaActual.getYear()+1900)+"_"+(horaActual.getMonth()+1)+"_"+horaActual.getDate()+
                                "_"+horaActual.getHours()+"_"+horaActual.getMinutes()+"_"+horaActual.getSeconds()+"_"+
                                today.get(Calendar.MILLISECOND);
                        File f = new File(getExternalFilesDir(null),"inmobiliaria_"+ inguardado.getId()+"_"+ hora+".jpg");
                        FileOutputStream fos = new FileOutputStream(f);
                        InputStream is = urlCon.getInputStream();
                        byte[] array = new byte[1000];
                        int leido = is.read(array);
                        while (leido > 0) {
                            fos.write(array, 0, leido);
                            leido = is.read(array);
                        }
                        is.close();
                        fos.close();
                    }
                }
            }
            } catch (Exception e) {
            }
            return r;
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(Principal.this,"Datos descargados correctamente",Toast.LENGTH_SHORT).show();
        }
    }

}
