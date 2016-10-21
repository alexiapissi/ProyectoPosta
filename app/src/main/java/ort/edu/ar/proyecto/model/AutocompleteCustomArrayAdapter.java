package ort.edu.ar.proyecto.model;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;

/**
 * Created by 41400475 on 14/10/2016.
 */
public class AutocompleteCustomArrayAdapter extends ArrayAdapter<Address> {

    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    ArrayList<Address> data= new ArrayList<>();

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId, ArrayList<Address> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                // inflate the layout
                LayoutInflater inflater = ((MainActivity) mContext).getLayoutInflater();
                convertView = inflater.inflate(layoutResourceId, parent, false);
            }

            // object item based on the position
            Address objectItem = data.get(position);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.textViewItem);
            if (!objectItem.getAddressLine(0).isEmpty()){
                textViewItem.setPadding(2,2,2,2);
                if (!objectItem.getAdminArea().isEmpty()){
                    textViewItem.setText(objectItem.getAddressLine(0) + ", " + objectItem.getAdminArea());
                } else {
                    textViewItem.setText(objectItem.getAddressLine(0));
                }
            }
            MainActivity.setDireccionPunto(objectItem);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;

    }
}

