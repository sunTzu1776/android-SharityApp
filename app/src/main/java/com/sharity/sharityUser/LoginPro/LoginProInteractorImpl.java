package com.sharity.sharityUser.LoginPro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.Application;
import com.sharity.sharityUser.BO.Business;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.google.android.gms.analytics.internal.zzy.e;
import static com.google.android.gms.analytics.internal.zzy.g;
import static com.google.android.gms.analytics.internal.zzy.t;
import static com.google.android.gms.analytics.internal.zzy.v;
import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.LoginActivity.db;

public class LoginProInteractorImpl implements LoginInteractor {

    private boolean emailVerified;
    Context context;
    private  OnLoginFinishedListener listener;
    @Override
    public void login(final Context context, final String username, final String password, final OnLoginFinishedListener listener) {
        this.context=context;
        this.listener=listener;
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;


                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    public void done(final ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.d("DB",String.valueOf(db.getBusinessCount()));
                                final String businessid=user.get("BusinessId").toString();
                                final ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                                query.getInBackground(businessid, new GetCallback<ParseObject>() {
                                    public void done(ParseObject object, ParseException e) {
                                        if (e == null) {
                                            ParseGeoPoint geoPoint = object.getParseGeoPoint("location");
                                            String RIB = object.get("RIB").toString();
                                            String owner = user.getObjectId();
                                            String Siret = object.get("Siret").toString();
                                            String _Businesname = object.get("businessName").toString();
                                            String officerName = object.get("officerName").toString();
                                            String address = object.get("address").toString();
                                            String telephoneNumber = object.get("telephoneNumber").toString();
                                            String email = object.get("email").toString();
                                            double latitude = geoPoint.getLatitude();
                                            double longitude = geoPoint.getLongitude();
                                            final Business business = new Business(object.getObjectId(), user.getUsername(), owner, officerName, _Businesname, RIB, Siret, telephoneNumber, address, String.valueOf(latitude), String.valueOf(longitude), email);
                                            if (db.getBusinessCount() <= 0) {
                                                db.addProProfil(business);
                                            }else if (db.getBusinessCount()>=1){
                                                db.deleteAllBusiness();
                                                db.addProProfil(business);
                                            }
                                            ObjectId_ToPref(user.getObjectId());
                                        } else {
                                            // something went wrong
                                        }
                                    }
                                });
                            Check_email_validate(user);
                        } else {
                            if (e.getCode() == 101) {
                                listener.onUserError();
                            }
                        }
                    }
                });

                if (TextUtils.isEmpty(username)) {
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    listener.onPasswordError();
                    error = true;
                    return;
                }
            }
        }, 800);
    }

    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }

    private String Check_email_validate(ParseUser user){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        if (user!=null) {
            query.getInBackground(user.getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        emailVerified = object.getBoolean("emailVerified");
                        if (emailVerified){
                            listener.onSuccess();
                        }else {
                            Toast.makeText(context,"vous n'avez pas confirmé l'email d'inscription qui vous a été envoyé, veuillez le confirmer",Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
        }
        return String.valueOf(emailVerified);
    }
}