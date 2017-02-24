package com.sharity.sharityUser.LoginClient;

public interface LoginClientView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

    void noNetworkConnectivity();
}