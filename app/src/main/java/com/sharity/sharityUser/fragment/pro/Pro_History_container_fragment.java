package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.testpager.PagerFragment;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_History_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection {

    PagerFragment.OnSelection onSelection;
    Button buttonmap;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;

        public static Pro_History_container_fragment newInstance() {
        Pro_History_container_fragment myFragment = new Pro_History_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history_container_pro, container, false);
        buttonmap=(Button)inflate.findViewById(R.id.buttonmap);
        circle_slide1=(ImageView)inflate.findViewById(R.id.circle_slide1);
        circle_slide2=(ImageView)inflate.findViewById(R.id.circle_slide2);


        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);

        buttonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MapActivity.class));
            }
        });

        return inflate;
    }

    @Override
    public void update() {
    }


    @Override
    public void OnSelect(int i) {
        if (i==0){
            circle_slide1.setImageResource(R.drawable.circles_slide_on);
            circle_slide2.setImageResource(R.drawable.circles_slide_off);
        }else {
            circle_slide1.setImageResource(R.drawable.circles_slide_off);
            circle_slide2.setImageResource(R.drawable.circles_slide_on);
        }
    }
}