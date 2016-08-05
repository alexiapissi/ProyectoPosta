package ort.edu.ar.proyecto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ort.edu.ar.proyecto.model.Usuario;

public class Registracion extends AppCompatActivity {

    //ImageButton fotoUsuario;
    EditText nomUsuario, mailUsuario, residenciaUsuario, contraUsuario, repContraUsuario;
    //TextView uriTV;
    static public int REQUEST_IMAGE_GET = 1;
    String contraseña;
    String nombre;
    String repContraseña;
    String foto;
    String mail;
    String residencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracion);

        //fotoUsuario = (ImageButton) findViewById(R.id.fotoUsuario);
        nomUsuario = (EditText) findViewById(R.id.nombreUsuario);
        residenciaUsuario = (EditText) findViewById(R.id.residenciaUsuario);
        contraUsuario = (EditText) findViewById(R.id.contraseñaUsuario);
        repContraUsuario = (EditText) findViewById(R.id.repContraseñaUsuario);
        mailUsuario = (EditText) findViewById(R.id.mailUsuario);
        //uriTV = (TextView) findViewById(R.id.uriFotoUsuario);

    }

    /*
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
    */

    public void btnAceptar(View view) {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        contraseña = contraUsuario.getText().toString();
        repContraseña = repContraUsuario.getText().toString();
        nombre = nomUsuario.getText().toString();
        //foto = uriTV.getText().toString();
        foto = "";
        mail = mailUsuario.getText().toString();
        residencia = residenciaUsuario.getText().toString();

        //validar el mail

        if (contraseña.length() == 0 || repContraseña.length() == 0 || nombre.length() == 0 || mail.length() == 0 || residencia.length() == 0 /* || foto.length() == 0 */) {
            Toast error = Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT);
            error.show();
        } else {
            if (validateEmailAddress(mail).equals("Valid Email Address")){
                if (contraseña.equals(repContraseña)) {
                    String url = "http://viajarort.azurewebsites.net/RegistroUsuario.php";
                    new UsuarioTask().execute(url);
                } else {
                    Toast error = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                    error.show();
                }
            } else {
                Toast error = Toast.makeText(this, "Mail no valido", Toast.LENGTH_SHORT);
                error.show();
            }

            if (contraseña.isEmpty() || contraseña.length() < 4 || contraseña.length() > 10) {
                contraUsuario.setError("between 4 and 10 alphanumeric characters");
            } else {
                contraUsuario.setError(null);
            }
        }
    }

    private class UsuarioTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            Toast registro;
            if (!resultado.isEmpty()) {
                if (resultado.equals("0")){
                registro = Toast.makeText(getApplicationContext(), "Hubo un problema en la registración, intente de nuevo", Toast.LENGTH_SHORT);
                } else {
                    if (resultado.equals("El mail ya existe")){
                        registro = Toast.makeText(getApplicationContext(), "El mail ya existe", Toast.LENGTH_SHORT);
                    } else {
                        registro = Toast.makeText(getApplicationContext(), "Registración completada", Toast.LENGTH_SHORT);
                        nomUsuario.setText("");
                        mailUsuario.setText("");
                        residenciaUsuario.setText("");
                        contraUsuario.setText("");
                        repContraUsuario.setText("");
                        //ir al inicio
                    }
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
            } catch (IOException | JSONException  e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON (){
            JSONObject json = new JSONObject();
            json.put("Nombre", nombre);
            json.put("Lugar_residencia", residencia);
            json.put("Email", mail);
            json.put("Contraseña", contraseña);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());

            return body;
        }

        String parsearRespuesta(String JSONstr) throws JSONException {
            org.json.JSONObject respuesta = new org.json.JSONObject(JSONstr);
            if (respuesta.has("Id")){
                String id = respuesta.getString("Id");
                return id;
            } else {
                String error = respuesta.getString("Error");
                return error;
            }

            //parsear si es "Error": El mail ya existe
            //arreglar esto
        }
    }

    public String validateEmailAddress(String emailAddress) {
        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher   = regexPattern.matcher(emailAddress);
        if(regMatcher.matches()){
            return "Valid Email Address";
        } else {
            return "Invalid Email Address";
        }
    }


}
