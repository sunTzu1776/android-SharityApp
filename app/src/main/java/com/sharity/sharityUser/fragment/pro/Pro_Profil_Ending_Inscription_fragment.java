package com.sharity.sharityUser.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sharity.sharityUser.activity.LoginActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Ending_Inscription_fragment extends Fragment{

    private View inflate;


    public static Pro_Profil_Ending_Inscription_fragment newInstance() {
        Pro_Profil_Ending_Inscription_fragment myFragment = new Pro_Profil_Ending_Inscription_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pro_profil_finalizeinscription_fragment, container, false);

        return inflate;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}