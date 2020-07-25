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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

import datos.Categoria;
import datos.ColeccionCategoria;


/**
 * A simple {@link Fragment} subclass.
 */
public class A06bMenu2Fragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<HashMap<String,String>> arrayList;
    Categoria categoria;
    ListView mlvCategorias;

    public A06bMenu2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_a06b_menu2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mlvCategorias = view.findViewById(R.id.lvCategorias);
        mostrarCategorias();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();
    }

    private void mostrarCategorias() {
        try {
            arrayList =  new ArrayList<>();
            for(int i = 0; i< ColeccionCategoria.micoleccioncategoria.size(); i++){
                categoria = (Categoria) ColeccionCategoria.micoleccioncategoria.get(i);
                String idct = categoria.idct;
                String categoriades = categoria.categoriades;
                HashMap<String,String> map = new HashMap<>();
                map.put("idct", idct);
                map.put("categoriades", categoriades);
                arrayList.add(map);
            }

            ListAdapter listAdapter = new SimpleAdapter(
                    getActivity(), arrayList,R.layout.item_categorias,
                    new String[]{"idct","categoriades"},
                    new int[]{R.id.tvIdCT,R.id.tvCategoriaDes}
            );

            mlvCategorias.setAdapter(listAdapter);
            mlvCategorias.setOnItemClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        HashMap<String,String> map = arrayList.get(position);

        String idct = map.get("idct");
        String categoriades = map.get("categoriades");

        Bundle bundle = new Bundle();
        bundle.putString("idct",idct);
        bundle.putString("categoriades",categoriades);

        A07cDetalle3Fragment a07cDetalle3Fragment = new A07cDetalle3Fragment();
        a07cDetalle3Fragment.setArguments(bundle);

        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .commit();*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, a07cDetalle3Fragment,"A06bMenu2Fragment")
                .addToBackStack(null)
                .commit();

        //getActivity().setTitle(categoriades + "\n Especialidades");
    }
}
