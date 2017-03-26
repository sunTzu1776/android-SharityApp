package com.sharity.sharityUser.fragment.client;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.attr.thumbnail;
import static com.google.android.gms.analytics.internal.zzy.C;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_fragment extends Fragment implements Updateable,SwipeRefreshLayout.OnRefreshListener {

    private Profile profile;
    private BroadcastReceiver statusReceiver;
    private IntentFilter mIntent;
    private SwipeRefreshLayout swipeContainer;
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    com.sharity.sharityUser.Utils.ProfilePictureView imageView;
    protected ParseUser parseUser= ProfilActivity.parseUser;
    private TextView points;
    public static client_Profil_fragment newInstance() {
        client_Profil_fragment myFragment = new client_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        username=(TextView) inflate.findViewById(R.id.username_login);
        points=(TextView) inflate.findViewById(R.id.points);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
         imageView=(com.sharity.sharityUser.Utils.ProfilePictureView)inflate.findViewById(R.id.picture_profil);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test logout
                ParseUser.logOut();

            }
        });
        return inflate;
    }


    @Override
    public  void update() {
            getProfilFromParse();
    }

    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        profile = Profile.getCurrentProfile();
        imageView.setProfileId(profile.getId());

        String objectId= getUserObjectId(getActivity());

        if (db.getUserCount()>0 && Utils.isConnected(getContext())) {
            try {
                getUser();
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
        }else {
            String usernameFB = profile.getName();
            User user = db.getUser(objectId);
            byte[] image = user.getPictureprofil();
            User update = new User(user.get_id(), usernameFB, user.get_email(), image);
            db.updateUser(update);
            Bitmap PictureProfile = BitmapFactory.decodeByteArray(image, 0, image.length);
            imageView.setProfileId(profile.getId());
            swipeContainer.setRefreshing(false);
            //DO network request to get User data

        }
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }


    private void getUser(){
            try {
                ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("_User");
                innerQuery.whereEqualTo("objectId", parseUser.getObjectId());
                ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Transaction");
                query3.whereMatchesQuery("customer", innerQuery);
                query3.orderByDescending("createdAt");
                query3.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> commentList, ParseException e) {
                        if (commentList!=null){
                            int sharepoints=0;
                            for (ParseObject object : commentList){

                                if (object.getInt("transactionType")==1){
                                    int sp = object.getInt("value");
                                    sharepoints=sharepoints+sp;
                                }
                            }
                            Log.d("sharepoints",String.valueOf(sharepoints));
                            points.setText(String.valueOf(sharepoints));
                            swipeContainer.setRefreshing(false);
                            UpdateUser(sharepoints);
                        }
                    }
                });
            }
            catch (NullPointerException f){

            }
        }


    public void UpdateUser(final int sharepoints){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String user_name = object.getString("username");
                    username.setText(user_name);
                    object.put("sharepoints",sharepoints);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("user update","success");
                            }else{
                                Log.d("user update failed",e.getMessage());

                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())){
            swipeContainer.setRefreshing(true);
            getProfilFromParse();
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    @Override
    public void onPause() {
        if(mIntent != null) {
            getActivity().unregisterReceiver(mMessageReceiver);
            mIntent = null;
        }
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            getUser();
        }
    };
}