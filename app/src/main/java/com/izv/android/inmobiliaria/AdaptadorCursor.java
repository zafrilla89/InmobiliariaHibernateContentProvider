package com.izv.android.inmobiliaria;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by ZAFRA on 27/11/2014.
 */
public class AdaptadorCursor extends CursorAdapter {
    private Cursor c;
    public AdaptadorCursor(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup vg) {
        LayoutInflater i = LayoutInflater.from(vg.getContext());
        View v = i.inflate(R.layout.item, vg, false);
        return v;
    }

    @Override
    public void bindView(View v, Context co, Cursor c) {
        Inmueble in=new Inmueble();
        in= getRow(c);
        TextView tvdireccion, tvtipo, tvprecio, tvsubido;
        tvdireccion = (TextView) v.findViewById(R.id.tvdireccion);
        tvtipo =(TextView)v.findViewById(R.id.tvtipo);
        tvprecio=(TextView)v.findViewById(R.id.tvprecio);
        tvsubido=(TextView)v.findViewById(R.id.tvsubido);
        tvdireccion.setText(in.getDireccion());
        tvtipo.setText(in.getTipo());
        tvprecio.setText(in.getPrecio()+"€");
        if (in.getSubido().compareTo("0")==0){
            tvsubido.setText("NO");
        }else{
            tvsubido.setText("SI");
        }
    }

    public static Inmueble getRow(Cursor c) {
        Inmueble in = new Inmueble();
        in.setId(c.getInt(0));
        in.setDireccion(c.getString(1));;
        in.setTipo(c.getString(2));
        in.setPrecio(c.getString(3));
        in.setSubido(c.getString(4));
        return in;
    }
}

