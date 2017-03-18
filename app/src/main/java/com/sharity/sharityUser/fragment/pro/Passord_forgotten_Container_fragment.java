package com.sharity.sharityUser.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharity.sharityUser.R;


/**
 * Created by Moi on 14/11/15.
 */
public class Passord_forgotten_Container_fragment extends DialogFragment implements View.OnClickListener {

    private TextView new_pass;
    private TextView forgot_pass;
    View inflate;
        public static Passord_forgotten_Container_fragment newInstance() {
        Passord_forgotten_Container_fragment myFragment = new Passord_forgotten_Container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            inflate = inflater.inflate(R.layout.dialog_forgot_password, container, false);

        forgot_pass=(TextView)inflate.findViewById(R.id.forgot_pass);
       // new_pass=(TextView)inflate.findViewById(R.id.new_pass);

        forgot_pass.setOnClickListener(this);
      //  new_pass.setOnClickListener(this);

        final FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
        ft2.replace(R.id.fragment_password, Passord_forgotten_fragment.newInstance("forgotPassword"), "Passord_forgotten_fragment");
        ft2.commit();

        return inflate;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forgot_pass:
                forgot_pass.setTextColor(getResources().getColor(R.color.green));
            //    new_pass.setTextColor(getResources().getColor(R.color.black));

                final FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
                ft2.replace(R.id.fragment_password, Passord_forgotten_fragment.newInstance("forgotPassword"), "Passord_forgotten_fragment");
                ft2.commit();
                break;
           /* case R.id.new_pass:
                forgot_pass.setTextColor(getResources().getColor(R.color.black));
            //    new_pass.setTextColor(getResources().getColor(R.color.green));
                final FragmentTransaction ft3 = getChildFragmentManager().beginTransaction();
                ft3.replace(R.id.fragment_password, Passord_forgotten_fragment.newInstance("Create_new_Password"), "Passord_forgotten_fragment");
                ft3.commit();
                break;*/
        }
    }
}