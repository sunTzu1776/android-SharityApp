package com.sharity.sharityUser.BO;

/**
 * Created by Moi on 21/11/15.
 */
public class User {

    //private variables
    String _id;
    String _name;
    String _email;
    String _code;
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

    public User(String id, String name, String email,byte[] pictureprofil,String code){
        this._id = id;
        this._name = name;
        this._email=email;
        this.pictureprofil = pictureprofil;
        this._code=code;
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

    public String get_code() {
        return _code;
    }
}
