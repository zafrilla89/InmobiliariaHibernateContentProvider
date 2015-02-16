package com.izv.android.inmobiliaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ZAFRA on 27/11/2014.
 */
public class Ayudante extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "inmobiliaria.db";
    public static final int DATABASE_VERSION = 2;

    public Ayudante(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table "+Contrato.TablaInmueble.TABLA+
                " ("+ Contrato.TablaInmueble._ID+
                " integer primary key autoincrement, "+
                Contrato.TablaInmueble.DIRECCION+" text, "+
                Contrato.TablaInmueble.TIPO+" text, "+
                Contrato.TablaInmueble.PRECIO+" text,"+
                Contrato.TablaInmueble.SUBIDO+" text,"+
                Contrato.TablaInmueble.USUARIO+" text) ";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion) {

    }


    }