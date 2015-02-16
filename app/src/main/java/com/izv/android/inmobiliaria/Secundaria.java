package com.izv.android.inmobiliaria;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class Secundaria extends Activity {

    private ArrayList<Inmueble> lista;

    /***********************************************************************/
    /*                                                                     */
    /*                              METODOS ON                             */
    /*                                                                     */
    /***********************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_secundaria);
        lista = getIntent().getExtras().getParcelableArrayList("datos");
        int i=getIntent().getExtras().getInt("index");
        Fragmento2 f2= (Fragmento2) getFragmentManager().findFragmentById(R.id.fragment4);
        File archivo = new File(String.valueOf(getExternalFilesDir(null)));
        ArrayList<Bitmap> lis=new ArrayList<Bitmap>();
        Adaptador ad = new Adaptador(Secundaria.this,R.layout.itemfoto,lis);
        ListView lv= (ListView)findViewById(R.id.lvfotos);
        lv.setAdapter(ad);
        f2.ponerimagen(ad,archivo,lista, i,lis);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        finish();
    }


}
