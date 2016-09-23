package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ort.edu.ar.proyecto.MainActivity;
import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Tour;

/**
 * Created by 41400475 on 16/9/2016.
 */
public class FragmentPrevisualizar extends Fragment implements View.OnClickListener {

    Button agregarpunto, finalizar;
    MainActivity ma;
    Tour tourcreando;
    TextView nombretour;

    @Override
    public void onCreate(Bundle savedInstantState) {
        super.onCreate(savedInstantState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstantState) {
        View view = inflater.inflate(R.layout.fragment_previsualizar, container, false);

        ma = (MainActivity) getActivity();
        agregarpunto = (Button) view.findViewById(R.id.agregarpunto);
        agregarpunto.setOnClickListener(this);
        finalizar = (Button) view.findViewById(R.id.finalizar);
        finalizar.setOnClickListener(this);
        tourcreando= ma.getTourcreando();
        nombretour=(TextView) view.findViewById(R.id.ntour);
        nombretour.setText(tourcreando.getNombre());
        return view;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.agregarpunto:
                ma.IraCrearPuntos();
                break;
            case R.id.finalizar:
                //json a php - crear
                ma.IraHome();
                break;
        }
    }
}
