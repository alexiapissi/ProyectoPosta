package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Dia;
import ort.edu.ar.proyecto.model.NonScrollListView;
import ort.edu.ar.proyecto.model.Punto;
import ort.edu.ar.proyecto.model.PuntoCreandoAdapter;

/**
 * Created by 41400475 on 21/10/2016.
 */
public class FragmentPrevisualizarPuntos extends Fragment {

    MainActivity ma;
    PuntoCreandoAdapter puntocadapter;
    NonScrollListView lvPuntos;
    ArrayList<Punto> puntoscreando;
    Dia dia;
    ArrayList<Dia> dias;
    int d;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizarpuntos, container, false);

        ma = (MainActivity) getActivity();
        dias = ma.getArrayDias();
        dia = ma.getDia();

        lvPuntos=(NonScrollListView) view.findViewById(R.id.lvPuntos);
        puntoscreando = new ArrayList<>();
        puntoscreando.addAll(ma.getListaPuntoscreando());
        if (!dia.getPuntos().isEmpty() && dia.getPuntos().size() > 0) {
            puntocadapter = new PuntoCreandoAdapter(getActivity(), dia.getPuntos());
            lvPuntos.setAdapter(puntocadapter);
            if(puntoscreando!=null){
                puntoscreando.clear();
                puntoscreando.addAll(ma.getListaPuntoscreando());
                puntocadapter.notifyDataSetChanged();
            }
        }

        Button guardar = (Button)view.findViewById(R.id.guardar);
        Button agregarpunto = (Button)view.findViewById(R.id.agregarpunto);
        TextView numerodia = (TextView)view.findViewById(R.id.dia);
        numerodia.setText("Dia " + dia.getDia());
        d = dia.getDia();

        agregarpunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.IraCrearPuntos();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setPuntos(puntoscreando);
                ma.getAdapterDias().notifyDataSetChanged();
                ma.IraPrevisualizar();
            }
        });


        return view;
    }

}
