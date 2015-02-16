package com.izv.android.inmobiliaria;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ZAFRA on 27/11/2014.
 */
public class Contrato {

    private Contrato(){

    }

    public static abstract class TablaInmueble implements BaseColumns {
        public static final String TABLA = "inmueble";
        public static final String DIRECCION = "direccion";
        public static final String TIPO = "tipo";
        public static final String PRECIO = "precio";
        public static final String SUBIDO = "subido";
        public static final String USUARIO = "usuario";
        public static final String CONTEXT_TYPE_INMUEBLE=
                "vnd.android.cursor.dir/vnd.izv.inmueble";
        public static final String CONTEXT_TYPE_INMUEBLES=
                "vnd.android.cursor.item.dir/vnd.inmuebles";
        public static final Uri CONTENT_URI =Uri.parse("content://" +
                ProveedorInmueble.AUTORIDAD + "/" + TABLA);
    }
}


