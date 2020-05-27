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
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import datos.DetallePedidoProvisional;


/**
 * A simple {@link Fragment} subclass.
 */
public class A08bPedido2Fragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListView mlvProductos;
    ArrayList<HashMap<String,String>> arrayList;
    DetallePedidoProvisional detallePedidoProvisional;
    TextView mtvTotal;
    Button mbtPedir, mbtLimpiar;


    public A08bPedido2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a08b_pedido2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mlvProductos = view.findViewById(R.id.lvProductos);
        //setListViewHeightBasedOnChildren(mlvProductos);
        //mlvProductos.setScrollContainer(false);
        mtvTotal = view.findViewById(R.id.tvTotal);
        mbtPedir = view.findViewById(R.id.btPedir);
        mbtLimpiar = view.findViewById(R.id.btLimpiar);
        mbtPedir.setOnClickListener(this);
        mbtLimpiar.setOnClickListener(this);
        if (Coleccion.micoleccion.size() == 0){
            mbtPedir.setVisibility(View.GONE);
            mbtLimpiar.setVisibility(View.GONE);
        }
        else
        {
            mbtPedir.setVisibility(View.VISIBLE);
            mbtLimpiar.setVisibility(View.VISIBLE);
        }
        if (Parametros.PedidoHecho == 1){
            mbtPedir.setEnabled(false);
            mbtPedir.setAlpha(.5f);
        }


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

    private void leerProductos(String idpe) {
        try {
            Parametros.Total = 0.0;
            for(int i = 0; i< Coleccion.micoleccion.size(); i++){
                detallePedidoProvisional = (DetallePedidoProvisional) Coleccion.micoleccion.get(i);

                String idpt = detallePedidoProvisional.idpt;
                String productodes = detallePedidoProvisional.productodes;
                String cantidad = String.valueOf(detallePedidoProvisional.cantidad);
                String precio = detallePedidoProvisional.precio;
                HashMap<String,String> map = new HashMap<>();
                map.put("idpt",idpt);
                Log.d("productodes: ", productodes);
                map.put("productodes",productodes);
                map.put("cantidad",cantidad);
                map.put("precio",precio);
                arrayList.add(map);
                Double subtotal = Integer.parseInt(map.get("cantidad"))*Double.parseDouble(map.get("precio"));
                Parametros.Total = Parametros.Total + subtotal;
            }
            /*
            ListAdapter listAdapter = new SimpleAdapter(
                    getActivity(),arrayList,R.layout.item_productos,
                    new String[]{"idproducto","nombre","detalle","precio"},
                    new int[]{R.id.tvIdproducto,R.id.tvNombre,R.id.tvDetalle,R.id.tvPrecio}
            );
            mlvProductos.setAdapter(listAdapter);
            */

            //Log.d("Total pedido: ", Parametros.Total.toString()); //NVO
            mtvTotal.setText("S/" + Parametros.Total.toString());

            PedidoAdapter pedidoAdapter = new PedidoAdapter(getActivity(),arrayList);
            mlvProductos.setAdapter(pedidoAdapter);
            setListViewHeightBasedOnChildren(mlvProductos);
            mlvProductos.setScrollContainer(false);
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
        final String cantidad = map.get("cantidad");

        Bundle bundle = new Bundle();
        bundle.putString("idpt",idpt);
        bundle.putString("productodes",productodes);
        bundle.putString("cantidad",cantidad);
        bundle.putInt("posicion",position);

        A085EdicionPedidoFragment a085EdicionPedidoFragment = new A085EdicionPedidoFragment();

        a085EdicionPedidoFragment.setArguments(bundle);

        //Para llamar desde un fragment a otro fragment
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .commit();*/
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .addToBackStack(null)
                .commit();*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, a085EdicionPedidoFragment,"A08bPedido2Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btPedir:
                pedir();
                break;
            case R.id.btLimpiar:
                limpiar();
                break;
        }
    }

    private void limpiar() {
        Coleccion.micoleccion.clear();
        Parametros.PedidoHecho = 0;
        mbtPedir.setEnabled(true);
        mbtPedir.setAlpha(1f);
        //Toast.makeText(this,"Colección vaciada",Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }

    private void pedir() {

        final String referencia = "";
        final String fechahorapedido = "";
        final String idcliente = Cliente.idCliente;
        final String monto = Parametros.Total.toString();
        final String estado = "Pendiente";
        final String observacion = "";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Parametros.rutaServidor + "createpedido_s.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA",response);
                        //evaluarRespuesta(response);
                        filtrarRespuesta(response);
                        //enviarDetalle();
                        Toast.makeText(getActivity(),"Pedido realizado",Toast.LENGTH_SHORT).show();
                        //mbtPedir.setVisibility(View.GONE);
                        Parametros.PedidoHecho = 1;
                        mbtPedir.setEnabled(false);
                        mbtPedir.setAlpha(.5f);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("referencia",referencia);
                map.put("fechahorapedido",fechahorapedido);
                map.put("idcliente",idcliente);
                map.put("monto",monto);
                map.put("estado",estado);
                map.put("observacion",observacion);
                return map;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void filtrarRespuesta(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String idpe = jsonObject.getString("idpe");
            enviarDetalle(idpe);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void enviarDetalle(final String idpe) {
        try {
            for(int i = 0; i< Coleccion.micoleccion.size(); i++){
                detallePedidoProvisional = (DetallePedidoProvisional) Coleccion.micoleccion.get(i);

                final String idpt = detallePedidoProvisional.idpt;
                final String cantidad = String.valueOf(detallePedidoProvisional.cantidad);
                final String observacion = "";

                //crearDetalle(idpe, idpt, cantidad, observacion);

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = Parametros.rutaServidor + "createdetallepedido_s.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("RESPUESTA",response);
                                //evaluarRespuesta(response);
                                //enviarDetalle();
                                //Toast.makeText(getActivity(),"Detalle pedido realizado",Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR",error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        HashMap<String,String> map = new HashMap<>();
                        map.put("idpe",idpe);
                        map.put("idpt",idpt);
                        Log.d("idpt final: ", idpt);
                        map.put("cantidad",cantidad);
                        map.put("observacion",observacion);
                        return map;
                    }
                };
// Add the request to the RequestQueue.
                queue.add(stringRequest);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
