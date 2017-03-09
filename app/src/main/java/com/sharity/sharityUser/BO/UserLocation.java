package com.sharity.sharityUser.BO;

import com.parse.ParseFile;

import java.io.Serializable;

public class UserLocation implements Serializable{

        private String id;
        private double latitude;
        private double longitude;
        private String username;
    byte[] pictureProfil;

        public UserLocation(String id,double latitude, double longitude, String username, byte[] pictureProfil){
            this.latitude=latitude;
            this.longitude=longitude;
            this.username=username;
            this.pictureProfil=pictureProfil;
            this.id=id;
        }

    public String getId() {
        return id;
    }

    public String getUsername() {
            return username;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

    public byte[] getPictureProfil() {
        return pictureProfil;
    }
}