package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class Editar extends Activity {

    private ArrayList<Inmueble> lista;
    private int index;
    private EditText etdireccion, ettipo, etprecio;
    private Inmueble i;

    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar);
        Bundle b = getIntent().getExtras();
        if(b !=null ) {
            lista = b.getParcelableArrayList("datos");
            index=b.getInt("index");
        }
        etdireccion = (EditText) findViewById(R.id.direccion);
        etprecio=(EditText)findViewById(R.id.precio);
        ettipo=(EditText)findViewById(R.id.tipo);
        i=lista.get(index);
        etdireccion.setText(i.getDireccion());
        ettipo.setText(i.getTipo());
        etprecio.setText(i.getPrecio());
    }

    /***********************************************************************/
    /*                                                                     */
    /*                        METODOS AUXILIARES                           */
    /*                                                                     */
    /***********************************************************************/

    public boolean comprueba(Inmueble i2, Inmueble i){
        if(i.equals(i2)){
            return true;
        }else {
            for (int c = 0; c < lista.size(); c++) {
                i = lista.get(c);
                if (i2.equals(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void tostada (String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /***********************************************************************/
    /*                                                                     */
    /*                           METODOS ONCLICK                           */
    /*                                                                     */
    /***********************************************************************/

    public void editar(View v){
        boolean semaforo;
        String di=etdireccion.getText().toString().trim();
        String ti=ettipo.getText().toString().trim();
        String pr=etprecio.getText().toString().trim();
        if (di.length() > 0 && ti.length() > 0 && pr.length() > 0 ) {
            Inmueble i2=new Inmueble(di,ti,pr,lista.get(index).getSubido(), lista.get(index).getUsuario());
            boolean comprobar=true;
            comprobar=comprueba(i2,i);
            if(comprobar==true){
                i2.setId(i.getId());
                update(i2);
                int id=i.getId();
                Intent i = new Intent(this,Hacerfotos.class);
                Bundle b = new Bundle();
                b.putLong("id", id);
                i.putExtras(b);
                startActivity(i);
                finish();
                tostada("INMUEBLE EDITADO");
            }else{
                tostada("INMUEBLE REPETIDO");
            }
            semaforo=true;
        }else{
            semaforo=false;
        }
        if(semaforo==false) {
            tostada("NO RELLENASTE TODOS LOS CAMPOS");
        }
    }

    public void volver(View v){
        Intent i =new Intent(this,Principal.class);
        startActivity(i);
        finish();
    }

    /***********************************************************************/
    /*                                                                     */
    /*                    METODOS CONTENT PROVIDER                         */
    /*                                                                     */
    /***********************************************************************/

    public void update(Inmueble in){
        Uri uri= Contrato.TablaInmueble.CONTENT_URI;
        ContentValues valores = new ContentValues();
        valores.put(Contrato.TablaInmueble.DIRECCION, in.getDireccion());
        valores.put(Contrato.TablaInmueble.TIPO, in.getTipo());
        valores.put(Contrato.TablaInmueble.PRECIO, in.getPrecio());
        valores.put(Contrato.TablaInmueble.USUARIO, in.getUsuario());
        String where=Contrato.TablaInmueble._ID+" =?";
        String[] args=new String[]{in.getId()+""};
        int i = getContentResolver().update(uri, valores, where, args);
    }

}
