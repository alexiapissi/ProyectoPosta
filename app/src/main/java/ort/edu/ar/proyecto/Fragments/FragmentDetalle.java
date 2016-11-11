package ort.edu.ar.proyecto.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.NonScrollListView;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.PuntosAdapter;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;


public class FragmentDetalle extends Fragment implements View.OnClickListener {


    ImageView fototour;
    ImageButton fotoUsuario;
    TextView nombre;
    TextView ubicacion;
    TextView nombreUsuario;
    ImageButton darlike;
    TextView cantLikes;
    NonScrollListView listPuntosVW;
    PuntosAdapter puntosAdapter;
    ArrayList<Punto> puntos;
    Tour tour;
    MainActivity ma;
    ProgressBar progressbar;
    int cantidadLikes;
    SessionManager session;
    String accion;
    String stringgustos;
    TextView gustost;
    ImageButton tacho;
    ProgressBar pbareliminar;

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
        ma = (MainActivity) getActivity();
        tour = ma.getTour();
        fototour = (ImageView) view.findViewById(R.id.Fototourd);
        progressbar = (ProgressBar) view.findViewById(R.id.progress);
        pbareliminar=(ProgressBar)view.findViewById(R.id.pbar);
        fotoUsuario = (ImageButton) view.findViewById(R.id.FotoUsuariod);
        addListenerOnButton();

        nombre = (TextView) view.findViewById(R.id.NombreTourd);
        ubicacion = (TextView) view.findViewById(R.id.UbicacionTourd);
        nombreUsuario = (TextView) view.findViewById(R.id.NombreUsuariod);
        cantLikes = (TextView) view.findViewById(R.id.cantlikesd);
        darlike = (ImageButton) view.findViewById(R.id.Liked);
        gustost = (TextView) view.findViewById(R.id.gustost);
        listPuntosVW = (NonScrollListView) view.findViewById(R.id.listPuntos);

        session = new SessionManager(getContext());
        tacho = (ImageButton) view.findViewById(R.id.tacho);
        tacho.setOnClickListener(this);
        if (session.checkLogin() == 1 && Integer.parseInt(session.getUserDetails().get(100)[2])==tour.getUsuario().getId()) {
            tacho.setVisibility(View.VISIBLE);
        }else{
            tacho.setVisibility(View.GONE);
        }
        puntos = new ArrayList<>();
        puntosAdapter = new PuntosAdapter(getActivity().getApplicationContext(), puntos);

        listPuntosVW.setAdapter(puntosAdapter);


        nombre.setText(tour.getNombre());
        Picasso
                .with(getActivity().getApplicationContext())
                .load(tour.getFoto())
                //.resize(700, 250)
                .into(fototour);
        if (!tour.getUsuario().getFoto().equals("")) {
            Picasso
                    .with(getActivity().getApplicationContext())
                    .load(tour.getUsuario().getFoto())
                    //.resize(40,40)
                    .transform(new CircleTransform())
                    .into(fotoUsuario);
        } else {
            fotoUsuario.setImageResource(R.drawable.user);
        }
        ubicacion.setText(tour.getUbicacion());
        nombreUsuario.setText(tour.getUsuario().getNombre());
        cantLikes.setText(tour.getLikes());

        if (tour.getGustos().size() > 0) {
            stringgustos = "";
            for (Gusto g : tour.getGustos()) {
                stringgustos += g.getNombre() + ", ";
            }
            stringgustos = stringgustos.substring(0, stringgustos.length() - 2);
            gustost.setText(stringgustos);
        } else {
            gustost.setText("");
        }

        if (session.checkLogin() == 1) {
            String url = "http://viajarort.azurewebsites.net/traerlikes.php";
            new ObtLike().execute(url);
        }

        darlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (session.checkLogin() == 1) {
                    if (darlike.getTag().equals("nolike")) {
                        accion = "insertar";
                        String url2 = "http://viajarort.azurewebsites.net/accionlikexusuario.php";
                        new CrearEliminarLike().execute(url2);
                    } else {
                        accion = "eliminar";
                        String url2 = "http://viajarort.azurewebsites.net/accionlikexusuario.php";
                        new CrearEliminarLike().execute(url2);
                    }
                } else {
                    Toast mensaje = Toast.makeText(getContext(), "Inicie sesion para dar likes", Toast.LENGTH_SHORT);
                    mensaje.show();
                }
            }
        });

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tacho:
                //eliminar tour
                AlertDialog().show();

                break;
        }
    }

    public void addListenerOnButton() {
        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.setIdUsuario(tour.getUsuario().getId());
                ma.getToursUsuarioAL().clear();
                ma.getToursLikeadosUsuario().clear();
                ma.mandarUsuario();
            }
        });

    }

   /* public void addListenerOnText() {
        nombreUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.setIdUsuario(tour.getUsuario().getId());
                ma.mandarUsuario();
            }
        });

    }*/

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

    private class ActualizarLikes extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                if (resultado.equals("Se actualizo el like correctamente")) {
                    //asdas
                } else {
                    Toast mensaje = Toast.makeText(getContext(), "No se pudo actualizar el like", Toast.LENGTH_SHORT);
                    mensaje.show();
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

    private class ObtLike extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                switch (resultado) {
                    case "true":
                        darlike.setImageResource(R.drawable.likes);
                        darlike.setTag("like");
                        break;
                    case "false":
                        darlike.setImageResource(R.drawable.nolike);
                        darlike.setTag("nolike");
                        break;
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
            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            int idusu = Integer.parseInt(session.getUserDetails().get(100)[2]);
            json.put("idtour", tour.getId());
            json.put("idusuario", idusu);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            String mensaje = respuesta.getString("respuesta");
            return mensaje;
        }
    }

    private class CrearEliminarLike extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                switch (resultado) {
                    case "eliminado":
                        darlike.setImageResource(R.drawable.nolike);
                        darlike.setTag("nolike");
                        cantidadLikes = Integer.parseInt(cantLikes.getText().toString());
                        cantidadLikes--;
                        cantLikes.setText(Integer.toString(cantidadLikes));
                        tour.setLikes(Integer.toString(cantidadLikes));
                        String url = "http://viajarort.azurewebsites.net/actu.php";
                        new ActualizarLikes().execute(url);
                        break;
                    case "insertado":
                        darlike.setImageResource(R.drawable.likes);
                        darlike.setTag("like");
                        cantidadLikes = Integer.parseInt(cantLikes.getText().toString());
                        cantidadLikes++;
                        cantLikes.setText(Integer.toString(cantidadLikes));
                        tour.setLikes(Integer.toString(cantidadLikes));
                        String url2 = "http://viajarort.azurewebsites.net/actu.php";
                        new ActualizarLikes().execute(url2);
                        break;
                    case "hubo un error":
                        Toast mensaje = Toast.makeText(getContext(), "Hubo un problema, intente de nuevo", Toast.LENGTH_SHORT);
                        mensaje.show();
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
            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            int idusu = Integer.parseInt(session.getUserDetails().get(100)[2]);
            json.put("accion", accion);
            json.put("idtour", tour.getId());
            json.put("idusuario", idusu);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            String mensaje = respuesta.getString("accion");
            return mensaje;
        }
    }

    private class EliminarTourTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            pbareliminar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            pbareliminar.setVisibility(View.GONE);
            if (!resultado.isEmpty()) {
                if (resultado.equals("NO")){
                    Toast mensaje = Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT);
                    mensaje.show();
                } else {
                    Toast mensaje = Toast.makeText(getContext(), "Tour eliminado", Toast.LENGTH_SHORT);
                    mensaje.show();
                    ma.IraHomeRefresh();
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
                return parsearRespuestaEliminar(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON() {
            org.json.simple.JSONObject json = new org.json.simple.JSONObject();
            json.put("id", tour.getId());
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            return body;
        }

        String parsearRespuestaEliminar(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            String resul;
            if (respuesta.has("mensaje")) {
                resul = respuesta.getString("mensaje");
            } else {
                 resul = String.valueOf(respuesta.getInt("Id"));
            }
            return resul;
        }
    }

    private Dialog AlertDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        dialogBuilder.setTitle("Alerta");
        dialogBuilder.setMessage("¿Está seguro de que desea eliminar este tour?");
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String url="http://viajarort.azurewebsites.net/EliminarTour.php";
                new EliminarTourTask().execute(url);

            }
        });
        return dialogBuilder.create();
    }
}


