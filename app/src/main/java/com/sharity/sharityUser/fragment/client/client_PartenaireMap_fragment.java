package com.sharity.sharityUser.fragment.client;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.ui.IconGenerator;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.LocationBusiness;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterGridViewCategorie;
import com.sharity.sharityUser.Utils.AdapterPartenaireClient;
import com.sharity.sharityUser.Utils.AdapterPartenaireMapClient;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.MapCallback;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import okhttp3.internal.Util;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.R.id.userlist;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.frameCategorie;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.geoPoint;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.getList_categorie;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.gridViewCategorie;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.images;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.isLocationUpdate;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.latitude;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.list_categorieReal;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.list_shop;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.list_shop_filtered;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.longitude;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mGoogleApiClient;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mMap;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.mViewcategorieColapse;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.on;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.recyclerFrame;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.search_layout;
import static com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment.vinflater;


/**
 * Created by Moi on 14/11/15.
 */
public class client_PartenaireMap_fragment extends Fragment implements
        OnMapReadyCallback, Updateable, AdapterPartenaireMapClient.OnItemDonateClickListener, GoogleMap.OnMarkerClickListener, AdapterGridViewCategorie.OnItemGridCategorieClickListener {

    private  RecyclerView recyclerview;
    private View inflate;
    private MapView mapView;
    private PermissionRuntime permissionRuntime;
    private LinearLayoutManager layoutManager;
    private AdapterPartenaireMapClient adapter2;
    private AdapterPartenaireMapClient.OnItemDonateClickListener onItemDonateClickListener;
    private AdapterGridViewCategorie.OnItemGridCategorieClickListener onItemGridCategorieClickListener;

    private byte[] imageByte = null;
    private ImageView close;
    private MapCallback onCloseMap;
    private boolean isShop;
    private Button shop_BT;
    private Button promo_BT;
    private  GridView gridview;
    private boolean issearch= true;

    public static client_PartenaireMap_fragment newInstance(ArrayList<LocationBusiness> data, boolean type) {
        client_PartenaireMap_fragment myFragment = new client_PartenaireMap_fragment();
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        args.putBoolean("type", type);
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vinflater=inflater;
        inflate = inflater.inflate(R.layout.fragment_partenaire_mapclient, container, false);

        mapView = (MapView) inflate.findViewById(R.id.map);
        recyclerview = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        close = (ImageView) inflate.findViewById(R.id.close);
        recyclerFrame = (FrameLayout) inflate.findViewById(R.id.recyclerFrame);
        search_layout = (LinearLayout) inflate.findViewById(R.id.search_layout);
        shop_BT = (Button) inflate.findViewById(R.id.shop_BT);
        promo_BT = (Button) inflate.findViewById(R.id.promo_BT);
        close.setOnClickListener(closeMapListener);
        search_layout.setOnClickListener(categorie);
        promo_BT.setOnClickListener(categorie);
        shop_BT.setOnClickListener(categorie);
        onItemDonateClickListener = this;
        onItemGridCategorieClickListener=this;


        mapView.onCreate(null);
        mapView.onResume();

        isShop = getArguments().getBoolean("type");

        list_shop = (ArrayList<LocationBusiness>) getArguments().getSerializable("data");


        Initalize_RecyclerView();
        if (mGoogleApiClient != null) {
            permissionRuntime = new PermissionRuntime(getActivity());
            if (ContextCompat.checkSelfPermission(getActivity(),
                    permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mapView.getMapAsync(this);
            } else {
                Log.d("PermissionRuTime","client_PartenaireMap_fragment");
                permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
            }
        }


        final int width = (int) (Utils.getScreenWidth(getActivity()) / 2);
            recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int diff=0;
                    TextView latitude=null;
                    TextView longitude=null;
                    View view1 = (View) recyclerView.getChildAt(0);
                    if (view1!=null) {
                         latitude = (TextView) view1.findViewById(R.id.latitude);
                         longitude = (TextView) view1.findViewById(R.id.longitude);
                        View itemcolor = (View) view1.findViewById(R.id.itembarcolor);
                        itemcolor.setBackgroundColor(getResources().getColor(R.color.green));
                        view1.getLeft();
                        diff = (view1.getLeft() - (recyclerView.getLeft()));
                    }
                    View view2 = (View) recyclerView.getChildAt(1);
                    if (view2!=null){
                    View itemcolor2 = (View) view2.findViewById(R.id.itembarcolor);
                    itemcolor2.setBackgroundColor(getResources().getColor(R.color.transparent));
                    }



                    if (diff >= -width && diff <= 0) {
                        if (mMap != null && latitude!=null) {
                            try {
                                LatLng latLng = new LatLng(Double.valueOf(latitude.getText().toString()), Double.valueOf(longitude.getText().toString()));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                            } catch (NumberFormatException e) {

                            }
                        }
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }

            });



        if (mapView!=null) {
            mapView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }

            });
        }
        return inflate;
    }

    @Override
    public void update() {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.isAdded()) {
            try {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.setOnMarkerClickListener(this);
                //Initialize Google Play Services
                permissionRuntime = new PermissionRuntime(getActivity());
                if (ContextCompat.checkSelfPermission(getActivity(),
                        permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    permissionRuntime.Askpermission(permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION, permissionRuntime.Code_ACCESS_FINE_LOCATION);
                }

                if (latitude!=0.0){
                    Display_icon_Map();
                }

                // Add a marker in Delhi and move the camera
                // GetBusiness();
            } catch (NullPointerException e) {

            }
        }


        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(delhi));
    }





    //Recycler
    ///////
    public void Initalize_RecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(layoutManager);
        if (isShop){
            adapter2 = new AdapterPartenaireMapClient("", true, getActivity(), list_shop, onItemDonateClickListener);
            recyclerview.setAdapter(adapter2);
        }else {
            adapter2 = new AdapterPartenaireMapClient("", false, getActivity(), list_shop, onItemDonateClickListener);
            recyclerview.setAdapter(adapter2);
        }
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof MapCallback) {
            onCloseMap = (MapCallback) getParentFragment();
        } else {
            throw new RuntimeException("The parent fragment must implement OnSelection");
        }
    }

    //Callback ItemClick RecyclerView
    @Override
    public void onItemClick(int item, Object bo) {

    }




    View.OnClickListener closeMapListener =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.close:
                    onCloseMap.onClose();
                    break;
            }
        }
    };

    View.OnClickListener categorie =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.search_layout:
                    //Show  "Categorie" expand collapse //
                    if (!isShop) {
                        if (issearch) {
                            frameCategorie = (RelativeLayout) inflate.findViewById(R.id.frame_expand);
                            mViewcategorieColapse = vinflater.inflate(R.layout.layout_editingsequence, frameCategorie, false);
                            frameCategorie.addView(mViewcategorieColapse);
                            gridview = (GridView) mViewcategorieColapse.findViewById(R.id.customgrid);
                            gridViewCategorie=new AdapterGridViewCategorie(getActivity(), list_categorieReal, onItemGridCategorieClickListener);

                            if (getList_categorie().isEmpty()){
                                ((client_Container_Partenaire_fragment)getParentFragment()).get_Categorie(new client_Container_Partenaire_fragment.DataCallBack() {
                                    @Override
                                    public void onSuccess() {
                                        gridViewCategorie.notifyDataSetChanged();
                                    }
                                });
                            }else {
                                gridview.setAdapter(gridViewCategorie);
                            }


                            Utils.expand(mViewcategorieColapse);
                            issearch = false;
                        } else {
                            //  frameCategorie.removeView(mViewcategorieColapse);
                            Utils.collapse(mViewcategorieColapse);
                            issearch = true;
                        }
                    }else {
                        Toast.makeText(getActivity(),"Recherche uniquement pour les promotions",Toast.LENGTH_LONG).show();
                    }
                    break;
                case R.id.shop_BT:
                    isShop=true;
                    if (!isLocationUpdate()){
                        ((client_Container_Partenaire_fragment) getParentFragment()).startLocationUpdates();
                    }
                    ShowShop();
                    break;
                case R.id.promo_BT:
                    isShop=false;
                    if (!isLocationUpdate()){
                        ((client_Container_Partenaire_fragment) getParentFragment()).startLocationUpdates();
                    }
                    ShowSPromotion();
                    break;
            }
        }
    };

    protected void Display_icon_Map() {
        if (list_shop.size()>0 || list_shop.size()>0) {
                    setIconMap();
        }

        if (mMap != null && latitude != 0.0) {
            LatLng latLng = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int position=-1;
        int count=0;

      /*  IconGenerator iconFactory = new IconGenerator(getActivity());
        iconFactory.setColor(getResources().getColor(R.color.green));
        addIcon(iconFactory, marker.getTitle(), marker.getPosition());*/

        if (isShop && list_shop.size()>=1) {
            count=list_shop.size();
            for (Object busi : list_shop) {
                position++;
                if (((LocationBusiness) busi).get_businessName().equals(marker.getTitle())) {
                    break;
                }
            }
            recyclerview.getLayoutManager().scrollToPosition(position);
            // layoutManager.scrollToPositionWithOffset(position, 1);
        }
        return false;
    }
    private void setIconMap(){
        if (mMap!=null){
            mMap.clear();

        }
        if (isShop){
            for (Object business : list_shop) {
                if (Utils.distance(((LocationBusiness) business).get_latitude(), ((LocationBusiness) business).get_longitude(), latitude, longitude) <= 500000) {
                    LatLng delhi = new LatLng(((LocationBusiness) business).get_latitude(), ((LocationBusiness) business).get_longitude());
                    MarkerOptions markeroptions = new MarkerOptions();
                    markeroptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_logo));
                    markeroptions.position(delhi);
                    markeroptions.title(((LocationBusiness) business).get_businessName());
                    if (mMap!=null){
                        mMap.addMarker(markeroptions).showInfoWindow();
                    }
                } else {
                }
            }

        }else {
            for (Object business : list_shop) {
                    LatLng delhi = new LatLng(((LocationBusiness) business).get_latitude(), ((LocationBusiness) business).get_longitude());
                    IconGenerator iconFactory = new IconGenerator(getActivity());
                    addIcon(iconFactory, ((LocationBusiness) business).getReduction(), delhi);

            }
        }
    }

    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */


    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().

                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(String.valueOf(text)))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        mMap.addMarker(markerOptions);
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }



    public void ShowShop(){
        adapter2 = new AdapterPartenaireMapClient("", true, getActivity(), list_shop, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
        ((client_Container_Partenaire_fragment) getParentFragment()).GetBusiness(new client_Container_Partenaire_fragment.DataCallBack() {
            @Override
            public void onSuccess() {
             //   HideNetworkView();
                adapter2.notifyDataSetChanged();
                Display_icon_Map();
            }
        });
        isShop = true;
    }

    public void ShowSPromotion(){
        adapter2 = new AdapterPartenaireMapClient("", false, getActivity(), list_shop, onItemDonateClickListener);
        recyclerview.setAdapter(adapter2);
        ((client_Container_Partenaire_fragment) getParentFragment()).get_Promo(new client_Container_Partenaire_fragment.DataCallBack() {
            @Override
            public void onSuccess() {
             //   HideNetworkView();
                adapter2.notifyDataSetChanged();
                Display_icon_Map();
            }
        });

        isShop = false;
    }

    /**
     * GridView for Categories
     */


    private void DisplayItemFromCategorie(String selectedCategorie){
        list_shop.clear();
        if (isShop){
            adapter2 = new AdapterPartenaireMapClient("", true, getActivity(), list_shop, onItemDonateClickListener);
        }else {
            adapter2 = new AdapterPartenaireMapClient("", false, getActivity(), list_shop, onItemDonateClickListener);
        }
        Log.d("onItemCategorieClick",selectedCategorie);
        for (LocationBusiness object : list_shop_filtered){
            if(object.getCategorie().equals(selectedCategorie) && !object.isoffset()){
                Log.d("onItemCategolickpassed",selectedCategorie);
                list_shop.add(object);
            }
        }
            if (list_shop.size()>1) {
                LocationBusiness business = list_shop.get(list_shop.size() - 1);
                Double lat = business.get_latitude();
                Double lon = business.get_longitude();
                String description = business.getDescription();
                String prix = business.getPrix();
                String reduction = business.getReduction();
                String busines = business.get_businessName();
                String categorie = business.getCategorie();
                list_shop.add(new LocationBusiness(categorie,lat, lon, busines, 0 , description,prix,reduction, true));
            }


            recyclerview.setAdapter(adapter2);
            adapter2.notifyDataSetChanged();
            Display_icon_Map();
            layoutManager.scrollToPosition(0);

    }

    @Override
    public void onItemCategorieClick(int item, String categorie) {
        Log.d("onItemCategoriemap","onItemCategorieClick");
        if (isLocationUpdate()){
            ((client_Container_Partenaire_fragment) getParentFragment()).RemoveLocationUpdate();
        }
        DisplayItemFromCategorie(categorie);
        Utils.collapse(mViewcategorieColapse);
    }
}