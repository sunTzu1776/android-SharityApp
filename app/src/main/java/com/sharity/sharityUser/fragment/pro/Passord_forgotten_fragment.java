package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterHistory;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.fragment.Updateable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;


/**
 * Created by Moi on 14/11/15.
 */
public class Passord_forgotten_fragment extends Fragment implements View.OnClickListener {
    private EditText email_ET;
    private EditText password;
    private Button cancel;
    private Button validate;


    View inflate;
        public static Passord_forgotten_fragment newInstance(String indice) {
        Passord_forgotten_fragment myFragment = new Passord_forgotten_fragment();
        Bundle args = new Bundle();
            args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String what= getArguments().get("indice").toString();

        if (what.equals("forgotPassword")){
            inflate = inflater.inflate(R.layout.view_forgot_password, container, false);
            Forgotten_Password(inflate);
            cancel.setOnClickListener(this);
            validate.setOnClickListener(this);
        }else if (what.equals("Create_new_Password")){
            inflate = inflater.inflate(R.layout.view_create_password, container, false);
            Create_New_Password(inflate);
        }

        return inflate;
    }

    private void Forgotten_Password(View inflate){
        email_ET=(EditText)inflate.findViewById(R.id.email);
        cancel=(Button)inflate.findViewById(R.id.cancel_btn);
        validate=(Button)inflate.findViewById(R.id.btn_dialog);
    }

    private void Create_New_Password(View inflate){
        password=(EditText)inflate.findViewById(R.id.password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel_btn:
            Fragment prev= getParentFragment().getFragmentManager().findFragmentByTag("dialog");
                if (prev!=null){
                    Passord_forgotten_Container_fragment df = (Passord_forgotten_Container_fragment)prev;
                    df.dismiss();
                }
                break;
            case R.id.btn_dialog:
                if (email_ET.getText().toString().length()>0 && email_ET.getText().toString().contains("@")){
                    ParseUser.requestPasswordResetInBackground(email_ET.getText().toString(),
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(),"Un email vous a été envoyer",Toast.LENGTH_LONG).show();
                                        Fragment prev= getParentFragment().getFragmentManager().findFragmentByTag("dialog");
                                        if (prev!=null){
                                            Passord_forgotten_Container_fragment df = (Passord_forgotten_Container_fragment)prev;
                                            df.dismiss();
                                        }
                                    } else {
                                    }
                                }
                            });
                }else {
                    Toast.makeText(getActivity(),"Saisie incorrecte",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}