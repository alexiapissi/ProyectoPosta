package ort.edu.ar.proyecto;

import android.content.Intent;
import android.os.AsyncTask;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.Detalle_Tour;
import ort.edu.ar.proyecto.Fragments.FHome;
import ort.edu.ar.proyecto.Fragments.FragmentBuscar;
import ort.edu.ar.proyecto.Fragments.Perfil_Usuario;
import ort.edu.ar.proyecto.model.Gusto;
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
    Fragment HomeFragment;
    FragmentBuscar fbusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        inicializarToolbar();
        fbusqueda=new FragmentBuscar();
        HomeFragment = new FHome();

        String url = "http://viajarort.azurewebsites.net/gustos.php";
        new GustosTask().execute(url);
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenido,HomeFragment)
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
            navUsuario.setText(session.getUserDetails().get("email"));
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

    public void IraHome() {

        //fragment.setTour(tour);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, HomeFragment)
                .commit();
    }

    public void IraBusqueda(){

        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fbusqueda)
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

    public void btnBusqueda (View v){
        IraBusqueda();
    }
    public void btnHome (View v){
        IraHome();
    }
    public void btnMiPerfil (View v){
        Log.d("ir a", "mi perfil");
    }

    private void setearListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        Log.d("Choose:","Home");
                        IraHome();
                        break;
                    case R.id.nav_user:
                        //mostrar cuando ya inicio sesion

                        Log.d("Choose:","user");
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
                    case R.id.nav_busqueda:
                        IraBusqueda();
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

    private class GustosTask extends AsyncTask<String, Void, ArrayList<Gusto>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPostExecute(ArrayList<Gusto> resultado) {
            super.onPostExecute(resultado);

            if (!resultado.isEmpty()) {
                fbusqueda.Setbusqueda(resultado);

            }
        }

        @Override
        protected ArrayList<Gusto> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return new ArrayList<>();
            }
        }


        // Convierte un JSON en un ArrayList de Direccion
        ArrayList<Gusto> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Gusto> gustos  = new ArrayList<>();
            JSONArray jsonGustos = new JSONArray(JSONstr);
            for (int i = 0; i < jsonGustos.length(); i++) {
                JSONObject jsonResultado = jsonGustos.getJSONObject(i);
                int jsonId = jsonResultado.getInt("Id");
                String jsonNombre = jsonResultado.getString("Nombre");

                Gusto g = new Gusto(jsonId, jsonNombre);
                gustos.add(g);
            }
            return gustos;
        }

    }

}
