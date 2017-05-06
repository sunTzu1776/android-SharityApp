package com.sharity.sharityUser.SignupPro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.R;

import java.util.Arrays;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.LoginActivity.db;

public class SignUpProInteractorImpl implements SignUpProInteractor {
    ParseUser user;
    private String _type;
    private String _username;
    private String _password;
    private String _Siret;
    private String _Businesname;
    private String _OwnerName;
    private String _Phone;
    private String _address;
    private String _addressfield;
    private String _RIB;
    private String _email;
    private View[] _field;
    private OnLoginFinishedListener _listener;
    ParseGeoPoint point;
    private Double latitude;
    private Double longitude;

    @Override
    public void login(final String type, final View[] fields, Object[] addresse, final OnLoginFinishedListener listener) {

     //   View[] fields={username,password,Siret,business_name,chief_name,phone,address,RIB,email};
        this._type = type;
        this._username = ((EditText)fields[0]).getText().toString();
        this._password = ((EditText)fields[1]).getText().toString();
        this._Siret = ((EditText)fields[2]).getText().toString();
        this._Businesname = ((EditText)fields[3]).getText().toString();
        this._OwnerName = ((EditText)fields[4]).getText().toString();
        this._Phone = ((EditText)fields[5]).getText().toString();
        this._addressfield = ((AutoCompleteTextView)fields[6]).getText().toString();;
        this._RIB = ((EditText)fields[7]).getText().toString();
        this._email = ((EditText)fields[8]).getText().toString();

        if ((Double)addresse[0]!=null){
           this.latitude=(Double)addresse[0];
           this.longitude=(Double)addresse[1];
           this._address = (String)addresse[2];
        }
        this._listener = listener;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                _field = fields;

                boolean fieldsOK = validate(_field);
                if (fieldsOK) {
                    CreateUser(_username, _password, _email);
                } else {
                    if (TextUtils.isEmpty(_username)) {
                        _listener.onUsernameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_password)) {
                        _listener.onPasswordError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Siret)) {
                        _listener.onRC3Error();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Businesname)) {
                        _listener.onBusinessNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_OwnerName)) {
                        _listener.onOwnerNameError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_Phone)) {
                        _listener.onPhoneError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_RIB)) {
                        _listener.onRIBError();
                        error = true;
                        return;
                    }
                    if (TextUtils.isEmpty(_addressfield)) {
                        _listener.onAddressError();
                        error = true;
                        return;
                    }

                    if (TextUtils.isEmpty(_email)) {
                        _listener.onEmailError();
                        error = true;
                        return;
                    }
                }


            }
        }, 400);
    }

    @Override
    public void loginSharity(String type, Object[] fields, Object[] addresse, OnLoginFinishedListener listener) {

    }

    private void CreateUser(String username, String password, String email) {
        user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
      if (_type.equals("pro")) {
          user.put("type",1); //is business
        }

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    try {
                        CreateBusiness();
                    }catch (NullPointerException d){

                    }
                } else {
                    if (e.getCode()==203){
                        _listener.onEmailError();
                    }
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    private void CreateBusiness() {
        ParseObject object = null;
      if (_type.equals("pro")) {
            object = new ParseObject("Business");
        }

        object.put("owner", ParseObject.createWithoutData("_User", user.getObjectId()));
        object.put("officerName", _OwnerName);
        object.put("RIB", _RIB);
        object.put("SIRET", _Siret);
        object.put("businessName", _Businesname);
        object.put("telephoneNumber", _Phone);
        object.put("address", _address);
        point = new ParseGeoPoint(latitude, longitude);
        object.put("location", point);
        object.put("email", _email);
        object.put("emailVerified", false);

        final ParseObject finalObject = object;
        finalObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    SaveLocationUser(point, finalObject.getObjectId());
                    final Business business=new Business(finalObject.getObjectId(),user.getUsername(),user.getObjectId(),_OwnerName,_Businesname,_RIB,_Siret,_Phone,_address,String.valueOf(latitude),String.valueOf(longitude),_email,"false");
                    if (db.getBusinessCount()>=1){
                        db.deleteAllBusiness();
                    }
                        db.addProProfil(business);
                    LoginPro();
                    _listener.onSuccess();
                } else {
                    Log.d(TAG, "ex" + e.getMessage());
                    if (e.getCode()==203){
                        _listener.onEmailError();
                    }
                }
            }
        });
    }

    private void LoginPro(){
        ParseUser.logInInBackground(_username, _password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    String GCMsenderId=String.valueOf(R.string.gcm_sender_id);
                    installation.put("user", ParseObject.createWithoutData("_User", user.getObjectId()));
                    installation.put("badge", 0);
                    installation.put("GCMSenderId", GCMsenderId);
                    String[] array={"Transaction"};
                    installation.put("channels", Arrays.asList(array));
                    installation.saveInBackground();
                } else {
                    if (e.getCode()==101){
                    }
                }
            }
        });
    }


    private void SaveLocationUser(ParseGeoPoint point,String objectid){
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("BusinessId",objectid);
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

    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "");         // getting String
        Log.d("RTOKE", accountDisconnect);
        return accountDisconnect;
    }

    private boolean validate(View[] fields) {
        for (int i = 0; i < fields.length; i++) {
            View currentField = fields[i];
            if (currentField instanceof EditText){
                EditText editText=(EditText)currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }else if (currentField instanceof AutoCompleteTextView){
                AutoCompleteTextView editText=(AutoCompleteTextView)currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }

        }
        return true;
    }
}