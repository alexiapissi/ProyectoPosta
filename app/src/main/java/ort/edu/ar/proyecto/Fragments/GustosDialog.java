package ort.edu.ar.proyecto.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ort.edu.ar.proyecto.R;
import ort.edu.ar.proyecto.model.Gusto;
import ort.edu.ar.proyecto.model.GustosAdapter;

public class GustosDialog extends DialogFragment implements View.OnClickListener{

    ArrayList<Gusto> gustos;
    Button aceptar;
    GustosAdapter gadapter;
    ListView lv;
    CheckBox cb;


    public void Setgustos (ArrayList<Gusto> gustos){
        this.gustos=gustos;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gustos, container);

        getDialog().setTitle("Seleccione sus gustos");
        aceptar= (Button) view.findViewById(R.id.aceptar);
        lv= (ListView) view.findViewById(R.id.lv);

        aceptar.setOnClickListener(this);
        if (gustos !=null){
            gadapter = new GustosAdapter(getActivity(), gustos);
            lv.setAdapter(gadapter);
        }else{
            Toast toast = Toast.makeText(getContext(),"Hubo un error, intente más adelante", Toast.LENGTH_SHORT);
            toast.show();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("para","aca");
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aceptar:
                ButtonClick();
                break;
        }
    }

    private void ButtonClick() {
        boolean algunocheckeado=false;
        /** get all values of the EditText-Fields */
        View v;
        ArrayList<Gusto> gustoselegidos = new ArrayList<>();

        for (int i = 0; i < lv.getCount(); i++) {
            Gusto g= (Gusto)gadapter.getItem(i);
            v = lv.getChildAt(i);
            cb = (CheckBox) v.findViewById(R.id.gustocb);
            if(cb.isChecked()){
                algunocheckeado=true;

                gustoselegidos.add(g);
                g.setCheckeado(true);
            }else{
                g.setCheckeado(false);
            }
        }
        if(algunocheckeado) {
            FragmentBuscar fb = (FragmentBuscar) getTargetFragment();
            fb.setGustosElegidos(gustoselegidos);
            dismiss();
        }else{
            FragmentBuscar fb = (FragmentBuscar) getTargetFragment();
            fb.setGustosElegidos(null);
            dismiss();
        }

    }


}

