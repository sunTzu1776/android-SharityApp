package com.sharity.sharityUser.BO;

import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.add;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.username;

/**
 * Created by Moi on 21/11/15.
 */
public class LocationBusiness {

    //private variables
    double _latitude;
    double _longitude;
    String _businessName;
    String _addresse;

    // Empty constructor


    public LocationBusiness() {

    }

    // constructor
    public LocationBusiness(double latitude, double longitude, String businessName, String addresse) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this._addresse = addresse;

    }

    // constructor


    public double get_longitude() {
        return _longitude;
    }

    public String get_businessName() {
        return _businessName;
    }

    public double get_latitude() {
        return _latitude;
    }

    public String get_addresse() {
        return _addresse;
    }
}
