package ort.edu.ar.proyecto.Fragments;



import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class Perfil_Usuario extends Fragment {

    private FragmentTabHost tabHost;
    TextView nombreUsuario, uriTV;
    ImageButton fotoUsuario;
    TextView residenciaUsuario;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Tour> toursLikeados;
    Usuario usu;
    String resid;
    String nom;
    String foto;
    int id;
    MainActivity ma;
    boolean estado;
    static public int REQUEST_IMAGE_GET = 1;
    SessionManager session;
    ImageView editar;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.activity_perfil_usuario,container,false);

        resid ="";
        ma= (MainActivity) getActivity();
        id = ma.getIdUsuario();
        estado = false;
        session = new SessionManager(getContext());

        String url = "http://viajarort.azurewebsites.net/usuario.php?id=";
        url += id;
        new UsuarioTask().execute(url);

        toursUsuarioAL = new ArrayList<>();
        toursLikeados = new ArrayList<>();
        //progressbar=(ProgressBar)v.findViewById(R.id.progress);
        nombreUsuario = (TextView) v.findViewById(R.id.nomUsu);
        residenciaUsuario = (TextView) v.findViewById(R.id.residenciaUsu);
        fotoUsuario = (ImageButton) v.findViewById(R.id.fotoUsu);
        fotoUsuario.setEnabled(false);
        editar = (ImageView)v.findViewById(R.id.editar);
        editar.setVisibility(View.GONE);

        fotoUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Elegir desde la galeria", "Cancelar"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ma);
                builder.setTitle("Agregar foto");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Elegir desde la galeria")) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            if (intent.resolveActivity(ma.getPackageManager()) != null) {
                                startActivityForResult(intent, REQUEST_IMAGE_GET);
                            }
                        } else if (items[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        uriTV = (TextView)v.findViewById(R.id.uriFotoUsuario);

        tabHost = (FragmentTabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup(getContext(), getChildFragmentManager(), android.R.id.tabcontent);
        tabHost.addTab(
                tabHost.newTabSpec("tab1").setIndicator("Tours Creados", null),
                FragmentToursCreados.class, null);
        tabHost.addTab(
                tabHost.newTabSpec("tab2").setIndicator("Tours Likeados", null),
                FragmentToursLikeados.class, null);

        usu = new Usuario("", "", 0, "", null, null);

        //Usuario usuario = ma.getUsuario();
        //usu = usuario;

        if (session.checkLogin() == 1 && Integer.parseInt(session.getUserDetails().get(100)[2])==id){
            fotoUsuario.setEnabled(true);
            editar.setVisibility(View.VISIBLE);
        }

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

            estado = true;
            ma.setEstado(estado);

            if (resultado.getResidencia() != null) {
                resid = resultado.getResidencia();
                residenciaUsuario.setText(resid);
            }
            nom = resultado.getNombre();
            nombreUsuario.setText(nom);
            foto= resultado.getFoto();

            //que no venga una foto sin nada, que venga "" asi se muestra la foto default
            if (!foto.equals("")) {
                Picasso
                        .with(getContext())
                        .load(usu.getFoto())
                        //.resize(40,40)
                        .transform(new CircleTransform())
                        .into(fotoUsuario);
            }

            toursUsuarioAL.clear();
            toursLikeados.clear();

            toursUsuarioAL.addAll(resultado.getToursCreados());
            ma.setToursUsuarioAL(toursUsuarioAL);

            toursLikeados.addAll(resultado.getToursLikeados());
            ma.setToursLikeadosUsuario(toursLikeados);

            tabHost.setCurrentTab(1);
            tabHost.setCurrentTab(0);

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

            if (jsonResidenciaUsuario != null){
                usu.setResidencia(jsonResidenciaUsuario);
            }

            //no muestra residencia, va al catch
            usu.setNombre(jsonNombreUsuario);
            usu.setFoto(jsonFotoUsuario);
            usu.setToursCreados(toursLocal);
            usu.setToursLikeados(toursLikeadosLocal);
            return usu;
        }

    }

    private class CambiarFotoTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                if (resultado.equals("NO")){
                    Toast.makeText(getContext(), "Error, intente en un instante", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), "Foto actualizada", Toast.LENGTH_SHORT).show();
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

            if (uriTV.getText() != null && uriTV.getText() != "") {
                Bitmap finalImage;
                if (Uri.parse(uriTV.getText().toString()).getScheme().startsWith("http")) {
                    finalImage = getBitmapFromURL(foto);
                } else {
                    // Get Bitmap image from Uri
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                getContext().getContentResolver().openFileDescriptor(Uri.parse(uriTV.getText().toString()), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap originalImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        finalImage = originalImage;
                    } catch (java.io.IOException e){
                        e.getMessage();
                        return null;
                    }

                }
                // Convert bitmap to output string
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);   // Compress to PNG lossless
                byte[] byteArray = stream.toByteArray();

                String fileName = UUID.randomUUID().toString() + ".png";

                String base64pic = Base64.encodeToString(byteArray, Base64.DEFAULT);
                json.put("Foto",base64pic);
                json.put("Id", Integer.parseInt(session.getUserDetails().get(100)[2]));

            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            String mensaje = respuesta.getString("Mensaje");
            return mensaje;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AlertDialog(requestCode, resultCode, data).show();
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    private Dialog AlertDialog(final int requestCode, final int resultCode, final Intent data) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());
        dialogBuilder.setTitle("Alerta");
        dialogBuilder.setMessage("¿Está seguro que desea cambiar su foto de perfil?");
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (requestCode == REQUEST_IMAGE_GET && resultCode == ma.RESULT_OK && data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(ma.getContentResolver(), uri);
                        Picasso
                                .with(getContext())
                                .load(uri.toString())
                                .transform(new CircleTransform())
                                .into(fotoUsuario);
                        uriTV.setText(uri.toString());
                        if (uriTV.getText() != ""){
                            String url = "http://viajarort.azurewebsites.net/FotoUsuario.php";
                            new CambiarFotoTask().execute(url);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return dialogBuilder.create();
    }

}
