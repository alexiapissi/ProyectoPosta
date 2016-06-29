package ort.edu.ar.proyecto.Fragments;

import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import ort.edu.ar.proyecto.Detalle_Tour;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Punto;

/**
 * Created by 41400475 on 10/6/2016.
 */
public class FragmentMapa extends Fragment implements OnMapReadyCallback {

    GoogleMap map;
    ArrayList<Punto> listapuntos;
    FragmentManager fm;

    public FragmentMapa() {
    }

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_mapa, container, false);


        Detalle_Tour dt = (Detalle_Tour) getActivity();
        listapuntos = new ArrayList<Punto>();
        listapuntos = dt.getPuntos();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        fm = getFragmentManager();
        if (listapuntos == null) {
            //fm.popBackStack();
            Toast toast = Toast.makeText(getActivity(),"Espere a que se muestren los puntos", Toast.LENGTH_LONG);
            toast.show();
            fm.popBackStack();
            //return;
        } else {
            this.map = map;
            map.getUiSettings().setZoomControlsEnabled(true);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            int color = 1;
            for (Punto p : listapuntos) {
                double lat = p.getLatitud();
                double lng = p.getLongitud();
                LatLng position = new LatLng(lat, lng);
                String coordStr = lat + "," + lng;
                if (map != null) {

                    map.addMarker(new MarkerOptions()
                            .position(position)
                            .title(p.getNombre())
                            .icon(BitmapDescriptorFactory.defaultMarker(new Random().nextInt(360))));
                    builder.include(position);

                        /*CameraUpdate center =
                                CameraUpdateFactory.newLatLng(new LatLng(lat, lng));
                       CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
                        map.moveCamera(center);
                        map.animateCamera(zoom);*/

                }

            }
            LatLngBounds bounds = builder.build();
            int padding = 80; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.moveCamera(cu);
            map.animateCamera(cu);


        }
    }
}

