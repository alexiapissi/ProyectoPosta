package ort.edu.ar.proyecto.model;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;

/**
 * Created by Tami on 5/10/2016.
 */
public class DireccionArrayAdapter extends ArrayAdapter<Address> {
    private final Context mContext;
    private final List<Address> mDirecciones;
    private final List<Address> mDirecciones_All;
    private final List<Address> mDirecciones_Suggestion;
    private final int mLayoutResourceId;

    public DireccionArrayAdapter(Context context, int resource, List<Address> direcciones) {
        super(context, resource, direcciones);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mDirecciones = new ArrayList<>(direcciones);
        this.mDirecciones_All = new ArrayList<>(direcciones);
        this.mDirecciones_Suggestion = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDirecciones.size();
    }

    @Override
    public Address getItem(int position) {
        return mDirecciones.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                //LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }


            Address address = getItem(position);
            TextView name = (TextView)convertView.findViewById(R.id.textView);
            name.setText(address.getFeatureName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Address) resultValue).getFeatureName();
            }

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    mDirecciones_Suggestion.clear();
                    for (Address address : mDirecciones_All) {
                        if (address.getFeatureName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            mDirecciones_Suggestion.add(address);
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = mDirecciones_Suggestion;
                    filterResults.count = mDirecciones_Suggestion.size();
                    return filterResults;
                } else {
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDirecciones.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    List<?> result = (List<?>) results.values;
                    for (Object object : result) {
                        if (object instanceof Address) {
                            mDirecciones.add((Address) object);
                        }
                    }
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    mDirecciones.addAll(mDirecciones_All);
                }
                notifyDataSetChanged();
            }
        };
    }
}
