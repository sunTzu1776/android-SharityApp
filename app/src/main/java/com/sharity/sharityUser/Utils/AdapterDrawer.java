package com.sharity.sharityUser.Utils;

import android.content.Context;
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

import de.hdodenhof.circleimageview.CircleImageView;



public class AdapterDrawer extends BaseAdapter {

    Context context;
    LayoutInflater inflat;
    private ArrayList<Drawer> items;


    public AdapterDrawer(Context context, ArrayList<Drawer> objects) {
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
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Drawer it = items.get(position);
        int listViewItemType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            if (listViewItemType == -1) {
                convertView = inflat.inflate(R.layout.adapter_drawer_toptitle, null);
                holder.items = (TextView) convertView
                        .findViewById(R.id.items);
            }
            if (listViewItemType == 0) {
                convertView = inflat.inflate(R.layout.row_drawer_profil, null);
                holder.picture_profil = (CircleImageView) convertView.findViewById(R.id.logo);
                holder.name_profil = (TextView) convertView.findViewById(R.id.name_profil);

            } else if(listViewItemType == 1){
                convertView = inflat.inflate(R.layout.adapter_drawer_sub, null);
                holder.items = (TextView) convertView
                        .findViewById(R.id.items);
                holder.divider=(View)convertView.findViewById(R.id.divider);
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (it != null) {
            if (listViewItemType == -1) {
                if (Status(context).equals("Business")) {
                    holder.items.setText(it.getItems());
                }else if (Status(context).equals("User")){
                    holder.items.setText(it.getItems());
                }
            }
           else if (listViewItemType == 0) {
                if (Status(context).equals("Business")) {
                    holder.picture_profil.setImageBitmap(it.getPictureProfile());
                }else if (Status(context).equals("User")){
                    holder.picture_profil.setImageBitmap(it.getPictureProfile());
                   holder.name_profil.setText(it.getItems());
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
        TextView name_profil;
        ImageView logo;
        CircleImageView picture_profil;

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

