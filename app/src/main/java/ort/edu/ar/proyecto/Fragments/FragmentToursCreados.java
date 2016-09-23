package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursUsuarioAdapter;

/**
 * Created by 41400475 on 2/9/2016.
 */
public class FragmentToursCreados extends Fragment {

    ListView toursUsuario;
    ToursUsuarioAdapter adapter;
    ArrayList<Tour> toursUsuarioAL;
    ArrayList<Tour> toursRecibidos;
    Tour Tourmandar;
    MainActivity ma;
    TextView creados;
    boolean estado;
    ProgressBar progess;

    public FragmentToursCreados() {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View v = inflater.inflate(R.layout.fragment_tourscreados, container, false);

        toursUsuario = (ListView) v.findViewById(R.id.listToursUsu);
        toursUsuario.invalidateViews();
        toursUsuario.refreshDrawableState();
        creados = (TextView) v.findViewById(R.id.tourscreados);
        progess = (ProgressBar)v.findViewById(R.id.progress);
        progess.setVisibility(View.GONE);

        ma = (MainActivity) getActivity();
        estado = ma.getEstado();

        toursUsuarioAL = new ArrayList<>();
        toursUsuarioAL.clear();
        toursUsuarioAL.addAll(ma.getToursUsuarioAL());

        if (toursUsuarioAL != null && toursUsuarioAL.size() != 0) {
            creados.setText("Tours creados:");
            //if (usu.getToursCreados() != null) {
            adapter = new ToursUsuarioAdapter(getActivity(), toursUsuarioAL);
            toursUsuario.setAdapter(adapter);
            toursRecibidos = ma.getTours();
            //}
            adapter.notifyDataSetChanged();

            toursUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View V, int position, long l) {

                    MainActivity ma = (MainActivity) getActivity();
                    for (Tour t : toursRecibidos) {
                        if (t.getId() == toursUsuarioAL.get(position).getId()) {
                            Tourmandar = t;
                        }
                    }
                    ma.IraDetalle(Tourmandar);
                }
            });

        }else {
            if (estado) {
                creados.setText("Este usuario no ha creado tours.");
            } else {
                progess.setVisibility(View.VISIBLE);
                //creados.setText("Cargando...");
            }
        }

        return v;
    }
}