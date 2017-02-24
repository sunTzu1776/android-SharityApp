package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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
import com.sharity.sharityUser.LoginPro.LoginPresenter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.ProfilActivity;


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

                        SaveCode_ToParse(saisie);

                        startActivity(new Intent(getActivity(), ProfilActivity.class));
                        getActivity().finish();
                    }else {
                        Toast.makeText(getActivity(),"Les codes entrés sont pas identiques",Toast.LENGTH_LONG).show();
                    }
                }


                break;
        }
    }

    private void SaveCode_ToParse(final String saisie){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.getInBackground(getUserObjectId(getActivity()), new GetCallback<ParseObject>() {
            public void done(ParseObject user, ParseException e) {
                if (e == null) {
                    user.put("securityCode", saisie);
                    user.saveInBackground();
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