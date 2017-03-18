package com.sharity.sharityUser.fragment.pro;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
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

import static com.sharity.sharityUser.R.id.button;
import static com.sharity.sharityUser.R.id.prix;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_History_fragment extends Fragment implements Updateable, View.OnClickListener {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    Button buttonmap;

    ArrayList<History> payment_value=new ArrayList<>();
    TextView payment;
    TextView dons;
    ListView listView;
    private String indice;
    private AdapterHistory customAdapter;

    View inflate;
        public static Pro_History_fragment newInstance() {
        Pro_History_fragment myFragment = new Pro_History_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_history_pro, container, false);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        buttonmap=(Button)inflate.findViewById(R.id.buttonmap);
        listView=(ListView)inflate.findViewById(R.id.ListView);
        payment=(TextView)inflate.findViewById(R.id.payment);
        dons=(TextView)inflate.findViewById(R.id.dons);
        payment.setOnClickListener(this);
        dons.setOnClickListener(this);
        buttonmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MapActivity.class));
            }
        });


        if (this.isAdded()) {
            payment.setTextColor(getResources().getColor(R.color.green));
            indice="payements";
            payment_value.clear();
            customAdapter = new AdapterHistory(getActivity(), payment_value);
            listView .setAdapter(customAdapter);
            if (Utils.isConnected(getContext())){
                get_client_Historic();
            }
        }

        return inflate;

    }
    private void get_client_Historic() {
        try {
            ParseQuery<ParseObject> innerQuery = ParseQuery.getQuery("_User");
            innerQuery.whereEqualTo("objectId", ProfilActivity.parseUser.getObjectId());
            ParseQuery<ParseObject> query3 = ParseQuery.getQuery("Transaction");
            query3.whereMatchesQuery("customer", innerQuery);
            query3.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList!=null){

                        if (indice.equals("payements")){
                            payment_value.add(new History("", "", "", "",0));
                        }
                        else {
                            payment_value.add(0,new History("", "", "", "",2));
                        }


                        for (ParseObject object : commentList){
                            String prix = String.valueOf(object.getInt("value"));
                            String business = String.valueOf(object.getString("recipientName"));
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
                                    payment_value.add(new History(id, business, newDate, prix,1));
                                }
                            }

                            if (transactionType==2){
                                if (indice.equals("dons")){
                                    payment_value.add(new History(id, business, newDate, prix,1));
                                }
                            }
                        }

                        customAdapter.notifyDataSetChanged();
                        // payment_value.add(new History("", "", "", "",3));
                    }
                }
            });
        }
        catch (NullPointerException f){

        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.payment:
                indice="payements";
                payment.setTextColor(getResources().getColor(R.color.green));
                dons.setTextColor(getResources().getColor(R.color.black));
                payment_value.clear();
                get_client_Historic();
                break;
            case R.id.dons:
                indice="dons";
                payment.setTextColor(getResources().getColor(R.color.black));
                dons.setTextColor(getResources().getColor(R.color.green));
                payment_value.clear();
                get_client_Historic();
                break;
        }
    }


    @Override
    public void update() {

    }
}