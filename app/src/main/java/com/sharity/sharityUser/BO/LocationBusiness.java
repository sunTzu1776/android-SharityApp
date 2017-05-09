package com.sharity.sharityUser.BO;

import com.fasterxml.jackson.core.SerializableString;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.add;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.distance;
import static com.sharity.sharityUser.R.id.latitude;
import static com.sharity.sharityUser.R.id.nom;
import static com.sharity.sharityUser.R.id.prix;
import static com.sharity.sharityUser.R.id.reduction;
import static com.sharity.sharityUser.R.id.username;

/**
 * Created by Moi on 21/11/15.
 */
public class LocationBusiness implements Serializable {

    private String categorie;
    //private variables
    private double _latitude;
    private double _longitude;
    String _businessName;
    private String _addresse;
    byte[] picture;
    float distance;
    boolean isoffset;
    private String description;
    private String prix;
    private String reduction;


    public LocationBusiness() {

    }

    // constructor
    public LocationBusiness(double latitude, double longitude, String businessName, String addresse) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this._addresse = addresse;
    }

    public LocationBusiness(double latitude, double longitude, String businessName, float distance, byte[] picture, boolean isOffset) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this.picture = picture;
        this.distance=distance;
        this.isoffset=isOffset;
    }

    public LocationBusiness(String categorie,double latitude, double longitude, String businessName, float distance, String description,String prix,String reduction, boolean isOffset) {
        this._latitude = latitude;
        this._longitude = longitude;
        this._businessName = businessName;
        this.distance=distance;
        this.isoffset=isOffset;
        this.description=description;
        this.prix=prix;
        this.reduction=reduction;
        this.categorie=categorie;

    }


    // constructor


    public boolean isoffset() {
        return isoffset;
    }

    public float getDistance() {
        return distance;
    }

    public byte[] getPicture() {
        return picture;
    }

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

    public String getCategorie() {
        return categorie;
    }

    //Promotion getter

    public String getDescription() {
        return description;
    }

    public String getPrix() {
        return prix;
    }

    public String getReduction() {
        return reduction;
    }
}
