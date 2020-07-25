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
import android.widget.TextView;
import android.widget.Toast;

import datos.Coleccion;

public class A05PlatformActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a05_platform);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title); //NVO
        setSupportActionBar(toolbar);
        //mTitle.setText(toolbar.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contenedor_principal);
                Fragment fragmentPadre = fragment.getParentFragment();
                if (fragment != null) {
                    getSupportFragmentManager().popBackStack();
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
        getMenuInflater().inflate(R.menu.a05_platform, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_inicio) {
            mostrarPlataforma();
        } else if (id == R.id.nav_menu) {
            mostrarMenu2();

        } else if (id == R.id.nav_pedido) {
            mostrarPedido();

        } else if (id == R.id.nav_contacto) {
            mostrarContacto();

        } else if (id == R.id.nav_cerrarSesion) {
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

    private void mostrarMenu2() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A06bMenu2Fragment())
                .commit();
    }

    private void mostrarPedido() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A08bPedido2Fragment())
                .commit();
    }

    private void mostrarContacto() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedor_principal, new A09ContactoFragment())
                .commit();
    }

}
