package com.sharity.sharityUser;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.parse.ParseUser;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.TutorialActivity;


/**
 * Created by Moi on 23/06/2016.
 */
public class LauncherApplication extends Activity {

  DatabaseHandler db = new DatabaseHandler(this);

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_launcher);


        if (savedInstanceState == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            ParseUser currentUser = ParseUser.getCurrentUser();
            boolean previouslyStarted = prefs.getBoolean("first", false);
            if (!previouslyStarted) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("first", Boolean.TRUE);
                edit.commit();

                startActivity(new Intent(getBaseContext(), TutorialActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();

            } else {
                if (currentUser != null && db.getUserCount()>0) {
                    Intent intent = new Intent(getBaseContext(), ProfilActivity.class);
                    startActivity(intent);
                    db.close();
                } else {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }
    }
}
