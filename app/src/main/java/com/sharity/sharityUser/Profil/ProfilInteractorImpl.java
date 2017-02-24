package com.sharity.sharityUser.Profil;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;


public class ProfilInteractorImpl implements ProfilInteractor {

    boolean error = false;

    @Override
    public void loginTwitter(final Context context, final OnLoginFinishedListener listener) {
        ParseTwitterUtils.logIn(context, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "The user cancelled the Twitter login.");
                    listener.onUsernameError();
                    error = true;
                    ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Twitter!");
                    if (!ParseTwitterUtils.isLinked(user)) {
                        ParseTwitterUtils.link(user, context, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(user)) {
                                    Log.d("MyApp", "Woohoo, user logged in with Twitter!");
                                }
                            }
                        });
                    }
                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                    listener.onSuccess();
                }
            }
        });

    }

    @Override
    public void loginFacebook(final Activity context, final OnLoginFinishedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                final List<String> permissions = Arrays.asList("public_profile", "email");
                ParseFacebookUtils.logInWithReadPermissionsInBackground(context, permissions, new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "The user cancelled the Facebook login.");
                            listener.onUsernameError();
                            ParseUser.logOut();

                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            listener.onSuccess();
                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, context, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                        }
                                    }

                                });
                            }
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            listener.onSuccess();

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, context, permissions, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
        }, 800);
    }
}