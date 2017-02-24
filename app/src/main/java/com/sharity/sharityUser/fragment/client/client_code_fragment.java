package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.LoginPro.LoginPresenter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.TutorialActivity;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.email;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;


/**
 * Created by Moi on 14/11/15.
 */
public class client_code_fragment extends Fragment implements View.OnClickListener {

    private View inflate;
    private ProgressBar progress;
    private EditText saisir_code;
    private EditText  confirmer_code;
    private Button valider;
    private TextView inscription;
    private LoginPresenter presenter;

    public static client_code_fragment newInstance() {
        client_code_fragment myFragment = new client_code_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_setcode_client, container, false);
        saisir_code=(EditText)inflate.findViewById(R.id.saisir_code);
        confirmer_code=(EditText)inflate.findViewById(R.id.confirmer_code);
        valider=(Button)inflate.findViewById(R.id.valider);

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
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("client_numCode", saisie);
                        edit.commit();

                        if (Utils.isConnected(getContext())){
                            SaveCode_ToParse(saisie);
                            startActivity(new Intent(getActivity(), ProfilActivity.class));
                            getActivity().finish();
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
        parseUser.saveInBackground(new SaveCallback() {

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

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

}