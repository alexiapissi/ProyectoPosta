package ort.edu.ar.proyecto.model;

import java.io.Serializable;

/**
 * Created by 41400475 on 22/04/2016.
 */
public class Tour implements Serializable{

    public String Nombre;
    public String Descripcion;
    public String Foto;
    public String Ubicacion;
    public int Id;
    public String Likes;
    public Usuario Usuario;


    public  Tour(String nombre, String descripcion, String foto, String ubicacion, int id, String Likes, Usuario usuario){
        this.Nombre = nombre;
        this.Descripcion = descripcion;
        this.Foto = foto;
        this.Ubicacion = ubicacion;
        this.Id = id;
        this.Likes = Likes;
        this.Usuario = usuario;
    }


    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }

    public String getUbicacion() {
        return Ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        Ubicacion = ubicacion;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public ort.edu.ar.proyecto.model.Usuario getUsuario() {
        return Usuario;
    }

    public void setUsuario(ort.edu.ar.proyecto.model.Usuario usuario) {
        Usuario = usuario;
    }

}
