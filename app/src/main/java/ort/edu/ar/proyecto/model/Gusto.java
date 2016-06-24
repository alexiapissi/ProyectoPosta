package ort.edu.ar.proyecto.model;

import java.io.Serializable;

/**
 * Created by 41400475 on 3/6/2016.
 */
public class Gusto implements Serializable {
    int Id;
    String Nombre;

    public Gusto(int id, String nombre) {
        Id = id;
        Nombre = nombre;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
