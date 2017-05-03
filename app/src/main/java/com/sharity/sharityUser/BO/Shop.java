package com.sharity.sharityUser.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class Shop {

    String _nom;
    String _descipriton;
    String prix;
    String reduction;

    // Empty constructor


    // constructor
    public Shop(String nom, String descipriton, String prix, String reduction) {
        this._nom = nom;
        this.prix = prix;
        this._descipriton = descipriton;
        this.reduction=reduction;

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
