package com.example.apprest;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ProductosAdapter extends ArrayAdapter {

    Activity mactivity;
    ArrayList marraylist;

    public ProductosAdapter(Activity activity, ArrayList arrayList) {
        super(activity,R.layout.item_productos,arrayList);
        mactivity = activity;
        marraylist = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = mactivity.getLayoutInflater().inflate(R.layout.item_productos,null);

        TextView mtvIdPT = rootView.findViewById(R.id.tvIdPT);
        TextView mtvProductoDes = rootView.findViewById(R.id.tvProductoDes);
        TextView mtvPrecio = rootView.findViewById(R.id.tvPrecio);

        ImageView mivRutaImagen = rootView.findViewById(R.id.ivRutaImagen);

        HashMap<String,String> map = (HashMap<String,String>)marraylist.get(position);

        mtvIdPT.setText(map.get("idpt"));
        mtvProductoDes.setText(map.get("productodes"));
        mtvPrecio.setText("S/ " + map.get("precio"));

        String ruta = Parametros.rutaServidor + map.get("rutaimagen");
        Picasso.get().load(ruta).into(mivRutaImagen);

        return rootView;
    }
}

