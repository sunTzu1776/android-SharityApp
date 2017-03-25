package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.Updateable;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_fragment extends Fragment implements Updateable,Pro_PaimentStepOne_fragment.OnChildPaymentSelection {
    private View inflate;
    private Pro_PaimentStepOne_fragment.OnChildPaymentSelection onSelection;
    public static Pro_Paiment_fragment newInstance() {
        Pro_Paiment_fragment myFragment = new Pro_Paiment_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paiment_pro, container, false);

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Pro_PaimentStepOne_fragment fragTwo = new Pro_PaimentStepOne_fragment();
            ft.add(R.id.Fragment_container, fragTwo);
            ft.addToBackStack(null);
            ft.commit();

        return inflate;
    }

    @Override
    public void update() {
    }



    @Override
    public void OnSelectGrid(UserLocation user, int i) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Pro_Paiment_StepTwo_fragment fragTwo = Pro_Paiment_StepTwo_fragment.newInstance(user);
        ft.add(R.id.Fragment_container, fragTwo);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void Classique() {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Pro_Paiment_StepTwo_Classique_fragment fragTwo = new Pro_Paiment_StepTwo_Classique_fragment();
        ft.add(R.id.Fragment_container, fragTwo);
        ft.addToBackStack(null);
        ft.commit();
    }
}