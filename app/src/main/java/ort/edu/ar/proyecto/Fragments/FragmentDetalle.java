package ort.edu.ar.proyecto.Fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
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
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import ort.edu.ar.proyecto.Login;
import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.PuntosAdapter;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Tour;

/**
 * Created by 41400475 on 10/6/2016.
 */
public class FragmentDetalle extends Fragment {


    ImageView fototour;
    ImageButton fotoUsuario;
    TextView nombre;
    //TextView descripcion;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageButton darlike;
    TextView cantLikes;
    ListView listPuntosVW;
    PuntosAdapter puntosAdapter;
    ArrayList<Punto> puntos;
    ArrayList<Tour> tours;
    int pos;
    Tour tour;
    MainActivity ma;
    ProgressBar progressbar;
    int cantidadLikes;
    SessionManager session;

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
        darlike = (ImageButton) view.findViewById(R.id.Liked);
        listPuntosVW = (ListView) view.findViewById(R.id.listPuntos);

        session = new SessionManager(getContext());

        darlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //que quede el like maracado cuando se va a otro lado
                if (session.checkLogin() == 1){
                    if (darlike.getTag().equals("nolike")){
                        darlike.setImageResource(R.drawable.likes);
                        cantidadLikes = Integer.parseInt(cantLikes.getText().toString());
                        cantidadLikes++;
                        cantLikes.setText(Integer.toString(cantidadLikes));
                        tour.setLikes(Integer.toString(cantidadLikes));
                        darlike.setTag("like");
                        String url = "http://viajarort.azurewebsites.net/actu.php";
                        new EditarTour().execute(url);
                    } else {
                        darlike.setImageResource(R.drawable.nolike);
                        darlike.setTag("nolike");
                        cantidadLikes = Integer.parseInt(cantLikes.getText().toString());
                        cantidadLikes--;
                        cantLikes.setText(Integer.toString(cantidadLikes));
                        tour.setLikes(Integer.toString(cantidadLikes));
                        String url = "http://viajarort.azurewebsites.net/actu.php";
                        new EditarTour().execute(url);
                    }
                } else {
                    Toast mensaje = Toast.makeText(getContext(), "Inicie sesion para dar likes", Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            }
        });

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

    private class EditarTour extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();


        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            Toast registro;
            if (!resultado.isEmpty()) {
                if (resultado.equals("Se actualizo el like correctamente")){
                    //asdas
                } else {
                    //sfs "no se pudo actualizar el like"
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            RequestBody body = generarJSON();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return parsearRespuesta(response.body().string());
            } catch (IOException | JSONException  e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON (){
            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            json.put("Id", tour.getId());
            json.put("Likes", tour.getLikes());

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            String actualizacion = respuesta.getString("Actualizacion");
            return actualizacion;
        }
    }

    public void addListenerOnButton() {
        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //dt.setUsuario(tour.getUsuario());
                ma.setIdUsuario(tour.getUsuario().getId());
                ma.mandarUsuario();
            }
        });

    }
    public void addListenerOnText() {
        nombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.setIdUsuario(tour.getUsuario().getId());
                ma.mandarUsuario();
            }
        });

    }


}

