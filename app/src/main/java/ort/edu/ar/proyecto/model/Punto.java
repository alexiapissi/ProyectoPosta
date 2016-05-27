package ort.edu.ar.proyecto.model;

import java.io.Serializable;

/**
 * Created by 41400475 on 27/5/2016.
 */
public class Punto implements Serializable {
    int Id;
    int Longitud;
    int Latitud;
    String Foto;
    String Direccion;
    String Nombre;
    int idTour;
    String fotoGoogle;
    String idGoogle;
    String Descripcion;
    int Dia;

    public Punto(int id, int longitud, int latitud, String direccion, String foto, String nombre, int idTour, String fotoGoogle, String idGoogle, String descripcion, int dia) {
        Id = id;
        Longitud = longitud;
        Latitud = latitud;
        Direccion = direccion;
        Foto = foto;
        Nombre = nombre;
        this.idTour = idTour;
        this.fotoGoogle = fotoGoogle;
        this.idGoogle = idGoogle;
        Descripcion = descripcion;
        Dia = dia;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getLongitud() {
        return Longitud;
    }

    public void setLongitud(int longitud) {
        Longitud = longitud;
    }

    public int getLatitud() {
        return Latitud;
    }

    public void setLatitud(int latitud) {
        Latitud = latitud;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFotoGoogle() {
        return fotoGoogle;
    }

    public void setFotoGoogle(String fotoGoogle) {
        this.fotoGoogle = fotoGoogle;
    }

    public int getIdTour() {
        return idTour;
    }

    public void setIdTour(int idTour) {
        this.idTour = idTour;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public void setIdGoogle(String idGoogle) {
        this.idGoogle = idGoogle;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public int getDia() {
        return Dia;
    }

    public void setDia(int dia) {
        Dia = dia;
    }
}
