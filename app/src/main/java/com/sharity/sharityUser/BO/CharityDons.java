package com.sharity.sharityUser.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class CharityDons {

    String _nom;
    String _descipriton;
    String objectid;
    byte[] _image;

    // Empty constructor


    // constructor
    public CharityDons(String objectid,String nom, String description, byte[] image) {
        this._nom = nom;
        this._descipriton = description;
        this._image = image;
        this.objectid=objectid;

    }


    public byte[] get_image() {
        return _image;
    }

    public String get_descipriton() {
        return _descipriton;
    }

    public String get_nom() {
        return _nom;
    }

    public String getObjectid() {
        return objectid;
    }
}
