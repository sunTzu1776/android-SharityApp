package com.sharity.sharityUser.BO;

import java.io.Serializable;

/**
 * Created by Moi on 21/11/15.
 */
public class Promo implements Serializable {

    String _nom;
    String _descipriton;
    String prix;
    String reduction;
    double latitude;
    double longitude;
    boolean isoffset;

    // Empty constructor


    // constructor
    public Promo(String nom, String descipriton, String prix, String reduction, double latitude, double longitude, boolean isoffset) {
        this._nom = nom;
        this.prix = prix;
        this._descipriton = descipriton;
        this.reduction=reduction;
        this.latitude=latitude;
        this.longitude=longitude;
        this.isoffset=isoffset;
    }

    public boolean isoffset() {
        return isoffset;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getReduction() {
        return reduction;
    }

    public String get_nom() {
        return _nom;
    }

    public String get_descipriton() {
        return _descipriton;
    }

    public String getPrix() {
        return prix;
    }
}
