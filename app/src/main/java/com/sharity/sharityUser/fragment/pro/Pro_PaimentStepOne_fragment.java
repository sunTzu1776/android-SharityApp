package com.sharity.sharityUser.fragment.pro;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.CustomGrid;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.List;

import static com.sharity.sharityUser.R.id.swipeContainer;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_PaimentStepOne_fragment extends Fragment implements Updateable, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private double myBusiness_latitude=0.0;
    private double myBusiness_longitude=0.0;
    private UserLocation user;
    private ParseGeoPoint geoPoint;
    private String objectiD;
    private CustomGrid adapter;
    private DatabaseHandler db;
    private List<UserLocation> locationUser=new ArrayList<>();
    private TextView paiment_classique;
    private View inflate;
    private SwipeRefreshLayout swipeContainer;
    GridView grid;
    private OnChildPaymentSelection onSelection;


    public interface OnChildPaymentSelection{
        public void OnSelectGrid(UserLocation user, int i);
        public void Classique();

    }

    public static Pro_PaimentStepOne_fragment newInstance() {
        Pro_PaimentStepOne_fragment myFragment = new Pro_PaimentStepOne_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paimentstepone_pro, container, false);
        paiment_classique=(TextView)inflate.findViewById(R.id.paiment_classique);
        swipeContainer = (SwipeRefreshLayout) inflate.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(this);
        paiment_classique.setOnClickListener(this);
        db = new DatabaseHandler(getActivity());

        grid=(GridView)inflate.findViewById(R.id.grid);
        adapter = new CustomGrid(getActivity(),locationUser);
        grid.setAdapter(adapter);
        locationUser.clear();

        if (Utils.isConnected(getActivity())){
            GetClients();
        }else {
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
        }

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final UserLocation User = (UserLocation) grid.getAdapter().getItem(position);
                onSelection.OnSelectGrid(User, position);
            }
        });

        return inflate;
    }

    @Override
    public void update() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.paiment_classique:
                onSelection.Classique();
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


        // check if parent Fragment implements listener
        if (getParentFragment() instanceof OnChildPaymentSelection) {
            onSelection = (OnChildPaymentSelection) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSelection = null;
    }



    private void GetClients() {
        if (db.getBusinessCount() > 0) {
            objectiD = db.getBusinessId();
            Business business = db.getBusiness(objectiD);
            myBusiness_latitude = Double.valueOf(business.getLatitude());
            myBusiness_longitude = Double.valueOf(business.get_longitude());
            geoPoint = new ParseGeoPoint(Double.valueOf(business.getLatitude()), Double.valueOf(business.get_longitude()));

            final ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("type", 0);
            query.whereWithinKilometers("geoloc", geoPoint, 0.70);

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        locationUser.clear();
                        double queryLatitude = 0.0;
                        double queryLongitude = 0.0;
                        byte[] image = new byte[0];
                        // Iterating over the results
                        for (int i = 0; i < objects.size(); i++) {
                            String userid = objects.get(i).getObjectId();

                            ParseFile parseFile = objects.get(i).getParseFile("picture");
                            if (parseFile != null) {
                                try {
                                    image = parseFile.getData();
                                } catch (ParseException e1) {
                                    e1.printStackTrace();
                                }
                            }
                            ParseGeoPoint geoPoint = objects.get(i).getParseGeoPoint("geoloc");
                            if (geoPoint != null) {
                                queryLatitude = geoPoint.getLatitude();
                                queryLongitude = geoPoint.getLongitude();
                                String username = objects.get(i).getString("username");
                                String token = objects.get(i).getString("fcm_device_id");

                                locationUser.add(new UserLocation(userid, queryLatitude, queryLongitude, username, image, token));
                            }
                        }
                        swipeContainer.setRefreshing(false);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public void onRefresh() {
        if (Utils.isConnected(getActivity())){
            swipeContainer.setRefreshing(true);
            GetClients();
        }else {
            swipeContainer.setRefreshing(false);
            Toast.makeText(getActivity(),"Veuillez activer votre wifi ou réseau",Toast.LENGTH_LONG).show();
        }
    }

}