package com.izv.android.inmobiliaria;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by ZAFRA on 16/01/2015.
 */
public class ProveedorInmueble extends ContentProvider{
    static String AUTORIDAD = "com.izv.android.inmobiliaria.proveedorinmueble";
    private Ayudante abd;
    private static final UriMatcher convierteUri2Int;
    private static final int INMUEBLES = 1, INMUEBLE_ID=2;

    static {
        convierteUri2Int = new UriMatcher(UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(AUTORIDAD,
                Contrato.TablaInmueble.TABLA, INMUEBLES);
        convierteUri2Int.addURI(AUTORIDAD,
                Contrato.TablaInmueble.TABLA + "/#", INMUEBLE_ID);
    }

    public ProveedorInmueble() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase bd = abd.getWritableDatabase();
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                break;
            case INMUEBLE_ID:
                selection = Contrato.TablaInmueble._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
        int n= bd.delete(Contrato.TablaInmueble.TABLA, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return n;
    }

    @Override
    public String getType(Uri uri) {
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                return Contrato.TablaInmueble.CONTEXT_TYPE_INMUEBLES;
            case INMUEBLE_ID:
                return Contrato.TablaInmueble.CONTEXT_TYPE_INMUEBLE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (convierteUri2Int.match(uri) != INMUEBLES){
            throw new IllegalArgumentException("URI " + uri);
        }
        SQLiteDatabase bd = abd.getWritableDatabase();
        long id = bd.insert(Contrato.TablaInmueble.TABLA, null, values);
        if (id > 0) {
            Uri uriElemento = ContentUris.withAppendedId(Contrato.TablaInmueble.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(uriElemento,null);
            return uriElemento;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        abd = new Ayudante(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                break;
            case INMUEBLE_ID:
                selection = Contrato.TablaInmueble._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
        SQLiteDatabase bd = abd.getReadableDatabase();
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int numero=0;
        SQLiteDatabase bd = abd.getReadableDatabase();
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                break;
            case INMUEBLE_ID:
                selection = Contrato.TablaInmueble._ID + " = ?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
        numero=bd.update(Contrato.TablaInmueble.TABLA, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri,null);
        return numero;
    }

}
