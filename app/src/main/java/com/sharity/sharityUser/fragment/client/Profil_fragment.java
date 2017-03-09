package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Moi on 14/11/15.
 */
public class Profil_fragment extends Fragment implements Updateable {

    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    protected ParseUser parseUser= ProfilActivity.parseUser;
    private TextView points;
    public static Profil_fragment newInstance() {
        Profil_fragment myFragment = new Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        imageView=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username=(TextView) inflate.findViewById(R.id.username_login);
        points=(TextView) inflate.findViewById(R.id.points);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //test logout
                ParseUser.logOut();

            }
        });
        return inflate;
    }



    @Override
    public  void update() {
        getProfilFromParse();
    }

    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Profile profile = Profile.getCurrentProfile();
        String objectId= getUserObjectId(getActivity());

        if (profile!=null) {
            try {
                String usernameFB = profile.getName();
                User user = db.getUser(objectId);
                byte[] image = user.getPictureprofil();
                User update = new User(user.get_id(), usernameFB, user.get_email(), image);
                db.updateUser(update);

                Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                imageView.setImageBitmap(PictureProfile);
                //DO network request to get User data
                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                query.getInBackground(getUserObjectId(getActivity()), new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            String user_name = object.getString("username");
                            String sharepoint = String.valueOf(object.getInt("sharepoints"));

                            username.setText(user_name);
                            points.setText(sharepoint);
                        } else {
                            // something went wrong
                        }
                    }
                });
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }
        //Get user data from localDB.
     /*   if (parseUser!=null) {
            getDataFromLocalDB();

        } else {
            //DO network request to get User data
            ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            query.getInBackground(getUserObjectId(getActivity()), new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        String user_name = object.getString("username");
                        String sharepoint = object.getString("sharepoints");

                        username.setText(user_name);
                        points.setText(sharepoint);
                    } else {
                        // something went wrong
                    }
                }
            });
        }*/
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    private void getDataFromLocalDB(){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Profile profile = Profile.getCurrentProfile();
        String objectId= getUserObjectId(getActivity());

        if (profile!=null){
            try {
                String usernameFB = profile.getName();
                User user = db.getUser(objectId);
                byte[] image = user.getPictureprofil();
                User update=new User(user.get_id(),usernameFB,user.get_email(),image);
                db.updateUser(update);

                Bitmap PictureProfile = DbBitmapUtility.getImage(image);
                imageView.setImageBitmap(PictureProfile);
                username.setText(update.get_name());
            }catch (CursorIndexOutOfBoundsException e){
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
    }
    }
}