package com.sharity.sharityUser.BO;

import com.parse.ParseFile;

import java.io.Serializable;

public class UserLocation implements Serializable{

        private String id;
        private double latitude;
        private double longitude;
        private String username;
        private String userToken;
    byte[] pictureProfil;

        public UserLocation(String id,double latitude, double longitude, String username, byte[] pictureProfil,String userToken){
            this.latitude=latitude;
            this.longitude=longitude;
            this.username=username;
            this.pictureProfil=pictureProfil;
            this.id=id;
            this.userToken=userToken;
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

    public String getUserToken() {
        return userToken;
    }
}