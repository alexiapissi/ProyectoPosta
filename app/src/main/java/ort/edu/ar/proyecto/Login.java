package ort.edu.ar.proyecto;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText mailUsuario, contraseñaUsuario;
    TextView registrarse;
    String mail, contraseña;
    private static final int REQUEST_SIGNUP = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mailUsuario = (EditText) findViewById(R.id.mail);
        contraseñaUsuario = (EditText) findViewById(R.id.contraseña);
        registrarse = (TextView) findViewById(R.id.registrate);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), Registracion.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void btnIngresar (View view){
        mail = mailUsuario.getText().toString();
        contraseña = contraseñaUsuario.getText().toString();

        if (contraseña.length() == 0 || mail.length() == 0 ) {
            Toast error = Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT);
            error.show();
        } else {
            if (validateEmailAddress(mail).equals("Valid Email Address")) {
                String url = "http://viajarort.azurewebsites.net/LoginUsuario.php";
                new LoginTask().execute(url);
            }
            else {
                Toast error = Toast.makeText(this, "Mail no valido", Toast.LENGTH_SHORT);
                error.show();
            }
        }
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            if (!resultado.isEmpty()) {
                if (resultado.equals("Llego")){
                    //ir al inicio
                } else {
                    Toast registro = Toast.makeText(getApplicationContext(), "El usuario y la contraseña no coinciden", Toast.LENGTH_SHORT);
                    registro.show();
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
                return response.body().string();
            } catch (IOException /* | JSONException */ e) {
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


