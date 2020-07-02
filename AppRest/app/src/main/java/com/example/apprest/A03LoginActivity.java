package com.example.apprest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
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

import datos.Categoria;
import datos.Cliente;
import datos.Coleccion;
import datos.ColeccionCategoria;
import datos.ColeccionProducto;
import datos.ColeccionRegla;
import datos.Producto;

public class A03LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button mbtnIniciarSesion, mbtRegistrar;
    EditText metCorreo, metContrasena;
    CheckBox mchkGuardarSesion;
    Categoria categoria;
    Producto producto;

    AnimationDrawable frameAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a03_login);
        //ImageView img = (ImageView)findViewById(R.id.imLogoAnimado);
        //img.setBackgroundResource(R.drawable.ic_logo_loop);
        metCorreo = findViewById(R.id.etCorreo);
        metContrasena = findViewById(R.id.etContrasena);
        mbtnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        mbtRegistrar = findViewById(R.id.btRegistrar);
        mchkGuardarSesion = findViewById(R.id.chkGuardarSesion);
        mbtnIniciarSesion.setOnClickListener(this);
        mbtRegistrar.setOnClickListener(this);

        //frameAnimation = (AnimationDrawable) img.getBackground();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //frameAnimation.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRegistrar:
                registrar();
                break;
            case R.id.btnIniciarSesion:
                iniciarSesion();
                break;
        }
    }
    public void registrar(){
        startActivity(new Intent(this,A02RegisterActivity.class));
    }

    /*public void iniciarSesion(View v) {startActivity(new Intent(this,A05PlatformActivity.class));
    }*/

    public void iniciarSesion() {
        final String usuario = metCorreo.getText().toString();
        final String clave = metContrasena.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        //String url ="http://172.17.69.19/servicio/servicioproductos.php";
        String url = Parametros.rutaServidor + "iniciarsesion_s.php";
        //String url ="http://localhost/servicio/servicioproductos.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA",response);
                        evaluarRespuesta(response);
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
                map.put("usuario",usuario);
                map.put("clave",clave);
                return map;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
        //startActivity(new Intent(this,EscritorioActivity.class));
    }

    private void evaluarRespuesta(String response) {
        if(response.equals("-1")){
            Toast.makeText(this,"El usuario no existe",Toast.LENGTH_SHORT).show();
        }else if(response.equals("-2")){
            Toast.makeText(this,"La clave es incorrecta",Toast.LENGTH_SHORT).show();
        }else{
            try {

                JSONArray jsonArray = new JSONArray(response);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                //String nombre = jsonObject.getString("nombre");

                Cliente.idCliente = jsonObject.getString("idCliente");
                Cliente.nombre = jsonObject.getString("nombre");
                Cliente.apellidoPaterno = jsonObject.getString("apellidoPaterno");
                Cliente.apellidoMaterno = jsonObject.getString("apellidoMaterno");
                Cliente.dni = jsonObject.getString("dni");
                Cliente.celular = jsonObject.getString("celular");
                Cliente.correoElectronico = jsonObject.getString("correoElectronico");
                Cliente.sexo = jsonObject.getString("sexo");
                Cliente.fechaNacimiento = jsonObject.getString("fechaNacimiento");
                Cliente.ciudadResidencia = jsonObject.getString("ciudadResidencia");
                Cliente.distritoResidencia = jsonObject.getString("distritoResidencia");
                Cliente.estado = jsonObject.getString("estado");
                //Parametros.coleccion = new ArrayList<>();
                Coleccion.micoleccion = new ArrayList<>();
                ColeccionRegla.micoleccionregla = new ArrayList<>();
                ColeccionCategoria.micoleccioncategoria = new ArrayList<>();
                ColeccionProducto.micoleccionproducto = new ArrayList<>();
                Parametros.Total = 0.0;
                Parametros.PedidoHecho = 0;
                Parametros.Respuesta = "";

                Toast.makeText(this,"Bienvenido, " + Cliente.nombre,Toast.LENGTH_SHORT).show();
                leerCategorias();
                leerProductos();
                startActivity(new Intent(this,A05PlatformActivity.class));
                verificarGuardarSesion(response);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void leerCategorias() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Parametros.rutaServidor + "listcategoria.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //final String response2 = response;
                        //Log.d("Categorías: ", response);
                        //Toast.makeText(getApplicationContext(), response,Toast.LENGTH_LONG).show(); //ok
                        guardarCategorias(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Categorías: ", volleyError.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void guardarCategorias(String response) {
        //Toast.makeText(this,"Ok", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this,response, Toast.LENGTH_SHORT).show();
        try {
            //Esto es para que el texto sea interpretado en formato JSON
            JSONArray jsonArray = new JSONArray(response);
            //ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
            for(int i = 0; i< jsonArray.length(); i++){
                categoria = new Categoria();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                categoria.idct = jsonObject.getString("idct");
                categoria.categoriades = jsonObject.getString("categoriades");
                ColeccionCategoria.micoleccioncategoria.add(i,categoria);
            }
            //Toast.makeText(this,"Tamaño: " + ColeccionCategoria.micoleccioncategoria.size(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void leerProductos() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Parametros.rutaServidor + "listproducto.php";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Productos: ", response);
                        guardarProductos(response);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Categorías: ", volleyError.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    private void guardarProductos(String response) {
        try {
            //Esto es para que el texto sea interpretado en formato JSON
            JSONArray jsonArray = new JSONArray(response);
            //ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
            for(int i = 0; i< jsonArray.length(); i++){
                producto = new Producto();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                producto.idpt = jsonObject.getString("idpt");
                producto.productodes = jsonObject.getString("productodes");
                producto.idct = jsonObject.getString("idct");
                producto.precio = jsonObject.getString("precio");
                producto.rutaimagen = jsonObject.getString("rutaimagen");
                ColeccionProducto.micoleccionproducto.add(i,producto);
            }
            //Toast.makeText(this,"Tamaño: " + ColeccionProducto.micoleccionproducto.size(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verificarGuardarSesion(String response) {
        if(mchkGuardarSesion.isChecked()){
            SharedPreferences sharedPreferences = getSharedPreferences("datosSesion", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("datosUsuario", response);
            editor.commit();
        }
    }
}
