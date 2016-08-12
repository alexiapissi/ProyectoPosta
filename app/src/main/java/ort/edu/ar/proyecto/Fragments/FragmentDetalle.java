package ort.edu.ar.proyecto.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.PuntosAdapter;
import ort.edu.ar.proyecto.model.Tour;


public class FragmentDetalle extends Fragment {


    ImageView fototour;
    ImageButton fotoUsuario;
    TextView nombre;
    //TextView descripcion;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageView likes;
    TextView cantLikes;
    ListView listPuntosVW;
    PuntosAdapter puntosAdapter;
    ArrayList<Punto> puntos;
    Tour tour;
    MainActivity ma;
    ProgressBar progressbar;

    public FragmentDetalle() {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_detalle, container, false);
        fototour = (ImageView) view.findViewById(R.id.Fototourd);
        progressbar=(ProgressBar) view.findViewById(R.id.progress);
        fotoUsuario = (ImageButton) view.findViewById(R.id.FotoUsuariod);
        addListenerOnButton();
        nombre = (TextView) view.findViewById(R.id.NombreTourd);
        //descripcion = (TextView) findViewById(R.id.DescripcionTourd);
        ubicacion = (TextView) view.findViewById(R.id.UbicacionTourd);
        nombreUsuario = (TextView) view.findViewById(R.id.NombreUsuariod);
        cantLikes = (TextView) view.findViewById(R.id.cantlikesd);
        likes = (ImageView) view.findViewById(R.id.Liked);
        listPuntosVW = (ListView) view.findViewById(R.id.listPuntos);

        puntos = new ArrayList<>();
        puntosAdapter = new PuntosAdapter(getActivity().getApplicationContext(), puntos);
        listPuntosVW.setAdapter(puntosAdapter);
        ma = (MainActivity) getActivity();
        tour = ma.getTour();

        nombre.setText(tour.getNombre());
        Picasso
                .with(getActivity().getApplicationContext())
                .load(tour.getFoto())
                //.resize(700, 250)
                .into(fototour);
        Picasso
                .with(getActivity().getApplicationContext())
                .load(tour.getUsuario().getFoto())
                //.resize(40,40)
                .transform(new CircleTransform())
                .into(fotoUsuario);
        //descripcion.setText(tour.getDescripcion());
        ubicacion.setText(tour.getUbicacion());
        nombreUsuario.setText(tour.getUsuario().getNombre());
        cantLikes.setText(tour.getLikes());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = "http://viajarort.azurewebsites.net/detalletour.php?id=";
        url += tour.getId();
        new DetalleTask().execute(url);  // Llamo a clase async con url
        Log.d("HOLA", "llamando url:" + url);
    }

    private class DetalleTask extends AsyncTask<String, Void, ArrayList<Punto>> {
        private OkHttpClient client = new OkHttpClient();
        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Punto> resultado) {

            super.onPostExecute(resultado);
            if (resultado != null) {
                puntos.clear();
                puntos.addAll(resultado);
                puntosAdapter.notifyDataSetChanged();
                progressbar.setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<Punto> doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return parsearResultado(response.body().string());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                //return new Tour(tour.getNombre(),tour.getDescripcion(),tour.getFoto(),tour.getUbicacion(),tour.getId(),tour.getLikes(),tour.getUsuario(),tour.getPuntos(), tour.getGustos());
                return null;
            }
        }

        // Convierte un JSON
        ArrayList<Punto> parsearResultado(String JSONstr) throws JSONException {
            ArrayList<Punto> puntosLocal = new ArrayList<>();
            JSONObject detalle = new JSONObject(JSONstr);
            JSONArray jsonPuntos = detalle.getJSONArray("Puntos");
            for (int i = 0; i < jsonPuntos.length(); i++) {
                JSONObject jsonResultado = jsonPuntos.getJSONObject(i);
                int jsonId = jsonResultado.getInt("Id");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonDireccion = jsonResultado.getString("Direccion");
                String jsonFotoP = jsonResultado.getString("FotoURL");
                double jsonLatitud = jsonResultado.getDouble("Latitud");
                double jsonLongitud = jsonResultado.getDouble("Longitud");
                String jsonDescripcion = jsonResultado.getString("Descripcion");
                int jsonDia = jsonResultado.getInt("Dia");

                Punto p = new Punto(jsonId, jsonLongitud, jsonLatitud, jsonDireccion, jsonFotoP, jsonNombre, 0, "", "", jsonDescripcion, jsonDia);
                puntosLocal.add(p);
            }
            tour.setPuntos(puntosLocal);
            Log.d("HOLA", "parseo listo");
            ma.setPuntos(puntosLocal);
            return puntosLocal;
        }

    }


    public void addListenerOnButton() {
        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //dt.setUsuario(tour.getUsuario());
                ma.setUsuario(tour.getUsuario());
                ma.mandarUsuario();
            }
        });

    }
    /*public void addListenerOnText() {
        nombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.setUsuario(tour.getUsuario());
                ma.mandarUsuario();
            }
        });

    }*/


}

