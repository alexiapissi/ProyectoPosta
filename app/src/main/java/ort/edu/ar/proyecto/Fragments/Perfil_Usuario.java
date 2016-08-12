package ort.edu.ar.proyecto.Fragments;



import android.os.AsyncTask;

import android.support.v4.app.Fragment;

import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ort.edu.ar.proyecto.Fragments.Detalle_Tour;
import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursUsuarioAdapter;
import ort.edu.ar.proyecto.model.Usuario;

public class Perfil_Usuario extends Fragment {

    TextView nombreUsuario;
    ImageView fotoUsuario;
    TextView residenciaUsuario;
    ListView toursUsuario;
    ToursUsuarioAdapter adapter;
    Usuario usu;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Tour> toursRecibidos;
    String resid;
    ProgressBar progressbar;
    Tour Tourmandar;



    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_perfil_usuario,container,false);
        nombreUsuario = (TextView) v.findViewById(R.id.nomUsu);
        progressbar=(ProgressBar)v.findViewById(R.id.progress);
        residenciaUsuario = (TextView) v.findViewById(R.id.residenciaUsu);
        fotoUsuario = (ImageView) v.findViewById(R.id.fotoUsu);
        toursUsuario = (ListView) v.findViewById(R.id.listToursUsu);

        MainActivity ma= (MainActivity) getActivity();
        Usuario usuario = ma.getUsuario();
        usu = usuario;
        nombreUsuario.setText(usu.getNombre());
        Picasso
                .with(getContext())
                .load(usu.getFoto())
                //.resize(40,40)
                .transform(new CircleTransform())
                .into(fotoUsuario);

        //adapter = new ToursUsuarioAdapter(getApplicationContext(), usu.getToursCreados());

        toursUsuarioAL = new ArrayList<>();
        adapter = new ToursUsuarioAdapter(getContext(), toursUsuarioAL);
        //if (usu.getToursCreados() != null) {
        toursUsuario.setAdapter(adapter);
        toursRecibidos=ma.getTours();
        //}

        toursUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick (AdapterView<?> adapter, View V, int position, long l) {

                MainActivity ma= (MainActivity)getActivity();
                for (Tour t : toursRecibidos){
                    if (t.getId() == toursUsuarioAL.get(position).getId()){
                        Tourmandar = t;
                    }
                }
                ma.IraDetalle(Tourmandar);
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        String url = "http://viajarort.azurewebsites.net/usuario.php?id=";
        url += usu.getId();
        new UsuarioTask().execute(url);
        // Llamo a clase async con url
    }

    private class UsuarioTask extends AsyncTask<String, Void, Usuario> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Usuario resultado) {
            super.onPostExecute(resultado);
            toursUsuarioAL.clear();
            toursUsuarioAL.addAll(resultado.getToursCreados());
            resid = "";
            resid = resultado.getResidencia();
            residenciaUsuario.setText(resid);
            adapter.notifyDataSetChanged();
            progressbar.setVisibility(View.GONE);
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
            String jsonResidenciaUsuario = usuario.getString("Residencia");

            ArrayList<Tour> toursLocal = new ArrayList<>();
            JSONArray jsonTours = usuario.getJSONArray("Tours");
            for (int i = 0; i < jsonTours.length(); i++) {
                JSONObject jsonResultado = jsonTours.getJSONObject(i);
                int jsonId = jsonResultado.getInt("Id");
                String jsonNombre = jsonResultado.getString("Nombre");
                String jsonFoto = jsonResultado.getString("FotoURL");

                Tour t = new Tour(jsonNombre, "", jsonFoto, "", jsonId, "", null, null, null);
                toursLocal.add(t);
            }

            //no muestra residencia, va al catch
            usu.setResidencia(jsonResidenciaUsuario);
            usu.setToursCreados(toursLocal);
            return usu;
        }

    }
}
