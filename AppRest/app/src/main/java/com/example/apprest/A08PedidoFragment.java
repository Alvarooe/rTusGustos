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


/**
 * A simple {@link Fragment} subclass.
 */
public class A08PedidoFragment extends Fragment {
    ListView mlvProductos;
    ArrayList<HashMap<String,String>> arrayList;


    public A08PedidoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a08_pedido, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mlvProductos = view.findViewById(R.id.lvProductos);


        //NVO
//        mivRegresar = view.findViewById(R.id.ivRegresar);
//        mivRegresar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                getActivity().onBackPressed();
//                //FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//                //ft.popbBackStack(); /*KO*/
//                //getActivity().getSupportFragmentManager().popBackStack(); /*KO*/
//            }
//        });


        //Bundle bundle = getArguments();

        String idpe = "1";
        //String idct = bundle.getString("idct");
        //String idpt = bundle.getString("idpt");
        //String productodes = bundle.getString("productodes");
        //String precio = bundle.getString("precio");
        //String categoriades = bundle.getString("categoriades");

        //getActivity().setTitle(categoriades);



        arrayList = new ArrayList<>();

        //Log.d("PARAMS: ", idpt + " - " + productodes + " - " + precio); //NVO
        leerProductos(idpe);

    }

    private void leerProductos(final String idpe) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //String url = Parametros.rutaServidor + "listcategoriaproducto.php";
        String url = Parametros.rutaServidor + "conpdetallepedido.php" + "?id=" + idpe;
        //Log.d("PRODUCTOS URL:",url);
        Log.d("PRODUCTOS idpe: ", idpe);
        //Log.d("PRODUCTOS URL: ", url);


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
                map.put("id", idpe);
                return map;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void mostrarListaProductos(String response) {
        try {
            //Log.d("PRODUCTOS (mostrarLP): ","algo");
            Log.d("PRODUCTOS (mostrarLP): ",response);
            JSONArray jsonArray = new JSONArray(response);
            for(int i= 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idpt = jsonObject.getString("idpt");
                String productodes = jsonObject.getString("productodes");
                String cantidad = jsonObject.getString("cantidad");
                String precio = jsonObject.getString("precio");
                HashMap<String,String> map = new HashMap<>();
                map.put("idpt",idpt);
                Log.d("productodes: ", productodes);
                map.put("productodes",productodes);
                map.put("cantidad",cantidad);
                map.put("precio",precio);
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

            PedidoAdapter pedidoAdapter = new PedidoAdapter(getActivity(),arrayList);
            mlvProductos.setAdapter(pedidoAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
