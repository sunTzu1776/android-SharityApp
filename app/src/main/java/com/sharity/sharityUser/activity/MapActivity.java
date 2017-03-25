package com.sharity.sharityUser.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

import com.facebook.FacebookSdk;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.pro.Pro_Partenaire_fragment;


/**
 * Created by Moi on 07/05/2016.
 */
public class MapActivity extends AppCompatActivity {

    public static DatabaseHandler db;


@Override
public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /* Enabling strict mode */
    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        db=new DatabaseHandler(this);

          /*  Context ctx = this; // for Activity, or Service. Otherwise simply get the context
            String dbname = "User";
            File dbpath = ctx.getDatabasePath(dbname);
            ctx.deleteDatabase(dbname);
*/

    if (savedInstanceState ==null){
        FacebookSdk.sdkInitialize(this.getApplicationContext());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Pro_Partenaire_fragment(), "Pro_Partenaire_fragment")
                     .addToBackStack(null)
                     .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu pMenu) {
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {
                    Pro_Partenaire_fragment Partenaire_Pro_fragment = (Pro_Partenaire_fragment) getSupportFragmentManager().findFragmentByTag("client_Partenaire_fragment");

                    if (Partenaire_Pro_fragment != null && Partenaire_Pro_fragment.isVisible()) {
                        Partenaire_Pro_fragment.update();
                    }
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}



