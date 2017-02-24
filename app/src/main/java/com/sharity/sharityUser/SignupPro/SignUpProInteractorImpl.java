package com.sharity.sharityUser.SignupPro;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.facebook.login.widget.ProfilePictureView.TAG;

public class SignUpProInteractorImpl implements SignUpProInteractor {

    @Override
    public void login(final String username, final String password, final String RC3number, final String Businesname, String OwnerName, final String Phone, final String address, final String RIB, final String email, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;

                ParseObject gameScore = new ParseObject("Business");
                gameScore.put("OfficerName", Businesname);
                gameScore.put("RIB", RIB);
                gameScore.put("RCSNumber", RC3number);
                gameScore.put("businessName", Businesname);
                gameScore.put("telephoneNumber", Phone);
                gameScore.put("address", address);
                ParseGeoPoint point = new ParseGeoPoint(40.0, -30.0);
                gameScore.put("location", point);
                gameScore.put("email",email);
                gameScore.put("Siret", 100);

                gameScore.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e==null){
                            listener.onSuccess();
                        } else {
                            Log.d(TAG, "ex" + e.getMessage());
                        }
                    }
                });

                if (TextUtils.isEmpty(username)){
                    listener.onUsernameError();
                    error = true;
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    listener.onPasswordError();
                    error = true;
                    return;
                }
            }
        }, 800);
    }
}