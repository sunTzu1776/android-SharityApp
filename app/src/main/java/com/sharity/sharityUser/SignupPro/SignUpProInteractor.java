package com.sharity.sharityUser.SignupPro;

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

    void login(EditText[] fields,String username, String password, String RC3number, String Businesname, String OwnerName, String Phone, String address, String RIB, String email, OnLoginFinishedListener listener);

}