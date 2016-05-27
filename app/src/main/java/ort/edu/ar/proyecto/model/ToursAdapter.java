package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Tour;


public class ToursAdapter  extends BaseAdapter {

    ArrayList<Tour> tours;
    Context context;

    public ToursAdapter(Context context, ArrayList<Tour> tours) {
        this.context = context;
        this.tours=tours;
    }

    @Override
    public int getCount() {
        return tours.size();
    }

    @Override
    public Object getItem(int i) {
        return tours.get(i);
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
            view = inflater.inflate(R.layout.objectlv, viewGroup, false);
        }

        TextView nombreTV = (TextView)view.findViewById(R.id.NombreTour);
        TextView descripcionTV = (TextView)view.findViewById(R.id.DescripcionTour);
        ImageView fotoTV = (ImageView) view.findViewById(R.id.FotoTour);
        TextView ubicacionTV = (TextView)view.findViewById(R.id.UbicacionTour);
        TextView LikesTV = (TextView)view.findViewById(R.id.cantlikes);
        TextView NombreuTV= (TextView)view.findViewById(R.id.NombreUsuario);
        ImageView fotoUsuarioTV = (ImageView) view.findViewById(R.id.FotoUsuario);

        Tour t = tours.get(position);
        Usuario usu= t.getUsuario();
        nombreTV.setText(t.getNombre());
        descripcionTV.setText(t.getDescripcion());
        ubicacionTV.setText(t.getUbicacion());
        LikesTV.setText(t.getLikes());

        NombreuTV.setText(usu.getNombre());
        Picasso
                .with(context)
                .load(usu.getFoto())
                //.resize(40,40)
                .transform(new CircleTransform())
                .into(fotoUsuarioTV);
        Picasso
                .with(context)
                .load(t.getFoto())
                .resize(700,250)
                .into(fotoTV);
        //Picasso.with(context).load(t.getFoto()).into(fotoTV);
        return view;
    }


}




