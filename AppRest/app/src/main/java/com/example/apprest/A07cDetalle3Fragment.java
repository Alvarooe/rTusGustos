package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datos.Cliente;
import datos.Coleccion;
import datos.Coleccion2;
import datos.ColeccionProducto;
import datos.DetallePedidoProvisional;
import datos.Producto;


/**
 * A simple {@link Fragment} subclass.
 */
public class A07cDetalle3Fragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<HashMap<String,String>> arrayList;
    Producto producto;
    DetallePedidoProvisional detallePedidoProvisional;
    TextView mtvCategoria;
    ListView mlvProductos;

    public A07cDetalle3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a07c_detalle3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtvCategoria = view.findViewById(R.id.tvCategoria);
        mlvProductos = view.findViewById(R.id.lvProductos);

        Bundle bundle = getArguments();
        String idct = bundle.getString("idct");
        String categoriades = bundle.getString("categoriades");

        //getActivity().setTitle(categoriades);

        mtvCategoria.setText(categoriades + "\n" + "Especialidades");

        arrayList = new ArrayList<>();

        mostrarProductos(idct);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.show();
    }

    private void mostrarProductos(String idct) {
        try {
            arrayList =  new ArrayList<>();
            for(int i = 0; i< ColeccionProducto.micoleccionproducto.size(); i++){
                producto = (Producto) ColeccionProducto.micoleccionproducto.get(i);
                if (producto.idct.equals(idct)) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("idpt",producto.idpt);
                    map.put("productodes",producto.productodes);
                    map.put("idct", producto.idct);
                    map.put("precio",producto.precio);
                    map.put("rutaimagen",producto.rutaimagen);
                    arrayList.add(map);
                }
            }
            ProductosAdapter productosAdapter = new ProductosAdapter(getActivity(),arrayList);
            mlvProductos.setAdapter(productosAdapter);
            mlvProductos.setOnItemClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map = arrayList.get(position);

        final String idpt = map.get("idpt");
        final String productodes = map.get("productodes");
        final String idct = map.get("idct");
        final String precio = map.get("precio");

        if (Parametros.PedidoHecho == 1) {
            Toast.makeText(getActivity(),"Limpie pedido anterior",Toast.LENGTH_SHORT).show();
        }
        else {
            añadir(idpt, productodes, idct, precio);
            Toast.makeText(getActivity(), "Se añadió " + productodes, Toast.LENGTH_SHORT).show();
        }
    }

    public void añadir(String idpt, String productdes, String idct, String precio) {
        int i;
        for (i=0; i<Coleccion.micoleccion.size(); i=i+1)
        {
            detallePedidoProvisional = (DetallePedidoProvisional) Coleccion.micoleccion.get(i);
            if (idpt.equals(detallePedidoProvisional.idpt))
            {
                detallePedidoProvisional.cantidad=detallePedidoProvisional.cantidad+1;
                Coleccion.micoleccion.set(i,detallePedidoProvisional);
                return;
            }
            //else if (idpt.compareTo(detallePedidoProvisional.idpt) > 0){
            else if (Integer.parseInt(idpt) < Integer.parseInt(detallePedidoProvisional.idpt)){
                break;
            }
        }
        detallePedidoProvisional = new DetallePedidoProvisional();
        detallePedidoProvisional.idpt = idpt;
        detallePedidoProvisional.productodes = productdes;
        detallePedidoProvisional.idct = idct;
        detallePedidoProvisional.precio = precio;
        detallePedidoProvisional.descuento = "0";
        detallePedidoProvisional.cantidad = 1;
        Coleccion.micoleccion.add(i,detallePedidoProvisional);
        return;
    }
}
