package ort.edu.ar.proyecto;

import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursAdapter;
import ort.edu.ar.proyecto.model.Usuario;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class Busqueda extends AppCompatActivity {

    ImageView foto;
    ImageView fotoUsuario;
    TextView nombre;
    TextView descripcion;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageView likes;
    TextView cantLikes;
    ListView listVW;

    ToursAdapter toursAdapter;
    ArrayList<Tour> tours;
    ArrayList<Gusto> gustos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);

        listVW = (ListView) findViewById(R.id.listv);

        foto = (ImageView) findViewById(R.id.FotoTour);
        fotoUsuario = (ImageView) findViewById(R.id.FotoUsuario);
        nombre = (TextView) findViewById(R.id.NombreTour);
        descripcion = (TextView) findViewById(R.id.DescripcionTour);
        ubicacion = (TextView) findViewById(R.id.UbicacionTour);
        nombreUsuario = (TextView) findViewById(R.id.NombreUsuario);
        cantLikes = (TextView) findViewById(R.id.cantlikes);

        likes = (ImageView) findViewById(R.id.Like);
        //likes.setImageResource(R.drawable.likes);

        listVW.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick (AdapterView<?> adapter, View V, int position, long l) {
                Intent intent = new Intent(Busqueda.this, Detalle_Tour.class);
                intent.putExtra("pos", position);
                intent.putExtra("listatours", tours);
                startActivity(intent);
            }
        });
        tours = new ArrayList<>();
        toursAdapter = new ToursAdapter(getApplicationContext(), tours);
        listVW.setAdapter(toursAdapter);

    }





    @Override
    public void onStart() {
        super.onStart();
        String url = "http://viajarort.azurewebsites.net/tours.php";
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

                Usuario usu = new Usuario(nomUsuario, fotoUsuario, idUsuario, "", null);

                gustos=new ArrayList<>();
                JSONArray jsongustos = jsonResultado.getJSONArray("Gusto");
                for (int j = 0; j < jsongustos.length(); j++) {
                    JSONObject jsonresultadoGustos = jsongustos.getJSONObject(j);
                    int jsonIdGusto = jsonresultadoGustos.getInt("Id");
                    String jsonnombregustos = jsonresultadoGustos.getString("Nombre");
                    Gusto gus= new Gusto(jsonIdGusto,jsonnombregustos);
                    gustos.add(gus);
                }

                Tour t = new Tour(jsonNombre, jsonDescripcion, jsonFoto, jsonUbicacion, jsonId, jsonLikes, usu, null, gustos);
                tours.add(t);
            }
            return tours;
        }

    }

}






