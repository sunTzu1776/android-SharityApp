package com.sharity.sharityUser;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.interceptors.ParseLogInterceptor;

public class Application extends android.app.Application {

    public static final String TAG = Application.class.getSimpleName();
    private static Application mInstance;
    public static ParseUser parseUser;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(getApplicationContext());
        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);

        mInstance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.ParseAppId)) // correspond to APP_ID
                .clientKey("")
                .enableLocalDataStore()
                .addNetworkInterceptor(new ParseLogInterceptor())
                .server("http://ec2-52-56-75-51.eu-west-2.compute.amazonaws.com:80/parse").build());

        ParseFacebookUtils.initialize(this);
        ParseTwitterUtils.initialize(getString(R.string.TwitterconsumerKey), getString(R.string.TwitterconsumerSecret));
        parseUser= ParseUser.getCurrentUser();

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

