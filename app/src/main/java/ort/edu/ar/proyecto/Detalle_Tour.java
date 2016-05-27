package ort.edu.ar.proyecto;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.PuntosAdapter;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class Detalle_Tour extends AppCompatActivity {

    static public int REQUEST_LIST=2;

    ImageView fototour;
    ImageView fotoUsuario;
    TextView nombre;
    TextView descripcion;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageView likes;
    TextView cantLikes;
    ListView listPuntosVW;
    PuntosAdapter puntosAdapter;
    ArrayList<Punto> puntos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle__tour);

        fototour = (ImageView) findViewById(R.id.Fototourd);
        fotoUsuario = (ImageView) findViewById(R.id.FotoUsuariod);
        nombre = (TextView) findViewById(R.id.NombreTourd);
        //descripcion = (TextView) findViewById(R.id.DescripcionTourd);
        ubicacion = (TextView) findViewById(R.id.UbicacionTourd);
        nombreUsuario = (TextView) findViewById(R.id.NombreUsuariod);
        cantLikes = (TextView) findViewById(R.id.cantlikesd);
        likes = (ImageView) findViewById(R.id.Liked);
        listPuntosVW = (ListView) findViewById(R.id.listPuntos);

        puntos = new ArrayList<>();
        puntosAdapter = new PuntosAdapter(getApplicationContext(), puntos);
        listPuntosVW.setAdapter(puntosAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        ArrayList<Tour> tours = (ArrayList<Tour>) extras.getSerializable("listatours");
        int posicion = extras.getInt("pos");
        Tour tour=tours.get(posicion);
        nombre.setText(tour.getNombre());
        Picasso
                .with(getApplicationContext())
                .load(tour.getFoto())
                .resize(700,250)
                .into(fototour);
        Picasso
                .with(getApplicationContext())
                .load(tour.getUsuario().getFoto())
                //.resize(40,40)
                .transform(new CircleTransform())
                .into(fotoUsuario);
        //descripcion.setText(tour.getDescripcion());
        ubicacion.setText(tour.getUbicacion());
        nombreUsuario.setText(tour.getUsuario().getNombre());
        cantLikes.setText(tour.getLikes());

    }

    //HACER TODO LO DE ABAJO JEJEJE

    /*
    @Override
    public void onStart() {
        super.onStart();
        String url = "http://10.152.2.37/Proyecto-Backend/detalletour.php";
        new ToursTask().execute(url);  // Llamo a clase async con url
    }

    private class ToursTask extends AsyncTask<String, Void, ArrayList<Tour>> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(ArrayList<Tour> resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                tours.clear();
                tours.addAll(resultado);
                toursAdapter.notifyDataSetChanged();
            }
        }


        @Override
        protected ArrayList<Tour> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return parsearResultado(response.body().string());      // Convierto el resultado en ArrayList<Direccion>
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return new ArrayList<Tour>();
            }
        }

        // Convierte un JSON en un ArrayList de Direccion
        ArrayList<Tour> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Tour> tours = new ArrayList<>();
            JSONArray jsonTours = new JSONArray(JSONstr);
            for (int i = 0; i < jsonTours.length(); i++) {
                JSONObject jsonResultado = jsonTours.getJSONObject(i);
                int jsonId = jsonResultado.getInt("Id");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonUbicacion = jsonResultado.getString("Ubicacion");
                String jsonFoto = jsonResultado.getString("FotoURL");
                String jsonLikes = jsonResultado.getString("Likes");
                String jsonDescripcion = jsonResultado.getString("Descripcion");

                JSONObject jsonResultadoUsuario = jsonResultado.getJSONObject("Usuario");
                int idUsuario = jsonResultadoUsuario.getInt("Id");
                String nomUsuario = jsonResultadoUsuario.getString("Nombre");
                String fotoUsuario = jsonResultadoUsuario.getString("FotoURL");

                Usuario usu = new Usuario(nomUsuario, fotoUsuario, idUsuario);
                Tour t = new Tour(jsonNombre, jsonDescripcion, jsonFoto, jsonUbicacion, jsonId, jsonLikes, usu);
                tours.add(t);
            }
            return tours;
        }

    }

*/


}
