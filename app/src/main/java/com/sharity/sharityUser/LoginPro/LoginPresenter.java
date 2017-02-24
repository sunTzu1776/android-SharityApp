package com.sharity.sharityUser.LoginPro;

public interface LoginPresenter {
    void validateCredentials(String username, String password);

    void onDestroy();
}