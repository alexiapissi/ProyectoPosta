package ort.edu.ar.proyecto;

/**
 * Created by 41400475 on 29/6/2016.
 */
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import java.util.ArrayList;

import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursUsuarioAdapter;
import ort.edu.ar.proyecto.model.Usuario;

public class Perfil_Usuario extends AppCompatActivity {

    TextView nombreUsuario;
    ImageView fotoUsuario;
    TextView residenciaUsuario;
    ListView toursUsuario;
    ToursUsuarioAdapter adapter;
    Usuario usu;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Tour> toursRecibidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        nombreUsuario = (TextView) findViewById(R.id.nomUsu);
        residenciaUsuario = (TextView) findViewById(R.id.residenciaUsu);
        fotoUsuario = (ImageView) findViewById(R.id.fotoUsu);
        toursUsuario = (ListView) findViewById(R.id.listToursUsu);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        Usuario usuario = (Usuario) extras.getSerializable("usuario");
        toursRecibidos = (ArrayList<Tour>) extras.getSerializable("listatours");
        usu = usuario;

        nombreUsuario.setText(usu.getNombre());
        residenciaUsuario.setText(usu.getResidencia());
        Picasso
                .with(getApplicationContext())
                .load(usu.getFoto())
                //.resize(40,40)
                .transform(new CircleTransform())
                .into(fotoUsuario);

        //adapter = new ToursUsuarioAdapter(getApplicationContext(), usu.getToursCreados());

        toursUsuarioAL = new ArrayList<>();
        adapter = new ToursUsuarioAdapter(getApplicationContext(), toursUsuarioAL);
        //if (usu.getToursCreados() != null) {
        toursUsuario.setAdapter(adapter);
        //}

        toursUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick (AdapterView<?> adapter, View V, int position, long l) {
                Intent intent = new Intent(Perfil_Usuario.this, Detalle_Tour.class);
                intent.putExtra("listatours", toursRecibidos);
                intent.putExtra("idTour", toursUsuarioAL.get(position).getId());
                startActivity(intent);
            }
        });
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
        protected void onPostExecute(Usuario resultado) {
            super.onPostExecute(resultado);
            toursUsuarioAL.clear();
            toursUsuarioAL.addAll(resultado.getToursCreados());
            adapter.notifyDataSetChanged();
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
