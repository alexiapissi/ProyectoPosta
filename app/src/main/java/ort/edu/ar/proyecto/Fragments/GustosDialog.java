package ort.edu.ar.proyecto.Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ort.edu.ar.proyecto.R;

/**
 * Created by 41824471 on 12/8/2016.
 */
public class GustosDialog extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gustos, container);

        getDialog().setTitle("Seleccione sus gustos");

        return view;
    }
}
