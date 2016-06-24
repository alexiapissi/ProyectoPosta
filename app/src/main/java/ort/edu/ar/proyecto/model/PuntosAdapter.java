package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ort.edu.ar.proyecto.R;

/**
 * Created by 41400475 on 27/5/2016.
 */
public class PuntosAdapter extends BaseAdapter {

    ArrayList<Punto> puntos;
    Context context;

    public PuntosAdapter(Context context, ArrayList<Punto> puntos) {
        this.context = context;
        this.puntos=puntos;
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
            view = inflater.inflate(R.layout.objectpunto, viewGroup, false);
        }
        Log.d("HOLA","llega a adapter");

        TextView nombre = (TextView)view.findViewById(R.id.nomPunto);
        TextView direccion = (TextView)view.findViewById(R.id.direccionPunto);
        TextView dia = (TextView)view.findViewById(R.id.diaPunto);
        TextView descripcion = (TextView)view.findViewById(R.id.descripcionPunto);
        ImageView foto = (ImageView) view.findViewById(R.id.fotoPunto);

        Punto p = puntos.get(position);
        nombre.setText(p.getNombre());
        direccion.setText(p.getDireccion());
        dia.setText("Día "+p.getDia());
        descripcion.setText(p.getDescripcion());
        Picasso
                .with(context)
                .load(p.getFoto())
                //.resize(180,130)
                .into(foto);

        Log.d("HOLA", "cargo tour");
        return view;
    }


}
