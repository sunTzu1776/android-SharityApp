package com.sharity.sharityUser.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.parse.ParseFacebookUtils;

import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.Login_fragment;
import com.sharity.sharityUser.fragment.client.client_PartenaireMap_fragment;
import com.sharity.sharityUser.fragment.sharity.Charity_Inscription_container_fragment;


/**
 * Created by Moi on 07/05/2016.
 */
public class LoginActivity extends AppCompatActivity {

    public static DatabaseHandler db;
    public static CallbackManager callbackManager;
    public static LoginActivity login_activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /* Enabling strict mode */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_login);
        db = new DatabaseHandler(this);
        login_activity = this;

        final Bundle data = this.getIntent().getExtras();
        if (data != null) {
            String emailVerified = data.getString("emailVerified");
            if (emailVerified.equals("false")) {
                Toast.makeText(LoginActivity.this, "vous n'avez pas confirmé l'email d'inscription qui vous a été envoyé, veuillez le confirmer", Toast.LENGTH_LONG).show();
            }
        }


    /*  Context ctx = this; //Permit to reset Sqlite database if needed
            String dbname = "User";
            File dbpath = ctx.getDatabasePath(dbname);
            ctx.deleteDatabase(dbname);
*/

        if (savedInstanceState == null) {
            FacebookSdk.sdkInitialize(this.getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new Login_fragment(), "Login_fragment")
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
        callbackManager.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static LoginActivity getInstance() {
        return login_activity;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 103: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Charity_Inscription_container_fragment Inscription_container_fragment = (Charity_Inscription_container_fragment) getSupportFragmentManager().findFragmentByTag("Charity_Inscription_container_fragment");
                    if (Inscription_container_fragment != null && Inscription_container_fragment.isVisible()) {
                        Inscription_container_fragment.CallGetPicture();
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
}



