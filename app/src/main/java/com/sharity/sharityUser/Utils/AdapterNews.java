package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.R;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.bottomBar;


public class AdapterNews extends BaseAdapter {

    Context context;
    LayoutInflater inflat;
    private ArrayList<Drawer> items;


    public AdapterNews(Context context, ArrayList<Drawer> objects) {
        inflat = LayoutInflater.from(context);
        items = objects;
        this.context=context;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Drawer it = items.get(position);
        int listViewItemType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            if (listViewItemType == 0) {
                convertView = inflat.inflate(R.layout.row_news_listview, null);
                holder.logo = (ImageView) convertView
                        .findViewById(R.id.logo);
            } else if(listViewItemType == 1){
                convertView = inflat.inflate(R.layout.adapter_newsfeed_list, null);
                holder.items = (TextView) convertView
                        .findViewById(R.id.items);
                holder.divider=(View)convertView.findViewById(R.id.divider);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (it != null) {
            if (listViewItemType == 0) {
                if (Status(context).equals("Business")) {
                    holder.logo.setImageResource(R.drawable.logopro_drawer);
                }else if (Status(context).equals("User")){
                    holder.logo.setImageResource(R.drawable.logo_drawer);
                }

            } else if(listViewItemType == 1) {
                if(holder.items!= null) {
                if (Status(context).equals("Business")) {
                    holder.divider.setBackgroundColor(context.getResources().getColor(R.color.red));
                }else if (Status(context).equals("User")){
                    holder.divider.setBackgroundColor(context.getResources().getColor(R.color.green));
                }
                    holder.items.setText(it.getItems());
                }
            }
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Drawer getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        View divider;
        TextView items;
        ImageView logo;

        public String toString() {
            return "-";
        }
    }

    private String Status(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("status", "");         // getting String
        return accountDisconnect;
    }
}

