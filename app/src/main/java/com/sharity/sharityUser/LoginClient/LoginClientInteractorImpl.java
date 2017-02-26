package com.sharity.sharityUser.LoginClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.FacebookConnectivity;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.User;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.bitmap;


public class LoginClientInteractorImpl implements LoginClientInteractor {

    protected User UserSession;
    protected boolean error = false;
    protected boolean isnewUser=false;
    protected DatabaseHandler db;
    protected Activity mContext;
    @Override
    public void loginTwitter(final Context context, final OnLoginFinishedListener listener) {
        ParseTwitterUtils.logIn(context, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "user cancelled login."+err.getMessage());
                    listener.onUsernameError();
                    error = true;
                    ParseUser.logOut();
                } else if (user.isNew()) {
                    Log.d("MyApp", "signed up and logged in through Twitter!");
                    getTwitterInfo_save();
                    if (!ParseTwitterUtils.isLinked(user)) {
                        ParseTwitterUtils.link(user, context, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(user)) {
                                    Log.d("MyApp", "user logged in with Twitter!");
                                }
                            }
                        });
                    }
                    listener.onSuccess();
                } else {
                    Log.d("MyApp", "User logged in through Twitter!");
                    getTwitterInfo_save();
                    if (!ParseTwitterUtils.isLinked(user)) {
                        ParseTwitterUtils.link(user, context, new SaveCallback() {
                            @Override
                            public void done(ParseException ex) {
                                if (ParseTwitterUtils.isLinked(user)) {
                                    Log.d("MyApp", "user logged in with Twitter!");
                                }
                            }
                        });
                    }
                    listener.onSuccess();
                }
            }
        });

    }

    @Override
    public void loginFacebook(final Activity context, final OnLoginFinishedListener listener, final CallbackManager callbackManager) {
        new Handler().postDelayed(new Runnable() {
            @Override public void run() {
                final List<String> permissions = Arrays.asList("public_profile", "email");
                mContext=context;
                ParseFacebookUtils.logInWithReadPermissionsInBackground(context, permissions, new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "The user cancelled the Facebook login.: "+err.getMessage());
                            listener.onUsernameError();
                            ParseUser.logOut();

                        } else if (user.isNew()) {
                            isnewUser=true;
                            Log.d("MyApp", "User signed up and logged in through Facebook!");

                             ObjectId_ToPref(user.getObjectId());

                            FacebookConnectivity FBconnectivity=new FacebookConnectivity(context,AccessToken.getCurrentAccessToken(),user,isnewUser);
                            FBconnectivity.getProfil();

                            AccessToken token = AccessToken.getCurrentAccessToken();
                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkInBackground(user, token, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                        }
                                    }

                                });
                            }
                            listener.onSuccess();

                        } else {
                            isnewUser=false;
                            Log.d("MyApp", "User logged in through Facebook!");
                            ObjectId_ToPref(user.getObjectId());

                            FacebookConnectivity FBconnectivity=new FacebookConnectivity(context,AccessToken.getCurrentAccessToken(),user,isnewUser);
                            FBconnectivity.getProfil();

                            AccessToken token = AccessToken.getCurrentAccessToken();

                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkInBackground(user, token, new SaveCallback() {
                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                        }
                                    }

                                });
                            }
                            listener.onSuccess();
                        }
                    }
                });
            }
        }, 200);
    }



    public static Bitmap DownloadImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("IMAGE", "Error getting bitmap", e);
        }
        return bm;
    }



    private void getTwitterInfo_save(){
        new Thread()
        {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet verifyGet = new HttpGet(
                            "https://api.twitter.com/1.1/account/verify_credentials.json");
                    ParseTwitterUtils.getTwitter().signRequest(verifyGet);
                    org.apache.http.HttpResponse response =  client.execute(verifyGet);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONObject credential = new JSONObject(builder.toString());
                    final ParseUser parseUser = ParseUser.getCurrentUser();
                    parseUser.setUsername(credential.getString("name"));
                    parseUser.put("Twitter_screen_name", credential.getString("screen_name"));
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                        }
                    });

                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void ObjectId_ToPref(String ObjectId){
        SharedPreferences pref = mContext.getSharedPreferences("Pref", mContext.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("User_ObjectId", ObjectId);  // Saving objectid in preference
        editor.commit();
    }
}

