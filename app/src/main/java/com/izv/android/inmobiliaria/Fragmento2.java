package com.izv.android.inmobiliaria;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmento2 extends Fragment {
    private Bitmap myBitmap;



    public Fragmento2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmento2, container, false);
    }

    public void ponerimagen(Adaptador ad, File archivo, ArrayList<Inmueble> lista, int posicion, ArrayList<Bitmap> lis){
        Inmueble i=lista.get(posicion);
        String id=""+i.getId();
        String[] listaDirectorio = archivo.list();
        for (int x=0;x<listaDirectorio.length;x++){
            String archi=listaDirectorio[x];
            if (archi.indexOf("inmobiliaria_"+id) != -1){
                myBitmap = BitmapFactory.decodeFile(archivo.getAbsolutePath()+"/"+archi);
                lis.add(myBitmap);
            }
        }
        ad.notifyDataSetChanged();
    }

}
