package ort.edu.ar.proyecto.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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


public class FragmentBuscar extends Fragment {
    ArrayList<Gusto> gustos;
    ProgressBar cargando;
    ToursAdapter toursAdapter;
    ArrayList<Tour> tours;
    EditText inputSearch;
    ListView listviewt;
    TextView mensaje;
    boolean vacio=true;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.layoutbusqueda, container, false);
        cargando = (ProgressBar) view.findViewById(R.id.progress);
        cargando.setVisibility(View.GONE);
        inputSearch = (EditText) view.findViewById(R.id.inputSearch);
        listviewt = (ListView) view.findViewById(R.id.lv);
        tours = new ArrayList<>();
        mensaje=(TextView) view.findViewById(R.id.msj);
        toursAdapter = new ToursAdapter(getActivity(), tours);
        listviewt.setAdapter(toursAdapter);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                if(cs.length()>=3){
                    new ToursTask().execute("http://viajarort.azurewebsites.net/tours.php");
                    cargando.setVisibility(View.VISIBLE);
                    vacio=false;
                    mensaje.setVisibility(View.GONE);
                }
                if(cs.length()==0){
                    vacio=true;
                    mensaje.setVisibility(View.VISIBLE);
                }




            }
            /*Character cr = s.toString().charAt(0);
            if(Character.isLetter(cr))
            {
                // do stuff here
            }
            else
            {
                // do stuff here
            }*/
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listviewt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View V, int position, long l) {
                MainActivity ma = (MainActivity) getActivity();
                ma.IraDetalle(tours.get(position));
            }
        });

        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.busquedaopc, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_opciones:
                Log.d("opciones", "ison");
                Toast.makeText(getContext(), "opciones", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private class ToursTask extends AsyncTask<String, Void, ArrayList<Tour>> {
        private OkHttpClient client = new OkHttpClient();
        private boolean estaCargando=false;

       /* public boolean getEstaCargando() {
            return estaCargando;
        }*/

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            estaCargando = true;
            if (vacio=true){
                mensaje.setVisibility(View.VISIBLE);
            }
            else{
                mensaje.setVisibility(View.GONE);
            }
        }


        @Override
        protected void onPostExecute(ArrayList<Tour> resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                tours.clear();
                tours.addAll(resultado);
                toursAdapter.notifyDataSetChanged();
            }
            cargando.setVisibility(View.GONE);
            estaCargando = false;
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

                gustos = new ArrayList<>();
                JSONArray jsongustos = jsonResultado.getJSONArray("Gusto");
                for (int j = 0; j < jsongustos.length(); j++) {
                    JSONObject jsonresultadoGustos = jsongustos.getJSONObject(j);
                    int jsonIdGusto = jsonresultadoGustos.getInt("Id");
                    String jsonnombregustos = jsonresultadoGustos.getString("Nombre");
                    Gusto gus = new Gusto(jsonIdGusto, jsonnombregustos);
                    gustos.add(gus);
                }

                Tour t = new Tour(jsonNombre, jsonDescripcion, jsonFoto, jsonUbicacion, jsonId, jsonLikes, usu, null, gustos);
                tours.add(t);
            }
            return tours;
        }

    }

}

