package com.sharity.sharityUser.ParsePushNotification;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.Utils.ToastInterface;
import com.sharity.sharityUser.activity.ProfilProActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.sharity.sharityUser.Application.getContext;
import static com.sharity.sharityUser.activity.LocationUserActivity.Pro_Location;
import static com.sharity.sharityUser.activity.LocationUserActivity.db;
import static com.sharity.sharityUser.activity.LocationUserActivity.parseUser;
import static com.sharity.sharityUser.fragment.pro.Pro_History_fragment.JSON;

/**
 * Created by Moi on 25/03/2017.
 */

public class SendNotification {

    private Activity parentActivity;
    private UserLocation user;
    private Context context;
    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private DatabaseHandler db;
    private ParseUser parseUser;
    ToastInterface toastInterface;
    OkHttpClient client = new OkHttpClient();

    public SendNotification(Activity parentActivity, Context context, UserLocation userLocation, ToastInterface toastInterface){
        this.parentActivity=parentActivity;
        user=userLocation;
        this.toastInterface=toastInterface;
        this.context=context;
        db = new DatabaseHandler(context);
        parseUser=ParseUser.getCurrentUser();

    }

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type","application/json")
                .addHeader("X-Parse-Application-Id","process.env.PARSE_APP_ID")
                .addHeader("X-Parse-Master-Key","process.env.PARSE_MASTER_KEY")
                .addHeader("X-Parse-Session-Token",parseUser.getSessionToken())
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public void Send(final String price){
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject param = new JSONObject();
            JSONObject data = new JSONObject();
            param.put("title", "Notification");
            param.put("sharepoints", price);
            param.put("BusinessName", db.getBusinessName());
            param.put("body", db.getBusinessName()+ " vous à envoyé une notification ");
            data.put("data", param);
            jsonObject.put("payload", data);


            Log.d("jsonout",jsonObject.toString());
            post("http://ec2-52-56-157-252.eu-west-2.compute.amazonaws.com/api/push/"+user.getId(), jsonObject.toString(), new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("Response Push Failed", e.getMessage());
                            toastInterface.onNotificationError();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                String responseStr = response.body().string();
                                Log.d("Response", responseStr);

                                if (parentActivity!=null){
                                    parentActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            toastInterface.onNotificationSuccess(user,price);
                                        }
                                    });
                                }
                            } else {
                                // toastInterface.onNotificationError();
                                if (parentActivity!=null){
                                    parentActivity.runOnUiThread(new Runnable() {
                                        public void run() {
                                            toastInterface.onNotificationSuccess(user,price);
                                            Toast.makeText(getContext(), "Une erreur s'est produite lors de la notification", Toast.LENGTH_LONG).show();
                                            //  toastInterface.onNotificationSuccess();
                                        }
                                    });
                                }
                                Log.d("Response Push Failed", response.message());
                            }
                        }
                    }
            );
        } catch (JSONException ex) {
            Log.d("Exception", "JSON exception", ex);
        }
    }

}


