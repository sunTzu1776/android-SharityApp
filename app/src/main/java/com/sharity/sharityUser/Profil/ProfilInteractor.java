package com.sharity.sharityUser.Profil;

import android.app.Activity;
import android.content.Context;

public interface ProfilInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onSuccess();
    }

    void loginFacebook(Activity context, OnLoginFinishedListener listener);
     void loginTwitter(Context context, final OnLoginFinishedListener listener);
    }