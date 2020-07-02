package com.example.apprest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import datos.Coleccion;

public class A05PlatformActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a05_platform);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //Toast.makeText(A05PlatformActivity.this, "clicked", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getSupportFragmentManager();
                //Fragment fragment = getFragmentManager().findFragmentById(R.id.llContenedor);
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contenedor_principal);
                Fragment fragmentPadre = fragment.getParentFragment();
                if (fragment != null) {
                    getSupportFragmentManager().popBackStack();
                    /*FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(fragment);
                    fragmentTransaction.commit();
                    if (fragmentPadre != null) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contenedor_principal,fragmentPadre)
                                .commit();
                    }*/
                    //mbtVer.setVisibility(View.VISIBLE);
                } else {
                    //super.onBackPressed();
                }

            }
        });
        fab.hide();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.a05_platform, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            mostrarPlataforma();
        } else if (id == R.id.nav_menu) {
            //mostrarMenu();
            mostrarMenu2();

        } else if (id == R.id.nav_pedido) {
            mostrarPedido();
            //mostrarMenu2();

        //} else if (id == R.id.nav_promocion) {
        //    mostrarPromocion();
            //mostrarMenu2();

        } else if (id == R.id.nav_contacto) {
            mostrarContacto();

        } else if (id == R.id.nav_cerrarSesion) {
            //mostrarAyuda();
            cerrarSesion();            

        } else if (id == R.id.nav_salir) {
            salir();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mostrarPlataforma() {
        LinearLayout ll = findViewById(R.id.contenedor_principal);
        ll.removeAllViews();
    }

    private void salir() {
        finish();
        //startActivity(new Intent(this, A01StartActivity.class));
    }

    private void cerrarSesion() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Cierre de sesión");
        alertDialog.setMessage("¿Seguro de cerrar la sesión?");

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cierreSesion();
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
        alertDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

    }

    private void cierreSesion() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                "datosSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        finish();
        startActivity(new Intent(this, A03LoginActivity.class));
    }

    private void mostrarMenu() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A06MenuFragment())
                .commit();
    }

    private void mostrarMenu2() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A06bMenu2Fragment())
                .commit();
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A06xMenuFragment())
                .commit();*/
    }

    private void mostrarPedido() {
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08PedidoFragment())
                .commit();*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08bPedido2Fragment())
                .commit();
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08xPedidoFragment())
                .commit();*/
    }

    private void mostrarPromocion() {
        /*getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08PedidoFragment())
                .commit();*/
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08ePromocionFragment())
                .commit();
    }

    private void mostrarContacto() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A09ContactoFragment())
                .commit();
    }

    private void mostrarAyuda() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A10AyudaFragment())
                .commit();
    }

//    private void regresar(View view){
//
//        A06bMenu2Fragment a06bMenu2Fragment = new A06bMenu2Fragment();
//
//        //Para llamar desde un fragment a otro fragment
//        /*getFragmentManager().beginTransaction()
//                .replace(R.id.contenedor_principal,a07DetalleFragment)
//                .commit();*/
//        /*getFragmentManager().beginTransaction()
//                .replace(R.id.contenedor_principal,a06bMenu2Fragment)
//                .commit();*/
//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.contenedor_principal, new A06bMenu2Fragment())
//                .commit();
//
//        //getActivity() hace referencia al activity en el que está contenido el fragment
//
//        //Con setTitle se cambia el titulo del Activity
//        //getActivity().setTitle(categoriades + "\n Especialidades");
//    }


    //Nvo
//    @Override
//    public void onBackPressed() {
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        //Fragment fragment = getFragmentManager().findFragmentById(R.id.llContenedor);
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contenedor_principal);
//        if (fragment != null) {
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            fragmentTransaction.remove(fragment);
//            fragmentTransaction.commit();
//            /*mbtVer.setVisibility(View.VISIBLE);*/
//        } else {
//            super.onBackPressed();
//        }
//
//    }

    /*public void limpiar(View view)
    {
        Coleccion.micoleccion.clear();
        //Toast.makeText(this,"Colección vaciada",Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction().detach().attach(this).commit();
    }*/
}
