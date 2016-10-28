package ort.edu.ar.proyecto.Fragments;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.widget.Toast;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.UUID;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Dia;
import ort.edu.ar.proyecto.model.DiaAdapter;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.NonScrollListView;
import ort.edu.ar.proyecto.model.PuntoCreandoAdapter;


public class FragmentPrevisualizar extends Fragment implements View.OnClickListener {

    Button finalizar;
    MainActivity ma;
    Tour t;
    ArrayList<Punto> puntoscreando;
    ArrayList<Gusto> listagustos;
    Tour tourcreando;
    TextView nombretour;
    NonScrollListView lvDias;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    ProgressBar progressBar;
    int cantDias;
    ArrayList<Dia> dias;
    DiaAdapter diaAdapter;
    ArrayList<Punto> puntos;
    int cantPuntos;
    ArrayList<Punto> todosPuntos;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizar, container, false);

        ma = (MainActivity) getActivity();
        lvDias = (NonScrollListView)view.findViewById(R.id.lvDias);
        cantDias = ma.getCantidadDiasTour();
        dias = new ArrayList<>();
        cantPuntos = 0;
        todosPuntos = new ArrayList<>();

        if (ma.getArrayDias().size() == 0) {
            for (int i = 1; i <= cantDias; i++) {
                int dia = i;
                puntos = new ArrayList<>();
                Dia d = new Dia(dia, puntos);
                dias.add(d);
            }
            ma.setArrayDias(dias);
        }
        else {
            dias = ma.getArrayDias();
        }
        diaAdapter = new DiaAdapter(getContext(), dias);
        lvDias.setAdapter(diaAdapter);
        ma.setAdapterDias(diaAdapter);

        finalizar = (Button) view.findViewById(R.id.finalizar);
        finalizar.setEnabled(true);
        finalizar.setOnClickListener(this);
        tourcreando= ma.getTourcreando();
        nombretour=(TextView) view.findViewById(R.id.ntour);
        nombretour.setText(tourcreando.getNombre());
        progressBar=(ProgressBar) view.findViewById(R.id.progress);

        t = ma.getTourcreando();
        listagustos = t.getGustos();

        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finalizar:
                for (Dia d : dias){
                    if (d.getPuntos().size() == 0){
                        Toast.makeText(getContext(), "Ingrese al menos 2 puntos por cada día", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Punto p : d.getPuntos()){
                            cantPuntos++;
                            todosPuntos.add(p);
                        }
                        //cambiar a dos cuando dejemos de crear tour cada dos por tres
                        if (cantPuntos >= 1 && cantPuntos <= 10 ){
                            String url = "http://viajarort.azurewebsites.net/AgregarTour.php";
                            new CrearTourTask().execute(url);
                        }else{
                            Toast.makeText(getContext(), "Ingrese entre 2 y 10 puntos por cada día", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private class CrearTourTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPreExecute() {
            // SHOW THE SPINNER WHILE LOADING FEEDS
            progressBar.setVisibility(View.VISIBLE);
            finalizar.setEnabled(false);
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            progressBar.setVisibility(View.GONE);
            Toast registro;
            if (!resultado.isEmpty()) {
                if (resultado.equals("No se agrego correctamente")) {
                    registro = Toast.makeText(getContext(), "Hubo un error, intente en un instante", Toast.LENGTH_SHORT);
                    finalizar.setEnabled(true);
                } else {
                    registro = Toast.makeText(getContext(), "Tour creado", Toast.LENGTH_SHORT);
                    puntoscreando.clear();
                    ma.setPuntoscreando(puntoscreando);
                    ma.IraHomeRefresh();
                }
                registro.show();
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

/*
        RequestBody generarJSON() {
            MultipartBuilder mpb = new MultipartBuilder()
                    .type(MultipartBuilder.FORM);

            //json.put("Foto", foto);
            try {

                    mpb = new MultipartBuilder()
                        .type(MultipartBuilder.FORM);

                addMultipartField(mpb,t.getNombre(),"Nombre");
                addMultipartField(mpb,t.getDescripcion(),"Descripcion");
                addMultipartField(mpb,t.getUbicacion(),"Ubicacion");
                addMultipartField(mpb,String.valueOf(t.getUsuario().getId()),"Idusuario");


                if (t.getFoto() != null && !t.getFoto().isEmpty()) {
                    Bitmap finalImage;
                    if (t.getFotoUri().getScheme().startsWith("http")) {
                        finalImage = getBitmapFromURL(t.getFoto());
                    } else {
                        // Get Bitmap image from Uri
                        ParcelFileDescriptor parcelFileDescriptor =
                                getContext().getContentResolver().openFileDescriptor(t.getFotoUri(), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap originalImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        finalImage = originalImage;

                    }
                    // Convert bitmap to output string
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    finalImage.compress(Bitmap.CompressFormat.PNG, 100, stream);   // Compress to PNG lossless
                    byte[] byteArray = stream.toByteArray();

                    String fileName = UUID.randomUUID().toString() + ".png";
                    mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + fileName + "\""),
                            RequestBody.create(MEDIA_TYPE_PNG, byteArray));

                }
            } catch (java.io.IOException |ArrayIndexOutOfBoundsException  e) {
                e.printStackTrace();
                Log.d("Error", e.getMessage());
                return null;
            }



            //JSONObject[] innerObjectGusto = new JSONObject[listagustos.size()];
            for (int i = 0; i < listagustos.size(); i++) {
                addMultipartField(mpb, String.valueOf(listagustos.get(i).getId()),"IdGusto"+ i);
            }
            //addMultipartField(mpb,gustos,"Gustos");

            for (int i = 0; i < puntoscreando.size(); i++) {
                //innerObjectPunto[i] = new JSONObject();
                addMultipartField(mpb, puntoscreando.get(i).getNombre(),"Nombre");
                addMultipartField(mpb,puntoscreando.get(i).getDescripcion(),"Descripcion");
                addMultipartField(mpb, String.valueOf(puntoscreando.get(i).getDia()),"Dia");
                addMultipartField(mpb,puntoscreando.get(i).getDireccion(),"Direccion");
                addMultipartField(mpb, String.valueOf(puntoscreando.get(i).getLatitud()),"Latitud");
                addMultipartField(mpb, String.valueOf(puntoscreando.get(i).getLongitud()),"Longitud");

                //FOTO
                //innerObjectPunto[i].put("Foto", fotoPunto);
            }

            //RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return mpb.build();
            //return body;
        }

*/

        RequestBody generarJSON() {
            JSONObject json = new JSONObject();
            json.put("Nombre", t.getNombre());
            json.put("Descripcion", t.getDescripcion());
            json.put("Ubicacion", t.getUbicacion());
            json.put("Idusuario",t.getUsuario().getId());

            if (t.getFoto() != null && !t.getFoto().isEmpty()) {
                Bitmap finalImage;
                if (t.getFotoUri().getScheme().startsWith("http")) {
                    finalImage = getBitmapFromURL(t.getFoto());
                } else {
                    // Get Bitmap image from Uri
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                getContext().getContentResolver().openFileDescriptor(t.getFotoUri(), "r");
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
               //mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + fileName + "\""),
                 //  RequestBody.create(MEDIA_TYPE_PNG, byteArray));

            }

            JSONArray gustos = new JSONArray();
            JSONObject[] innerObjectGusto = new JSONObject[listagustos.size()];
            for (int i = 0; i < listagustos.size(); i++) {
                innerObjectGusto[i] = new JSONObject();
                innerObjectGusto[i].put("Idgusto", listagustos.get(i).getId());
                gustos.add(innerObjectGusto[i]);
            }
            json.put("Gustos", gustos);

            JSONArray puntos = new JSONArray();
            JSONObject[] innerObjectPunto = new JSONObject[todosPuntos.size()];
            for (int i = 0; i < todosPuntos.size(); i++) {
                innerObjectPunto[i] = new JSONObject();
                innerObjectPunto[i].put("Nombre", todosPuntos.get(i).getNombre());
                innerObjectPunto[i].put("Descripcion", todosPuntos.get(i).getDescripcion());
                innerObjectPunto[i].put("Dia", todosPuntos.get(i).getDia());
                innerObjectPunto[i].put("Direccion", todosPuntos.get(i).getDireccion());
                innerObjectPunto[i].put("Latitud", todosPuntos.get(i).getLatitud());
                innerObjectPunto[i].put("Longitud", todosPuntos.get(i).getLongitud());
                if (todosPuntos.get(i).getFoto() != null && !todosPuntos.get(i).getFoto().isEmpty()) {
                    Bitmap finalImage;
                    if (todosPuntos.get(i).getFotoUri().getScheme().startsWith("http")) {
                        finalImage = getBitmapFromURL(todosPuntos.get(i).getFoto());
                    } else {
                        // Get Bitmap image from Uri
                        try {
                            ParcelFileDescriptor parcelFileDescriptor =
                                    getContext().getContentResolver().openFileDescriptor(todosPuntos.get(i).getFotoUri(), "r");
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
                    innerObjectPunto[i].put("Foto",base64pic);
                    //mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\"image\"; filename=\"" + fileName + "\""),
                    //  RequestBody.create(MEDIA_TYPE_PNG, byteArray));

                }

                puntos.add(innerObjectPunto[i]);
            }
            json.put("Puntos", puntos);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            if (respuesta.has("id")) {
                String id = respuesta.getString("id");
                return id;
            } else {
                String error = respuesta.getString("Error");
                return error;
            }
        }

        private void addMultipartField(MultipartBuilder mpb, String value, String fieldname){
            if (!value.isEmpty())
                mpb.addPart(Headers.of("Content-Disposition", "form-data; name=\""+fieldname+"\""),
                        RequestBody.create(null, value));

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


    }

}

