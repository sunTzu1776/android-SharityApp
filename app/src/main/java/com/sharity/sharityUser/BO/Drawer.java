package com.sharity.sharityUser.BO;

import android.graphics.Bitmap;
import android.widget.ImageView;

import static android.R.attr.bitmap;
import static android.R.attr.type;
import static com.sharity.sharityUser.R.array.items;

/**
 * Created by Moi on 13/03/2017.
 */

public class Drawer {


    int logo;
    String items;
    int type;
    Bitmap PictureProfile;

    //business
    public Drawer(int logo, String items, int type){
        this.logo=logo;
        this.items=items;
        this.type=type;
        this.type=type;
    }


    //client
    public Drawer(Bitmap PictureProfile, String items, int type){
        this.PictureProfile=PictureProfile;
        this.items=items;
        this.type=type;
        this.type=type;
    }

    public int getLogo() {
        return logo;
    }

    public int getType() {
        return type;
    }

    public String getItems() {
        return items;
    }

    public Bitmap getPictureProfile() {
        return PictureProfile;
    }
}
