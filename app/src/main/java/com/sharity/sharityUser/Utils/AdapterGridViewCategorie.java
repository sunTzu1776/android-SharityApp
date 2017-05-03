package com.sharity.sharityUser.Utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.matthewtamlin.android_utilities_library.views.SquareImageView;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.R;

import java.util.ArrayList;


public class AdapterGridViewCategorie extends BaseAdapter{

    OnItemGridCategorieClickListener categorieClickListener;
    Context context;
    ArrayList<Integer> imageId;
    ArrayList<String> listitem=new ArrayList<String>();
    private static LayoutInflater inflater=null;

    public interface OnItemGridCategorieClickListener {
        void onItemCategorieClick(int item, String categorie);
    }

    public AdapterGridViewCategorie(Context context, ArrayList<String> listitem, ArrayList<Integer> osImages, OnItemGridCategorieClickListener categorieClickListener) {
        // TODO Auto-generated constructor stub
        this.categorieClickListener=categorieClickListener;
        this.listitem=listitem;
        this.context=context;
        imageId=osImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
    }
 
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listitem.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    public class Holder
    {
        TextView os_text;
        com.sharity.sharityUser.Utils.SquareImageView os_img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
 
        rowView = inflater.inflate(R.layout.sample_gridlayout, null);
        holder.os_text =(TextView) rowView.findViewById(R.id.os_texts);
        holder.os_img =(com.sharity.sharityUser.Utils.SquareImageView) rowView.findViewById(R.id.os_images);
 
        holder.os_text.setText(listitem.get(position));
        if (listitem.get(position).equals("loisirs")){
            holder.os_img.setImageResource(R.drawable.categorie_loisirs);
        }else if (listitem.get(position).equals("alimentation")){
            holder.os_img.setImageResource(R.drawable.categorie_alimentation);
        }else {
            holder.os_img.setImageResource(imageId.get(position));
        }

        rowView.setOnClickListener(new OnClickListener() {
 
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                categorieClickListener.onItemCategorieClick(position, listitem.get(position));
            }
        });
 
        return rowView;
    }
 
}