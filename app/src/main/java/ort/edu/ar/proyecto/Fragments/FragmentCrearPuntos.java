package ort.edu.ar.proyecto.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.DireccionArrayAdapter;
import ort.edu.ar.proyecto.model.Punto;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentCrearPuntos extends Fragment {

    Button agregar;
    EditText nombre, dia, descripcion;
    AutoCompleteTextView direccion;
    TextView uriTV;
    ImageButton foto;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static public int REQUEST_IMAGE_GET = 2;
    static final int REQUEST_TAKE_PHOTO = 3;
    String mCurrentPhotoPath;
    MainActivity ma;
    Punto puntocreando;
    boolean iswaiting =false;
    ArrayList<Address> direccs;
    DireccionArrayAdapter adressAdapter;
    ListView listViewAutocomplete;

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
        direccion = (AutoCompleteTextView) view.findViewById(R.id.direccionPunto);
        direccion.setThreshold(1);
        dia = (EditText) view.findViewById(R.id.diaPunto);
        descripcion = (EditText) view.findViewById(R.id.descripcionPunto);
        foto = (ImageButton) view.findViewById(R.id.imagenPunto);
        uriTV = (TextView) view.findViewById(R.id.uri);
        listViewAutocomplete = (ListView) view.findViewById(R.id.listView);

        direccs = new ArrayList<>();
        //adressAdapter = new ArrayAdapter<Address>(getContext(),
          //      android.R.layout.simple_dropdown_item_1line, direccs);
        //direccion.setAdapter(adressAdapter);

        direccion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence cs, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {

                if(cs.length()>=2){
                    iswaiting=true;
                    consultarDireccion(cs.toString());
                }
                if(cs.length()==0){
                   //h
                }
                if(cs.length()<2){
                    direccs.clear();
                    //adressAdapter.notifyDataSetChanged();
                    iswaiting =false;
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(nombre) || isEmpty(direccion)|| isEmpty(dia)|| isEmpty(descripcion)|| foto.getDrawable() == null){
                    Toast.makeText(getContext(), "Campos incompletos", Toast.LENGTH_SHORT).show();
                }else {
                    puntocreando= new Punto(-1,0,0,direccion.getText().toString(),null,nombre.getText().toString(),-1,null,null,descripcion.getText().toString(),Integer.parseInt(dia.getText().toString()));
                    //falta usuario y validar foto y sacar foto
                    ma.agregarPuntoCreando(puntocreando);
                    ma.IraPrevisualizar();

                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Sacar foto", "Elegir desde la galeria", "Cancelar"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ma);
                builder.setTitle("Agregar foto");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        if (items[item].equals("Sacar foto")) {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(ma.getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                /*
                                File photoFile = null;
                                try {
                                    photoFile = createImageFile();
                                } catch (IOException ex) {
                                    // Error occurred while creating the File
                                    Toast mensj = Toast.makeText(getContext(), "Hubo un error", Toast.LENGTH_SHORT);
                                    mensj.show();
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    Uri photoURI = FileProvider.getUriForFile(getContext(),
                                            "ort.edu.ar.proyecto.fileprovider",
                                            photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                                }*/
                            }
                        } else if (items[item].equals("Elegir desde la galeria")) {
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

        listViewAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View V, int position, long l) {
                //direccion es la seleccionada
            }
        });

        return view;
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        ma.sendBroadcast(mediaScanIntent);
        uriTV.setText(contentUri.toString());
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = foto.getWidth();
        int targetH = foto.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        foto.setImageBitmap(bitmap);
    }

    public void consultarDireccion(String dir) {
        //String dirStr = direccion.getText().toString();
        if (!dir.isEmpty()) {
            new GeolocalizacionTask().execute(dir);  // Llamo a clase async con url
        }
    }

    // List<Address> - lo que devuelve doInBackground
    private class GeolocalizacionTask extends AsyncTask<String, Void,List<Address>> {

        @Override
        protected void onPostExecute(List<Address> direcciones) {
            super.onPostExecute(direcciones);

            if (!direcciones.isEmpty() && iswaiting && direcciones != null) {
                direccs.clear();
                direccs.addAll(direcciones);
                adressAdapter = new DireccionArrayAdapter(getContext(), R.layout.objlistview_autocomplete, direccs);
                listViewAutocomplete.setAdapter(adressAdapter);
                listViewAutocomplete.setVisibility(View.VISIBLE);
                //adressAdapter.notifyDataSetChanged();

                /*
                // Muestro coordenadas
                double lat = dirRecibida.getLatitude(); //
                double lng = dirRecibida.getLongitude();
                String coordStr = lat+ "," + lng;
                coordenadas.setText(coordStr);  // Muestro coordenadas en pantalla
                */
            } else {
                direccs.clear();
                //adressAdapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            String address = params[0];

            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = null;
            try {
                boolean hola = geocoder.isPresent();
                // Utilizo la clase Geocoder para buscar la direccion. Limito a 5 resultados
                addresses = geocoder.getFromLocationName(address, 5, -54.928471, -73.283694, -22.595863, -54.475101);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return addresses;
        }

    }

}
