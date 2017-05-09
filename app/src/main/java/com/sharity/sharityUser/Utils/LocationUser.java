package com.sharity.sharityUser.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.client.client_Partenaire_list_fragment;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Moi on 08/05/2017.
 */

public class LocationUser implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    public static ParseGeoPoint geoPoint;
    public double latitude = 0.0;
    public double longitude = 0.0;
    protected  LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    protected PermissionRuntime permissionRuntime;
    public android.location.Location mLastLocation;
    protected ParseUser parseUser;
    protected  boolean on=false;
    protected Context context;
    private int count=0;
    int REQUEST_CHECK_SETTINGS = 100;
    private Activity activity;
    private GoogleApiClient.ConnectionCallbacks connectionCallbacks;
    public static void activityResult(int requestCode, int resultCode, Intent data) {
    }

    public LocationUser(Context context, Activity activity){
        this.context=context;
        this.activity=activity;
        parseUser = ParseUser.getCurrentUser();

    }


    public void buildGoogleApiClient() {
        if (mGoogleApiClient==null)
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        OpenGPSettings();

        if (context != null) {
            if (ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null) {
                    latitude = mLastLocation.getLatitude();
                    longitude = mLastLocation.getLongitude();

                    Log.d("latitudeLocationnn", String.valueOf(latitude));
                    geoPoint = new ParseGeoPoint(latitude, longitude);
                    UpdateUserLocation(latitude,longitude);
            }
        }else {
                permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public LocationListener gpsLocationListener = new com.google.android.gms.location.LocationListener() {
        @Override
        public void onLocationChanged(final android.location.Location location) {
            mLastLocation = location;

            if (Utils.isConnected(context)) {
                if (geoPoint != null) {
                 //   SaveLocationUser(geoPoint);
                }
            }

            Log.d("latitde",String.valueOf(latitude));
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            geoPoint = new ParseGeoPoint(latitude, longitude);
        }
    };

    private void Disconnect(){
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationListener);
            }
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  GPS disabled show the user a dialog to turn it on
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {
                    //failed to show dialog
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {
                UpdateUserLocation(latitude,longitude);
                Log.d("NetworkLocation",",GPS is enabled");
            } else {
                Log.d("NetworkLocation",",GPS is not enabled");
            }
        }
    }


    public void OpenGPSettings(){
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(this);
    }


    public void UpdateUserLocation(final double latitude, final double longitude) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
                    gameScore.put("geoloc", point);
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                            } else {
                                Log.d("okok", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}
