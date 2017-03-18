package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.R;

import java.util.List;


public class AdapterHistoricUser extends ArrayAdapter<History> {

    public AdapterHistoricUser(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterHistoricUser(Context context, int resource, List<History> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_historique_client, null);
        }

        History p = getItem(position);
        if (p != null) {
            TextView toBusiness_name = (TextView) v.findViewById(R.id.business_name);
            TextView prix = (TextView) v.findViewById(R.id.prix);
            TextView date = (TextView) v.findViewById(R.id.date);


            if (toBusiness_name != null) {
                toBusiness_name.setText(p.get_businessname());
            }
            if (prix != null) {
                prix.setText(p.get_prix());
            }
            if (date != null) {
                date.setText(p.get_date());
            }


        }

        return v;
    }

}