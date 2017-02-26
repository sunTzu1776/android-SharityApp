package com.sharity.sharityUser.LoginPro;

public interface LoginInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();

        void onPasswordError();

        void onUserError();

        void onSuccess();
    }

    void login(String username, String password, OnLoginFinishedListener listener);

}