package ort.edu.ar.proyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.IOException;

import ort.edu.ar.proyecto.model.Usuario;

public class Registracion extends AppCompatActivity {

    ImageButton fotoUsuario;
    EditText nomUsuario, mailUsuario, residenciaUsuario, contraUsuario, repContraUsuario;
    TextView uriTV;
    static public int REQUEST_IMAGE_GET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracion);

        fotoUsuario = (ImageButton) findViewById(R.id.fotoUsuario);
        nomUsuario = (EditText) findViewById(R.id.nombreUsuario);
        residenciaUsuario = (EditText) findViewById(R.id.residenciaUsuario);
        contraUsuario = (EditText) findViewById(R.id.contraseñaUsuario);
        repContraUsuario = (EditText) findViewById(R.id.repContraseñaUsuario);
        mailUsuario = (EditText) findViewById(R.id.mailUsuario);
        uriTV = (TextView) findViewById(R.id.uriFotoUsuario);
    }

    public void seleccionarFotoUsuario(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                fotoUsuario.setImageBitmap(bitmap);
                uriTV.setText(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAceptar(View view) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        String contraseña = contraUsuario.getText().toString();
        String repContraseña = repContraUsuario.getText().toString();
        String nombre = nomUsuario.getText().toString();
        String foto = uriTV.getText().toString();
        String mail = mailUsuario.getText().toString();
        String residencia = residenciaUsuario.getText().toString();
        if (contraseña.length() == 0 || repContraseña.length() == 0 || nombre.length() == 0 || mail.length() == 0 || residencia.length() == 0 || foto.length() == 0) {
            Toast error = Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT);
            error.show();
        } else {
            if (contraseña == repContraseña) {
                try {

                    /*hacerlo en un asynctask
                    private class ToursTask extends AsyncTask<String, Void, ACA IRIA STRING> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(STRING resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                tours.clear();
                tours.addAll(resultado);
                toursAdapter.notifyDataSetChanged();
            }
        }


        @Override
        protected STRING doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return RESPONSE.BODY().STRING();
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());                          // Error de Network o al parsear JSON
                return STRING;
                    */

                    OkHttpClient client = new OkHttpClient();
                    String url = "http://viajarort.azurewebsites.net/RegistroUsuario.php";
                    JSONObject json = new JSONObject();
                    json.put("Nombre", nombre);
                    json.put("Foto", foto);
                    json.put("Lugar_residencia", residencia);
                    json.put("Email", mail);
                    json.put("Contraseña", contraseña);

                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();

                    Response response = client.newCall(request).execute();
                    Log.d("Response", response.body().string());
                    Intent intent = new Intent(this, Busqueda.class);
                    startActivity(intent);
                } catch (IOException e) {
                    Log.d("Error", e.getMessage());
                }
            } else {
                Toast error = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                error.show();
            }

        }
    }

    private class UsuarioTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                //fijate que poner aca
            }
        }

        @Override
        protected String doInBackground(String... params) {
            //cambiar esto por lo que esta arriba

            String url = params[0];
            RequestBody body = generarJSON();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON (){
            String url = "http://viajarort.azurewebsites.net/RegistroUsuario.php";
            JSONObject json = new JSONObject();
            json.put("Nombre", nombre);
            json.put("Foto", foto);
            json.put("Lugar_residencia", residencia);
            json.put("Email", mail);
            json.put("Contraseña", contraseña);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }
    }


}
