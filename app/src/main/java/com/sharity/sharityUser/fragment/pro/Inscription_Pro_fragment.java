package com.sharity.sharityUser.fragment.pro;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.sharity.sharityUser.GooglePlaces.ParseAutoCompleteAdapter;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Moi on 14/11/15.
 */
public class Inscription_Pro_fragment extends Fragment implements View.OnClickListener, SignUpProView, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

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

    public static Inscription_Pro_fragment newInstance(String type) {
        Inscription_Pro_fragment myFragment = new Inscription_Pro_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_inscription_pro, container, false);

        type=getArguments().get("type").toString();
        Log.d("Type",type);
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        Siret=(EditText)inflate.findViewById(R.id.Siret);
        business_name=(EditText)inflate.findViewById(R.id.business_name);
        chief_name=(EditText)inflate.findViewById(R.id.chief_name);
        phone=(EditText)inflate.findViewById(R.id.phone_number);
        RIB=(EditText)inflate.findViewById(R.id.RIB);
        address=(AutoCompleteTextView)inflate.findViewById(R.id.address);
        email=(EditText)inflate.findViewById(R.id.email);
        done=(Button)inflate.findViewById(R.id.done);
        done.setOnClickListener(this);

        presenter = new SignUpProPresenterImpl(this);

      try {
          mGoogleApiClient = new GoogleApiClient
                  .Builder(getActivity())
                  .enableAutoManage(getActivity(), 0, this)
                  .addApi(Places.GEO_DATA_API)
                  .addApi(Places.PLACE_DETECTION_API)
                  .addConnectionCallbacks(this)
                  .addOnConnectionFailedListener(this)
                  .build();
      }catch (IllegalStateException e){

      }


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
            address.setBackgroundColor(getResources().getColor(R.color.white));
        } else{
            // do something for phones running an SDK before lollipop
        }

        address.requestFocus();
        address.setSelection(0);
        address.setAdapter(new ParseAutoCompleteAdapter(getActivity()));

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Utils.isConnected(getActivity())){
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView desc_lcoation = (TextView) view.findViewById(R.id.description);
                TextView id_location = (TextView) view.findViewById(R.id.id);
                id_place = id_location.getText().toString();
                location_Desc = desc_lcoation.getText().toString();

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        RequestDetailsLocation(id_place);
                    }
                });



                //  PendingResult<PlaceBuffer> placeId= Places.GeoDataApi.getPlaceById(mGoogleApiClient,id_place);
                //  placeId.setResultCallback(mUpdatePlaceDetailsCallBack);
            }
        });

        return inflate;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done:{
                    View[] fields={username,password,Siret,business_name,chief_name,phone,address,RIB,email};
                    String _username= username.getText().toString();
                    String _password= password.getText().toString();
                    String _Siret= Siret.getText().toString();
                    String _business_name= business_name.getText().toString();
                    String _chief_name= chief_name.getText().toString();
                    String _phone= phone.getText().toString();
                    String _RIB= RIB.getText().toString();
                    String _email= email.getText().toString();
                    String _addresse= address.getText().toString();

                Object[] address={latitude,longitude,addresse};
                    presenter.validateCredentials(type,fields,address,_username,_password,_Siret,_business_name,_chief_name, _phone, _addresse, _RIB,_email);
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
        username.setError("usernameError");
    }

    @Override
    public void setPasswordError() {
        password.setError("usernameError");

    }

    @Override
    public void setRC3Error() {
        Siret.setError("usernameError");

    }

    @Override
    public void setBusinessNameError() {
        business_name.setError("usernameError");

    }

    @Override
    public void setOwnerNameError() {
        chief_name.setError("usernameError");

    }

    @Override
    public void setPhoneError() {
        phone.setError("usernameError");

    }

    @Override
    public void setAddressError() {
        address.setError("usernameError");

    }

    @Override
    public void setRIBError() {
        RIB.setError("RIB Error");

    }

    @Override
    public void setEmailError() {
        email.setError("usernameError");

    }

    @Override
    public void navigateToHome() {
        Toast.makeText(getActivity(),"ok inscrit",Toast.LENGTH_LONG).show();
        startActivity(new Intent(getActivity(), ProfilActivity.class));
        getActivity().finish();
    }


    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            mResolvingError = true;
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("GoogleAPI","connected");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static void onDialogDismissed() {
        mResolvingError = false;
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            onDialogDismissed();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == getActivity().RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }


    public void RequestDetailsLocation(String input) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?placeid="+input+"&key=AIzaSyDdBXFxMBhCBO1XmB8c9MEQ1bsigXuGIwg");
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the resultsinto a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("TAG", "Error processing Places API URL", e);
        } catch (IOException e) {
            Log.e("TAG", "Error connecting to Places API", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try {
            street_number="";
            route="";
            sublocality_level_1="";
            locality="";
            administrative_area_level_2="";
            administrative_area_level_1="";
            country="";
            postalCode="";

            Log.d("TAG", String.valueOf(jsonResults));

            // Create a JSON object hierarchy from the results
            org.json.JSONObject jsonObj2 = new org.json.JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj2.getJSONObject("result").getJSONArray("address_components");
            for (int i = 0; i < predsJsonArray.length(); i++) {
                String name= predsJsonArray.getJSONObject(i).getString("long_name");
                String type= predsJsonArray.getJSONObject(i).getString("types");

                if ((type.contains("street_number"))){
                    street_number=name+", ";
                    Log.d("long_name","street_number"+street_number);
                }
                if ((type.contains("route"))){
                    route=name+", ";
                    Log.d("long_name","route"+route);
                }
                if ((type.contains("locality"))){
                    if (type.contains("sublocality_level_1")){
                        sublocality_level_1=name;
                        Log.d("long_name","sublocality_level"+locality);
                    }else if(!type.contains("sublocality_level")){
                        locality=name+"";
                        Log.d("long_name","locality"+locality);
                    }
                }
                if ((type.contains("administrative_area_level_2"))){
                    administrative_area_level_2=name;
                    Log.d("long_name","administrative_area_level_2"+administrative_area_level_2);
                }
                if ((type.contains("administrative_area_level_1"))){
                    administrative_area_level_1=name;
                    Log.d("long_name","administrative_area_level_1"+administrative_area_level_1);
                }

                if ((type.contains("country"))){
                    country=name;
                    Log.d("long_name","country"+country);
                }
                if ((type.contains("postal_code"))){
                    postalCode=name+" ";
                    Log.d("long_name","postalCode"+postalCode);
                }

                org.json.JSONObject result=jsonObj2.getJSONObject("result").getJSONObject("geometry").getJSONObject("location");
                latitude=result.getDouble("lat");
                longitude=result.getDouble("lng");
                if (latitude==null){
                    latitude=0.0;
                }
                if (longitude==null){
                    longitude=0.0;
                }
            }

            addresse= street_number+route+postalCode+locality;
            hasSelectLocation=true;
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(address.getWindowToken(), 0);

        } catch (JSONException e) {
            Log.e("TAG", "Cannot process JSON results", e);
        }catch (NullPointerException e){

        }
    }

}