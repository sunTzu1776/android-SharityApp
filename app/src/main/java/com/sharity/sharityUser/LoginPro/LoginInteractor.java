package com.sharity.sharityUser.LoginPro;

import android.content.Context;

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onUserError();

        void onSuccess();
    }

    void login(Context context,String username, String password, OnLoginFinishedListener listener);

}