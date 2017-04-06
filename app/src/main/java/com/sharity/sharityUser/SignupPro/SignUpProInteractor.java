package com.sharity.sharityUser.SignupPro;

import android.view.View;
import android.widget.EditText;

import static com.sharity.sharityUser.R.id.username;

public interface SignUpProInteractor {

    interface OnLoginFinishedListener {
        void onUsernameError();
        void onPasswordError();
        void onRC3Error();
        void onBusinessNameError();
        void onOwnerNameError();
        void onPhoneError();
        void onAddressError();
        void onRIBError();
        void onEmailError();

        void onSuccess();
    }

    void login(String type, View[] fields, Object[] addresse,OnLoginFinishedListener listener);

}