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
        //El método getView examina una a uno los elementos del arrayList
        //Double total = 0.0;
        //Parametros.Total = 0.0;

        View rootView = mactivity.getLayoutInflater().inflate(R.layout.item_pedido,null);
        //Esta variable representa al layout de cada item
        TextView mtvIdPT = rootView.findViewById(R.id.tvIdPT);
        TextView mtvProductoDes = rootView.findViewById(R.id.tvProductoDes);
        TextView mtvCantidad = rootView.findViewById(R.id.tvCantidad);
        TextView mtvPrecio = rootView.findViewById(R.id.tvPrecio);
        TextView mtvSubtotal = rootView.findViewById(R.id.tvSubtotal);
        TextView mtvTotal = rootView.findViewById(R.id.tvTotal);

        //La variable position indica la posicion del elemento que se está examinando
        HashMap<String,String> map = (HashMap<String,String>)marraylist.get(position);

        mtvIdPT.setText(map.get("idpt"));
        mtvProductoDes.setText(map.get("productodes"));
        mtvCantidad.setText(map.get("cantidad"));
        mtvPrecio.setText("S/" + map.get("precio"));
        //String subtotal = String.valueOf(Integer.parseInt(map.get("cantidad"))*Double.parseDouble(map.get("precio")));
        Double subtotal = Integer.parseInt(map.get("cantidad"))*Double.parseDouble(map.get("precio"));
        mtvSubtotal.setText("S/" + subtotal.toString());
        //Parametros.Total = Parametros.Total + subtotal;
        //Log.d("Total adaptador: ", Parametros.Total.toString()); //NVO
        //mtvTotal.setText("S/" + Parametros.Total.toString());

        return rootView;
    }
}

