package com.izv.android.inmobiliaria;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by 2dam on 28/11/2014.
 */
public class Adaptador extends ArrayAdapter<Bitmap> {

    private Context contexto;
    private ArrayList<Bitmap> lista;
    private int recurso;
    static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Bitmap> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.lista=objects;
        this.recurso=resource;
        this.i=(LayoutInflater)contexto.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder{
        public ImageView ivfoto;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.ivfoto=(ImageView)convertView.findViewById(R.id.iv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Bitmap i;
        i=lista.get(position);
        vh.ivfoto.setImageBitmap(i);
        return convertView;
    }
}
