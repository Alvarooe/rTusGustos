package com.example.apprest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datos.Cliente;
import datos.Coleccion;
import datos.ColeccionRegla;
import datos.DetallePedidoProvisional;
import datos.Regla;


/**
 * A simple {@link Fragment} subclass.
 */
public class A08bPedido2Fragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    ArrayList<HashMap<String,String>> arrayList;
    DetallePedidoProvisional detallePedidoProvisional;
    Regla regla;
    Regla perfil;
    Regla recomendacion;
    ListView mlvProductos;
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
        if (!Parametros.Respuesta.equals("")) {
            leerPedidoProvisional();
            pedir();
        }
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
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.hide();

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

        //getActivity().setTitle(categoriades);

        arrayList = new ArrayList<>();

        leerPedidoProvisional();
    }

    private void leerPedidoProvisional() {
        try {
            Parametros.Total = 0.0;
            for(int i = 0; i< Coleccion.micoleccion.size(); i++){
                detallePedidoProvisional = (DetallePedidoProvisional) Coleccion.micoleccion.get(i);
                String idpt = detallePedidoProvisional.idpt;
                String productodes = detallePedidoProvisional.productodes;
                String cantidad = String.valueOf(detallePedidoProvisional.cantidad);
                String precio = String.valueOf(Double.parseDouble(detallePedidoProvisional.precio) - Double.parseDouble(detallePedidoProvisional.descuento));
                HashMap<String,String> map = new HashMap<>();
                map.put("idpt",idpt);
                map.put("productodes",productodes);
                map.put("cantidad",cantidad);
                map.put("precio",precio);
                arrayList.add(map);
                Double subtotal = Integer.parseInt(map.get("cantidad"))*Double.parseDouble(map.get("precio"));
                Parametros.Total = Parametros.Total + subtotal;
            }
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

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, a085EdicionPedidoFragment,"A08bPedido2Fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btPedir:
                //pedir();
                obtenerReglas();
                break;
            case R.id.btLimpiar:
                limpiar();
                break;
        }
    }

    private void obtenerReglas() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = Parametros.rutaServidor + "listocasionpromocion.php" + "?id=" + Cliente.idCliente;
        //Log.d("PRODUCTOS idct: ", idct);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final String response2 = response.toString();
                        //Log.d("REGLAS",response);
                        //Toast.makeText(getActivity(), response,Toast.LENGTH_SHORT).show(); //OK
                        //Toast.makeText(getActivity(), "respuesta 2",Toast.LENGTH_SHORT).show(); //OK
                        //Toast.makeText(getActivity(), response2,Toast.LENGTH_LONG).show(); //OK
                        //guardarReglas(response2);
                        //guardarReglas("algo");
                        guardarReglas(response2.toString());
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
                map.put("id", Cliente.idCliente);
                return map;
            }
        };
        queue.add(stringRequest);
    }

    private void guardarReglas(String response) {
        //Toast.makeText(getActivity(), "Guardará reglas",Toast.LENGTH_LONG).show(); //OK
        //Toast.makeText(getActivity(), response.toString(),Toast.LENGTH_LONG).show(); //OK
        try {
            //Log.d("PRODUCTOS (mostrarLP): ",response);
            //Toast.makeText(getActivity(), "Guardará reglas",Toast.LENGTH_SHORT).show(); //KO
            JSONArray jsonArray = new JSONArray(response);
            for(int i= 0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String idpr = jsonObject.getString("idpr");
                String idcliente = jsonObject.getString("idcliente");
                String entradafria = jsonObject.getString("entradafria");
                String entradacaliente = jsonObject.getString("entradacaliente");
                String pasta = jsonObject.getString("pasta");
                String pescado = jsonObject.getString("pescado");
                String ress = jsonObject.getString("res");
                String postre = jsonObject.getString("postre");
                String sandwich = jsonObject.getString("sandwich");
                String bebida = jsonObject.getString("bebida");
                String idpt = jsonObject.getString("idpt");
                String precio = jsonObject.getString("precio");

                regla = new Regla();
                regla.idpr = idpr;
                regla.idcliente = idcliente;
                regla.entradafria = entradafria;
                regla.entradacaliente = entradacaliente;
                regla.pasta = pasta;
                regla.pescado = pescado;
                regla.ress = ress;
                regla.postre = postre;
                regla.sandwich = sandwich;
                regla.bebida = bebida;
                regla.idpt = idpt;
                regla.precio = precio;
                //Toast.makeText(getActivity(), regla.precio.toString(),Toast.LENGTH_LONG).show(); //OK

                //Toast.makeText(getActivity(), ColeccionRegla.micoleccionregla.size(),Toast.LENGTH_LONG).show(); //OK
                ColeccionRegla.micoleccionregla.add(i,regla);
                generarPerfil();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getActivity(), "ok",Toast.LENGTH_LONG).show(); //OK

        //Object[] reglaarray = ColeccionRegla.micoleccionregla.toArray();
        //Toast.makeText(getActivity(), reglaarray.toString(),Toast.LENGTH_LONG).show(); //KO
        //for(int i2 = 0; i2 < reglaarray.length; i2++) {
            //Log.d("Regla: ", (String) reglaarray[i2]); //KO
            //Toast.makeText(getActivity(), reglaarray[i2].toString(),Toast.LENGTH_LONG).show(); //KO
        //}
        //Regla regla0 = (Regla) ColeccionRegla.micoleccionregla.get(0);
        //Toast.makeText(getActivity(), regla0.idpt,Toast.LENGTH_LONG).show(); //KO
        //Toast.makeText(getActivity(), "fin",Toast.LENGTH_LONG).show(); //KO

    }

    private void generarPerfil() {
        perfil = new Regla();
        perfil.idcliente = Cliente.idCliente;
        for(int i = 0; i< Coleccion.micoleccion.size(); i++){
            detallePedidoProvisional = (DetallePedidoProvisional) Coleccion.micoleccion.get(i);

            switch (detallePedidoProvisional.idct) {
                case "1":
                    perfil.entradafria = "1";
                case "2":
                    perfil.entradacaliente = "1";
                case "3":
                    perfil.pasta = "1";
                case "4":
                    perfil.pescado = "1";
                case "5":
                    perfil.ress = "1";
                case "6":
                    perfil.postre = "1";
                case "7":
                    perfil.sandwich = "1";
                case "8":
                    perfil.bebida = "1";
            }
            if (detallePedidoProvisional.idpt=="14") {
                perfil.idpt = "1";
            }
        }
        //Object perfilcad = perfil.toString();
        //Log.d("perfil: ", (String) perfilcad);
        //Toast.makeText(getActivity(), perfil.toString(),Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(), perfil.idcliente,Toast.LENGTH_LONG).show(); //ok
        extraerRegla();
    }

    private void extraerRegla() {
        //Toast.makeText(getActivity(), perfil.idcliente,Toast.LENGTH_LONG).show(); //ok
        String str="";
        boolean coinciden = true;
        try {
            Class<?> c = perfil.getClass();
            Field[] fields = c.getDeclaredFields();
            //String str = "";
            for (Field field : fields) {
                if (field.get(perfil) == null) {
                    field.set(perfil, "0");
                    //str += field.getName();
                }
                //str += field.getName() + ": " + field.get(perfil) + "; "; //ok
                str += field.getName() + ": " + field.get(ColeccionRegla.micoleccionregla.get(0)) + "; "; //ok
            };
            //Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show(); //ok
            for (int i = 0; i < ColeccionRegla.micoleccionregla.size(); i++) {
                Class<?> c1 = perfil.getClass();
                Field[] fields1 = c1.getDeclaredFields();
                //Class<?> c2 = ColeccionRegla.micoleccionregla.getClass();
                //Field[] fields2 = c2.getDeclaredFields();
                //Toast.makeText(getActivity(), fields1[0].getName(), Toast.LENGTH_LONG).show(); //ok
                //String str = "";
                for (Field field1 : fields1) {
                    //for (Field field2 : fields2) {
                        //if (field1.getName() == field2.getName() &
                            if ((!field1.get(perfil).toString().equals(field1.get(ColeccionRegla.micoleccionregla.get(i)).toString())) &&
                                    (field1.getName() != "precio") & (field1.getName() != "idpr") &&
                                    (field1.getName() != "idpt")) {
                            coinciden = false;
                            //Toast.makeText(getActivity(), "Campo diferente: " + field1.getName(), Toast.LENGTH_LONG).show();
                            //Toast.makeText(getActivity(), "Campo diferente: " + field1.getName() + "-" + field1.get(perfil) + ":" + field1.get(ColeccionRegla.micoleccionregla.get(i)), Toast.LENGTH_LONG).show();
                            break;
                        }
                        else {
                            //Toast.makeText(getActivity(), "Campo igual: " + field1.getName(), Toast.LENGTH_LONG).show();
                        }
                        //str += field.get(perfil); //ok
                    //}
                };
                if (coinciden == true){
                    //Regla recomendacion; // = new Regla();
                    //recomendacion = (Regla) ColeccionRegla.micoleccionregla.get(i);
                    recomendacion = (Regla) ColeccionRegla.micoleccionregla.get(i);
                    //Toast.makeText(getActivity(), recomendacion.idpt + " - " + recomendacion.precio,Toast.LENGTH_LONG).show();

                    Bundle bundle = new Bundle();
                    bundle.putString("idpt",recomendacion.idpt);
                    bundle.putString("precio",recomendacion.precio);
                    A088RecomendacionFragment a088RecomendacionFragment = new A088RecomendacionFragment();

                    a088RecomendacionFragment.setArguments(bundle);

                    //Para llamar desde un fragment a otro fragment
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .commit();*/
        /*getFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal,a07bDetalle2Fragment)
                .addToBackStack(null)
                .commit();*/
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.contenedor_principal, a088RecomendacionFragment,"A08bPedido2Fragment")
                            .addToBackStack(null)
                            .commit();
                }

            }
        }catch (Exception e) {
            e.printStackTrace();

        }
        //Toast.makeText(getActivity(), "str: " +  str, Toast.LENGTH_LONG).show(); //ok //ok
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
                        Parametros.Respuesta = "";
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
                final String descuento = detallePedidoProvisional.descuento;
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
                        map.put("descuento", descuento);
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
