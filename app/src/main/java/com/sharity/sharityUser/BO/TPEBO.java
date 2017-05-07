package com.sharity.sharityUser.BO;

import java.util.Date;

/**
 * Created by Moi on 07/05/2017.
 */

public class TPEBO {

    //private variables
    String _id;
    String _name;
    String _TPEid;
    Date  createdAt;
    Date  updatedAt;


    // Empty constructor
    public TPEBO(){

    }
    // constructor
    public TPEBO(String id, String name, String TPEid,Date createdAt,Date updatedAt){
        this._id = id;
        this._name = name;
        this._TPEid=TPEid;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
    }

    // constructor


    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_TPEid() {
        return _TPEid;
    }
}
