package ort.edu.ar.proyecto.model;

import java.util.ArrayList;

/**
 * Created by 41400475 on 21/10/2016.
 */
public class Dia {

    int dia;
    ArrayList<Punto> puntos;

    public Dia(int dia, ArrayList<Punto> puntos) {
        this.dia = dia;
        this.puntos = puntos;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }




}
