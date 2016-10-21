package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ort.edu.ar.proyecto.Fragments.FragmentCrearPuntos;
import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;

/**
 * Created by 41400475 on 14/10/2016.
 */
public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    ArrayList<Address> direccs;
    MainActivity mainActivity;
    AutocompleteCustomArrayAdapter adapter;

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;

    public CustomAutoCompleteTextChangedListener(Context context){
        this.context = context;
        direccs = new ArrayList<>();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {


        if (userInput.length() >= 3) {
            try {
                consultarDireccion(userInput.toString());

                mainActivity = ((MainActivity) context);

                // update the adapater
                adapter = mainActivity.getAdapterAutocomplete();
                adapter.notifyDataSetChanged();

                // update the adapter
                adapter = new AutocompleteCustomArrayAdapter(mainActivity, R.layout.list_view_row, direccs);

                mainActivity.getAutocomplete().setAdapter(adapter);

            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void consultarDireccion(String dir) {
        //String dirStr = direccion.getText().toString();
        if (!dir.isEmpty()) {
            new GeolocalizacionTask().execute(dir);  // Llamo a clase async con url
        }
    }

    private class GeolocalizacionTask extends AsyncTask<String, Void,List<Address>> {

        @Override
        protected void onPostExecute(List<Address> direcciones) {
            super.onPostExecute(direcciones);

            if (!direcciones.isEmpty() && direcciones != null && direcciones.size() != 0) {
                direccs.clear();
                direccs.addAll(direcciones);
                adapter.notifyDataSetChanged();
                //adressAdapter.notifyDataSetChanged();

            } else {
                direccs.clear();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected List<Address> doInBackground(String... params) {
            String address = params[0];

            Geocoder geocoder = new Geocoder(context);
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

