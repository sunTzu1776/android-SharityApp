package com.sharity.sharityUser.fragment.pro;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sharity.sharityUser.R.id.RIB_value;
import static com.sharity.sharityUser.R.id.sharepoint_value;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;
import static com.sharity.sharityUser.activity.ProfilProActivity.profilProActivity;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_fragment extends Fragment implements Updateable,SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeContainer;
    private BroadcastReceiver statusReceiver;
    private IntentFilter mIntent;
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    protected ParseUser parseUser= ProfilActivity.parseUser;
    private TextView points;
    private TextView email;
    private TextView phone;
    private TextView sharepoint_generated;
    private TextView RIB_value;



    public static Pro_Profil_fragment newInstance() {
        Pro_Profil_fragment myFragment = new Pro_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_pro, container, false);
        imageView=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username=(TextView) inflate.findViewById(R.id.username_login);
        email=(TextView) inflate.findViewById(R.id.email);
        phone=(TextView) inflate.findViewById(R.id.telephone);
        sharepoint_generated=(TextView) inflate.findViewById(R.id.sharepoint_value);
        RIB_value=(TextView) inflate.findViewById(R.id.RIB_value);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
            }
        });

        if (isAdded()) {
            getProfilFromParse();
        }

        return inflate;
    }

    @Override
    public  void update() {
    }

    private void getProfilFromParse() {
        final String objectid = db.getBusinessId();
        Business business = db.getBusiness(objectid);

        if (Utils.isConnected(getContext())) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
            query.whereEqualTo("objectId", objectid);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        String _user_name="";
                        String _email="";
                        String _phone="";
                        String _RIB="";
                        int _generated_sharepoints=0;

                        for (ParseObject object : objects){
                            _user_name = object.getString("officerName");
                            _email = object.getString("email");
                            _phone = object.getString("telephoneNumber");
                            _RIB = object.getString("RIB");
                            _generated_sharepoints = object.getInt("generated_sharepoints");
                        }

                        username.setText(_user_name);
                        email.setText(_email);
                        phone.setText(_phone);
                        RIB_value.setText(_RIB);
                        sharepoint_generated.setText(String.valueOf(_generated_sharepoints));
                        swipeContainer.setRefreshing(false);

                    } else {

                    }

                }
            });

            Log.d("emailVerified",business.getEmailveried());
            db.close();
        }
        else {
            username.setText(business.get_officerName());
            email.setText(business.get_email());
            phone.setText(business.get_telephoneNumber());
            RIB_value.setText(business.get_RIB());
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver2,
                new IntentFilter("eventRefresh"));
    }

    @Override
    public void onPause() {
        if(mIntent != null) {
            getActivity().unregisterReceiver(mMessageReceiver2);
            mIntent = null;
        }
        super.onPause();
    }

    private BroadcastReceiver mMessageReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);
            updateProfil();
        }
    };

    public void updateProfil() {
        profilProActivity.runOnUiThread(new Runnable() {
            public void run() {
                getProfilFromParse();
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
}