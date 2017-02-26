package com.sharity.sharityUser.SignupPro;

import android.view.View;
import android.widget.EditText;

import static com.sharity.sharityUser.R.id.username;

public interface SignUpProPresenter {
    void validateCredentials(String type, View[] fields, Object[] addresse, String username, String password, String Siret, String Businesname, String OwnerName, String Phone, String address, String RIB, String email);

    void onDestroy();
}