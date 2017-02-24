package com.sharity.sharityUser.fragment.tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharity.sharityUser.R;


public class fragment_Tutorial4 extends Fragment{
   private View inflate;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.tutorial_fragment4, container, false);

        return inflate;

    }


    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }


}


