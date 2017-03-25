package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.Application;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.LoginPro.LoginPresenter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fonts.EditTextMontserra;
import com.sharity.sharityUser.fonts.TextViewSegoeUi;

import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.LoginActivity.db;
import static java.lang.Boolean.getBoolean;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_code_fragment extends Fragment implements View.OnClickListener {

    private Boolean emailVerified=null;
    private View inflate;
    private ProgressBar progress;
    private EditTextMontserra saisir_code;
    private EditTextMontserra  confirmer_code;
    private TextViewSegoeUi valider;
    private TextView inscription;
    private LoginPresenter presenter;

    public static Pro_code_fragment newInstance() {
        Pro_code_fragment myFragment = new Pro_code_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_setcode_pro, container, false);
        saisir_code=(EditTextMontserra)inflate.findViewById(R.id.saisir_code);
        confirmer_code=(EditTextMontserra)inflate.findViewById(R.id.confirmer_code);
        valider=(TextViewSegoeUi)inflate.findViewById(R.id.valider);

        saisir_code.setOnClickListener(this);
        confirmer_code.setOnClickListener(this);
        valider.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saisir_code:

                break;

            case R.id.confirmer_code:
                break;

            case R.id.valider:
               String saisie= saisir_code.getText().toString();
                String confirmation= confirmer_code.getText().toString();
                if (saisie.length()<4 || confirmation.length()<4){
                    Toast.makeText(getActivity(),"Les codes doivent être de 4 chiffres numériques",Toast.LENGTH_LONG).show();

                }else {
                    if (saisie.equals(confirmation)){
                        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("Business_numCode", saisie);
                        editor.commit();


                        if (Utils.isConnected(getContext())){
                            SaveCode_ToParse(saisie);
                            Check_email_validate();
                        }else {
                            Utils.showDialog3(getActivity(), getString(R.string.dialog_network),getString(R.string.network),true, new Utils.Click() {
                            @Override
                            public void Ok() {

                            }
                            @Override
                            public void Cancel() {

                            }
                        });

                        }

                    }else {
                        Toast.makeText(getActivity(),"Les codes entrés sont pas identiques",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void SaveCode_ToParse(final String saisie){
        ParseUser parseUser = ParseUser.getCurrentUser();
        parseUser.put("securityCode", saisie);
        parseUser.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                // TODO Auto-generated method stub
                if (e != null){
                    e.printStackTrace();
                }else{
                    //updated successfully
                }
            }
        });

    }

    private String getBusinessObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("Business_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    private String Check_email_validate(){
        Application.parseUser= ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        if (Application.parseUser!=null) {
            query.getInBackground(Application.parseUser.getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                     Boolean emailVerified = object.getBoolean("emailVerified");
                        if (emailVerified){
                            if (LoginActivity.db.getBusinessCount()>0) {
                                String objectid = db.getBusinessId();
                                Business business=new Business(objectid,"true");
                                db.UpdateEmailVerified(business);
                                db.close();
                            }
                            startActivity(new Intent(getActivity(), ProfilProActivity.class));
                            getActivity().finish();
                        }else {
                            Toast.makeText(getActivity(),"vous n'avez pas confirmé l'email d'inscription qui vous a été envoyé, veuillez le confirmer",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }
        return String.valueOf(emailVerified);
    }

}