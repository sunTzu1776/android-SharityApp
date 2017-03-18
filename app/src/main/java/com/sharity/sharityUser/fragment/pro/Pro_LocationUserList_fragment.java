package com.sharity.sharityUser.fragment.pro;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterListUser;
import com.sharity.sharityUser.Utils.ToastInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.google.android.gms.analytics.internal.zzy.p;
import static com.google.android.gms.analytics.internal.zzy.v;
import static com.parse.ParsePush.sendMessageInBackground;
import static com.sharity.sharityUser.Application.getContext;
import static com.sharity.sharityUser.R.id.price;
import static com.sharity.sharityUser.activity.LocationUserActivity.db;
import static com.sharity.sharityUser.activity.LocationUserActivity.parseUser;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_LocationUserList_fragment extends Fragment implements ToastInterface {
    ListView listView;
    ArrayList<UserLocation> userLocations;
    ToastInterface toastInterface;
    View inflate;
        public static Pro_LocationUserList_fragment newInstance(ArrayList<UserLocation> userLocations) {
        Pro_LocationUserList_fragment myFragment = new Pro_LocationUserList_fragment();
        Bundle args = new Bundle();
            args.putSerializable("userLocation",userLocations);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pro_user_list, container, false);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        toastInterface=this;
        userLocations= (ArrayList<UserLocation>)getArguments().getSerializable("userLocation");

// get data from the table by the ListAdapter
        AdapterListUser customAdapter = new AdapterListUser(getActivity(), userLocations,toastInterface);
        listView .setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final UserLocation obj = (UserLocation) listView.getAdapter().getItem(position);
                final String id  = obj.getId();
                Log.d("userid",id);
            }
        });
        return inflate;

    }

    @Override
    public void onNotificationError() {

    }

    @Override
    public void onNotificationSuccess(UserLocation user, String price) {
        CreateTransaction(user, price);
        Toast.makeText(getContext(), "Notification envoyée", Toast.LENGTH_LONG).show();
    }

    private void CreateTransaction(UserLocation userid,String price) {
        Number num = Integer.parseInt(price);
        ParseObject object = new ParseObject("Transaction");
        object.put("senderName", userid.getUsername());
        object.put("business", ParseObject.createWithoutData("Business", db.getBusinessId()));
        object.put("recipientName", db.getBusinessName());
        object.put("value", num);
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