package com.sharity.sharityUser.fragment.client;


import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.StoreAdapter2;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sharity.sharityUser.R.id.charity_description;
import static com.sharity.sharityUser.R.id.charity_dons_validate;
import static com.sharity.sharityUser.R.id.dons_view;
import static com.sharity.sharityUser.R.id.recycler_charity;
import static com.sharity.sharityUser.R.id.sharepoints_moins;
import static com.sharity.sharityUser.R.id.sharepoints_plus;


/**
 * Created by Moi on 14/11/15.
 */
public class client_PartenaireMap_fragment extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
         OnMapReadyCallback,Updateable,StoreAdapter2.OnItemDonateClickListener {

    private double latitude=0.0;
    private double longitude=0.0;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private View inflate;
    private MapView mapView;
    private GoogleMap mMap;
    private PermissionRuntime permissionRuntime;
    private List<LocationBusiness> locationBusiness=new ArrayList<>();
    private RecyclerView recyclerview;
    private FrameLayout recyclerFrame;
    StoreAdapter2 adapter2;
    private StoreAdapter2.OnItemDonateClickListener onItemDonateClickListener;
    private ArrayList<CharityDons> list_dons = new ArrayList<CharityDons>();
    private byte[] imageByte = null;


    public static client_PartenaireMap_fragment newInstance() {
        client_PartenaireMap_fragment myFragment = new client_PartenaireMap_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire, container, false);
            try {
                mapView = (MapView) inflate.findViewById(R.id.map);
                recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
                recyclerFrame = (FrameLayout) inflate.findViewById(R.id.recyclerFrame);
                onItemDonateClickListener = this;
                ShowDonateView();

                mapView.onCreate(savedInstanceState);
                mapView.onResume();

                if (mGoogleApiClient==null){
                    buildGoogleApiClient();
                    permissionRuntime = new PermissionRuntime(getActivity());
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mapView.getMapAsync(this);
                    }
                    else {
                        permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
                    }
                }
            }catch (NullPointerException e){


        }

        return inflate;
    }

    @Override
    public void update() {
       // client_Partenaire_fragment myFragment = (client_Partenaire_fragment)getFragmentManager().findFragmentByTag("client_Partenaire_fragment");
       // if (myFragment != null && myFragment.isVisible()) {
        }
   // }

   /* @Override
    public void onResume() {
        super.onResume();
        followMeLocationSource.getBestAvailableProvider();
        setUpMapIfNeeded();
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMap.setMyLocationEnabled(false);

        super.onPause();
    }*/



    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.isAdded()){
            try {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //Initialize Google Play Services
                permissionRuntime = new PermissionRuntime(getActivity());
                if (ContextCompat.checkSelfPermission(getActivity(),
                        permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                else {
                    permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
                }

                // Add a marker in Delhi and move the camera
                GetBusiness();
            }catch (NullPointerException e){

            }
        }


      //  mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
    }

    private void GetBusiness(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // Iterating over the results
                    for (int i = 0; i < objects.size(); i++) {
                        double queryLatitude = objects.get(i).getParseGeoPoint("location").getLatitude();
                        double queryLongitude = objects.get(i).getParseGeoPoint("location").getLongitude();
                        String business_name=  objects.get(i).get("businessName").toString();
                        String addresse=  objects.get(i).get("address").toString();
                        locationBusiness.add(new LocationBusiness(queryLatitude,queryLongitude,business_name,addresse));
                    }

                    for (LocationBusiness business : locationBusiness){
                        Log.d("Busibb",business.get_businessName());
                            LatLng delhi = new LatLng(business.get_latitude(), business.get_longitude());
                            MarkerOptions markeroptions = new MarkerOptions();
                            markeroptions.position(delhi);
                            markeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_logo));
                            markeroptions.title(business.get_businessName() + System.getProperty("line.separator") + business.get_addresse());
                            mMap.addMarker(markeroptions);
                    }
                    //Place current location marker
                }
            }
        });
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, gpsLocationListener);
        }
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

    private final com.google.android.gms.location.LocationListener gpsLocationListener =new com.google.android.gms.location.LocationListener() {

        @Override
        public void onLocationChanged(final Location location) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            latitude=location.getLatitude();
            longitude=location.getLongitude();

            ParseGeoPoint geoPoint=new ParseGeoPoint(location.getLatitude(),location.getLongitude());
            SaveLocationUser(geoPoint);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMap.clear();
                    for (LocationBusiness business : locationBusiness){
                        if (distance(business.get_latitude(), business.get_longitude(), location.getLatitude(), location.getLongitude()) <= 500000) {
                            LatLng delhi = new LatLng(business.get_latitude(), business.get_longitude());
                            MarkerOptions markeroptions = new MarkerOptions();
                            markeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_logo));
                            markeroptions.position(delhi);
                            markeroptions.title(business.get_businessName() + System.getProperty("line.separator") + business.get_addresse());
                            mMap.addMarker(markeroptions);
                        }
                        else {
                        }
                    }
                    //move map camera
                }
            }, 1000);

            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            //stop location updates
           /* if (mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
           ic_launcher.png }*/
        }
    };


    public static float distance(double latitudecatastrophe, double longitudecatastrophe, double latitude, double longitude){
        Location loc1 = new Location("");
        loc1.setLatitude(latitudecatastrophe);
        loc1.setLongitude(longitudecatastrophe);

        Location loc2 = new Location("");
        loc2.setLatitude(latitude);
        loc2.setLongitude(longitude);
        float distanceInMeters = loc1.distanceTo(loc2);
        Log.d("result", String.valueOf(distanceInMeters));
        return distanceInMeters;
    }

    private void SaveLocationUser(ParseGeoPoint point){
        try {
            ParseUser parseUser = ParseUser.getCurrentUser();
            parseUser.put("geoloc", point);
            parseUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    // TODO Auto-generated method stub
                    if (e != null){
                        e.printStackTrace();
                    }else{
                        //updated successfully
                    }
                }
            });
        }catch (NullPointerException e){
        }
    }


    //Recycler
    ///////


    public void ShowDonateView() {
        recyclerFrame.setVisibility(View.VISIBLE);
        if (Utils.isConnected(getApplicationContext())) {
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerview.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                    layoutManager.getOrientation());
            recyclerview.addItemDecoration(dividerItemDecoration);
            adapter2 = new StoreAdapter2(getActivity(), list_dons, onItemDonateClickListener);
            recyclerview.setAdapter(adapter2);
            get_Charity();
        } else {
        }
    }

    private void get_Charity() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Charity");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        for (final ParseObject object : commentList) {
                            ParseFile image = (ParseFile) object.getParseFile("Logo");
                            final String name = object.getString("name");
                            final String description = object.getString("description");
                            final String id = object.getObjectId();
                            Log.d("obj", id);
                            try {
                                imageByte = image.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            list_dons.add(new CharityDons(id, name, description, imageByte));
                        }

                        adapter2.notifyDataSetChanged();
                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }

    @Override
    public void onItemClick(int item, CharityDons bo) {

    }
}