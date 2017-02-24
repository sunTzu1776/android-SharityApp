package com.sharity.sharityUser.fragment.client;


import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.parse.ParseUser;
import com.sharity.sharityUser.LoginClient.LoginClientPresenter;
import com.sharity.sharityUser.LoginClient.LoginClientPresenterImpl;
import com.sharity.sharityUser.LoginClient.LoginClientView;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.fragment.pro.Login_Pro_fragment;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Login_fragment extends Fragment implements View.OnClickListener, LoginClientView {

    private View inflate;
    private TextView connexion;
    private ImageView facebook;
    private ImageView twitter;
    private LoginClientPresenter presenter;
    private Button access_pro;

    public static client_Login_fragment newInstance() {
        client_Login_fragment myFragment = new client_Login_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_login_client, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        twitter = (ImageView) inflate.findViewById(R.id.twitter_login);
        facebook = (ImageView) inflate.findViewById(R.id.facebook_login);
        access_pro = (Button) inflate.findViewById(R.id.pro_login_acces);

        twitter.setOnClickListener(this);
        facebook.setOnClickListener(this);
        access_pro.setOnClickListener(this);

        presenter = new LoginClientPresenterImpl(this);

        return inflate;
    }

    boolean boolFacebook = false;
    boolean boolTwitter = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pro_login_acces:
                ParseUser.logOut();
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.login, new Login_Pro_fragment(), "Login_Pro_fragment");
                ft.addToBackStack(null);
                ft.commit();
                break;

            case R.id.twitter_login:
                boolFacebook = false;
                boolTwitter = true;
                twitter.setImageResource(R.drawable.twitter_click);
                facebook.setImageResource(R.drawable.facebook_unclick);
                Connexion();
                break;
            case R.id.facebook_login:
                boolFacebook = true;
                boolTwitter = false;
                twitter.setImageResource(R.drawable.twitter_unclick);
                facebook.setImageResource(R.drawable.facebook_click);
                Connexion();
                break;
        }
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void navigateToHome() {
        AccessToken token = AccessToken.getCurrentAccessToken();
        Log.d("token", String.valueOf(token));
        final FragmentTransaction connexion = getFragmentManager().beginTransaction();
        connexion.replace(R.id.login, new client_code_fragment(), "Client_Code_fragment");
        connexion.addToBackStack(null);
        connexion.commit();
    }


    @Override
    public void noNetworkConnectivity() {
        Utils.showDialog3(getActivity(), getString(R.string.dialog_network), getString(R.string.network), true, new Utils.Click() {
            @Override
            public void Ok() {
            }

            @Override
            public void Cancel() {

            }
        });
    }

    private void Connexion() {
        if (!Utils.isConnected(getContext())) {
            noNetworkConnectivity();
        } else {
            if (!boolFacebook && !boolTwitter) {
                Toast.makeText(getContext(), "Veuillez séléctionner un moyen de connexion par Facebook ou Twitter", Toast.LENGTH_LONG).show();
            } else {
                if (!boolFacebook) {
                    presenter.Login_Client(getActivity(), getActivity(), "twitter", LoginActivity.callbackManager);

                } else {
                    presenter.Login_Client(getActivity(), getActivity(), "facebook", LoginActivity.callbackManager);
                }
            }
        }
    }
}