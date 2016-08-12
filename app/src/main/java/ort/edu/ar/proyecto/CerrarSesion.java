package ort.edu.ar.proyecto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


/**
 * Created by 41400475 on 5/8/2016.
 */
public class CerrarSesion extends AppCompatActivity {

    //SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = "http://viajarort.azurewebsites.net/CerrarSesion.php";
        new Logout().execute(url);

        //session = new SessionManager(getApplicationContext());
    }

    private class Logout extends AsyncTask<String, Void, String> {
        private OkHttpClient client = new OkHttpClient();

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            Toast toast = Toast.makeText(getApplicationContext(), "Sesion cerrada", Toast.LENGTH_SHORT);
            toast.show();
            //session.logoutUser();
            //ir al inicio
            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            return "";

        }

    }

}
