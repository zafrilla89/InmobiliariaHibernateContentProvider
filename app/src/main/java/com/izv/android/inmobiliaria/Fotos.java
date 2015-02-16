package com.izv.android.inmobiliaria;

import org.json.JSONObject;

/**
 * Created by ZAFRA on 13/02/2015.
 */
public class Fotos {
    int id, idinmueble;
    String nombre;

    public Fotos(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.nombre=object.getString("nombre");
            this.idinmueble = object.getInt("idinmueble");
        } catch (Exception e) {

        }
    }

    @Override
    public String toString() {
        return "Fotos{" +
                "id=" + id +
                ", idinmueble=" + idinmueble +
                ", nombre='" + nombre + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdinmueble() {
        return idinmueble;
    }

    public void setIdinmueble(int idinmueble) {
        this.idinmueble = idinmueble;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
