package com.sharity.sharityUser.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.SimpleBackPage;
import com.sharity.sharityUser.fragment.client.client_Login_fragment;
import com.sharity.sharityUser.fragment.client.client_Option_fragment;
import com.sharity.sharityUser.fragment.pro.LocationUserList_Pro_fragment;
import com.sharity.sharityUser.fragment.pro.Partenaire_Pro_fragment;
import com.sharity.sharityUser.fragment.pro.Profil_pro_fragment;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.bottomBar;
import static com.sharity.sharityUser.R.id.pager;
import static com.sharity.sharityUser.R.id.tab_option;
import static com.sharity.sharityUser.R.id.toolbar;


/**
 * Created by Moi on 07/05/2016.
 */
public class LocationUserActivity extends AppCompatActivity {
    public static DatabaseHandler db;
    public static ParseUser parseUser;
    ArrayList<UserLocation> userLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            db = new DatabaseHandler(this);
            parseUser=ParseUser.getCurrentUser();

            userLocations =  (ArrayList<UserLocation>)getIntent().getExtras().getBundle("Bundle").getSerializable("listuser");
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, LocationUserList_Pro_fragment.newInstance(userLocations), "LocationUserList_Pro_fragment")
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}



