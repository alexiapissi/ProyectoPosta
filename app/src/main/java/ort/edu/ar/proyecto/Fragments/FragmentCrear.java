package ort.edu.ar.proyecto.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.GustosAdapter;
import ort.edu.ar.proyecto.model.NonScrollListView;
import ort.edu.ar.proyecto.model.SessionManager;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.Usuario;

public class FragmentCrear extends Fragment implements View.OnClickListener {

    Button crear;
    MainActivity ma;
    CheckBox cb;
    GustosAdapter gadapter;
    EditText nombre, ubicacion, descripcion, dias;
    ImageButton ibfoto;
    ArrayList<Gusto> gustos;
    NonScrollListView gustoslv;
    Drawable camara;
    ArrayList<Gusto>gustoselegidoscrear;
    Tour tourcreando;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static public int REQUEST_IMAGE_GET = 2;
    //static final int REQUEST_TAKE_PHOTO = 3;
    String mCurrentPhotoPath;
    TextView uriTV;
    Usuario usu;
    SessionManager session;
    int cantDias;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_crear, container, false);

        crear = (Button) view.findViewById(R.id.crear);
        crear.setOnClickListener(this);
        gustoslv=(NonScrollListView)view.findViewById(R.id.listGustos);
        nombre = (EditText)view.findViewById(R.id.nombreTour);
        ubicacion = (EditText)view.findViewById(R.id.ubicacionTour);
        descripcion = (EditText)view.findViewById(R.id.descripcionTour);
        dias = (EditText)view.findViewById(R.id.cantDias);
        ibfoto=(ImageButton)view.findViewById(R.id.foto);
        camara= ibfoto.getDrawable();
        uriTV=(TextView)view.findViewById(R.id.uritv);
        ibfoto.setOnClickListener(this);
        ma = (MainActivity) getActivity();
        gustos=ma.getGustos();
        if (gustos !=null){
            gadapter = new GustosAdapter(getActivity(), gustos);
            gustoslv.setAdapter(gadapter);
        }else{
            Toast toast = Toast.makeText(getContext(),"Hubo un error, intente más adelante", Toast.LENGTH_SHORT);
            toast.show();
        }

        session = new SessionManager(getContext());
        int idUsuario = Integer.parseInt(session.getUserDetails().get(100)[2]);
        usu = new Usuario("", "", idUsuario, "", null, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("para","aca");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crear:
                if(isEmpty(nombre) || isEmpty(ubicacion)|| isEmpty(descripcion)|| isEmpty(dias)|| ibfoto.getDrawable() == null || ibfoto.getDrawable()==camara || !gustoscheckeados()){
                    Toast.makeText(getContext(), "Campos incompletos", Toast.LENGTH_SHORT).show();
                }else {
                    cantDias = Integer.parseInt(dias.getText().toString());
                    if (cantDias <= 15) {
                        tourcreando = new Tour(nombre.getText().toString(), descripcion.getText().toString(), uriTV.getText().toString(), ubicacion.getText().toString(), -1, "0", usu, null, gustoselegidoscrear);
                        ma.setTourCreando(tourcreando);
                        ma.setCantidadDiasTour(cantDias);
                        ma = (MainActivity) getActivity();
                        ma.IraPrevisualizar();
                    }else {
                        Toast.makeText(getContext(), "La cantidad máxima de dias es 15", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case R.id.foto:
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
                break;
        }
    }

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }

    private boolean gustoscheckeados(){
        boolean algunocheckeado=false;
        View v;
        gustoselegidoscrear = new ArrayList<>();
        for (int i = 0; i < gustoslv.getCount(); i++) {
            Gusto g = (Gusto) gadapter.getItem(i);
            v = gustoslv.getChildAt(i);
            cb = (CheckBox) v.findViewById(R.id.gustocb);
            if (cb.isChecked()) {
                algunocheckeado = true;
                gustoselegidoscrear.add(g);
            }
        }
            return algunocheckeado;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_GET && resultCode == ma.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(ma.getContentResolver(), uri);
                ibfoto.setImageBitmap(bitmap);
                uriTV.setText(uri.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == ma.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ibfoto.setImageBitmap(imageBitmap);
            //setPic();
            //galleryAddPic();
        }
    }


  /* private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
    ".jpg",
    storageDir
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
    return image;
}*/

   /*private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        ma.sendBroadcast(mediaScanIntent);
        uriTV.setText(contentUri.toString());
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = ibfoto.getWidth();
        int targetH = ibfoto.getHeight();

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
        ibfoto.setImageBitmap(bitmap);
    }*/

}
