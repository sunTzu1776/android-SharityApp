package com.sharity.sharityUser.BO;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Moi on 21/11/15.
 */

@ParseClassName("TPE")
public class TPE extends ParseObject {

    // Ensure that your subclass has a public default constructor
    public TPE() {
        super();
    }

    // Add a constructor that contains core properties
    public TPE(String body) {
        super();
    }

    // Use getString and others to access fields
    public String getName() {
        return getString("name");
    }

    // Use put to modify field values
    public void setName(String value) {
        put("name", value);
    }


    public String getTPEid() {
        return getString("TPEid");
    }

    // Use put to modify field values
    public void setTPEid(String value) {
        put("TPEid", value);
    }

    // Use put to modify field values
    public void setobjectId(String value) {
        put("objectId", value);
    }


    public String getobjectId() {
        return getString("objectId");
    }

    public Date getcreatedAt() {
        return getDate("createdAt");
    }

    public void setcreatedAt(Date value) {
        put("createdAt", value);
    }

    public Date getupdatedAt() {
        return getDate("updatedAt");
    }

    public void setupdatedAt(Date value) {
        put("updatedAt", value);
    }

}
