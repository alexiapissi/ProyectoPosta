package ort.edu.ar.proyecto;

import android.content.ClipData;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ort.edu.ar.proyecto.Fragments.Detalle_Tour;
import ort.edu.ar.proyecto.Fragments.FBusqueda;
import ort.edu.ar.proyecto.Fragments.Perfil_Usuario;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    TextView navUsuario;
    Tour tour;
    ArrayList<Tour> tours;
    //int posicion;
    ArrayList<Punto> puntos;
    Usuario usuario;
    SessionManager session;
    NavigationView navigationView;
    MenuItem usu, login, logout;
    Fragment Busquedafragment;
    String miId;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        inicializarToolbar();
        Busquedafragment = new FBusqueda();
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenido,Busquedafragment)
                .addToBackStack(null)
                .commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navUsuario = (TextView)navigationView.getHeaderView(0).findViewById(R.id.nav_username);

        session = new SessionManager(this);

        //usu = navigationView.getMenu().findItem(R.id.nav_user);
        //login = navigationView.getMenu().findItem(R.id.login);
        //logout = navigationView.getMenu().findItem(R.id.logout);
        if (session.checkLogin() == 0){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_items_login);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_items_logout);
            String email = session.getUserDetails().get(100)[0];
            navUsuario.setText(email);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            miId = extras.getString("miId");
        }

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
    /*public int getPos(){
        return posicion;
    }*/
    public void setPuntos(ArrayList<Punto> puntos) {
        tour.setPuntos(puntos);
    }
    public ArrayList<Punto> getPuntos(){

        puntos=tour.getPuntos();
        return puntos;
    }

    /*public void setPos (int pos){
        posicion=pos;
    }*/

    public void IraBusqueda() {

        //fragment.setTour(tour);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, Busquedafragment)
                .commit();
    }


    public void mandarUsuario(){
        Perfil_Usuario fragment = new Perfil_Usuario();
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

    public int getIdUsuario() { return idUsuario; }

    public void setIdUsuario(int id) { idUsuario = id; }

    public void setTours (ArrayList<Tour> tourss){
        tours = tourss;
    }

    public void IraLogin() {
        Intent intent= new Intent(this,Login.class);
        startActivity(intent);
    }

    public void IraCerrarSesion() {
        Intent intent= new Intent(this,CerrarSesion.class);
        startActivity(intent);
    }


    public void IraMiPerfil(){
        idUsuario = Integer.parseInt(session.getUserDetails().get(100)[2]);
        this.setIdUsuario(idUsuario);
        Perfil_Usuario fragment = new Perfil_Usuario();
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido,fragment)
                .commit();
    }


    private void inicializarToolbar() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        setearListener(navigationView);
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
                        //mostrar cuando inicio sesion
                        Log.d("Choose:","user");
                        IraMiPerfil();
                        break;
                    case R.id.login:
                        Log.d("Choose:","Login");
                        IraLogin();
                        break;
                    case R.id.logout:
                        //mostrar cuando ya inicio sesion
                        Log.d("Choose:","Logout");
                        IraCerrarSesion();
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
