package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;

/**
 * Created by 41400475 on 21/10/2016.
 */
public class DiaAdapter extends BaseAdapter {

    ArrayList<Dia> dias;
    MainActivity ma;

    Context context;
    public DiaAdapter(Context context, ArrayList<Dia> diass) {
        this.context = context;
        this.dias=diass;
    }

    @Override
    public int getCount() {
        return dias.size();
    }

    @Override
    public Object getItem(int i) {
        return dias.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.obj_dia, viewGroup, false);
        }

        TextView lvnombre = (TextView)view.findViewById(R.id.dia);
        Button ver = (Button)view.findViewById(R.id.verpuntos);
        ma = (MainActivity) context;

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ma.setDia(dias.get(position));
                ma.getAdapterDias().notifyDataSetChanged();
                ma.IraPrevisualizarPuntos();
            }
        });

        lvnombre.setText("Dia " + dias.get(position).getDia());
        return view;
    }
}

