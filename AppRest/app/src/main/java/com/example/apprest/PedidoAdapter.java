package com.example.apprest;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


public class PedidoAdapter extends ArrayAdapter {

    Activity mactivity;
    ArrayList marraylist;

    public PedidoAdapter(Activity activity, ArrayList arrayList) {
        super(activity,R.layout.item_pedido,arrayList);
        mactivity = activity;
        marraylist = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = mactivity.getLayoutInflater().inflate(R.layout.item_pedido,null);

        TextView mtvIdPT = rootView.findViewById(R.id.tvIdPT);
        TextView mtvProductoDes = rootView.findViewById(R.id.tvProductoDes);
        TextView mtvCantidad = rootView.findViewById(R.id.tvCantidad);
        TextView mtvPrecio = rootView.findViewById(R.id.tvPrecio);
        TextView mtvSubtotal = rootView.findViewById(R.id.tvSubtotal);
        TextView mtvTotal = rootView.findViewById(R.id.tvTotal);

        HashMap<String,String> map = (HashMap<String,String>)marraylist.get(position);

        mtvIdPT.setText(map.get("idpt"));
        mtvProductoDes.setText(map.get("productodes"));
        mtvCantidad.setText(map.get("cantidad"));
        mtvPrecio.setText("S/" + map.get("precio"));
        Double subtotal = Integer.parseInt(map.get("cantidad"))*Double.parseDouble(map.get("precio"));
        mtvSubtotal.setText("S/" + subtotal.toString());

        return rootView;
    }
}

