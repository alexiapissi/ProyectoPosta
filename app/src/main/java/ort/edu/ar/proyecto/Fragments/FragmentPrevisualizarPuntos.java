package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<Punto> PuntosDia;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizarpuntos, container, false);

        ma = (MainActivity) getActivity();
        PuntosDia = new ArrayList<>();
        dias = ma.getArrayDias();
        dia = ma.getDia();

        for (Punto p : ma.getListaPuntoscreando()){
            if (p.getDia() == dia.getDia()){
                PuntosDia.add(p);
            }
        }

        lvPuntos=(NonScrollListView) view.findViewById(R.id.lvPuntos);
        if (!dia.getPuntos().isEmpty() && dia.getPuntos().size() > 0) {
            puntoscreando = new ArrayList<>();
            puntoscreando.addAll(PuntosDia);
            puntocadapter = new PuntoCreandoAdapter(getActivity(), PuntosDia);
            lvPuntos.setAdapter(puntocadapter);
            if(puntoscreando!=null){
                puntoscreando.clear();
                puntoscreando.addAll(PuntosDia);
                puntocadapter.notifyDataSetChanged();
            }
        }

        Button guardar = (Button)view.findViewById(R.id.guardar);
        Button agregarpunto = (Button)view.findViewById(R.id.agregarpunto);
        TextView numerodia = (TextView)view.findViewById(R.id.dia);
        numerodia.setText("Dia " + dia.getDia());

        agregarpunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cant = ma.getCantPuntos();
                if (cant < 10) {
                    ma.IraCrearPuntos();
                } else {
                    Toast.makeText(getContext(), "No es posible crear más de 10 puntos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dia.setPuntos(PuntosDia);
                ma.getAdapterDias().notifyDataSetChanged();
                ma.IraPrevisualizar();
            }
        });


        return view;
    }

}
