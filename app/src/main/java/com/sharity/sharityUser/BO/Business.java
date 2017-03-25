package com.sharity.sharityUser.BO;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import static android.R.attr.name;
import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.user;

/**
 * Created by Moi on 21/11/15.
 */
public class Business {

    //private variables
    String _id;
    String _username;
    String _ower;
    String _officerName;
    String _RIB;
    String _businessName;
    String _Siret;
    String _telephoneNumber;
    String _address;
    String _latitude;
    String _longitude;
    String _email;
    String emailveried;

    // Empty constructor


    // constructor
    public Business(String id,String username, String ower,String officerName,String businessName,String RIB,String Siret,String telephoneNumber, String address, String latitude,String longitude, String email,String emailveried) {
        this._id = id;
        this._username = username;
        this._ower = ower;
        this._officerName = officerName;
        this._RIB = RIB;
        this._Siret = Siret;
        this._businessName=businessName;
        this._telephoneNumber = telephoneNumber;
        this._address = address;
        this._latitude = latitude;
        this._longitude=longitude;
        this._email = email;
        this.emailveried=emailveried;
    }

    // constructor
    public Business(String id,String emailveried) {
        this._id = id;
        this.emailveried=emailveried;
    }


    public String getLatitude() {
        return _latitude;
    }

    public String get_longitude() {
        return _longitude;
    }

    public String get_address() {
        return _address;
    }

    public String get_email() {
        return _email;
    }

    public String get_id() {
        return _id;
    }

    public String get_officerName() {
        return _officerName;
    }

    public String get_ower() {
        return _ower;
    }

    public String get_RIB() {
        return _RIB;
    }

    public String get_Siret() {
        return _Siret;
    }

    public String get_telephoneNumber() {
        return _telephoneNumber;
    }

    public String get_username() {
        return _username;
    }

    public String get_businessName() {
        return _businessName;
    }

    public String getEmailveried() {
        return emailveried;
    }
}
