package ort.edu.ar.proyecto.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Punto;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentCrearPuntos extends Fragment {

    Button agregar;
    EditText nombre, direccion, dia, descripcion;
    TextView uriTV;
    ImageButton foto;
    static public int REQUEST_IMAGE_GET = 1;
    MainActivity ma;
    Punto punto;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_crearpuntos, container, false);

        ma = (MainActivity) getActivity();
        agregar = (Button) view.findViewById(R.id.agregar);
        nombre = (EditText) view.findViewById(R.id.nombrePunto);
        direccion = (EditText) view.findViewById(R.id.direccionPunto);
        dia = (EditText) view.findViewById(R.id.diaPunto);
        descripcion = (EditText) view.findViewById(R.id.descripcionPunto);
        foto = (ImageButton) view.findViewById(R.id.fotoPunto);
        uriTV = (TextView) view.findViewById(R.id.uri);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.IraPrevisualizar();
            }
        });

        return view;
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
                foto.setImageBitmap(bitmap);
                uriTV.setText(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    */

}
