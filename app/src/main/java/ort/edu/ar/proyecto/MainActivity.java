package ort.edu.ar.proyecto;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import ort.edu.ar.proyecto.Fragments.FragmentCrear;
import ort.edu.ar.proyecto.Fragments.FragmentCrearPuntos;
import ort.edu.ar.proyecto.Fragments.FragmentPrevisualizar;
import ort.edu.ar.proyecto.Fragments.FragmentPrevisualizarPuntos;
import ort.edu.ar.proyecto.Fragments.Perfil_Usuario;
import ort.edu.ar.proyecto.model.AutocompleteCustomArrayAdapter;
import ort.edu.ar.proyecto.model.CustomAutoCompleteView;
import ort.edu.ar.proyecto.model.Dia;
import ort.edu.ar.proyecto.model.DiaAdapter;
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
    ArrayList<Gusto> gustos;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Punto> puntos;
    Usuario usuario;
    SessionManager session;
    NavigationView navigationView;
    String miId;
    int idUsuario;
    ArrayList<Tour> ToursLikeadosUsuario;
    ArrayList<Punto> arraypuntoscreando;
    FHome HomeFragment;
    FragmentBuscar fbusqueda;
    boolean estado;
    Tour tourcreando;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    AutocompleteCustomArrayAdapter adapterAutocomplete;
    CustomAutoCompleteView myAutocomplete;
    static Address dirPunto;
    int cantDias;
    ort.edu.ar.proyecto.model.Dia Dia;
    ArrayList<Dia> dias;
    DiaAdapter adapterDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        inicializarToolbar();
        fbusqueda = new FragmentBuscar();
        HomeFragment = new FHome();
        arraypuntoscreando=new ArrayList<>();

        String url = "http://viajarort.azurewebsites.net/gustos.php";
        new GustosTask().execute(url);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenido, HomeFragment)
                .addToBackStack(null)
                .commit();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navUsuario = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_username);

        session = new SessionManager(this);

        //usu = navigationView.getMenu().findItem(R.id.nav_user);
        //login = navigationView.getMenu().findItem(R.id.login);
        //logout = navigationView.getMenu().findItem(R.id.logout);
        if (session.checkLogin() == 0) {
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

        toursUsuarioAL = new ArrayList<>();
        ToursLikeadosUsuario = new ArrayList<>();
        dias = new ArrayList<>();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void IraDetalle(Tour tour) {
        Detalle_Tour fragment = new Detalle_Tour();
        //fragment.setTour(tour);
        this.tour = tour;
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public void IraCrearPuntos(){
        FragmentCrearPuntos fragment = new FragmentCrearPuntos();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public void IraPrevisualizar(){
        FragmentPrevisualizar fragment = new FragmentPrevisualizar();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public void IraCrear(){
        FragmentCrear fragment = new FragmentCrear();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour t) {
        tour = t;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estad) {
        estado = estad;
    }

    public ArrayList<Tour> getTours() {
        return tours;
    }

    /*public int getPos(){
        return posicion;
    }*/
    public void setPuntos(ArrayList<Punto> puntos) {
        tour.setPuntos(puntos);
    }
    public void agregarPuntoCreando(Punto punto){
        arraypuntoscreando.add(punto);
    }

    public ArrayList<Punto> getListaPuntoscreando(){
        return  arraypuntoscreando;
    }
    public void setPuntoscreando(ArrayList<Punto> puntos){
        arraypuntoscreando=puntos;
    }

    public ArrayList<Punto> getPuntos() {

        puntos = tour.getPuntos();
        return puntos;
    }

    public void setToursUsuarioAL(ArrayList<Tour> toursUsuario) {
        toursUsuarioAL = toursUsuario;
    }

    public ArrayList<Tour> getToursUsuarioAL() {
        return toursUsuarioAL;
    }

    public void setToursLikeadosUsuario(ArrayList<Tour> toursLikeados) {
        ToursLikeadosUsuario = toursLikeados;
    }

    public ArrayList<Tour> getToursLikeadosUsuario() {
        return ToursLikeadosUsuario;
    }

    public void IraHome() {


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, HomeFragment)
                .commit();
    }
    public void IraHomeRefresh() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, HomeFragment)
                .commit();
        HomeFragment.refrescar();
    }

     public void IraBusqueda() {

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fbusqueda)
                .commit();
    }

    public void IraPrevisualizarPuntos(){
        FragmentPrevisualizarPuntos fragment = new FragmentPrevisualizarPuntos();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public void setDia(Dia dia){
        Dia = dia;
    }

    public Dia getDia (){
        return Dia;
    }

    public void setArrayDias(ArrayList<Dia> diass){
        dias = diass;
    }

    public ArrayList<Dia> getArrayDias(){
        return dias;
    }

    public void mandarUsuario() {
        Perfil_Usuario fragment = new Perfil_Usuario();
        this.tour = tour;
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
                .commit();
    }

    public void setUsuario(Usuario usu) {
        usuario = usu;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int id) {
        idUsuario = id;
    }

    public void setTours(ArrayList<Tour> tourss) {
        tours = tourss;
    }

    public void setTourCreando(Tour t){
        tourcreando=t;
    }
    public Tour getTourcreando(){
        return tourcreando;
    }

    public ArrayList<Gusto> getGustos(){
        return gustos;
    }

    public void setAdapterAutocomplete(AutocompleteCustomArrayAdapter adapter){
        adapterAutocomplete = adapter;
    }

    public AutocompleteCustomArrayAdapter getAdapterAutocomplete(){
        return adapterAutocomplete;
    }

    public void setAutocomplete(CustomAutoCompleteView autocomplete){
        myAutocomplete = autocomplete;
    }

    public CustomAutoCompleteView getAutocomplete(){
        return myAutocomplete;
    }

    public static void setDireccionPunto (Address dir){
        dirPunto = dir;
    }

    public static Address getDireccionPunto(){
        return dirPunto;
    }

    public void setCantidadDiasTour(int cant){
        cantDias = cant;
    }

    public int getCantidadDiasTour(){
        return cantDias;
    }

    public void setAdapterDias(DiaAdapter adap){
        adapterDia = adap;
    }

    public DiaAdapter getAdapterDias(){
        return adapterDia;
    }

    public void IraLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void IraCerrarSesion() {
        Intent intent = new Intent(this, CerrarSesion.class);
        startActivity(intent);
    }

    public void IraMiPerfil() {
        idUsuario = Integer.parseInt(session.getUserDetails().get(100)[2]);
        this.setIdUsuario(idUsuario);
        Perfil_Usuario fragment = new Perfil_Usuario();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contenido, fragment)
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

    public void btnBusqueda(View v) {
        IraBusqueda();
    }

    public void btnHome(View v) {
        IraHome();
    }

    public void btnMiPerfil(View v) {
        Log.d("ir a", "mi perfil");
        if (session.checkLogin() == 1) {
            IraMiPerfil();
        } else {
            Toast mensaje = Toast.makeText(getApplicationContext(), "Inicie sesion", Toast.LENGTH_SHORT);
            mensaje.show();
            IraLogin();
        }
    }

    private void setearListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.login:
                        Log.d("Choose:", "Login");
                        IraLogin();
                        break;
                    case R.id.logout:
                        //mostrar cuando ya inicio sesion
                        Log.d("Choose:", "Logout");
                        IraCerrarSesion();
                        break;
                    case R.id.crear:
                        Log.d("Choose:", "Crear");
                        dias = new ArrayList<>();
                        cantDias = 0;
                        setArrayDias(dias);
                        IraCrear();
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ort.edu.ar.proyecto/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://ort.edu.ar.proyecto/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
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
            gustos = new ArrayList<>();
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
