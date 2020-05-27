package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class A07DetalleFragment extends Fragment {

    TextView mtvCategoria;
    ListView mlvProductos;
    ArrayList<HashMap<String,String>> arrayList;

    public A07DetalleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a07_detalle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtvCategoria = view.findViewById(R.id.tvCategoria);
        mlvProductos = view.findViewById(R.id.lvProductos);

        Bundle bundle = getArguments();

        String idct = bundle.getString("idct");
        String categoriades = bundle.getString("categoriades");

        getActivity().setTitle(categoriades);

        mtvCategoria.setText(categoriades + "\n" + "Especialidades");


        leerProductos(idct);

    }

    private void leerProductos(final String idct) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Parametros.rutaServidor + "listcategoriaproducto.php" + "?id=" + idct;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
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
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void mostrarListaProductos(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            for(int i= 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idpt = jsonObject.getString("idpt");
                String productodes = jsonObject.getString("productodes");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
