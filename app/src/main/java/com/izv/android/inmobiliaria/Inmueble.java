package com.izv.android.inmobiliaria;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Created by 2dam on 27/11/2014.
 */
public class Inmueble implements Comparable<Inmueble>, Parcelable{

    private String direccion;
    private String tipo;
    private String precio;
    private String subido;
    private String usuario;
    private int id;

    public Inmueble() {
    }

    public Inmueble(JSONObject object) {
        try {
            this.id = object.getInt("id");
            this.tipo = object.getString("tipo");
            this.precio=object.getString("precio");
            this.usuario=object.getString("usuario");
            this.direccion=object.getString("direccion");
            this.subido="1";
        } catch (Exception e) {

        }
    }

    public Inmueble(String direccion, String tipo, String precio, String subido, String usuario, int id) {
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
        this.subido = subido;
        this.usuario = usuario;
        this.id = id;
    }

    public Inmueble(String direccion, String tipo, String precio, String subido, String usuario) {
        this.direccion = direccion;
        this.tipo = tipo;
        this.precio = precio;
        this.subido = subido;
        this.usuario = usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getSubido() {
        return subido;
    }

    public void setSubido(String subido) {
        this.subido = subido;
    }

    @Override
    public String toString() {
        return "Inmueble{" +
                "direccion='" + direccion + '\'' +
                ", tipo='" + tipo + '\'' +
                ", precio='" + precio + '\'' +
                ", subido='" + subido + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        Inmueble i=(Inmueble)o;
        if (direccion.equalsIgnoreCase(i.getDireccion())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = direccion != null ? direccion.hashCode() : 0;
        result = 31 * result + (tipo != null ? tipo.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        result = 31 * result + (subido != null ? subido.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Inmueble i) {
        if (this.getDireccion().compareToIgnoreCase(i.getDireccion()) != 0) {
            return this.getDireccion().compareToIgnoreCase(i.getDireccion());
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.direccion);
        parcel.writeString(this.tipo);
        parcel.writeString(this.precio);
        parcel.writeString(this.subido);
        parcel.writeString(this.usuario);
        parcel.writeInt(this.id);
    }

    public static final Parcelable.Creator<Inmueble> CREATOR = new Parcelable.Creator<Inmueble>() {

        @Override
        public Inmueble createFromParcel(Parcel p) {
            String direccion = p.readString();
            String tipo = p.readString();
            String precio = p.readString();
            String subido = p.readString();
            String usuario = p.readString();
            int id=p.readInt();
            return new Inmueble(direccion, tipo, precio, subido, usuario, id);
        }

        @Override
        public Inmueble[] newArray(int i) {
            return new Inmueble[i];
        }
    };


}

