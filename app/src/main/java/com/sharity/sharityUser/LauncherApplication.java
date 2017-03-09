package com.sharity.sharityUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.parse.ParseUser;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.activity.TutorialActivity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;


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
                if (currentUser != null) {
                    if (Status(getApplicationContext()).equals("Business")) {
                        Intent intent = new Intent(getBaseContext(), ProfilProActivity.class);
                        startActivity(intent);
                        db.close();
                        finish();
                    } else if (Status(getApplicationContext()).equals("User")) {
                        Intent intent = new Intent(getBaseContext(), ProfilActivity.class);
                        startActivity(intent);
                        db.close();
                        finish();
                    }
                } else {
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                        db.close();
                        finish();
                }
            }
        }
    }

    private String Status(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("status", "");         // getting String
        return accountDisconnect;
    }
}
