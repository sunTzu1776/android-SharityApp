package com.sharity.sharityUser.ParsePushNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;
import com.sharity.sharityUser.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MyCustomReceiver extends ParsePushBroadcastReceiver {
    public static final String intentAction = "com.parse.push.intent.RECEIVE";
      private static final String TAG = "MyCustomReceiver";  
      @Override  
      public void onReceive(Context context, Intent intent) {

                if (intent == null)  
                {  
                     Log.d(TAG, "Receiver intent null");
                }  
                else  
                {
                    Notification.Builder n = new Notification.Builder(context)
                            .setContentTitle("title")
                            .setContentText("message")
                            .setSmallIcon(R.drawable.icon_option)
                            .setAutoCancel(true);
                    n.setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT));
                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                    n.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    notificationManager.notify(1, n.build());
                }


      }  
 }  