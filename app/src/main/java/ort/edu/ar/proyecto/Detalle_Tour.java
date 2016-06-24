package ort.edu.ar.proyecto;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.FragmentDetalle;
import ort.edu.ar.proyecto.Fragments.FragmentMapa;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;

public class Detalle_Tour extends AppCompatActivity {

    static public int REQUEST_LIST = 2;
    ArrayList<Tour> tours;
    int posicion;
    ArrayList<Punto> puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__tour);

        inicializarTabs();

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return ;
        }
        tours = (ArrayList<Tour>) extras.getSerializable("listatours");
         posicion = extras.getInt("pos");

    }

    private FragmentTabHost tabHost;
    private void inicializarTabs() {
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("tab1").setIndicator("Detalle", null),
                FragmentDetalle.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("tab2").setIndicator("Mapa", null),
                FragmentMapa.class, null);
    }

    public ArrayList<Tour> getTours(){
        return tours;
    }
    public int getPos(){
        return posicion;
    }
    public void setPuntos(ArrayList<Punto> puntos) {
        (tours.get(posicion)).setPuntos(puntos);
    }
    public ArrayList<Punto> getPuntos(){

        puntos=(tours.get(posicion)).getPuntos();
        return puntos;
    }

}
