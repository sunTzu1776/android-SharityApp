package com.sharity.sharityUser.fragment.client;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.MapCallback;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.List;

import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.isLocationUpdate;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.list_shop;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mGoogleApiClient;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mLastLocation;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mLocationRequest;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Container_Mission_fragment extends Fragment implements Updateable{



    public static client_Container_Mission_fragment newInstance() {
        client_Container_Mission_fragment myFragment = new client_Container_Mission_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View inflate = inflater.inflate(R.layout.dummy_fragment4, container, false);


        return inflate;
    }

    // Callback open map when user click open map.



    @Override
    public void update() {
    }


}