package com.sharity.sharityUser.SignupPro;

public interface SignUpProPresenter {
    void validateCredentials(String username, String password,String RC3number,String Businesname,String OwnerName,String Phone,String address,String RIB,String email);

    void onDestroy();
}