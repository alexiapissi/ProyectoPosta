package ort.edu.ar.proyecto.model;


import java.io.Serializable;

/**
 * Created by 41400475 on 22/04/2016.
 */
public class Usuario implements Serializable{

    public String Nombre;
    public String Foto;
    public int Id;


    public Usuario(String nombre, String foto, int id) {
        this.Nombre = nombre;
        this.Foto = foto;
        this.Id = id;
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
}


