package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.client.client_Historique_fragment;
import com.sharity.sharityUser.fragment.testpager.PagerFragment;

import java.util.List;

import static android.R.attr.data;
import static android.R.attr.fragment;


/**
 * Created by Moi on 14/11/15.
 */
public class History_container_fragment extends Fragment implements Updateable, PagerFragment.OnSelection,ProfilActivity.OnNotificationUpdateHistoric, View.OnClickListener {

    PagerFragment.OnSelection onSelection;
    Button buttonmap;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;

    public static History_container_fragment newInstance() {
        History_container_fragment myFragment = new History_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getActivity() instanceof ProfilProActivity) {
            inflate = inflater.inflate(R.layout.fragment_history_container_pro, container, false);
            buttonmap = (Button) inflate.findViewById(R.id.buttonmap);
            buttonmap.setOnClickListener(this);
        } else {
            ((ProfilActivity) getActivity()).setHistoricListener(History_container_fragment.this);
            inflate = inflater.inflate(R.layout.fragment_history_container_client, container, false);
        }
        circle_slide1 = (ImageView) inflate.findViewById(R.id.circle_slide1);
        circle_slide2 = (ImageView) inflate.findViewById(R.id.circle_slide2);
        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);


        return inflate;
    }

    @Override
    public void update() {
    }


    @Override
    public void OnSelect(int i) {
        if (i == 0) {
            circle_slide1.setImageResource(R.drawable.circles_slide_on);
            circle_slide2.setImageResource(R.drawable.circles_slide_off);
        } else {
            circle_slide1.setImageResource(R.drawable.circles_slide_off);
            circle_slide2.setImageResource(R.drawable.circles_slide_on);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonmap:
                startActivity(new Intent(getActivity(), MapActivity.class));
                break;
        }
    }

    @Override
    public void TaskOnNotification(String business, String sharepoints) {
        PagerFragment fragment2 = (PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.pager.setAdapter(fragment2.getAdapter());
        fragment2.getAdapter().FragmentOperation();
    }

}