package com.sharity.sharityUser.SignupPro;

public interface SignUpProView {
    void showProgress();

    void hideProgress();

    void setUsernameError();

    void setPasswordError();

    void setRC3Error();

    void setBusinessNameError();

    void setOwnerNameError();

    void setPhoneError();

    void setAddressError();

    void setRIBError();

    void setEmailError();

    void navigateToHome();
}