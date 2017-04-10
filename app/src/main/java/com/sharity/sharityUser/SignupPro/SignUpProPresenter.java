package com.sharity.sharityUser.SignupPro;

import android.view.View;
import android.widget.EditText;

import static com.sharity.sharityUser.R.id.username;

public interface SignUpProPresenter {
    void validateCredentials(String type, View[] fields, Object[] addresse);
    void validateCredentialsSharity(String type, Object[] fields, Object[] addresse);

    void onDestroy();
}