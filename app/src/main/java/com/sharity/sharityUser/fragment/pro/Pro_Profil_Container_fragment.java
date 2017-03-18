package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.internal.zznk.fm;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;
import static com.sharity.sharityUser.activity.ProfilProActivity.profileSource;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Container_fragment extends Fragment implements Updateable {

    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    protected ParseUser parseUser= ProfilActivity.parseUser;
    private TextView points;
    private TextView email;
    private TextView phone;

    public static Pro_Profil_Container_fragment newInstance(String source) {
        Pro_Profil_Container_fragment myFragment = new Pro_Profil_Container_fragment();
        Bundle args = new Bundle();
        args.putString("source",source);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_container_pro, container, false);

        if (profileSource.equals("Profilinfo")){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fm.beginTransaction();
            Pro_Profil_Infos_fragment fragTwo = new Pro_Profil_Infos_fragment();
            ft.add(R.id.Fragment_profil_container, fragTwo);
            ft.commit();

        }else if (profileSource.equals("Profil")){
            Fragment currentFagment= getChildFragmentManager().findFragmentById(R.id.Fragment_profil_container);
            if (currentFagment instanceof Pro_Profil_fragment ){
                Log.d("fragment Profil","Pro_Profil_fragment visible");
            }else {
                Pro_Profil_fragment fragTwo = new Pro_Profil_fragment();
                FragmentManager fm = getChildFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                fm.beginTransaction();
                ft.add(R.id.Fragment_profil_container, fragTwo);
                ft.commit();
            }


        }

        return inflate;
    }



    @Override
    public  void update() {
    }



}