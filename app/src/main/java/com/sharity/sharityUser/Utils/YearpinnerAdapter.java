package com.sharity.sharityUser.Utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sharity.sharityUser.R;

import java.util.ArrayList;


/**
 * Created by Moi on 23/02/2017.
 */

public class YearpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    public final Context activity;
    private ArrayList<Integer> asr;
    private String type;
    public YearpinnerAdapter(Context context, ArrayList<Integer> asr){
        this.activity=context;
        this.asr=asr;

    }
    @Override
    public int getCount() {
        return asr.size();
    }

    @Override
    public Object getItem(int i) {
        return asr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return (long)i;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView textView=new TextView(activity);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(16,16,16,16);
        textView.setTextSize(15);
        textView.setText(String.valueOf(asr.get(position)));
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView=new TextView(activity);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(16,16,16,16);
        textView.setTextSize(15);
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.vertical_scroll,0);

        textView.setText(String.valueOf(asr.get(i)));
        textView.setTextColor(Color.parseColor("#000000"));
        return textView;
    }
}
