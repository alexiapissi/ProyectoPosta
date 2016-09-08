package ort.edu.ar.proyecto;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ort.edu.ar.proyecto.Fragments.FHome;
import ort.edu.ar.proyecto.model.SessionManager;

public class Login extends AppCompatActivity  {

    EditText mailUsuario, contraseñaUsuario;
    TextView registrarse;
    String mail, contraseña;
    private static final int REQUEST_SIGNUP = 0;
    SessionManager session;
    Button ing;
    String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailUsuario = (EditText) findViewById(R.id.mail);
        contraseñaUsuario = (EditText) findViewById(R.id.contraseña);
        registrarse = (TextView) findViewById(R.id.registrate);
        ing = (Button) findViewById(R.id.ingresar);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Registracion.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        session = new SessionManager(getApplicationContext());
    }

    public void btnIngresar (View view){
        ing.setEnabled(false);
        mail = mailUsuario.getText().toString();
        contraseña = contraseñaUsuario.getText().toString();

        if (contraseña.length() == 0 || mail.length() == 0 ) {
            Toast error = Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT);
            error.show();
            ing.setEnabled(true);
        } else {
            if (validateEmailAddress(mail).equals("Invalid Email Address")) {
                mailUsuario.setError("Mail no valido");
                ing.setEnabled(true);
            }

            if (contraseña.length() < 4 || contraseña.length() > 10){
                contraseñaUsuario.setError("La contraseña debe tener entre 4 y 10 caracteres");
                ing.setEnabled(true);
            }

            if (mailUsuario.getError() == null && contraseñaUsuario.getError() == null){
                ing.setEnabled(false);
                String url = "http://viajarort.azurewebsites.net/LogueoUsuario.php";
                new LoginTask().execute(url);
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                if (resultado.equals("No existe el usuario")){
                    Toast registro = Toast.makeText(getApplicationContext(), "El usuario y la contraseña no coinciden", Toast.LENGTH_SHORT);
                    registro.show();
                    ing.setEnabled(true);
                } else {
                    idUsuario = resultado;
                    session.createLoginSession(contraseñaUsuario.getText().toString(), mailUsuario.getText().toString(), idUsuario);
                    //ir al inicio
                    Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                    intent.putExtra("miId", idUsuario);
                    startActivity(intent);
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
                CookieManager cookieManager = new CookieManager();
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
                client.setCookieHandler(cookieManager);
                return parsearRespuesta(response.body().string());
            } catch (IOException | JSONException e) {
                Log.d("Error", e.getMessage());
                return "";
            }
        }

        RequestBody generarJSON (){
            JSONObject json = new JSONObject();
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

    /*
    private List<HttpCookie> getCookies() {
        if(cookieManager == null)
            return null;
        else
            return cookieManager.getCookieStore().getCookies();
    }
    */

}


