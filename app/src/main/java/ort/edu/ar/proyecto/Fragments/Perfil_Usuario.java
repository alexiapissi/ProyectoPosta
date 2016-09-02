package ort.edu.ar.proyecto.Fragments;



import android.os.AsyncTask;

import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class Perfil_Usuario extends Fragment {

    private FragmentTabHost tabHost;
    TextView nombreUsuario;
    ImageView fotoUsuario;
    TextView residenciaUsuario;
    ProgressBar progressbar;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Tour> toursLikeados;
    Usuario usu;
    String resid;
    String nom;
    String foto;
    int id;
    MainActivity ma;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_perfil_usuario,container,false);

        ma= (MainActivity) getActivity();
        id = ma.getIdUsuario();

        String url = "http://viajarort.azurewebsites.net/usuario.php?id=";
        url += id;
        new UsuarioTask().execute(url);

        tabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("tab1").setIndicator("Tours Creados", null),
                FragmentToursCreados.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("tab2").setIndicator("Tours Likeados", null),
                FragmentToursLikeados.class, null);

        toursUsuarioAL = new ArrayList<>();
        toursLikeados = new ArrayList<>();
        //progressbar=(ProgressBar)v.findViewById(R.id.progress);
        nombreUsuario = (TextView) v.findViewById(R.id.nomUsu);
        residenciaUsuario = (TextView) v.findViewById(R.id.residenciaUsu);
        fotoUsuario = (ImageView) v.findViewById(R.id.fotoUsu);

        usu = new Usuario("", "", 0, "", null, null);

        //Usuario usuario = ma.getUsuario();
        //usu = usuario;

        return v;
    }

    private class UsuarioTask extends AsyncTask<String, Void, Usuario> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            //progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Usuario resultado) {
            super.onPostExecute(resultado);
            toursUsuarioAL.clear();
            toursLikeados.clear();

                toursUsuarioAL.addAll(resultado.getToursCreados());
                ma.setToursUsuarioAL(toursUsuarioAL);

                toursLikeados.addAll(resultado.getToursLikeados());
                ma.setToursLikeadosUsuario(toursLikeados);

            resid = "";
            resid = resultado.getResidencia();
            residenciaUsuario.setText(resid);
            nom = "";
            nom = resultado.getNombre();
            nombreUsuario.setText(nom);
            foto = "";
            foto= resultado.getFoto();

            //que no venga una foto sin nada, que venga "" asi se muestra la foto default
            if (!foto.isEmpty()) {
                Picasso
                        .with(getContext())
                        .load(usu.getFoto())
                        //.resize(40,40)
                        .transform(new CircleTransform())
                        .into(fotoUsuario);
            }

            //progressbar.setVisibility(View.GONE);
        }

        @Override
        protected Usuario doInBackground(String... params) {
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
                return null;
            }
        }

        // Convierte un JSON
        Usuario parsearResultado(String JSONstr) throws JSONException {
            JSONObject usuario = new JSONObject(JSONstr);
            String jsonNombreUsuario = usuario.getString("Nombre");
            String jsonResidenciaUsuario = usuario.getString("Residencia");
            String jsonFotoUsuario = usuario.getString("FotoURL");

            ArrayList<Tour> toursLocal = new ArrayList<>();
            if (usuario.getJSONArray("ToursCreados") != null) {
                JSONArray jsonTours = usuario.getJSONArray("ToursCreados");
                for (int i = 0; i < jsonTours.length(); i++) {
                    JSONObject jsonResultado = jsonTours.getJSONObject(i);
                    int jsonId = jsonResultado.getInt("Id");
                    String jsonNombre = jsonResultado.getString("Nombre");
                    String jsonFoto = jsonResultado.getString("FotoURL");

                    Tour t = new Tour(jsonNombre, "", jsonFoto, "", jsonId, "", null, null, null);
                    toursLocal.add(t);
                }
            } else {
                toursLocal = null;
            }

            ArrayList<Tour> toursLikeadosLocal = new ArrayList<>();
            if (usuario.getJSONArray("ToursLikeados") != null) {
                JSONArray jsonTours = usuario.getJSONArray("ToursLikeados");
                for (int i = 0; i < jsonTours.length(); i++) {
                    JSONObject jsonResultado = jsonTours.getJSONObject(i);
                    int jsonId = jsonResultado.getInt("Id");
                    String jsonNombre = jsonResultado.getString("Nombre");
                    String jsonFoto = jsonResultado.getString("FotoURL");

                    Tour t = new Tour(jsonNombre, "", jsonFoto, "", jsonId, "", null, null, null);
                    toursLikeadosLocal.add(t);
                }
            } else {
                toursLikeadosLocal = null;
            }

            //no muestra residencia, va al catch
            usu.setNombre(jsonNombreUsuario);
            usu.setResidencia(jsonResidenciaUsuario);
            usu.setFoto(jsonFotoUsuario);
            usu.setToursCreados(toursLocal);
            usu.setToursLikeados(toursLikeadosLocal);
            return usu;
        }

    }


}
