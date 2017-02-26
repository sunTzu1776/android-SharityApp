package com.sharity.sharityUser.LoginPro;

public interface LoginProView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void setUserError();

    void navigateToHome();
}