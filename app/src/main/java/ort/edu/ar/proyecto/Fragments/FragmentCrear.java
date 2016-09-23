package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Gusto;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentCrear extends Fragment {

    Button crear;
    MainActivity ma;
    EditText nombre, ubicacion, descripcion;
    ImageButton foto;
    ArrayList<Gusto> gustos;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_crear, container, false);

        crear = (Button) view.findViewById(R.id.crear);
        nombre = (EditText)view.findViewById(R.id.nombreTour);
        ubicacion = (EditText)view.findViewById(R.id.ubicacionTour);
        descripcion = (EditText)view.findViewById(R.id.descripcionTour);


        ma = (MainActivity) getActivity();

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.IraPrevisualizar();
            }
        });

        return view;
    }
}
