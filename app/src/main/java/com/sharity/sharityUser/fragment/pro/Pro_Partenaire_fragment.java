package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LocationUserActivity;
import com.sharity.sharityUser.fragment.Updateable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sharity.sharityUser.R.id.map;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Partenaire_fragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
         OnMapReadyCallback,Updateable {


    private double myBusiness_latitude=0.0;
    private double myBusiness_longitude=0.0;

    private int counter=0;
    private ParseGeoPoint geoPoint;
    private String objectiD;
    private Handler mHandler = new Handler();
    private DatabaseHandler db;
    private View mView;
    public static long NOTIFY_INTERVAL = 10000; //10s = 10 000ms
    public static boolean ServiceisWorking=false;
    public static Timer mTimer = null;
    private GoogleApiClient mGoogleApiClient;
    private View inflate;
    private MapView mapView;
    private GoogleMap mMap;
    private List<UserLocation> locationUser=new ArrayList<>();
    private Button userListBT;
    private ImageView imageView;
        public static Pro_Partenaire_fragment newInstance() {
        Pro_Partenaire_fragment myFragment = new Pro_Partenaire_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire, container, false);
            try {
                mapView = (MapView) inflate.findViewById(map);
                userListBT=(Button)inflate.findViewById(R.id.userlist);
                imageView=(ImageView)inflate.findViewById(R.id.imgMyLocation);
                imageView.setVisibility(View.VISIBLE);
                mapView.onCreate(savedInstanceState);
                mapView.onResume();
                mapView.getMapAsync(this);

                if (mGoogleApiClient==null){
                    buildGoogleApiClient();
                }


        userListBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), LocationUserActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("listuser",(Serializable)locationUser);
                intent.putExtra("Bundle",bundle);
                startActivity(intent);
            }
        });

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myBusiness_latitude!=0.0 && myBusiness_longitude!=0.0){
                            LatLng sydney = new LatLng(myBusiness_latitude, myBusiness_longitude);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
                        }
                    }
                });

            }catch (NullPointerException e){
            }
        return inflate;
    }

    @Override
    public void update() {
                mapView.getMapAsync(this);
        }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        db = new DatabaseHandler(getActivity());

        try {
            if (db.getBusinessCount()>0) {
                objectiD = db.getBusinessId();
                Business business = db.getBusiness(objectiD);
                myBusiness_latitude=Double.valueOf(business.getLatitude());
                myBusiness_longitude=Double.valueOf(business.get_longitude());
                geoPoint=new ParseGeoPoint(Double.valueOf(business.getLatitude()),Double.valueOf(business.get_longitude()));
                imageView.performClick();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    LocateUsers();
                }
            }, 100);
        }catch (NullPointerException e){

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    private void LocateUsers(){
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            Log.v("Recreate Timer", "recreat");
            ServiceisWorking=true;
            mTimer = new Timer();
        }
        // schedule task
        try {
            mTimer.scheduleAtFixedRate(new DisasterTimerCheck(), 0, NOTIFY_INTERVAL);

        }catch (IllegalStateException e){
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new DisasterTimerCheck(), 0, NOTIFY_INTERVAL);

        }
}

    class DisasterTimerCheck extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (Utils.isConnected(getApplicationContext())) {
                        if (mView != null && mView.isShown()) {
                        } else {
                            if (ServiceisWorking) {
                                locationUser.clear();
                                Log.v("isChecking", "ServiceisWorking");
                                CheckUser();
                            } else {
                                mTimer.cancel();
                                ServiceisWorking = false;
                                Log.v("isChecking", "ServiceisStopped");
                            }
                        }
                    }
                }
            });
        }
    }

    public void CheckUser() {
        counter=counter+1;
        locationUser.clear();
        mMap.clear();
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereNotEqualTo("userIsBusiness",true);
        query.whereWithinKilometers("geoloc", geoPoint, 0.70);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    double queryLatitude = 0.0;
                    double queryLongitude=0.0;
                    byte[] image = new byte[0];
                    // Iterating over the results
                    for (int i = 0; i < objects.size(); i++) {
                        String userid= objects.get(i).getObjectId();

                       ParseFile parseFile=objects.get(i).getParseFile("picture");
                        if (parseFile!=null){
                        try {
                            image = parseFile.getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        }
                        ParseGeoPoint geoPoint = objects.get(i).getParseGeoPoint("geoloc");
                            if (geoPoint != null) {
                                queryLatitude = geoPoint.getLatitude();
                                queryLongitude = geoPoint.getLongitude();
                                String username = objects.get(i).getString("username");
                                String token = objects.get(i).getString("fcm_device_id");

                                locationUser.add(new UserLocation(userid,queryLatitude, queryLongitude, username,image,token));
                                }
                        }


                    for (UserLocation business : locationUser){
                            LatLng delhi = new LatLng(business.getLatitude(), business.getLongitude());
                            MarkerOptions markeroptions = new MarkerOptions();
                            markeroptions.position(delhi);
                            markeroptions.title(business.getUsername());
                            mMap.addMarker(markeroptions);
                    }


                    //Bizness Marker
                    objectiD=db.getBusinessId();
                    Business business= db.getBusiness(objectiD);
                    LatLng delhi = new LatLng(Double.valueOf(business.getLatitude()),Double.valueOf(business.get_longitude()));
                    MarkerOptions markeroptions = new MarkerOptions();
                    markeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_logo));
                    markeroptions.position(delhi);
                    markeroptions.title(business.get_businessName());
                    mMap.addMarker(markeroptions);
                    //Place current location marker


                    if (counter>1){
                        if (!locationUser.isEmpty()){
                            userListBT.setVisibility(View.VISIBLE);
                        }else {
                            userListBT.setVisibility(View.INVISIBLE);
                        }
                    }else {
                        userListBT.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
}

        @Override
        public void onDestroy(){
            if (mTimer!=null){
                mTimer.cancel();
            }
            super.onDestroy();
        }

}