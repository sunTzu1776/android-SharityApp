package com.sharity.sharityUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.Transaction;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static com.sharity.sharityUser.LoginClient.LoginClientInteractorImpl.DownloadImageBitmap;
import static com.sharity.sharityUser.R.id.user;

/**
 * Created by Moi on 23/02/2017.
 */

public class FacebookConnectivity {
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
    OnFBUserCreated onFBUserCreated;

    public interface OnFBUserCreated{
        public void OnFBUserCreated();
    }
    public FacebookConnectivity(Context context,AccessToken accessToken, ParseUser user, boolean isNewUser){
        db = new DatabaseHandler(context);
        this.accessToken=accessToken;
        this.parseUser=user;
        this.isnewUser=isNewUser;
        this.context=context;
    }

    public void getProfil(OnFBUserCreated onFBUserCreated) {
        this.onFBUserCreated=onFBUserCreated;

        Bundle parameters = new Bundle();
        parameters.putString("fields", "email,name,picture");
        new GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
         /* handle the result */
                        try {
                            id = response.getJSONObject().getString("id");
                            email = response.getJSONObject().getString("email");
                             name = response.getJSONObject().getString("name");

                            JSONObject picture = response.getJSONObject().getJSONObject("picture");
                            JSONObject data = picture.getJSONObject("data");
                            String pictureUrl = data.getString("url");
                            new ProfilePhotoAsync(pictureUrl).execute();
                            Log.d("pictureUrl", pictureUrl);
                        } catch (JSONException e) {
                            Log.d("FacebookConnectivity", "Facebook error fetch data json");
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
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
        parseUser.put("emailClient",email);
        if (isnewUser){
            parseUser.put("sharepoints",0);
            parseUser.put("facebookId",FBid);
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
            onFBUserCreated.OnFBUserCreated();
            Log.d("DB", "User Added");

        }else {
            db.deleteAllUser();
            db.addUserProfil(UserSession);
            onFBUserCreated.OnFBUserCreated();
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
