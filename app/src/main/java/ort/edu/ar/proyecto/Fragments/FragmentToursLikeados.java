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

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Tour;
import ort.edu.ar.proyecto.model.ToursUsuarioAdapter;

/**
 * Created by 41400475 on 2/9/2016.
 */
public class FragmentToursLikeados extends Fragment {

    ListView toursUsuario;
    ToursUsuarioAdapter adapter;
    ArrayList<Tour> toursUsuarioLikeadosAL;
    ArrayList<Tour> toursRecibidos;
    MainActivity ma;
    Tour Tourmandar;
    TextView likeados;
    boolean estado;
    ProgressBar progess;

    public FragmentToursLikeados() {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View v = inflater.inflate(R.layout.fragment_tourslikeados, container, false);

        toursUsuario = (ListView)v.findViewById(R.id.listToursUsuLikeados);
        likeados = (TextView) v.findViewById(R.id.tourslikeados);
        likeados.setText("");
        progess = (ProgressBar)v.findViewById(R.id.progress);
        progess.setVisibility(View.VISIBLE);

        ma = (MainActivity) getActivity();
        estado = ma.getEstado();

        toursUsuarioLikeadosAL = new ArrayList<>();
        toursUsuarioLikeadosAL.clear();
        toursUsuarioLikeadosAL.addAll(ma.getToursLikeadosUsuario());
        toursUsuario.invalidateViews();
        toursUsuario.refreshDrawableState();

        if (toursUsuarioLikeadosAL != null && toursUsuarioLikeadosAL.size() != 0) {
            progess.setVisibility(View.GONE);
            likeados.setVisibility(View.GONE);
            //if (usu.getToursCreados() != null) {
            adapter = new ToursUsuarioAdapter(getActivity(), toursUsuarioLikeadosAL);
            toursUsuario.setAdapter(adapter);
            toursRecibidos = ma.getTours();
            //}
            adapter.notifyDataSetChanged();

            toursUsuario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapter, View V, int position, long l) {

                    MainActivity ma = (MainActivity) getActivity();
                    for (Tour t : toursRecibidos) {
                        if (t.getId() == toursUsuarioLikeadosAL.get(position).getId()) {
                            Tourmandar = t;
                        }
                    }
                    ma.IraDetalle(Tourmandar);
                }
            });
        } else {
            if (estado) {
                progess.setVisibility(View.GONE);
                likeados.setText("Este usuario no ha likeado tours.");
            } else {
                progess.setVisibility(View.VISIBLE);
                likeados.setText("");
                //likeados.setText("Cargando...");
            }
        }

        ma.setAdapterToursLikeados(adapter);

        return v;
    }

}
