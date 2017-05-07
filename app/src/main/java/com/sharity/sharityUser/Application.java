package com.sharity.sharityUser;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;
import com.parse.interceptors.ParseLogInterceptor;
import com.sharity.sharityUser.BO.CISSTransaction;
import com.sharity.sharityUser.BO.TPE;
import com.sharity.sharityUser.ParsePushNotification.MyCustomReceiver;

import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.sharity.sharityUser.BO.CISSTransaction.approved;
import static com.sharity.sharityUser.BO.CISSTransaction.transaction;


public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();
    private static Context context;
    private static Application mInstance;
    public static ParseUser parseUser;
    public static ParseLiveQueryClient parseLiveQueryClient;
    public static SubscriptionHandling<CISSTransaction> subscriptionHandling;
    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        mInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        ParseObject.registerSubclass(CISSTransaction.class);
        ParseObject.registerSubclass(TPE.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.ParseAppId)) // correspond to APP_ID
                .clientKey("034ba10e9b381a67c9a3340acc1ad2a425987c4176tfvbji876tghj")
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("https://sharity-back.com/parse").build());

        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize(getString(R.string.TwitterconsumerKey), getString(R.string.TwitterconsumerSecret));
        parseUser = ParseUser.getCurrentUser();

        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();
    }




    public static synchronized Application getInstance() {
        return mInstance;
    }


    private ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            // new activity created; force its orientation to portrait
            activity.setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

}

