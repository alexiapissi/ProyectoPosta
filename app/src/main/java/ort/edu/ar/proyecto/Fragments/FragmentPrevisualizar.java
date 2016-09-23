package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentPrevisualizar extends Fragment {

    Button agregarpunto, finalizar;
    MainActivity ma;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizar, container, false);

        ma = (MainActivity) getActivity();
        agregarpunto = (Button) view.findViewById(R.id.agregarpunto);
        finalizar = (Button) view.findViewById(R.id.finalizar);

        agregarpunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.IraCrearPuntos();
            }
        });

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.IraHome();
            }
        });

        return view;
    }
}
