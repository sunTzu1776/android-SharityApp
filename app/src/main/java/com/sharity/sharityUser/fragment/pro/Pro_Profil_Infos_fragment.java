package com.sharity.sharityUser.fragment.pro;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.GooglePlaces.ParseAutoCompleteAdapter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sharity.sharityUser.R.id.profil;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;
import static com.sharity.sharityUser.activity.ProfilProActivity.parseUser;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Infos_fragment extends Fragment{

    private ParseUser parseUser;
    private CircleImageView picture;
    private EditText username;
    private EditText password;
    private EditText Siret;
    private EditText business_name;
    private EditText chief_name;
    private EditText phone;
    private EditText address;
    private EditText email;
    private EditText RIB;
    private Button done;
    private View inflate;


    public static Pro_Profil_Infos_fragment newInstance() {
        Pro_Profil_Infos_fragment myFragment = new Pro_Profil_Infos_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_pro_profilinfos, container, false);
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        Siret=(EditText)inflate.findViewById(R.id.Siret);
        business_name=(EditText)inflate.findViewById(R.id.business_name);
        chief_name=(EditText)inflate.findViewById(R.id.chief_name);
        phone=(EditText)inflate.findViewById(R.id.phone_number);
        RIB=(EditText)inflate.findViewById(R.id.RIB);
        address=(EditText) inflate.findViewById(R.id.address);
        email=(EditText)inflate.findViewById(R.id.email);
        done=(Button)inflate.findViewById(R.id.done);
        picture=(CircleImageView) inflate.findViewById(R.id.picture_profil);

        String business = db.getBusinessId();
        Business biz = db.getBusiness(business);

        if (getActivity() instanceof ProfilProActivity){
            parseUser=ProfilProActivity.parseUser;
        }
        if (getActivity() instanceof ProfilActivity){
            parseUser=ProfilActivity.parseUser;
        }
        username.setText("Nom d'utilisateur: "+ parseUser.getUsername());
        password.setText("Mot de Passe : "+"");
        business_name.setText("Nom de l'entreprise : "+biz.get_businessName());
        chief_name.setText("Nom du dirigeant : "+biz.get_officerName());
        phone.setText("N° de téléphone : "+biz.get_telephoneNumber());
        address.setText("Adresse : "+biz.get_address());
        RIB.setText("RIB : "+biz.get_RIB());
        email.setText("E-mail : "+biz.get_email());

        picture.setImageResource(R.drawable.logo);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ProfilProActivity)getActivity()).onBackPressed();
            }
        });
        return inflate;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}