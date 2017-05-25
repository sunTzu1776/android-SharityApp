package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.StoreAdapter2;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.LoginActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fonts.TextViewGeoManis;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Profil_fragment extends Fragment implements Updateable,ProfilActivity.OnNotificationUpdateProfil, SwipeRefreshLayout.OnRefreshListener, StoreAdapter2.OnItemDonateClickListener {

    private LayoutInflater vinflater;
    protected ParseUser parseUser = ProfilActivity.parseUser;
    private int recycler_position = -1;
    private StoreAdapter2.OnItemDonateClickListener onItemDonateClickListener;
    private ArrayList<CharityDons> list_dons = new ArrayList<CharityDons>();
    private Profile profile;
    private SwipeRefreshLayout swipeContainer;
    private View inflate;
    private RecyclerView recycler_charity;
    private StoreAdapter2 adapter2=null;
    private byte[] imageByte = null;
    private TextView username;

    //Field donation to charity
    private String CharityName;
    private String CharityId;

    private TextViewGeoManis points;
    private TextView do_donationTV;
    private TextView charity_description;
    private TextView sharepoints_moins;
    private TextView sharepoints_plus;
    private TextView dummyanchor;


    private LinearLayout dons_view;
    private Button charity_dons_validate;
    private int sharepoints_user_donate = 0;
    private int sharepoints_user_temp;
    boolean donation=false;
    private RelativeLayout frame_donate_validate;
    private View view_donate_validate;
    private View view_success_donate;
    TextView success_donate_tobusiness;
    TextView success_donate_sharepoint;
    LinearLayout success_donate_view;
    private CircularProgressBar circularProgressBar;
    public static client_Profil_fragment newInstance() {
        client_Profil_fragment myFragment = new client_Profil_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_profile_client, container, false);
        ((ProfilActivity) getActivity()).setProfilListener(client_Profil_fragment.this);
        points = (TextViewGeoManis) inflate.findViewById(R.id.points);
        username = (TextView) inflate.findViewById(R.id.username_login);
        do_donationTV = (TextView) inflate.findViewById(R.id.do_donationTV);
        dummyanchor = (TextView) inflate.findViewById(R.id.dummyanchor);
        frame_donate_validate = (RelativeLayout) inflate.findViewById(R.id.frame_expand);

        charity_description = (TextView) inflate.findViewById(R.id.charity_description);
        sharepoints_moins = (TextView) inflate.findViewById(R.id.sharepoints_moins);
        sharepoints_plus = (TextView) inflate.findViewById(R.id.sharepoints_plus);
        dons_view = (LinearLayout) inflate.findViewById(R.id.dons_view);
        recycler_charity = (RecyclerView) inflate.findViewById(R.id.recycler_charity);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        circularProgressBar=(CircularProgressBar)inflate.findViewById(R.id.circularprogress);
        int animationDuration = 3000; // 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(0);

        onItemDonateClickListener = this;
        points.setText(String.valueOf(0) + " : " + String.valueOf(sharepoints_user_temp));
        ShowDonateView();
        do_donationTV.setText("Faire un don");
        donation=true;

        do_donationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!donation){
                    do_donationTV.setText(String.valueOf(0) + " : " + String.valueOf(sharepoints_user_temp));
                    ShowDonateView();
                    do_donationTV.setText("Faire un don");
                    donation=true;
                }
            }
        });

        sharepoints_moins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sharepoints_user_donate >= 0 && sharepoints_user_donate <= sharepoints_user_temp) {
                    sharepoints_user_donate = sharepoints_user_donate - 10;
                    if (sharepoints_user_donate <= 0) {
                        sharepoints_user_donate = 0;
                        sharepoints_moins.setVisibility(View.INVISIBLE);
                    }
                    sharepoints_plus.setVisibility(View.VISIBLE);
                    points.setText(String.valueOf(sharepoints_user_donate) + " : " + String.valueOf(sharepoints_user_temp));
                }
            }
        });


        sharepoints_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharepoints_user_donate >= 0 && sharepoints_user_donate <= sharepoints_user_temp) {
                    sharepoints_user_donate = sharepoints_user_donate + 10;
                    if (sharepoints_user_donate >= sharepoints_user_temp) {
                        sharepoints_user_donate = sharepoints_user_temp;
                        sharepoints_plus.setVisibility(View.INVISIBLE);
                    }
                    sharepoints_moins.setVisibility(View.VISIBLE);
                    points.setText(String.valueOf(sharepoints_user_donate) + " : " + String.valueOf(sharepoints_user_temp));
                }
            }
        });

        getProfilFromParse();
        return inflate;
    }



    @Override
    public void update() {
        Log.d("clicli","update");
        int animationDuration2 = 100; // 2500ms = 2,5s
        points.setText(String.valueOf(sharepoints_user_temp));
        username.setText(String.valueOf(sharepoints_user_temp));
        int animationDuration = 3000; // 2500ms = 2,5s
        circularProgressBar.setProgress(0);
        if (sharepoints_user_temp<=100) {
            circularProgressBar.setProgressWithAnimation(sharepoints_user_temp/1,animationDuration);
        }
        else if (sharepoints_user_temp>100 && sharepoints_user_temp <=1000){
            circularProgressBar.setProgressWithAnimation(sharepoints_user_temp/10,animationDuration);
        }else if(sharepoints_user_temp>1000 && sharepoints_user_temp<10000){
            circularProgressBar.setProgressWithAnimation(sharepoints_user_temp/100,animationDuration);
        }


        circularProgressBar.invalidate();
    }

    private void getProfilFromParse() {
        DatabaseHandler db = new DatabaseHandler(getActivity());
        //if user connected via Facebook, get picture profil


        if (db.getUserCount() > 0 && Utils.isConnected(getContext())) {
            try {
                getTransaction();
            } catch (CursorIndexOutOfBoundsException e) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                swipeContainer.setRefreshing(false);
            }
        }
    }


    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }


    /*
     * Get local database to display nav drawer including Profil picture etc
     **/
    private void getTransaction() {
        try {
            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("_User");
            innerQuery.whereEqualTo("objectId", parseUser.getObjectId());

            ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Transaction");
            query1.whereMatchesQuery("customer", innerQuery);

            ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Transaction");
            query2.whereMatchesQuery("clientDonator", innerQuery);

            List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
            queries.add(query1);
            queries.add(query2);
            ParseQuery<ParseObject> mainquery = ParseQuery.or(queries);
            mainquery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        int sharepoints = 0;
                        for (ParseObject object : commentList) {
                            if (object.getInt("transactionType") == 1) {
                                int sp = object.getInt("sharepoints");
                                sharepoints = sharepoints + sp;
                            }
                            if (object.getInt("transactionType") == 2) {
                                int sp = object.getInt("sharepoints");
                                sharepoints = sharepoints - sp;
                                Log.d("sharepoints", String.valueOf(sp));
                                //   sharepoints=sharepoints-sp;
                            }
                        }
                        points.setText(String.valueOf(sharepoints));
                        username.setText(String.valueOf(sharepoints));
                        int animationDuration = 3000; // 2500ms = 2,5s
                        if (sharepoints<=100) {
                            circularProgressBar.setProgressWithAnimation(sharepoints/1,animationDuration);
                        }
                        else if (sharepoints<=1000){
                            circularProgressBar.setProgressWithAnimation(sharepoints/10,animationDuration);
                        }else if(sharepoints>1000 && sharepoints<10000){
                            circularProgressBar.setProgressWithAnimation(sharepoints/100,animationDuration);
                        }

                        sharepoints_user_temp = sharepoints;
                        Log.d("sharepoints", String.valueOf(sharepoints));
                        swipeContainer.setRefreshing(false);
                        UpdateUser(sharepoints);
                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }


    public void UpdateUser(final int sharepoints) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    String user_name = object.getString("username");
                    object.put("sharepoints", sharepoints);
                    object.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                points.setText(String.valueOf(sharepoints_user_temp));
                                Log.d("user update", "success");
                            } else {
                                Log.d("user update failed", e.getMessage());

                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())) {
            swipeContainer.setRefreshing(true);
            getProfilFromParse();
        }
    }


    private void get_Charity() {
        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Charity");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        for (final ParseObject object : commentList) {
                            ParseFile image = (ParseFile) object.getParseFile("Logo");
                            final String name = object.getString("name");
                            final String description = object.getString("description");
                            final String id = object.getObjectId();
                            Log.d("obj", id);
                            try {
                                imageByte = image.getData();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            list_dons.add(new CharityDons(id, name, description, imageByte));
                        }

                        if (adapter2==null){
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recycler_charity.setLayoutManager(layoutManager);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_charity.getContext(),
                                    layoutManager.getOrientation());
                            recycler_charity.addItemDecoration(dividerItemDecoration);
                            adapter2 = new StoreAdapter2(getActivity(), list_dons, onItemDonateClickListener);
                            recycler_charity.setAdapter(adapter2);
                        }else {
                            adapter2.notifyDataSetChanged();
                        }
                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }

    private void CreateTransaction(final String charityName, String charityId, final String price) {
        final Number num = Integer.parseInt(price);
        ParseObject object = new ParseObject("Transaction");
        object.put("sender_name", parseUser.getUsername());
        object.put("clientDonator", ParseObject.createWithoutData("_User", parseUser.getObjectId()));
        object.put("recipient_name", charityName);
        object.put("sharepoints", num);
        object.put("amount", Integer.parseInt(price)*100);
        object.put("status", 2);
        object.put("transactionType", 2);
        object.put("currency_code", "EUR");
        object.put("charity", ParseObject.createWithoutData("Charity", charityId));

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    final Number num = sharepoints_user_temp - Integer.parseInt(price);
                    UpdateUserSharepoints(num,charityName);
                    frame_donate_validate.removeView(view_donate_validate);
                    ShowSuccessDonation();
                } else {
                    Log.d("Transaction", "ex" + e.getMessage());
                }
            }
        });
    }

    public void UpdateUserSharepoints(final Number sharepoints, final String name) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {

                    gameScore.put("sharepoints", sharepoints);
                    int currentsharepoints = gameScore.getInt("sharepoints");
                    sharepoints_user_temp = currentsharepoints;
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                UpdateCharitySharepoints();
                                points.setText(String.valueOf(sharepoints_user_donate) + " : " + String.valueOf(sharepoints_user_temp));
                            } else {
                                Log.d("okok", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    public void UpdateCharitySharepoints() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Charity");
        query.getInBackground(CharityId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {

                    int currentsharepoints = gameScore.getInt("sharepoints");
                    final Number SP = currentsharepoints + sharepoints_user_donate;
                    gameScore.put("sharepoints", SP);
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Log.d("UpdateCharitySharepoint", "success");
                            } else {
                                Log.d("okok", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }

    boolean isextand= false;

    public void ShowDonateView() {
        view_donate_validate = vinflater.inflate(R.layout.view_expand_donate, frame_donate_validate, false);
        frame_donate_validate.addView(view_donate_validate);
        TextView valider = (TextView) view_donate_validate.findViewById(R.id.valider);
        TextView annuler = (TextView) view_donate_validate.findViewById(R.id.annuler);
        Utils.expand(view_donate_validate);
        isextand = true;

        sharepoints_moins.setVisibility(View.VISIBLE);
        sharepoints_plus.setVisibility(View.VISIBLE);
        recycler_charity.setVisibility(View.VISIBLE);
        charity_description.setVisibility(View.VISIBLE);
        dons_view.setVisibility(View.VISIBLE);

        if (Utils.isConnected(getApplicationContext())) {
            get_Charity();

        } else {
        }


        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recycler_position >= 0) {
                    if (CharityId != null) {
                        if (sharepoints_user_donate > 0) {
                            CreateTransaction(CharityName, CharityId, String.valueOf(sharepoints_user_donate));
                        } else {
                            Toast.makeText(getActivity(), "Veuillez envoyer une valeur supérieur à 0", Toast.LENGTH_LONG).show();
                        }
                    } else {
                    }
                } else {
                    Toast.makeText(getActivity(), "Veuillez séléctionner une charité", Toast.LENGTH_LONG).show();
                }
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_dons.clear();
                sharepoints_user_donate=0;
                recycler_position=-1;
                sharepoints_moins.setVisibility(View.INVISIBLE);
                sharepoints_plus.setVisibility(View.INVISIBLE);
                dons_view.setVisibility(View.INVISIBLE);
                do_donationTV.setText("faire un don");
                points.setText(String.valueOf(sharepoints_user_temp));
                donation=false;
                Utils.collapse(view_donate_validate);
                frame_donate_validate.removeView(view_donate_validate);
                isextand = false;
            }
        });
    }

    public void ShowSuccessDonation() {
        dons_view.setVisibility(View.INVISIBLE);
        view_success_donate = vinflater.inflate(R.layout.view_success_donate, frame_donate_validate, false);
        frame_donate_validate.addView(view_success_donate);
        TextView valider = (TextView) view_success_donate.findViewById(R.id.valider);
        TextView partager = (TextView) view_success_donate.findViewById(R.id.partager);
        Utils.expand(view_success_donate);

        success_donate_tobusiness = (TextView) view_success_donate.findViewById(R.id.success_donate_tobusiness);
        success_donate_sharepoint = (TextView) view_success_donate.findViewById(R.id.success_donate_sharepoint);
        success_donate_sharepoint.setText(String.valueOf(sharepoints_user_donate)+" SHAREPOINTS "+"("+String.valueOf(sharepoints_user_donate)+"€)");
        success_donate_tobusiness.setText("à "+CharityName);
        if (Utils.isConnected(getApplicationContext())) {

        } else {
        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_dons.clear();
                sharepoints_user_donate=0;
                recycler_position=-1;
                sharepoints_moins.setVisibility(View.INVISIBLE);
                sharepoints_plus.setVisibility(View.INVISIBLE);
                dons_view.setVisibility(View.INVISIBLE);
                do_donationTV.setText("faire un don");
                points.setText(String.valueOf(sharepoints_user_temp));
                donation=false;
                Utils.collapse(view_success_donate);
                frame_donate_validate.removeView(view_success_donate);
                isextand = false;
            }
        });

        partager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void onItemClick(int item, CharityDons bo) {
        recycler_position = item;
        charity_description.setText(bo.get_descipriton());
        CharityName = bo.get_nom();
        CharityId = bo.getObjectid();
    }

    @Override
    public void TaskOnNotification(String business, String sharepoints) {
     /*   Popup_onNotification onNotification=new Popup_onNotification();
        if (dummyanchor!=null){
            onNotification.displayPopupWindow(dummyanchor,getActivity(),"","");
        }*/
        getTransaction();
    }


    public void UpdateUserLocation(final double latitude, final double longitude) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject gameScore, ParseException e) {
                if (e == null) {
                  ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
                    gameScore.put("geoloc", point);
                    gameScore.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                            } else {
                                Log.d("okok", e.getMessage());
                            }
                        }
                    });
                }
            }
        });
    }
}