package ort.edu.ar.proyecto;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.FragmentDetalle;
import ort.edu.ar.proyecto.Fragments.FragmentMapa;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class Detalle_Tour extends AppCompatActivity {

    static public int REQUEST_LIST = 2;
    ArrayList<Tour> tours;
    int posicion;
    ArrayList<Punto> puntos;
    Tour tour;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__tour);

        inicializarTabs();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tours = (ArrayList<Tour>) extras.getSerializable("listatours");
            if (extras.containsKey("pos")){
                posicion = extras.getInt("pos");
                tour = tours.get(posicion);
            } else {
                int id = extras.getInt("idTour");
                for (Tour t : tours){
                    if (t.getId() == id){
                        tour = t;
                    }
                }
            }
        } else { return; }

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

    public void mandarUsuario(){
        Intent intent = new Intent(getApplicationContext(), Perfil_Usuario.class);
        intent.putExtra("usuario", this.getUsuario());
        intent.putExtra("listatours", tours);
        startActivity(intent);
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

    public Tour getTour(){ return tour; }

    public void setTour(Tour t){
        tour = t;
    }

    public void setUsuario(Usuario usu){
        usuario = usu;
    }

    public Usuario getUsuario(){ return usuario; }

}
