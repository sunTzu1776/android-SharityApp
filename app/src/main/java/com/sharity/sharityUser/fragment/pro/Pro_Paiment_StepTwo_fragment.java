package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.ParsePushNotification.SendNotification;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.ToastInterface;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.login.widget.ProfilePictureView.TAG;

import static com.sharity.sharityUser.R.id.price;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_StepTwo_fragment extends Fragment implements Updateable,ToastInterface, View.OnClickListener {
    private View inflate;
    GridView grid;
    private TextView valider;
    private EditText amount_paiment;
    private TextView sharepoint_supplementary;
    private CircleImageView picture_profil;
    private TextView username_login;
    private ImageView cash;
    private ImageView CB;
    private  UserLocation userLocation;
    private ToastInterface toastInterface;

    public static Pro_Paiment_StepTwo_fragment newInstance(UserLocation userLocation) {
        Pro_Paiment_StepTwo_fragment myFragment = new Pro_Paiment_StepTwo_fragment();
        Bundle args = new Bundle();
        args.putSerializable("user", userLocation);
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflate = inflater.inflate(R.layout.fragment_paiment_steptwo_normal_pro, container, false);
        toastInterface=this;
        valider=(TextView)inflate.findViewById(R.id.valider);
        amount_paiment=(EditText)inflate.findViewById(R.id.amount_paiment);
        sharepoint_supplementary=(TextView)inflate.findViewById(R.id.sharepoint_supplementary);
        picture_profil=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username_login=(TextView)inflate.findViewById(R.id.username_login);
      // amount_paiment.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
        cash=(ImageView)inflate.findViewById(R.id.cash);
        CB=(ImageView)inflate.findViewById(R.id.CB);


        cash.setOnClickListener(this);
        CB.setOnClickListener(this);
        valider.setOnClickListener(this);

        userLocation= (UserLocation) getArguments().getSerializable("user");
        if (userLocation.getPictureProfil()!=null){
            Bitmap bmp = null;
            bmp = BitmapFactory.decodeByteArray(userLocation.getPictureProfil(), 0, userLocation.getPictureProfil().length);
            picture_profil.setImageBitmap(bmp);
        }

        return inflate;
    }

    @Override
    public void update() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.valider:
                if (Utils.isConnected(getActivity())){
                SendNotification sendNotification= new SendNotification(getActivity(),getActivity(),userLocation,toastInterface);
                sendNotification.Send(amount_paiment.getText().toString());
                }else {
                    Toast.makeText(getActivity(), "Veuillez vous connécter à un réseau", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cash:
                cash.setImageResource(R.drawable.cash_icone_on);
                CB.setImageResource(R.drawable.cb_icone);

                break;
            case R.id.CB:
                cash.setImageResource(R.drawable.cash_icone);
                CB.setImageResource(R.drawable.cb_icone_on);

                break;

        }
    }

    @Override
    public void onNotificationError() {

    }

    @Override
    public void onNotificationSuccess(UserLocation location, String price) {
        Toast.makeText(getActivity(), "Notification envoyée", Toast.LENGTH_LONG).show();
        CreateTransaction(location,price);
    }


    private void CreateTransaction(final UserLocation userid, final String price) {
        Number num = Integer.parseInt(price);
        ParseObject object = new ParseObject("Transaction");
        object.put("senderName", db.getBusinessName());
        object.put("business", ParseObject.createWithoutData("Business", db.getBusinessId()));
        object.put("recipientName", userid.getUsername());
        object.put("value", num);
        object.put("approved", "NO");
        object.put("transactionType", 1);
        object.put("currencyCode", "EUR");
        object.put("customer", ParseObject.createWithoutData("_User", userid.getId()));

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    UpdateBusiness();
                }
                else {
                    Log.d(TAG, "ex" + e.getMessage());
                }
            }
        });
    }


    //Get All transaction for self business to count Generated_sharepoints
    private void UpdateBusiness(){
        try {
            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Business");
            innerQuery.whereEqualTo("objectId", db.getBusinessId());
            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Transaction");
            query3.whereMatchesQuery("business", innerQuery);
            query3.orderByDescending("createdAt");
            query3.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList!=null){
                        int Generated_sharepoints=0;
                        for (ParseObject object : commentList){
                            if (object.getInt("transactionType")==1){
                                int sp = object.getInt("value");
                                Generated_sharepoints=Generated_sharepoints+sp;
                            }
                        }
                        UpdateBusiness_sharepoint(db.getBusinessId(),Generated_sharepoints);
                    }
                }
            });
        }
        catch (NullPointerException f){

        }
    }


    //Update Business value : sharepoint_generated
    public void UpdateBusiness_sharepoint(String objectId, final int sharepoints){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                    gameScore.put("generated_sharepoints",sharepoints);
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("generated_sharepoints","update");
                                Log.d("sender", "Broadcasting message");
                                Intent intent = new Intent("eventRefresh");
                                intent.putExtra("message", "This is my message!");
                                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                            }else{
                                Log.d("okok",e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

}