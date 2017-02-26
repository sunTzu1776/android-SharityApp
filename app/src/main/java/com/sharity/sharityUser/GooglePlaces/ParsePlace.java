package com.sharity.sharityUser.GooglePlaces;

import java.io.Serializable;

public class ParsePlace implements Serializable {


    String description;
    String place_id;


    public ParsePlace(String description, String place_id) {
        this.description = description;
        this.place_id = place_id;
    }

    public ParsePlace() {

    }


    public String getDescription() {
        return description;
    }

    public String getPlace_id() {
        return place_id;
    }
}
