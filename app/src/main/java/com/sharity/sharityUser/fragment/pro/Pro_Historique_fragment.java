package com.sharity.sharityUser.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.CustomSpinnerAdapter;
import com.sharity.sharityUser.Utils.YearpinnerAdapter;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.client.client_Option_fragment;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Historique_fragment extends Fragment implements Updateable {
    private View inflate;

    public static Pro_Historique_fragment newInstance() {
        Pro_Historique_fragment myFragment = new Pro_Historique_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_historique_pro, container, false);


        return inflate;
    }

    @Override
    public void update() {
    }
}