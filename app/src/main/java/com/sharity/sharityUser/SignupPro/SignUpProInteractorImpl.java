package com.sharity.sharityUser.SignupPro;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.sharity.sharityUser.R.id.business_name;
import static com.sharity.sharityUser.R.id.chief_name;
import static com.sharity.sharityUser.R.id.username;

public class SignUpProInteractorImpl implements SignUpProInteractor {

    @Override
    public void login(final EditText[] fields, final String username, final String password, final String RC3number, final String Businesname, final String OwnerName, final String Phone, final String address, final String RIB, final String email, final OnLoginFinishedListener listener) {
        // Mock login. I'm creating a handler to delay the answer a couple of seconds
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                boolean error = false;
                EditText[] field=fields;
                boolean fieldsOK=validate(field);
                if (fieldsOK){
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
                }else {
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
                    if (TextUtils.isEmpty(RC3number)){
                        listener.onRC3Error();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(Businesname)){
                        listener.onBusinessNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(OwnerName)){
                        listener.onOwnerNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(Phone)){
                        listener.onPhoneError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(RIB)){
                        listener.onRIBError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(address)){
                        listener.onAddressError();
                        error = true;
                        return;
                    }

                    if (TextUtils.isEmpty(email)){
                        listener.onEmailError();
                        error = true;
                        return;
                    }
                }



            }
        }, 400);
    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }
}