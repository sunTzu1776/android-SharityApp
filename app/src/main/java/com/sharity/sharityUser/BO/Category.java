package com.sharity.sharityUser.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class Category {



    String objectid;
    String nom;
    byte[] _image;

    // Empty constructor


    // constructor
    public Category(String objectid,String nom, byte[] image) {
        this.nom = nom;
        this.objectid = objectid;
        this._image = image;
    }

    public byte[] get_image() {
        return _image;
    }

    public String getNom() {
        return nom;
    }

    public String getObjectid() {
        return objectid;
    }
}
