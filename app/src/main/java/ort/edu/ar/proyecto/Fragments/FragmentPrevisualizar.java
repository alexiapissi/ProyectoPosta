package ort.edu.ar.proyecto.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.NonScrollListView;
import ort.edu.ar.proyecto.model.PuntoCreandoAdapter;


public class FragmentPrevisualizar extends Fragment implements View.OnClickListener {

    Button agregarpunto, finalizar;
    MainActivity ma;
    Tour t;
    ArrayList<Punto> puntoscreando;
    ArrayList<Gusto> listagustos;
    Tour tourcreando;
    TextView nombretour;
    PuntoCreandoAdapter puntocadapter;
    NonScrollListView lvPuntos;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizar, container, false);

        ma = (MainActivity) getActivity();
        lvPuntos=(NonScrollListView) view.findViewById(R.id.lvPuntos);
        puntoscreando = new ArrayList<>();
        puntoscreando.addAll(ma.getListaPuntoscreando());
        puntocadapter = new PuntoCreandoAdapter(getActivity(), puntoscreando);
        lvPuntos.setAdapter(puntocadapter);
        if(puntoscreando!=null){
            puntoscreando.clear();
            puntoscreando.addAll(ma.getListaPuntoscreando());
            puntocadapter.notifyDataSetChanged();
        }
        agregarpunto = (Button) view.findViewById(R.id.agregarpunto);
        agregarpunto.setOnClickListener(this);
        finalizar = (Button) view.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);
        tourcreando= ma.getTourcreando();
        nombretour=(TextView) view.findViewById(R.id.ntour);
        nombretour.setText(tourcreando.getNombre());

        t = ma.getTourcreando();
        listagustos = t.getGustos();

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agregarpunto:
                ma.IraCrearPuntos();
                break;
            case R.id.finalizar:
                String url = "http://viajarort.azurewebsites.net/RegistroUsuario.php";
                new CrearTourTask().execute(url);
                break;
        }
    }

    private class CrearTourTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            Toast registro;
            if (!resultado.isEmpty()) {
                if (resultado.equals("0")) {

                } else {
                    //ir al inicio
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
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON() {
            JSONObject json = new JSONObject();
            json.put("Nombre", t.getNombre());
            json.put("Descripcion", t.getDescripcion());
            json.put("Ubicacion", t.getUbicacion());
            json.put("Idusuario",t.getUsuario().getId());
            //json.put("Foto", foto);

            JSONArray gustos = new JSONArray();
            JSONObject[] innerObjectGusto = new JSONObject[listagustos.size()];
            for (int i = 0; i < listagustos.size(); i++) {
                innerObjectGusto[i] = new JSONObject();
                innerObjectGusto[i].put("IdGusto", listagustos.get(i).getId());
                gustos.add(innerObjectGusto[i]);
            }
            json.put("Gustos", gustos);

            JSONArray puntos = new JSONArray();
            JSONObject[] innerObjectPunto = new JSONObject[puntoscreando.size()];
            for (int i = 0; i < puntoscreando.size(); i++) {
                innerObjectPunto[i] = new JSONObject();
                innerObjectPunto[i].put("Nombre", puntoscreando.get(i).getNombre());
                innerObjectPunto[i].put("Descripcion", puntoscreando.get(i).getDescripcion());
                innerObjectPunto[i].put("Dia", puntoscreando.get(i).getDia());
                innerObjectPunto[i].put("Direccion", puntoscreando.get(i).getDireccion());
                innerObjectPunto[i].put("Latitud", puntoscreando.get(i).getLatitud());
                innerObjectPunto[i].put("Longitud", puntoscreando.get(i).getLongitud());
                //innerObjectPunto[i].put("Foto", fotoPunto);

                puntos.add(innerObjectPunto[i]);
            }
            json.put("Puntos", puntos);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            if (respuesta.has("Id")) {
                String id = respuesta.getString("Id");
                return id;
            } else {
                String error = respuesta.getString("Error");
                return error;
            }
        }
    }
}

