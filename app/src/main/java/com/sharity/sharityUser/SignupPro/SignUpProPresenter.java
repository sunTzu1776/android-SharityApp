package com.sharity.sharityUser.SignupPro;

import android.widget.EditText;

import static com.sharity.sharityUser.R.id.username;

public interface SignUpProPresenter {
    void validateCredentials(EditText[] fields,String username, String password, String RC3number, String Businesname, String OwnerName, String Phone, String address, String RIB, String email);

    void onDestroy();
}