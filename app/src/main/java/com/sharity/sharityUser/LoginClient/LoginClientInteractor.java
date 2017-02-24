package com.sharity.sharityUser.LoginClient;

import android.app.Activity;
import android.content.Context;

import com.facebook.CallbackManager;

public interface LoginClientInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void loginFacebook(Activity context, OnLoginFinishedListener listener, CallbackManager manager);
     void loginTwitter(Context context, final OnLoginFinishedListener listener);
    }