package com.sharity.sharityUser.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Moi on 20/06/2016.
 */
public class PermissionRuntime {
    public static int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    public static String MY_PERMISSIONS_REQUEST_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static String MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static String MY_PERMISSIONS_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static String MY_PERMISSIONS_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static String MY_PERMISSIONS_SEND_SMS = Manifest.permission.SEND_SMS;

    public static int Code_READ_CONTACT = 100;
    public static int Code_ACCESS_FINE_LOCATION = 102;
    public static int Code_WRITE_EXTERNAL_STORAGE = 103;
    public static int Code_SEND_SMS = 104;


    Activity context;
    public PermissionRuntime(Activity context){
        this.context=context;
    }
   private int getApiVersion(){
       return currentapiVersion;
   }



    public void Askpermission(String permission, int permission_code){
        if (ContextCompat.checkSelfPermission(context,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(context,
                        new String[]{permission},
                        permission_code);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }
}
