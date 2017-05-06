package com.sharity.sharityUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import static android.R.attr.data;
import static com.sharity.sharityUser.LoginClient.LoginClientInteractorImpl.DownloadImageBitmap;

/**
 * Created by Moi on 23/02/2017.
 */

public class TwitterConnectivity {
    private DatabaseHandler db;
    private User UserSession;
    private AccessToken accessToken;
    private ParseUser parseUser;
    private String email;
    private String name;
    private String id;
    private byte [] byte_pictureFB;
    private Bitmap  pictureFB;
    private boolean isnewUser;
    private Context context;
    OnTwitterUserCreated OnTwitterUserCreated;
    private String pictureUrl;
    public interface OnTwitterUserCreated{
        public void UserCreated();
    }
    public TwitterConnectivity(Context context, AccessToken accessToken, ParseUser user, boolean isNewUser){
        db = new DatabaseHandler(context);
        this.accessToken=accessToken;
        this.parseUser=user;
        this.isnewUser=isNewUser;
        this.context=context;
    }

    public void getProfil(OnTwitterUserCreated onFBUserCreated) {
        this.OnTwitterUserCreated=onFBUserCreated;
        new Thread()
        {
            @Override
            public void run() {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet verifyGet = new HttpGet(
                            "https://api.twitter.com/1.1/account/verify_credentials.json?include_email=true");
                    ParseTwitterUtils.getTwitter().signRequest(verifyGet);
                    org.apache.http.HttpResponse response =  client.execute(verifyGet);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    StringBuilder builder = new StringBuilder();
                    for (String line = null; (line = reader.readLine()) != null;) {
                        builder.append(line).append("\n");
                    }
                    JSONObject credential = new JSONObject(builder.toString());
                    id = credential.getString("id");
                    //email = credential.getString("email");
                    name = credential.getString("name");
                    pictureUrl = credential.getString("profile_image_url");
                    new ProfilePhotoAsync(pictureUrl).execute();
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

    class ProfilePhotoAsync extends AsyncTask<String, String, String> {
        String url;

        public ProfilePhotoAsync(String url) {
            this.url = url;
        }
        @Override
        protected String doInBackground(String... params) {
            // Fetching data from URI and storing in bitmap
            pictureFB = DownloadImageBitmap(url);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            byte_pictureFB = DbBitmapUtility.getBytes(pictureFB);
            UserSession=new User(parseUser.getObjectId(),name,email,byte_pictureFB,"");
            saveUser_ParseServer(id,byte_pictureFB,name,email,isnewUser);
        }
    }

    private void saveUser_ParseServer(String FBid, byte[] profil,String username, String email,boolean isnewUser) {
        parseUser.setUsername(username);
        if (email!=null) {
            parseUser.put("emailClient", email);
        }
        if (isnewUser){
            parseUser.put("sharepoints",0);
            parseUser.put("twitterId",FBid);
        }
        if (getFCMToken(context).length()>0) {
            parseUser.put("fcm_device_id", getFCMToken(context));
        }
        parseUser.put("emailVerified",true);
        parseUser.put("type",0); //is user

        byte[] data = profil;
        String thumbName = parseUser.getUsername().replaceAll("\\s+", "");
        final ParseFile parseFile = new ParseFile(thumbName + "_thumb.jpg", data);

        parseFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                parseUser.put("picture", parseFile);
                //Finally save all the user details
                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Log.d("FacebookConnectivity", "Saved to parse");
                    }
                });
            }
        });

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        String GCMsenderId=String.valueOf(R.string.parse_sender_id);
        installation.put("user", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
        installation.put("badge", 0);
        installation.put("GCMSenderId", GCMsenderId);
        String[] array={"TransactionTestAndroid"};
        installation.put("channels", Arrays.asList(array));
        installation.saveInBackground();

        //Add user to localDB
        if (db.getUserCount()<=0){
            db.addUserProfil(UserSession);
            OnTwitterUserCreated.UserCreated();
            Log.d("DB", "User Added");

        }else {
            db.deleteAllUser();
            db.addUserProfil(UserSession);
            OnTwitterUserCreated.UserCreated();
            Log.d("DB", "User updated");
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    public byte[] getByte_pictureFB() {
        return byte_pictureFB;
    }

    public Bitmap getPictureFB() {
        return pictureFB;
    }

    private String getFCMToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("TokenFireBase", "");         // getting String
        Log.d("RTOKE", accountDisconnect);
        return accountDisconnect;
    }
}
