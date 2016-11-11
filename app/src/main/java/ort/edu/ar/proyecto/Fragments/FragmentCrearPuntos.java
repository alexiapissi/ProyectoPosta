package ort.edu.ar.proyecto.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.AutocompleteCustomArrayAdapter;
import ort.edu.ar.proyecto.model.CustomAutoCompleteTextChangedListener;
import ort.edu.ar.proyecto.model.CustomAutoCompleteView;
import ort.edu.ar.proyecto.model.Dia;
import ort.edu.ar.proyecto.model.Punto;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentCrearPuntos extends Fragment {

    Button agregar;
    EditText nombre, dia, descripcion;
    TextView uriTV;
    ImageButton foto;
    Drawable camara;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static public int REQUEST_IMAGE_GET = 2;
    static final int REQUEST_TAKE_PHOTO = 3;
    String mCurrentPhotoPath;
    MainActivity ma;
    Punto puntocreando;
    CustomAutoCompleteView myAutoComplete;
    AutocompleteCustomArrayAdapter myAdapter;
    ArrayList<Address> direccs;

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
        descripcion = (EditText) view.findViewById(R.id.descripcionPunto);
        foto = (ImageButton) view.findViewById(R.id.imagenPunto);
        uriTV = (TextView) view.findViewById(R.id.uri);
        camara=foto.getDrawable();

        try{
            direccs = new ArrayList<>();
            myAutoComplete = (CustomAutoCompleteView) view.findViewById(R.id.myautocomplete);

            myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                    RelativeLayout rl = (RelativeLayout) arg1;
                    TextView tv = (TextView) rl.getChildAt(0);
                    myAutoComplete.setText(tv.getText().toString());
                }

            });

            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(getContext()));

            // ObjectItemData has no value at first
            ArrayList<Address> ObjectItemData = new ArrayList<>();

            // set the custom ArrayAdapter
            myAdapter = new AutocompleteCustomArrayAdapter(getContext(), R.layout.list_view_row, ObjectItemData);
            myAutoComplete.setAdapter(myAdapter);
            ma.setAdapterAutocomplete(myAdapter);
            ma.setAutocomplete(myAutoComplete);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(nombre) || isEmpty(myAutoComplete)|| isEmpty(descripcion)|| foto.getDrawable() == null|| foto.getDrawable()==camara){
                    Toast.makeText(getContext(), "Campos incompletos", Toast.LENGTH_SHORT).show();
                }else {
                    Address direccion = ma.getDireccionPunto();
                    Dia dia = ma.getDia();
                    int d = dia.getDia();
                    if (direccion != null){
                        puntocreando= new Punto(-1,direccion.getLongitude(),direccion.getLatitude(),direccion.getAddressLine(0),uriTV.getText().toString(),nombre.getText().toString(),-1,null,null,descripcion.getText().toString(),d);
                    } else {
                        puntocreando= new Punto(-1,1,2,myAutoComplete.getText().toString(),uriTV.getText().toString(),nombre.getText().toString(),-1,null,null,descripcion.getText().toString(),d);
                    }
                    dia.getPuntos().add(puntocreando);
                    ma.getAdapterDias().notifyDataSetChanged();

                    //falta usuario y validar foto y sacar foto
                    ma.agregarPuntoCreando(puntocreando);
                    ma.IraPrevisualizarPuntos();

                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == ma.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ma.getContentResolver(), uri);

                foto.setImageBitmap(bitmap);
                uriTV.setText(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == ma.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(imageBitmap);
            //setPic();
            //galleryAddPic();
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE=70;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

}
