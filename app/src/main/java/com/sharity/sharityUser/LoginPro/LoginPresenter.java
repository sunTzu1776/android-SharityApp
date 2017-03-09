package com.sharity.sharityUser.LoginPro;

import android.content.Context;

public interface LoginPresenter {
    void validateCredentials(Context context,String username, String password);

    void onDestroy();
}