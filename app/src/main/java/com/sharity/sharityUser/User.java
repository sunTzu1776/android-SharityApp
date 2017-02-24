package com.sharity.sharityUser;

/**
 * Created by Moi on 21/11/15.
 */
public class User {

    //private variables
    String _id;
    String _name;
    String _email;
    byte[] pictureprofil;
    // Empty constructor
    public User(){

    }
    // constructor
    public User(String id, String name, String email,byte[] pictureprofil){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
    }

    // constructor

    public byte[] getPictureprofil() {
        return pictureprofil;
    }

    public String get_id() {
        return _id;
    }

    public String get_email() {
        return _email;
    }

    public String get_name() {
        return _name;
    }
}
