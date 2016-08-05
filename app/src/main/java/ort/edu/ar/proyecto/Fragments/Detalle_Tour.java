package ort.edu.ar.proyecto.Fragments;

import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.FragmentDetalle;
import ort.edu.ar.proyecto.Fragments.FragmentMapa;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class Detalle_Tour extends Fragment {

    static public int REQUEST_LIST = 2;
    ArrayList<Tour> tours;
    int posicion;
    ArrayList<Punto> puntos;
    Tour tour;
    Usuario usuario;
    private FragmentTabHost tabHost;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Bundle extras = getIntent().getExtras();
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
        } else { return; }*/

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detalle__tour, container, false);
        tabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("tab1").setIndicator("Detalle", null),
                FragmentDetalle.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("tab2").setIndicator("Mapa", null),
                FragmentMapa.class, null);
        return v;
    }



    /*public void mandarUsuario(){
        Intent intent = new Intent(getActivity(), Perfil_Usuario.class);
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

    public void setPos (int pos){
        posicion=pos;
    }

    public Tour getTour(){ return tour; }

    public void setTour(Tour t){
        tour = t;
    }

    public void setUsuario(Usuario usu){
        usuario = usu;
    }

    public Usuario getUsuario(){ return usuario; }*/

}
