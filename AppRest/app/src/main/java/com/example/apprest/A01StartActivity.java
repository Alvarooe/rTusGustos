package com.example.apprest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class A01StartActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a01_start);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarInicio();
                /*Intent intent=new Intent(A01StartActivity.this,MainActivity.class);
                startActivity(intent);*/
                finish();
            }
        },3000);
    }

    private void mostrarInicio() {
        SharedPreferences sharedPref = this.getSharedPreferences("datosSesion", Context.MODE_PRIVATE);
        String datos = sharedPref.getString("datosUsuario","00000");
        if(datos.equals("00000")) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            startActivity(new Intent(this, A05PlatformActivity.class));
        }
        finish();//Para que se cierre el activity
    }
}
