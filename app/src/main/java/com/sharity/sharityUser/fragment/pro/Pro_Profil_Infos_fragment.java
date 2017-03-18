package com.sharity.sharityUser.fragment.pro;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.sharity.sharityUser.GooglePlaces.ParseAutoCompleteAdapter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilProActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.e;
import static com.sharity.sharityUser.activity.LoginActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_Infos_fragment extends Fragment{

    private EditText username;
    private EditText password;
    private EditText Siret;
    private EditText business_name;
    private EditText chief_name;
    private EditText phone;
    private EditText RIB;
    private EditText email;
    private Button done;
    private SignUpProPresenter presenter;

    private View inflate;
    private GoogleApiClient mGoogleApiClient;

    public static boolean hasSelectLocation=false;
    private String location_Desc;
    private String id_place;
    private Double latitude;
    private Double longitude;
    private TextView savedTV;
    private TextView currentloc;

    private String addresse=null;
    private String street_number="";
    private String route="";
    private String sublocality_level_1="";
    private String locality="";
    private String administrative_area_level_2="";
    private String administrative_area_level_1="";
    private String country="";
    private String postalCode="";
    AutoCompleteTextView address;

    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private static boolean mResolvingError = false;
    private String type;

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
        done=(Button)inflate.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return inflate;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    private void IsPro(){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "Business");
        editor.commit();
    }

    private String getBusiness_numCode(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("Business_numCode", "");         // getting String
        return accountDisconnect;
    }

}