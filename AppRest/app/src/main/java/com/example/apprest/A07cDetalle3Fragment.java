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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datos.Cliente;
import datos.Coleccion;
import datos.Coleccion2;
import datos.DetallePedidoProvisional;


/**
 * A simple {@link Fragment} subclass.
 */
public class A07cDetalle3Fragment extends Fragment implements AdapterView.OnItemClickListener {

    TextView mtvCategoria;
    ListView mlvProductos;
    ArrayList<HashMap<String,String>> arrayList;
    DetallePedidoProvisional detallePedidoProvisional;

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

        leerProductos(idct);
    }

    private void leerProductos(final String idct) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Parametros.rutaServidor + "listcategoriaproducto.php" + "?id=" + idct;
        //Log.d("PRODUCTOS idct: ", idct);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PRODUCTOS",response);
                        mostrarListaProductos(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PRODUCTOS",error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", idct);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    private void mostrarListaProductos(String response) {
        try {
            //Log.d("PRODUCTOS (mostrarLP): ",response);
            JSONArray jsonArray = new JSONArray(response);
            for(int i= 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idpt = jsonObject.getString("idpt");
                String productodes = jsonObject.getString("productodes");
                String idct2 = jsonObject.getString("idct");
                String precio = jsonObject.getString("precio");
                String rutaimagen = jsonObject.getString("rutaimagen");
                HashMap<String,String> map = new HashMap<>();
                map.put("idpt",idpt);
                map.put("productodes",productodes);
                map.put("precio",precio);
                map.put("rutaimagen",rutaimagen);
                arrayList.add(map);
            }
            /*
            ListAdapter listAdapter = new SimpleAdapter(
                    getActivity(),arrayList,R.layout.item_productos,
                    new String[]{"idproducto","nombre","detalle","precio"},
                    new int[]{R.id.tvIdproducto,R.id.tvNombre,R.id.tvDetalle,R.id.tvPrecio}
            );
            mlvProductos.setAdapter(listAdapter);
            */
            ProductosAdapter productosAdapter = new ProductosAdapter(getActivity(),arrayList);
            mlvProductos.setAdapter(productosAdapter);
            mlvProductos.setOnItemClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map = arrayList.get(position);

        final String idpt = map.get("idpt");
        final String productodes = map.get("productodes");
        final String precio = map.get("precio");

        if (Parametros.PedidoHecho == 1) {
            Toast.makeText(getActivity(),"Limpie pedido anterior",Toast.LENGTH_SHORT).show();
        }
        else {
            añadir(idpt, productodes, precio);
            Toast.makeText(getActivity(), "Se añadió " + productodes, Toast.LENGTH_SHORT).show();
        }
        //Log.d("Producto añadido: ", productodes + " - " + idpt + " - " + position);
    }

    public void añadir(String idpt, String productdes, String precio) {
        //Coleccion coleccion = (Coleccion) getActivity().getApplicationContext();
        int i;
        //Toast.makeText(getActivity(),"Tamaño colección: " + idpt + " - " + Coleccion.micoleccion.size(),Toast.LENGTH_SHORT).show();
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
        detallePedidoProvisional.precio = precio;
        detallePedidoProvisional.cantidad = 1;
        //Log.d("Posicion: ", String.valueOf(i));
        Coleccion.micoleccion.add(i,detallePedidoProvisional);
        return;

    }
}
