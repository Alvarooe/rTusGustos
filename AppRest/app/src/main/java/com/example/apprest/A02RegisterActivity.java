package com.example.apprest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import datos.Cliente;

public class A02RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText metNombre, metUsuario, metContrasena;
    EditText metApellidoPaterno, metApellidoMaterno, metSexo, metFechaNacimiento, metDistritoResidencia;
    Button mbtRegistrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a02_register);
        metNombre = findViewById(R.id.etNombre);
        metApellidoPaterno = findViewById(R.id.etApellidoPaterno);
        metApellidoMaterno = findViewById(R.id.etApellidoMaterno);
        metSexo = findViewById(R.id.etSexo);
        metFechaNacimiento = findViewById(R.id.etFechaNacimiento);
        metDistritoResidencia = findViewById(R.id.etDistritoResidencia);
        metUsuario = findViewById(R.id.etUsuario);
        metContrasena = findViewById(R.id.etContrasena);
        mbtRegistrarse = findViewById(R.id.btRegistrarse);
        mbtRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRegistrarse:
                registrarse();
                break;
        }
    }

    private void registrarse() {
        final String nombre = metNombre.getText().toString();
        final String apellidopaterno = metApellidoPaterno.getText().toString();
        final String apellidomaterno = metApellidoMaterno.getText().toString();
        final String dni = "99999999";
        final String celular = "999999999";
        final String correoelectronico = "";
        final String sexo = metSexo.getText().toString();
        final String fechanacimiento = metFechaNacimiento.getText().toString(); //"15/01/2000";
        final String ciudadresidencia = "Lima";
        final String distritoresidencia = metDistritoResidencia.getText().toString();
        final String estado = "Activo";
        final String usuario = metUsuario.getText().toString();
        final String contrasena = metContrasena.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Parametros.rutaServidor + "createcliente_s.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPUESTA",response);
                        //enviarDetalle();
                        //Toast.makeText(this, "Cliente creado",Toast.LENGTH_SHORT).show();
                        desplazarse();
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
                map.put("nombre",nombre);
                map.put("apellidopaterno",apellidopaterno);
                map.put("apellidomaterno",apellidomaterno);
                map.put("dni",dni);
                map.put("celular",celular);
                map.put("correoelectronico",correoelectronico);
                map.put("sexo",sexo);
                map.put("fechanacimiento",fechanacimiento);
                map.put("ciudadresidencia",ciudadresidencia);
                map.put("distritoresidencia",distritoresidencia);
                map.put("estado",estado);
                map.put("usuario",usuario);
                map.put("contrasena",contrasena);
                return map;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    private void desplazarse() {
        startActivity(new Intent(this,A03LoginActivity.class));
    }
}