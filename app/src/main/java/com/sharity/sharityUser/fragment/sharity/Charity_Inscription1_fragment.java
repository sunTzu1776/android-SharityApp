package com.sharity.sharityUser.fragment.sharity;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.sharity.sharityUser.GooglePlaces.ParseAutoCompleteAdapter;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.FragOne;
import com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment;
import com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerInscriptionAdapter;
import com.sharity.sharityUser.fragment.pro.Pro_code_fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.sharity.sharityUser.R.id.RIB;
import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.address;
import static com.sharity.sharityUser.R.id.business_name;
import static com.sharity.sharityUser.R.id.chief_name;
import static com.sharity.sharityUser.R.id.tab_historique;


/**
 * Created by Moi on 14/11/15.
 */
public class Charity_Inscription1_fragment extends Fragment implements View.OnClickListener
{

    private EditText username;
    private EditText password;
    private Object byte_pictureFB;
    private static final int SELECT_PHOTO = 100;
    Bitmap yourSelectedImage;
    private ImageView done;
    private View inflate;
    public static boolean hasSelectLocation=false;
    private Double latitude;
    private Double longitude;
    private String type;
    private  CircleImageView profil;
    private FragOne onSelect;

    public PagerInscriptionAdapter adapter;


    public static Charity_Inscription1_fragment newInstance(String type) {
        Charity_Inscription1_fragment myFragment = new Charity_Inscription1_fragment();
        Bundle args = new Bundle();
        args.putString("type",type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_inscription1_charite, container, false);

        type=getArguments().get("type").toString();
        username=(EditText)inflate.findViewById(R.id.username);
        password=(EditText)inflate.findViewById(R.id.password);
        profil=(CircleImageView)inflate.findViewById(R.id.profil);
        done=(ImageView)inflate.findViewById(R.id.done);
        done.setOnClickListener(this);

        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionRuntime permissionRuntime = new PermissionRuntime(getActivity());
                if (ContextCompat.checkSelfPermission(getActivity(),
                        permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    GetPicture();
                }
                else {
                    permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE, permissionRuntime.Code_WRITE_EXTERNAL_STORAGE);
                }
            }
        });

        return inflate;
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.done:{
                if (username.getText().length()>0 && password.getText().length()>0){
                    Object[] fields={username,password,byte_pictureFB};
                    onSelect.OnSelector(fields);
               //     presenter.validateCredentials(type,fields,address);
                }else {
                    Toast.makeText(getActivity(),"Veuillez entrer un nom d'utilisateur et un mot de passe",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    public void GetPicture(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    public void onStart() {
        super.onStart();
    }

    public void onStop() {
        super.onStop();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // check if parent Fragment implements listener
        if (getParentFragment() instanceof FragOne) {
            onSelect = (FragOne) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelect = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    InputStream imageStream = null;
                    new BackgroundTask(imageStream,selectedImage).execute();
                }
        }
    }


    private class BackgroundTask extends AsyncTask<String, Void, Void> {
        InputStream stream;
        Uri selectedImage;

        public BackgroundTask(InputStream stream, Uri selectedImage){
            this.stream=stream;
            this.selectedImage=selectedImage;
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                stream = getActivity().getContentResolver().openInputStream(selectedImage);
                yourSelectedImage = BitmapFactory.decodeStream(stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            profil.setImageBitmap(yourSelectedImage);
            byte_pictureFB = DbBitmapUtility.getBytes(yourSelectedImage);
            Log.d("UI thread", "I am the UI thread");
        }
    }
}
