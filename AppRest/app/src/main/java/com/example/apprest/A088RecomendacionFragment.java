package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import datos.Coleccion;
import datos.ColeccionProducto;
import datos.DetallePedidoProvisional;
import datos.Producto;


/**
 * A simple {@link Fragment} subclass.
 */
public class A088RecomendacionFragment extends Fragment implements View.OnClickListener {

    Button mbtAceptar, mbtRechazar;
    TextView mtvPromocion;
    Bundle bundle;
    String idpt, precio, idpr;
    String productodes = "", idct = "", preciooriginal = "";
    DetallePedidoProvisional detallePedidoProvisional;
    Producto producto;

    public A088RecomendacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a088_recomendacion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtvPromocion = view.findViewById(R.id.tvPromocion);
        mbtAceptar = view.findViewById(R.id.btAceptar);
        mbtAceptar.setEnabled(true);
        mbtAceptar.setAlpha(1f);
        mbtAceptar.setOnClickListener(this);
        mbtRechazar = view.findViewById(R.id.btRechazar);
        mbtRechazar.setOnClickListener(this);

        bundle = getArguments();

        idpt = bundle.getString("idpt");
        precio = bundle.getString("precio");
        idpr = bundle.getString("idpr");

        for(int i=0; i < ColeccionProducto.micoleccionproducto.size(); i++) {
            producto = (Producto) ColeccionProducto.micoleccionproducto.get(i);
            if (producto.idpt.equals(idpt)) {
                productodes = producto.productodes;
                idct = producto.idct;
                preciooriginal = producto.precio;
                break;
            }
        }
        mtvPromocion.setText("1 " + productodes + " a S/" + precio);
    }

    @Override
    public void onClick(View v) {
        A08bPedido2Fragment.idpr = idpr;
        switch (v.getId()) {
            case R.id.btAceptar:
                mbtAceptar.setEnabled(false);
                mbtAceptar.setAlpha(.5f);
                //bundle.putString("respuesta", "1");
                Parametros.Respuesta = "1";
                modificar();
                getActivity().onBackPressed();
                break;
            case R.id.btRechazar:
                //bundle.putString("respuesta", "0");
                Parametros.Respuesta = "0";
                getActivity().onBackPressed();
                break;
        }

    }

    private void modificar() {
        //Toast.makeText(getActivity(),idpt + ":" + precio,Toast.LENGTH_SHORT).show();

        /*for(int i=0; i < ColeccionProducto.micoleccionproducto.size(); i++) {
            producto = (Producto) ColeccionProducto.micoleccionproducto.get(i);
            if (producto.idpt.equals(idpt)) {
                productodes = producto.productodes;
                idct = producto.idct;
                preciooriginal = producto.precio;
                break;
            }
        }*/
        productodes = producto.productodes;
        idct = producto.idct;
        preciooriginal = producto.precio;
        //Toast.makeText(getActivity(),productodes,Toast.LENGTH_SHORT).show();

        detallePedidoProvisional = new DetallePedidoProvisional();
        detallePedidoProvisional.idpt = idpt;
        detallePedidoProvisional.productodes = productodes;
        detallePedidoProvisional.idct = idct;
        detallePedidoProvisional.descuento = String.valueOf(Double.parseDouble(preciooriginal) - Double.parseDouble(precio));
        detallePedidoProvisional.precio = preciooriginal;
        detallePedidoProvisional.cantidad = 1;
        Coleccion.micoleccion.add(detallePedidoProvisional);


    }


}
