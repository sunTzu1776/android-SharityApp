package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;
import com.parse.ParseUser;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.LocalDatabase.DbBitmapUtility;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sharity.sharityUser.activity.ProfilProActivity.db;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Profil_fragment extends Fragment implements Updateable {

    public static final String ARG_PLANET_NUMBER = "planet_number";
    private View inflate;
    private TextView username;
    private CircleImageView imageView;
    protected ParseUser parseUser= ProfilActivity.parseUser;
    private TextView points;
    private TextView email;
    private TextView phone;

    public static Pro_Profil_fragment newInstance() {
        Pro_Profil_fragment myFragment = new Pro_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_profile_pro, container, false);
        imageView=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username=(TextView) inflate.findViewById(R.id.username_login);
        email=(TextView) inflate.findViewById(R.id.email);
        phone=(TextView) inflate.findViewById(R.id.telephone);
        getProfilFromParse();


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        if (db.getBusinessCount()>0) {
            String objectid = db.getBusinessId();
            Business business = db.getBusiness(objectid);
            Log.d("emailVerified",business.getEmailveried());
            username.setText(business.get_officerName());
            email.setText(business.get_email());
            phone.setText(business.get_telephoneNumber());
            db.close();
        }
        else {
           /* try {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (int i =0; i<objects.size();i++){
                                ParseObject object = objects.get(i).getParseObject("user");
                               Log.d("objo",object.getObjectId());
                               // String user_name = objects.getString("officerName");
                              //  username.setText(user_name);
                            }
                        } else {
                            // something went wrong
                        }

                    }
                });
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }*/

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
    }

    private String getObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    private void getDataFromLocalDB(){
        DatabaseHandler db = new DatabaseHandler(getActivity());
        Profile profile = Profile.getCurrentProfile();
        String objectId= getObjectId(getActivity());

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