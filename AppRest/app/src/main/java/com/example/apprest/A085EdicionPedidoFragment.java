package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import datos.Coleccion;
import datos.DetallePedidoProvisional;


/**
 * A simple {@link Fragment} subclass.
 */
public class A085EdicionPedidoFragment extends Fragment implements View.OnClickListener {

    TextView mtvIdPT;
    TextView mtvProductoDes;
    EditText metCantidad;
    Button mbtEditar;
    DetallePedidoProvisional detallePedidoProvisional;

    public A085EdicionPedidoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a085_edicion_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtvIdPT = view.findViewById(R.id.tvIdPT);
        mtvProductoDes = view.findViewById(R.id.tvProductoDes);
        metCantidad = view.findViewById(R.id.etCantidad);
        mbtEditar = view.findViewById(R.id.btEditar);
        mbtEditar.setOnClickListener(this);

        Bundle bundle = getArguments();

        mtvIdPT.setText(bundle.getString("idpt"));
        mtvProductoDes.setText(bundle.getString("productodes"));
        metCantidad.setText(bundle.getString("cantidad"));
    }

    @Override
    public void onClick(View v) {
        Log.d("Click: ", "Entra");
        Bundle bundle = getArguments();
        String idpt = bundle.getString("idpt");
        String productodes = bundle.getString("productodes");
        String cantidad = metCantidad.getText().toString();
        int posicion = bundle.getInt(("posicion"));
        Log.d("Producto editado: ", productodes + " - " + cantidad + " - " + posicion);
        modificar(idpt, productodes, cantidad, posicion );
        getActivity().onBackPressed();
    }

    private void modificar(String idpt, String productodes, String cantidad, int posicion) {
        if (Integer.parseInt(cantidad)>0)
        {
            detallePedidoProvisional = new DetallePedidoProvisional();
            detallePedidoProvisional = (DetallePedidoProvisional)Coleccion.micoleccion.get(posicion);
            detallePedidoProvisional.cantidad = Integer.parseInt(cantidad);
            Coleccion.micoleccion.set(posicion,detallePedidoProvisional);
        }
        else
        {
            Coleccion.micoleccion.remove(posicion);
        }
    }
}
