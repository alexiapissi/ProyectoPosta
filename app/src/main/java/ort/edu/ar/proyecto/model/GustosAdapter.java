package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import java.util.ArrayList;

import ort.edu.ar.proyecto.R;


public class GustosAdapter extends BaseAdapter {

    ArrayList<Gusto> gustos;

    Context context;
    public GustosAdapter(Context context, ArrayList<Gusto> gustoss) {
        this.context = context;
        this.gustos=gustoss;
    }

    @Override
    public int getCount() {
        return gustos.size();
    }

    @Override
    public Object getItem(int i) {
        return gustos.get(i);
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
            view = inflater.inflate(R.layout.objgusto, viewGroup, false);
        }

        CheckBox cbgusto = (CheckBox)view.findViewById(R.id.gustocb);

        Gusto g= gustos.get(position);
        cbgusto.setText(g.getNombre());

        return view;
    }
}
