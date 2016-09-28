package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.R;

/**
 * Created by 41824471 on 28/9/2016.
 */
public class PuntoCreandoAdapter extends BaseAdapter {

    ArrayList<Punto> puntos;

    Context context;
    public PuntoCreandoAdapter(Context context, ArrayList<Punto> puntoss) {
        this.context = context;
        this.puntos=puntoss;
    }

    @Override
    public int getCount() {
        return puntos.size();
    }

    @Override
    public Object getItem(int i) {
        return puntos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.obj_punto_creando, viewGroup, false);
        }

        TextView lvnombre = (TextView)view.findViewById(R.id.nombrePlv);

        Punto p= puntos.get(position);
        lvnombre.setText(p.getNombre());
        return view;
    }
}

