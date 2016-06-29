package ort.edu.ar.proyecto.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 41400475 on 22/04/2016.
 */
public class Usuario implements Serializable{

    public String Nombre;
    public String Foto;
    public int Id;
    public String Residencia;
    ArrayList<Tour> ToursCreados;

    public Usuario(String nombre, String foto, int id, String residencia, ArrayList<Tour> toursCreados) {
        this.Nombre = nombre;
        this.Foto = foto;
        this.Id = id;
        this.Residencia = residencia;
        this.ToursCreados = toursCreados;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getResidencia() {
        return Residencia;
    }

    public void setResidencia(String residencia) {
        Residencia = residencia;
    }

    public ArrayList<Tour> getToursCreados() {
        return ToursCreados;
    }

    public void setToursCreados(ArrayList<Tour> toursCreados) {
        ToursCreados = toursCreados;
    }
}


