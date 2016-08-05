package ort.edu.ar.proyecto;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.Detalle_Tour;
import ort.edu.ar.proyecto.Fragments.FBusqueda;
import ort.edu.ar.proyecto.Fragments.Perfil_Usuario;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    TextView navUsuario;
    Tour tour;
    ArrayList<Tour> tours;
    int posicion;
    ArrayList<Punto> puntos;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        inicializarToolbar();
        Fragment fragment = new FBusqueda();
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenido,fragment)
                .addToBackStack(null)
                .commit();


    }

    public void IraDetalle(Tour tour){
        Detalle_Tour fragment = new Detalle_Tour();
        //fragment.setTour(tour);
        this.tour=tour;
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido,fragment)
                .commit();
    }
    public Tour getTour(){ return tour; }
    public void setTour (Tour t){
        tour=t;
    }
    public ArrayList<Tour> getTours(){
        return tours;
    }
    public int getPos(){
        return posicion;
    }
    public void setPuntos(ArrayList<Punto> puntos) {
        tour.setPuntos(puntos);
    }
    public ArrayList<Punto> getPuntos(){

        puntos=tour.getPuntos();
        return puntos;
    }

    public void setPos (int pos){
        posicion=pos;
    }

    public void IraBusqueda() {
        FBusqueda fragment = new FBusqueda();
        //fragment.setTour(tour);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }


    public void mandarUsuario(){
        /*Intent intent = new Intent(this, Perfil_Usuario.class);
        intent.putExtra("usuario", this.getUsuario());
        intent.putExtra("listatours", tours);
        startActivity(intent);*/
        Perfil_Usuario fragment = new Perfil_Usuario();
        //fragment.setTour(tour);
        this.tour=tour;
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido,fragment)
                .commit();
    }

    public void setUsuario(Usuario usu){
        usuario = usu;
    }

    public Usuario getUsuario(){ return usuario; }



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


    private void setearListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        Log.d("Choose:","Home");
                        IraBusqueda();
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
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
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
}
