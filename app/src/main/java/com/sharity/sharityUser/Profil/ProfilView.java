package com.sharity.sharityUser.Profil;

public interface ProfilView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void navigateToHome();

    void noNetworkConnectivity();
}