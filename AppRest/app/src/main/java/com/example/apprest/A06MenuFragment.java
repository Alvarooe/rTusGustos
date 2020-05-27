package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class A06MenuFragment extends Fragment {

    ListView mlvCategorias;

    public A06MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a06_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mlvCategorias = view.findViewById(R.id.lvCategorias);
        leerCategorias();
    }

    private void leerCategorias() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Parametros.rutaServidor + "listcategoria.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Categorías: ", response);
                        mostrarListaCategorias(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Categorías: ", volleyError.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void mostrarListaCategorias(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);


            String[] categorias = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String categoriades = jsonObject.getString("categoriades");
                categorias[i] = categoriades;
                Log.e("categoría i:", categorias[i]);
            }

            ListAdapter listAdapter = new ArrayAdapter<>(
                    getActivity(), android.R.layout.simple_list_item_1, categorias
            );


            //arrayList = new ArrayList<>();

            /*
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int idct = jsonObject.getInt("idct");
                String categoriades = jsonObject.getString("categoriades");
                HashMap<String, Object> map = new HashMap<>();
                map.put("idct",idct);
                map.put("categoriades",categoriades);
                arrayList.add(map);
                Log.e("categoría i:", String.valueOf(idct) + " " + categoriades);
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    getActivity(),arrayList,R.layout.item_categorias,
                    (String[]) new Object[]{"idct","categoriades"},
                    new int[]{R.id.tvIdCT,R.id.tvCategoriaDes}
            );
            */


            mlvCategorias.setAdapter(listAdapter);

            //mlvCategorias.setOnItemClickListener(this);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }



}
