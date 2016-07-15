package ort.edu.ar.proyecto;

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
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.FragmentDetalle;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Tour;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    TextView navUsuario;

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
                .commit();

    }
    public void IraDetalle(int pos, ArrayList<Tour> tours){
        Fragment fragment = new FragmentDetalle();
        FragmentManager fm= getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.contenido,fragment)
                .commit();
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

    private void setearListener(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                switch(item.getItemId()) {
                    case R.id.nav_home:
                        Log.d("Choose:","Home");
                        //Intent intent = new Intent(Detalle_Tour.this, Busqueda.class);
                        //startActivity(intent);
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
}
