package com.sharity.sharityUser.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DummyPagerFragment extends Fragment {

    final static String LAYOUT_ID = "layoutId";
    protected int layout;


    public static DummyPagerFragment newInstance(int layoutId) {
        DummyPagerFragment pane = new DummyPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LAYOUT_ID, layoutId);
        pane.setArguments(bundle);
        return pane;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup inflate = (ViewGroup) inflater.inflate(getArguments().getInt(LAYOUT_ID, -1), container, false);
        layout = getArguments().getInt(LAYOUT_ID, -1);

        return inflate;

    }
}

