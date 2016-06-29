package ort.edu.ar.proyecto.model;

/**
 * Created by 41400475 on 29/6/2016.
 */
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

/**
 * Created by 41824471 on 10/6/2016.
 */
public class ToursUsuarioAdapter extends BaseAdapter{

    ArrayList<Tour> tours;
    Context context;

    public ToursUsuarioAdapter(Context context, ArrayList<Tour> tours) {
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
            view = inflater.inflate(R.layout.objecttoursusuario, viewGroup, false);
        }

        TextView nombreTour = (TextView)view.findViewById(R.id.nomTourUsu);
        ImageView fotoTour = (ImageView) view.findViewById(R.id.fotoTourUsu);

        Tour t = tours.get(position);
        nombreTour.setText(t.getNombre());

        Picasso
                .with(context)
                .load(t.getFoto())
                //.resize(700,250)
                .into(fotoTour);
        //Picasso.with(context).load(t.getFoto()).into(fotoTV);
        return view;
    }


}


