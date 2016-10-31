package ort.edu.ar.proyecto;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ort.edu.ar.proyecto.Fragments.FHome;
import ort.edu.ar.proyecto.model.CircleTransform;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Usuario;

public class Registracion extends AppCompatActivity {

    ImageButton fotoUsuario;
    EditText nomUsuario, mailUsuario, residenciaUsuario, contraUsuario, repContraUsuario;
    TextView uriTV;
    static public int REQUEST_IMAGE_GET = 1;
    String contraseña;
    String nombre;
    String repContraseña;
    String foto;
    String mail;
    String residencia;
    SessionManager session;

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

        session = new SessionManager(getApplicationContext());
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
                Picasso
                        .with(getApplicationContext())
                        .load(uri.toString())
                        .transform(new CircleTransform())
                        .into(fotoUsuario);
                //fotoUsuario.setImageBitmap(bitmap);
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

        contraseña = contraUsuario.getText().toString();
        repContraseña = repContraUsuario.getText().toString();
        nombre = nomUsuario.getText().toString();
        foto = uriTV.getText().toString();
        mail = mailUsuario.getText().toString();
        residencia = residenciaUsuario.getText().toString();

        if (contraseña.length() == 0 || repContraseña.length() == 0 || nombre.length() == 0 || mail.length() == 0 || residencia.length() == 0 /* || foto.length() == 0 */) {
            Toast error = Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_SHORT);
            error.show();
        } else {
            if (contraseña.length() < 4 || contraseña.length() > 10) {
                contraUsuario.setError("La contraseña debe tener entre 4 y 10 caracteres");
            }

            if (repContraseña.length() < 4 || repContraseña.length() > 10) {
                repContraUsuario.setError("La contraseña debe tener entre 4 y 10 caracteres");
            }

            if (validateEmailAddress(mail).equals("Invalid Email Address")) {
                mailUsuario.setError("Mail no valido");
            }

            if (contraUsuario.getError() == null && repContraUsuario.getError() == null && mailUsuario.getError() == null) {
                if (contraseña.equals(repContraseña)) {
                    String url = "http://viajarort.azurewebsites.net/RegistroUsuario.php";
                    new UsuarioTask().execute(url);
                } else {
                    Toast error = Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
                    error.show();
                }
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
                registro.show();
                } else {
                    if (resultado.equals("El email ya existe.")){
                        mailUsuario.setError("El mail ya existe");
                        //registro = Toast.makeText(getApplicationContext(), "El mail ya existe", Toast.LENGTH_SHORT);
                    } else {
                        registro = Toast.makeText(getApplicationContext(), "Registración completada", Toast.LENGTH_SHORT);
                        registro.show();
                        session.createLoginSession(contraUsuario.getText().toString(), mailUsuario.getText().toString(), resultado);
                        //ir al inicio
                        Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }
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
            JSONObject json = new JSONObject();
            json.put("Nombre", nombre);
            json.put("Lugar_residencia", residencia);
            json.put("Email", mail);
            json.put("Contraseña", contraseña);

            if (foto != null && !foto.isEmpty()) {
                Bitmap finalImage;
                if (Uri.parse(foto).getScheme().startsWith("http")) {
                    finalImage = getBitmapFromURL(foto);
                } else {
                    // Get Bitmap image from Uri
                    try {
                        ParcelFileDescriptor parcelFileDescriptor =
                                getApplicationContext().getContentResolver().openFileDescriptor(Uri.parse(foto), "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap originalImage = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        finalImage = originalImage;
                    } catch (java.io.IOException e) {
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
                json.put("Foto", base64pic);
            }

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
