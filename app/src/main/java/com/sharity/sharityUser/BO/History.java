package com.sharity.sharityUser.BO;

import static android.R.attr.name;

/**
 * Created by Moi on 21/11/15.
 */
public class History {

    //private variables
    String _id;
    String _businessname;
    String _date;
    String _prix;
    int type;

    // Empty constructor
    public History(){

    }
    // constructor
    public History(String id, String businessname, String date, String prix,int type){
        this._id = id;
        this._businessname = businessname;
        this._date=date;
        this._prix = prix;
        this.type=type;
    }



    public String get_businessname() {
        return _businessname;
    }

    public String get_date() {
        return _date;
    }

    public String get_id() {
        return _id;
    }

    public String get_prix() {
        return _prix;
    }

    public int getType() {
        return type;
    }
}
