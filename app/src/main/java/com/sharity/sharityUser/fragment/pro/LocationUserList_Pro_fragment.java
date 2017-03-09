package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterListUser;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.Updateable;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.duration;
import static android.content.Context.WINDOW_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.google.android.gms.analytics.internal.zzy.g;
import static com.google.android.gms.analytics.internal.zzy.i;
import static com.parse.ParsePush.sendMessageInBackground;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.LocationUserActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class LocationUserList_Pro_fragment extends Fragment {

    ListView listView;
    ArrayList<UserLocation> userLocations;
    View inflate;
        public static LocationUserList_Pro_fragment newInstance(ArrayList<UserLocation> userLocations) {
        LocationUserList_Pro_fragment myFragment = new LocationUserList_Pro_fragment();
        Bundle args = new Bundle();
            args.putSerializable("userLocation",userLocations);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.locationuserlist, container, false);
         final ListView listView=(ListView)inflate.findViewById(R.id.ListView);

        userLocations= (ArrayList<UserLocation>)getArguments().getSerializable("userLocation");

// get data from the table by the ListAdapter
        AdapterListUser customAdapter = new AdapterListUser(getActivity(), R.layout.itemlistrow, userLocations);
        listView .setAdapter(customAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final UserLocation obj = (UserLocation) listView.getAdapter().getItem(position);
                final String id  = obj.getId();
                Log.d("userid",id);
                CreateBusiness(obj);
              /*  ParsePush parsePush = new ParsePush();
                ParseQuery pQuery = ParseInstallation.getQuery(); //
                ParseObject object=new ParseObject("_User",id);
                 pQuery.whereEqualTo("user", id);
                parsePush.sendMessageInBackground("Only for special people", pQuery);*/
                ParseQuery pushQuery = ParseInstallation.getQuery();
                LinkedList<String> channels = new LinkedList<String>();
                channels.add("TransactionTestAndroid");
                String message = "Test Push Notification";
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery);
                push.setChannels(channels);
                push.setMessage(message);
                push.sendInBackground();

            }
        });
        return inflate;

    }

    private void CreateBusiness(UserLocation userid) {

        ParseObject object = new ParseObject("Transaction");
        object.put("senderName", userid.getUsername());
        object.put("business", ParseObject.createWithoutData("Business", db.getBusinessId()));
        object.put("recipientName", db.getBusinessName());
        object.put("value", 50.50);
        object.put("approved", "NO");
        object.put("transactionType", 1);
        object.put("currencyCode", "EUR");
        object.put("customer", ParseObject.createWithoutData("_User", userid.getId()));
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                }
                else {
                    Log.d(TAG, "ex" + e.getMessage());
                }
            }
        });
    }
}