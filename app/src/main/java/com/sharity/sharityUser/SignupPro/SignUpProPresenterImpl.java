package com.sharity.sharityUser.SignupPro;

import android.view.View;
import android.widget.EditText;

import static com.sharity.sharityUser.R.id.username;

public class SignUpProPresenterImpl implements SignUpProPresenter, SignUpProInteractor.OnLoginFinishedListener {

    private SignUpProView loginView;
    private SignUpProInteractor loginInteractor;

    public SignUpProPresenterImpl(SignUpProView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new SignUpProInteractorImpl();
    }

    @Override
    public void validateCredentials(String type, View[] fields, Object[] addresse) {
        if (loginView != null) {
            loginView.showProgress();
        }

        loginInteractor.login(type,fields,addresse, this);
    }

    @Override public void onDestroy() {
        loginView = null;
    }

    @Override public void onUsernameError() {
        if (loginView != null) {
            loginView.setUsernameError();
            loginView.hideProgress();
        }
    }

    @Override public void onPasswordError() {
        if (loginView != null) {
            loginView.setPasswordError();
            loginView.hideProgress();
        }
    }

    @Override
    public void onRC3Error() {
        if (loginView != null) {
            loginView.setRC3Error();
        }
    }

    @Override
    public void onBusinessNameError() {
        if (loginView != null) {
            loginView.setBusinessNameError();
        }
    }

    @Override
    public void onOwnerNameError() {
        if (loginView != null) {
            loginView.setOwnerNameError();
        }
    }

    @Override
    public void onPhoneError() {
        if (loginView != null) {
            loginView.setPhoneError();
        }
    }

    @Override
    public void onAddressError() {
        if (loginView != null) {
            loginView.setAddressError();
        }
    }

    @Override
    public void onRIBError() {
        if (loginView != null) {
            loginView.setRIBError();
        }
    }

    @Override
    public void onEmailError() {
        if (loginView != null) {
            loginView.setEmailError();
        }
    }

    @Override public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}