package com.sharity.sharityUser.fragment.tutorial;



import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharity.sharityUser.R;


public class fragment_Tutorial2 extends Fragment {
    View inflate;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        inflate = inflater.inflate(R.layout.tutorial__fragment2, container, false);

        return inflate;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}



