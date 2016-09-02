package ort.edu.ar.proyecto.Fragments;



import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursAdapter;
import ort.edu.ar.proyecto.model.Usuario;


public class FHome extends Fragment {
    ImageView foto;
    ImageView fotoUsuario;
    TextView nombre;
    TextView descripcion;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageView likes;
    TextView cantLikes;
    ListView listVW;
    ProgressBar cargando;
    ToursAdapter toursAdapter;
    ArrayList<Tour> tours;
    ArrayList<Gusto> gustos;
    SwipeRefreshLayout swipeLayout;
    boolean actualiza=false;
    View v;
    public FHome(){


    }

    @Override
    public void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v == null) {
            setHasOptionsMenu(true);
            v = inflater.inflate(R.layout.layouthome, container, false);

            listVW = (ListView) v.findViewById(R.id.listv);

            foto = (ImageView) v.findViewById(R.id.FotoTour);
            fotoUsuario = (ImageView) v.findViewById(R.id.FotoUsuario);
            nombre = (TextView) v.findViewById(R.id.NombreTour);
            swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
            descripcion = (TextView) v.findViewById(R.id.DescripcionTour);
            ubicacion = (TextView) v.findViewById(R.id.UbicacionTour);
            nombreUsuario = (TextView) v.findViewById(R.id.NombreUsuario);
            cantLikes = (TextView) v.findViewById(R.id.cantlikes);
            likes = (ImageView) v.findViewById(R.id.Like);
            cargando = (ProgressBar) v.findViewById(R.id.progress);
            //likes.setImageResource(R.drawable.likes);

            swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    actualiza=true;
                    new ToursTask().execute("http://viajarort.azurewebsites.net/tours.php");
                }
            });


            listVW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View V, int position, long l) {
                /*Intent intent = new Intent(Busqueda.this, Detalle_Tour.class);
                intent.putExtra("pos", position);
                intent.putExtra("listatours", tours);
                startActivity(intent);*/
                    //IR AL OTRO FRAGMENT
                    MainActivity ma = (MainActivity) getActivity();
                    ma.IraDetalle(tours.get(position));
                }
            });
            tours = new ArrayList<>();
            toursAdapter = new ToursAdapter(getActivity(), tours);
            listVW.setAdapter(toursAdapter);
            String url = "http://viajarort.azurewebsites.net/tours.php";
            new ToursTask().execute(url);  // Llamo a clase async con url

            MainActivity ma = (MainActivity)getActivity();
            ma.setTours(tours);
        }
        return v;
    }





    private class ToursTask extends AsyncTask<String, Void, ArrayList<Tour>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            if (!actualiza){
                cargando.setVisibility(View.VISIBLE);
            }else{
                cargando.setVisibility(View.GONE);
                actualiza=false;
            }

        }
        @Override
        protected void onPostExecute(ArrayList<Tour> resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                tours.clear();
                tours.addAll(resultado);
                toursAdapter.notifyDataSetChanged();
                cargando.setVisibility(View.GONE);
                swipeLayout.setRefreshing(false);
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
                return new ArrayList<>();
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
                JSONArray jsongustos = jsonResultado.getJSONArray("Gustos");
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
