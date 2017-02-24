package com.sharity.sharityUser.Profil;

import android.app.Activity;
import android.content.Context;

public interface ProfilPresenter {
    void Login_Client(Context context, Activity activity, String type);

    void onDestroy();
}