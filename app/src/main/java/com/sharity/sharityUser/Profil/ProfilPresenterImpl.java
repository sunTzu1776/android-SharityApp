package com.sharity.sharityUser.Profil;

import android.app.Activity;
import android.content.Context;

import com.sharity.sharityUser.Utils.Utils;

public class ProfilPresenterImpl implements ProfilPresenter, ProfilInteractor.OnLoginFinishedListener {

    private ProfilView loginView;
    private ProfilInteractor loginInteractor;

    public ProfilPresenterImpl(ProfilView loginView) {
        this.loginView = loginView;
        this.loginInteractor = new ProfilInteractorImpl();
    }

    @Override public void Login_Client(Context context, Activity activity,String type) {
       /* if (loginView != null) {
            loginView.showProgress();
        }*/

        if (Utils.isConnected(context)){
            if (type.equals("twitter")){
                loginInteractor.loginTwitter(activity,this);

            }else if(type.equals("facebook")){
                loginInteractor.loginFacebook(activity,this);

            }
        }else {
            loginView.noNetworkConnectivity();
        }


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

    @Override public void onSuccess() {
        if (loginView != null) {
            loginView.navigateToHome();
        }
    }
}