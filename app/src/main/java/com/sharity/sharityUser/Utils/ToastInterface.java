package com.sharity.sharityUser.Utils;

import android.app.Activity;
import android.content.Context;

import com.facebook.CallbackManager;
import com.sharity.sharityUser.BO.UserLocation;

public interface ToastInterface {


        void onNotificationError();

        void onNotificationSuccess(UserLocation location, String price);

    }