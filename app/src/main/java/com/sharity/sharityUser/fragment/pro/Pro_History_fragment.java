package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterHistory;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.Updateable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static com.google.android.gms.analytics.internal.zzy.c;
import static com.google.android.gms.analytics.internal.zzy.p;
import static com.sharity.sharityUser.R.id.button;
import static com.sharity.sharityUser.R.id.prix;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.R.id.username;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;
import static okhttp3.Protocol.get;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_History_fragment extends Fragment implements Updateable, SwipeRefreshLayout.OnRefreshListener {

    ParseObject obj= null;
    String customer="";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private SwipeRefreshLayout swipeContainer;
    ArrayList<History> payment_value=new ArrayList<>();
    ListView listView;
    private String indice;
    private AdapterHistory customAdapter;

    View inflate;
        public static Pro_History_fragment newInstance(String indice) {
        Pro_History_fragment myFragment = new Pro_History_fragment();
        Bundle args = new Bundle();
            args.putString("indice",indice);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history_pro, container, false);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);

            indice= getArguments().getString("indice");
            payment_value.clear();
            customAdapter = new AdapterHistory(getActivity(), payment_value);
            listView .setAdapter(customAdapter);
            if (Utils.isConnected(getContext())){
                get_client_Historic();
            }
        return inflate;

    }
    private void get_client_Historic() {
        try {
            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("Business");
            innerQuery.whereEqualTo("objectId", db.getBusinessId());
            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Transaction");
            query3.whereMatchesQuery("business", innerQuery);
            query3.orderByDescending("createdAt");
            query3.include("_User");
            query3.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList!=null){
                        Log.d("history","passed");
                        if (indice.equals("payements")){
                            payment_value.add(new History("", "", "", "",0));
                        }
                        else {
                            payment_value.add(0,new History("", "", "", "",2));
                        }

                        ParseObject sale = ParseObject.create("_User");
                        for (ParseObject object : commentList){

                            //Get Pointer customer
                         /*   try {
                                sale = object.getParseObject("customer").fetchIfNeeded();
                                Log.d("post",sale.getString("username"));

                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }

                            String toto=sale.getString("username");
                            */
                            String recipientName=object.getString("recipientName");
                            String prix = String.valueOf(object.getInt("value"));
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
                                    payment_value.add(new History(id, recipientName, newDate, prix,1));
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
    public void update() {

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getContext())){
            swipeContainer.setRefreshing(true);
            payment_value.clear();
            customAdapter.notifyDataSetChanged();
            get_client_Historic();
        }
    }
}