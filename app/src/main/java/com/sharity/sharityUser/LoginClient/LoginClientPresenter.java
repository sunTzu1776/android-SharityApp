package com.sharity.sharityUser.LoginClient;

import android.app.Activity;
import android.content.Context;

import com.facebook.CallbackManager;

public interface LoginClientPresenter {
    void Login_Client(Context context, Activity activity, String type, CallbackManager callbackManager);

    void onDestroy();
}