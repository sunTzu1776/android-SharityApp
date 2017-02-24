package com.sharity.sharityUser.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sharity.sharityUser.LoginClient.LoginClientPresenterImpl;
import com.sharity.sharityUser.LoginClient.LoginClientView;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.activity.LoginActivity;

import static com.sharity.sharityUser.R.id.add;
import static com.sharity.sharityUser.R.id.saisir_code;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.R.id.username_login;


/**
 * Created by Moi on 14/11/15.
 */
public class Inscription_Pro_fragment extends Fragment implements View.OnClickListener, SignUpProView {

    private View inflate;
    private EditText username;
    private EditText password;
    private EditText RC3number;
    private EditText business_name;
    private EditText chief_name;
    private EditText phone;
    private EditText RIB;
    private EditText address;
    private EditText email;
    private Button done;
    private SignUpProPresenter presenter;

    public static Inscription_Pro_fragment newInstance() {
        Inscription_Pro_fragment myFragment = new Inscription_Pro_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_inscription_pro, container, false);
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        RC3number=(EditText)inflate.findViewById(R.id.rc3number);
        business_name=(EditText)inflate.findViewById(R.id.business_name);
        chief_name=(EditText)inflate.findViewById(R.id.chief_name);
        phone=(EditText)inflate.findViewById(R.id.phone_number);
        RIB=(EditText)inflate.findViewById(R.id.RIB);
        address=(EditText)inflate.findViewById(R.id.address);
        email=(EditText)inflate.findViewById(R.id.email);
        done=(Button)inflate.findViewById(R.id.done);
        done.setOnClickListener(this);
        presenter = new SignUpProPresenterImpl(this);

        return inflate;
    }

    private boolean validate(EditText[] fields){
        for(int i=0; i<fields.length; i++){
            EditText currentField=fields[i];
            if(currentField.getText().toString().length()<=0){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done:{
                boolean fieldsOK=validate(new EditText[]{username, password, RC3number,business_name,chief_name,phone,RIB,address,email});
                if (fieldsOK){
                    String _username= username.getText().toString();
                    String _password= password.getText().toString();
                    String _RC3number= RC3number.getText().toString();
                    String _business_name= business_name.getText().toString();
                    String _chief_name= chief_name.getText().toString();
                    String _phone= phone.getText().toString();
                    String _RIB= RIB.getText().toString();
                    String _address= address.getText().toString();
                    String _email= email.getText().toString();
                    presenter.validateCredentials(_username,_password,_RC3number,_business_name,_chief_name, _phone, _RIB, _address,_email);

                }else {

                }
            }
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
    public void setRC3Error() {

    }

    @Override
    public void setBusinessNameError() {

    }

    @Override
    public void setOwnerNameError() {

    }

    @Override
    public void setPhoneError() {

    }

    @Override
    public void setAddressError() {

    }

    @Override
    public void setRIBError() {

    }

    @Override
    public void setEmailError() {

    }

    @Override
    public void navigateToHome() {

    }
}