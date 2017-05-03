package com.sharity.sharityUser.fragment.client;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterHistoricUser;
import com.sharity.sharityUser.Utils.AdapterHistory;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.pro.Passord_forgotten_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_History_fragment;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.StringTokenizer;

import static com.sharity.sharityUser.BO.CISSTransaction.transactionType;
import static com.sharity.sharityUser.R.id.active_network;
import static com.sharity.sharityUser.R.id.animation_nonetwork;
import static com.sharity.sharityUser.R.id.forgot_pass;
import static com.sharity.sharityUser.R.id.frame_nonetwork;
import static com.sharity.sharityUser.activity.ProfilActivity.parseUser;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mGoogleApiClient;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Historique_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private View inflate;
    ArrayList<History> payment_value=new ArrayList<>();
    TextView payment;
    private SwipeRefreshLayout swipeContainer;
    private Button active_network;
    private RelativeLayout frame_nonetwork;
    private LottieAnimationView animation_nonetwork;
    TextView dons;
    ListView listView;
    private String indice;
    private AdapterHistory customAdapter;
    private RelativeLayout recyclerscreen;


    public static client_Historique_fragment newInstance(String indice) {
        client_Historique_fragment myFragment = new client_Historique_fragment();
        Bundle args = new Bundle();
        args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.locationuserlist, container, false);

        listView=(ListView)inflate.findViewById(R.id.ListView);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        recyclerscreen= (RelativeLayout)inflate.findViewById(R.id.framebackground);
        frame_nonetwork = (RelativeLayout) inflate.findViewById(R.id.frame_nonetwork);
        active_network = (Button) inflate.findViewById(R.id.active_network);
        animation_nonetwork = (LottieAnimationView) inflate.findViewById(R.id.animation_nonetwork);

        if (this.isAdded()) {
            indice= getArguments().getString("indice");
            Log.d("indice",indice);
            payment_value.clear();
            customAdapter = new AdapterHistory(getActivity(), payment_value);
            listView .setAdapter(customAdapter);
            if (Utils.isConnected2(getActivity())){
                get_client_Historic();
            }else {
                ShowNetworkView();
            }
        }
        return inflate;

    }

    @Override
    public void update() {

    }

    private void get_client_Historic() {
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
            mainquery.orderByDescending("createdAt");
            mainquery.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList!=null){
                        payment_value.clear();
                        Log.d("m1stFragment","get_client_Historic");

                        if (indice.equals("payements")){
                            payment_value.add(new History("", "", "", "",0));
                        }
                        else {
                            payment_value.add(0,new History("", "", "", "",2));
                        }

                        for (ParseObject object : commentList){
                            String sharepoints;
                            String prix;
                            String recipientName;

                            sharepoints  = String.valueOf(object.getInt("sharepoints"));
                            prix  = String.valueOf(object.getInt("amount")/100);
                            recipientName = String.valueOf(object.getString("recipient_name"));

                            String business = String.valueOf(object.getString("sender_name"));
                            String id = String.valueOf(object.getString("objectId"));
                            int transactionType = (object.getInt("transactionType"));
                            Date date = (object.getCreatedAt());
                            String da = "";
                            String newDate="";
                            if (date!=null) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                                 da = df.format(date);
                                try {
                                    Date newDatee= df.parse(da);
                                    df=new SimpleDateFormat("dd-MM-yyyy / HH:mm");
                                    newDate= df.format(newDatee);
                                } catch (java.text.ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }

                            if (transactionType==1){
                                if (indice.equals("payements")){
                                    payment_value.add(new History(id, recipientName, newDate, prix,1));
                                }
                                }

                            if (transactionType==2){
                                if (indice.equals("dons")){
                                    payment_value.add(new History(id, recipientName, newDate, sharepoints,3));
                                }
                            }
                        }

                        customAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                        // payment_value.add(new History("", "", "", "",3));
                    }
                }
            });
        }
        catch (NullPointerException f){

        }
    }


    @Override
    public void onRefresh() {
        if (Utils.isConnected(getActivity())){
            Log.d("m1stFragment","onRefresh");
            swipeContainer.setRefreshing(true);
            payment_value.clear();
            customAdapter.notifyDataSetChanged();
            get_client_Historic();
            HideNetworkView();
        }else {
            swipeContainer.setRefreshing(false);
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
            ShowNetworkView();
        }}


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frame_nonetwork:
                break;
            case R.id.active_network:
                if (Utils.isConnected2(getActivity())){
                    get_client_Historic();
                    HideNetworkView();
                }
        }
    }

    public void ShowNetworkView(){
        frame_nonetwork.setVisibility(View.VISIBLE);
        active_network.setVisibility(View.VISIBLE);
        animation_nonetwork.setVisibility(View.VISIBLE);
        recyclerscreen.setVisibility(View.INVISIBLE);
        frame_nonetwork.setOnClickListener(this);
        active_network.setOnClickListener(this);
        animation_nonetwork.setAnimation("loading.json");
        animation_nonetwork.loop(true);
        animation_nonetwork.playAnimation();
    }

    public void HideNetworkView(){
        frame_nonetwork.setVisibility(View.INVISIBLE);
        active_network.setVisibility(View.INVISIBLE);
        animation_nonetwork.setVisibility(View.INVISIBLE);
        recyclerscreen.setVisibility(View.VISIBLE);
        active_network.setOnClickListener(this);
        frame_nonetwork.setOnClickListener(this);
        animation_nonetwork.loop(false);
        animation_nonetwork.cancelAnimation();
    }



}