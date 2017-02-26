package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.sharity.sharityUser.LoginPro.LoginPresenter;
import com.sharity.sharityUser.LoginPro.LoginProPresenterImpl;
import com.sharity.sharityUser.LoginPro.LoginProView;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;


/**
 * Created by Moi on 14/11/15.
 */
public class Login_Pro_fragment extends Fragment implements LoginProView,View.OnClickListener {

    private View inflate;
    private ProgressBar progress;
    private EditText username;
    private EditText password;
    private Button login_BT;
    private TextView inscription;
    private LoginPresenter presenter;
    private static String type;
    private TextView forgot_password;
    private Button icon_charite;
    private Button icon_pro;

    public static Login_Pro_fragment newInstance(String type) {
        Login_Pro_fragment myFragment = new Login_Pro_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_login_pro, container, false);

        type=getArguments().get("type").toString();
        icon_charite = (Button) inflate.findViewById(R.id.charite_icon);
        icon_pro = (Button) inflate.findViewById(R.id.pro_icon);
        progress = (ProgressBar) inflate.findViewById(R.id.progress);
        inscription=(TextView)inflate.findViewById(R.id.inscription);
        forgot_password=(TextView)inflate.findViewById(R.id.forgotpassword);
        login_BT = (Button) inflate.findViewById(R.id.login_BT);
        username = (EditText) inflate.findViewById(R.id.username_login);
        password = (EditText) inflate.findViewById(R.id.password_login);

        inscription.setOnClickListener(this);
        login_BT.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        presenter = new LoginProPresenterImpl(this);

        if (type.equals("charite")){
            icon_charite.setVisibility(View.VISIBLE);
            icon_pro.setVisibility(View.INVISIBLE);
        }else if (type.equals("pro")){
            icon_charite.setVisibility(View.INVISIBLE);
            icon_pro.setVisibility(View.VISIBLE);
        }

        return inflate;
    }

    @Override
    public void onClick(View view) {
            switch(view.getId()) {
                case R.id.inscription:
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.login, Inscription_Pro_fragment.newInstance(type), "Inscription_Pro_fragment");
                    ft.addToBackStack(null);
                    ft.commit();
                    break;

                case R.id.login_BT:
                    presenter.validateCredentials(username.getText().toString(), password.getText().toString());
                    break;

                case R.id.forgotpassword:
                    Utils.ForgottenPasswordDialog(getActivity(), false, new Utils.ProcessEmail() {
                        @Override
                        public void SetEmail(String email) {
                            ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(),"email envoyé avec succés",Toast.LENGTH_LONG);
                                    } else {
                                        Toast.makeText(getActivity(),"erreur: l'email n'a pu être envoyé",Toast.LENGTH_LONG);
                                    }
                                }
                            });
                        }

                        @Override
                        public void Cancel() {
                        }
                    });
                    break;
        }
    }


    @Override public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override public void setUsernameError() {
        username.setError("usernameError");
    }

    @Override public void setPasswordError() {
        password.setError("passwordError");
    }

    @Override
    public void setUserError() {
        Toast.makeText(getActivity(),"Username or password invalid",Toast.LENGTH_LONG).show();
    }

    @Override public void navigateToHome() {
        startActivity(new Intent(getActivity(), ProfilActivity.class));
        getActivity().finish();
    }

}