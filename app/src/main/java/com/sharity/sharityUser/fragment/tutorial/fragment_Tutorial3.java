package com.sharity.sharityUser.fragment.tutorial;

/**
 * Created by Moi on 15/06/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharity.sharityUser.R;

public class fragment_Tutorial3 extends Fragment {
    View inflate;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.tutorial__fragment3, container, false);

        return inflate;
    }

}



