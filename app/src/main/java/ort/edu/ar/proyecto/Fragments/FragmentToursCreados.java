package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

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

    public FragmentToursCreados() {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View v = inflater.inflate(R.layout.fragment_tourscreados, container, false);

        ma = (MainActivity) getActivity();

        toursUsuario = (ListView) v.findViewById(R.id.listToursUsu);

        toursUsuarioAL = ma.getToursUsuarioAL();
        if (toursUsuarioAL != null) {
            adapter = new ToursUsuarioAdapter(getActivity(), toursUsuarioAL);
            //if (usu.getToursCreados() != null) {
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
        }

        return v;
    }
}
