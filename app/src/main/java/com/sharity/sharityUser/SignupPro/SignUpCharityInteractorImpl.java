package com.sharity.sharityUser.SignupPro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.label;
import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.sharity.sharityUser.Application.parseUser;
import static com.sharity.sharityUser.activity.LoginActivity.db;

/**
 * Created by Moi on 07/04/2017.
 */

public class SignUpCharityInteractorImpl implements SignUpProInteractor {
    ParseObject object;
    ParseUser user;
    private String _type;
    private String _username;
    private String _password;
    private String _Siret;
    private String _Businesname;
    private String _OwnerName;
    private String _Phone;
    private String descrition;
    private String _address;
    private String _addressfield;
    private String _RIB;
    private String _email;
    private Object[] _tempfield;
    private View[] _finalfield;

    private byte[] picture;
    private OnLoginFinishedListener _listener;
    ParseGeoPoint point;
    private Double latitude;
    private Double longitude;

    @Override
    public void login(String type, final View[] fields, Object[] addresse, OnLoginFinishedListener listener) {
    }

    @Override
    public void loginSharity(String type, final Object[] fields, Object[] addresse, OnLoginFinishedListener listener) {

        //View[] fields={_username,_password,business_name,chief_name,description,Siret,phone,address,RIB,email};
        this._type = type;
        this._username = ((EditText) fields[0]).getText().toString();
        this._password = ((EditText) fields[1]).getText().toString();
        this.picture = ((byte[]) fields[2]);
        this._Businesname = ((EditText) fields[3]).getText().toString();
        this._OwnerName = ((EditText) fields[4]).getText().toString();
        this.descrition=((EditText) fields[5]).getText().toString();
        this._Siret = ((EditText) fields[6]).getText().toString();
        this._Phone = ((EditText) fields[7]).getText().toString();
        this._addressfield = ((AutoCompleteTextView) fields[8]).getText().toString();
        this._RIB = ((EditText) fields[9]).getText().toString();
        this._email = ((EditText) fields[10]).getText().toString();

        if ((Double) addresse[0] != null) {
            this.latitude = (Double) addresse[0];
            this.longitude = (Double) addresse[1];
            this._address = (String) addresse[2];
        }
        this._listener = listener;

        _finalfield = new View[fields.length];
        List<View> numlist = new ArrayList<View>();
        for(int i= 0;i<fields.length;i++)
        {
            if(i !=2)
            {
                numlist.add((View) fields[i]);
            }
            else
            {
            }
        }
        _finalfield = numlist .toArray(new View[numlist.size()]);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean error = false;
                boolean fieldsOK = validate((_finalfield));
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

    private void CreateUser(String username, String password, String email) {
        user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        if (_type.equals("charite")) {
            user.put("type",2); //is charity
        }

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    try {
                        CreateBusiness();
                    } catch (NullPointerException d) {

                    }
                } else {
                    if (e.getCode() == 203) {
                        _listener.onEmailError();
                    }
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    private void CreateBusiness() {
        object = null;
        if (_type.equals("charite")) {
            Log.d("charite",descrition);
            object = new ParseObject("Charity");
            object.put("owner", ParseObject.createWithoutData("_User", user.getObjectId()));
            object.put("officerName", _OwnerName);
            object.put("RIB", _RIB);
            object.put("SIRET", _Siret);
            object.put("businessName", _Businesname); //nom association
            object.put("name", _Businesname); //nom association
            object.put("telephoneNumber", _Phone);
            object.put("address", _address);
            point = new ParseGeoPoint(latitude, longitude);
            object.put("location", point);
            object.put("description", descrition); //description association
            object.put("email", _email);
            object.put("sharepoints", 0);
            object.put("emailVerified", false);

            byte[] data = picture;
            final ParseFile parseFile = new ParseFile("profil.jpg", data);
            parseFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    object.put("Logo", parseFile);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                SaveLocationUser(point, object.getObjectId());
                                final Business business = new Business(object.getObjectId(), user.getUsername(), user.getObjectId(), _OwnerName, _Businesname, _RIB, _Siret, _Phone, _address, String.valueOf(latitude), String.valueOf(longitude), _email, "false");
                                if (db.getBusinessCount() >= 1) {
                                    db.deleteAllBusiness();
                                }
                                db.addProProfil(business);
                                LoginPro();
                                _listener.onSuccess();
                            } else {
                                Log.d(TAG, "ex" + e.getMessage());
                                if (e.getCode() == 203) {
                                    _listener.onEmailError();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void LoginPro() {
        ParseUser.logInInBackground(_username, _password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    String GCMsenderId = String.valueOf(R.string.gcm_sender_id);
                    installation.put("user", ParseObject.createWithoutData("_User", user.getObjectId()));
                    installation.put("badge", 0);
                    installation.put("GCMSenderId", GCMsenderId);
                    String[] array = {"Transaction"};
                    installation.put("channels", Arrays.asList(array));
                    installation.saveInBackground();
                } else {
                    if (e.getCode() == 101) {
                    }
                }
            }
        });
    }


    private void SaveLocationUser(ParseGeoPoint point, String objectid) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (_type.equals("charite")) {
            parseUser.put("SharityId", objectid);
            parseUser.put("geoloc", point);
        }

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // TODO Auto-generated method stub
                if (e != null) {
                    e.printStackTrace();
                } else {
                    //updated successfully
                }
            }
        });

    }



    private boolean validate(View[] fields) {
        for (int i = 0; i < fields.length; i++) {
            View currentField = fields[i];
            if (currentField instanceof EditText) {
                EditText editText = (EditText) currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            } else if (currentField instanceof AutoCompleteTextView) {
                AutoCompleteTextView editText = (AutoCompleteTextView) currentField;
                if (editText.getText().toString().length() <= 0) {
                    return false;
                }
            }

        }
        return true;
    }
}