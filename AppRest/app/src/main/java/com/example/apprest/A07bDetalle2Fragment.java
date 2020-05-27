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
import android.widget.ImageView;
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

import datos.DetallePedidoProvisional;


/**
 * A simple {@link Fragment} subclass.
 */
public class A07bDetalle2Fragment extends Fragment implements AdapterView.OnItemClickListener {

    TextView mtvCategoria;
    ListView mlvProductos;
    ArrayList<HashMap<String,String>> arrayList;

//    ImageView mivRegresar;


    public A07bDetalle2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a07b_detalle2, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mtvCategoria = view.findViewById(R.id.tvCategoria);
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
        //String url = Parametros.rutaServidor + "listcategoriaproducto.php";
        String url = Parametros.rutaServidor + "listcategoriaproducto.php" + "?id=" + idct;
        //Log.d("PRODUCTOS URL:",url);
        Log.d("PRODUCTOS idct: ", idct);
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
                map.put("id", idct);
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
                String idct2 = jsonObject.getString("idct");
                String precio = jsonObject.getString("precio");
                String rutaimagen = jsonObject.getString("rutaimagen");
                HashMap<String,String> map = new HashMap<>();
                map.put("idpt",idpt);
                Log.d("productodes: ", productodes);
                Log.d("idct2: ", idct2);
                Log.d("rutaimagen0: ", rutaimagen);
                Log.d("precio0: ", precio);
                map.put("productodes",productodes);
                Log.d("idct2: ", idct2);
                //Log.d("productodes: ", productodes);
                Log.d("idct: ", idct2);
                map.put("precio",precio);
                Log.d("precio: ", precio);
                map.put("rutaimagen",rutaimagen);
                Log.d("rutaimagen: ", rutaimagen);
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
        //Se obtiene en un HashMap el elemento del arraylist seleccionado

        final String idpt = map.get("idpt");
        final String productodes = map.get("productodes");
        final String idct = map.get("idct");
        final String precio = map.get("precio");
        String rutaimagen = map.get("rutaimagen");
        //Se obtiene el contenidon de un campo del HashMap

        Toast.makeText(getActivity(),productodes,Toast.LENGTH_SHORT).show();
        Log.d("Producto: ", productodes + " - " + idpt + " - " + position);




        Bundle bundle = new Bundle();
        bundle.putString("idpt",idpt);
        bundle.putString("productodes",productodes);
        bundle.putString("idct",idct);
        bundle.putString("precio",precio);
        bundle.putString("rutaimagen",rutaimagen);








        RequestQueue queue2 = Volley.newRequestQueue(getActivity());
        String url2 = Parametros.rutaServidor + "createdetallepedido_s.php";
//url2 = "https://pix.pe/servicioandroid/servicioproductos.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA",response);
                        //evaluarRespuesta(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.d("ERROR",error.getMessage());
                //Log.e("ERROR",error.getMessage());
                Log.i("ERROR",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map2 = new HashMap<>();
                map2.put("idpe","1");
                map2.put("idpt",idpt);
                map2.put("cantidad","1");
                map2.put("observacion","");
                //map2.put("caty","1");
                return map2;
            }
        };
// Add the request to the RequestQueue.
        queue2.add(stringRequest2);








        //String idct2 = bundle.getString("idct");
        //String categoriades2 = bundle.getString("categoriades");
        //Log.d("Categoría2: ", categoriades2 + " - " + idct2);

        //A07DetalleFragment a07DetalleFragment = new A07DetalleFragment();
        A08PedidoFragment a08PedidoFragment = new A08PedidoFragment();

        //a07DetalleFragment.setArguments(bundle);
        a08PedidoFragment.setArguments(bundle);

        //Para llamar desde un fragment a otro fragment
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .commit();*/
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .addToBackStack(null)
                .commit();*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, a08PedidoFragment,"A07bDetalle2Fragment")
                .addToBackStack(null)
                .commit();

        //getActivity() hace referencia al activity en el que está contenido el fragment

        //Con setTitle se cambia el titulo del Activity
        //getActivity().setTitle(categoriades + "\n Especialidades");
    }

//    private void regresar(View view){
//
//        A06bMenu2Fragment a06bMenu2Fragment = new A06bMenu2Fragment();
//
//        //Para llamar desde un fragment a otro fragment
//        /*getFragmentManager().beginTransaction()
//                .replace(R.id.contenedor_principal,a07DetalleFragment)
//                .commit();*/
//
//
//        getFragmentManager().beginTransaction()
//                .replace(R.id.contenedor_principal,a06bMenu2Fragment)
//                .commit();
//
//
//
//        //getActivity() hace referencia al activity en el que está contenido el fragment
//
//        //Con setTitle se cambia el titulo del Activity
//        //getActivity().setTitle(categoriades + "\n Especialidades");
//    }
}
