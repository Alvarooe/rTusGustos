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

public class A03LoginActivity extends AppCompatActivity implements View.OnClickListener {
    Button mbtnIniciarSesion, mbtRegistrar;
    EditText metCorreo, metContrasena;
    CheckBox mchkGuardarSesion;

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
                Parametros.Total = 0.0;
                Parametros.PedidoHecho = 0;


                Toast.makeText(this,"Bienvenido, " + Cliente.nombre,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,A05PlatformActivity.class));
                verificarGuardarSesion(response);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
