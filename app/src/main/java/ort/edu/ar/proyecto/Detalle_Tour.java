package ort.edu.ar.proyecto;

import android.content.Intent;
import android.sax.StartElementListener;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
    private DrawerLayout drawerLayout;
    private TextView navUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__tour);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //inicializarToolbar();
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


    private void inicializarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setearListener(navigationView);
        navUsuario = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_username);

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
    private void setearListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        Log.d("Choose:","Home");
                        Intent intent = new Intent(Detalle_Tour.this, Busqueda.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_user:
                        Log.d("Choose:","user");
                        break;
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
