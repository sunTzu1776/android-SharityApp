package com.sharity.sharityUser.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Profile;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.BusinessTransaction;
import com.sharity.sharityUser.BO.CISSTransaction;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.BO.History;
import com.sharity.sharityUser.BO.User;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterDrawer;
import com.sharity.sharityUser.Utils.AdapterNews;
import com.sharity.sharityUser.Utils.LocationUser;
import com.sharity.sharityUser.Utils.PermissionRuntime;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.client.client_Container_Mission_fragment;
import com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment;
import com.sharity.sharityUser.fragment.client.client_Partenaire_list_fragment;
import com.sharity.sharityUser.fragment.client.client_Profil_fragment;
import com.sharity.sharityUser.fragment.client.client_PartenaireMap_fragment;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Profil_Container_fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.parse.SubscriptionHandling.Event.UPDATE;
import static com.sharity.sharityUser.Application.parseLiveQueryClient;
import static com.sharity.sharityUser.Application.subscriptionHandling;
import static com.sharity.sharityUser.BO.CISSTransaction.transactionType;
import static com.sharity.sharityUser.R.id.coordinatorLayout;
import static com.sharity.sharityUser.R.id.date;
import static com.sharity.sharityUser.R.id.latitude;
import static com.sharity.sharityUser.R.id.longitude;
import static com.sharity.sharityUser.R.id.swipeContainer;
import static com.sharity.sharityUser.R.id.tab_option;
import static com.sharity.sharityUser.R.id.user;
import static com.sharity.sharityUser.activity.LoginActivity.callbackManager;
import static com.sharity.sharityUser.activity.ProfilActivity.TOTAL_PAGES;
import static com.sharity.sharityUser.activity.ProfilProActivity.db;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilActivity extends AppCompatActivity implements OnTabSelectListener {

    public static DatabaseHandler db;
    private IntentFilter mIntent;
    static int TOTAL_PAGES = 4;
    public ViewPager pager;
    private LocationManager manager;
    private Toolbar toolbar;
    private BottomBar bottomBar;
    public TextView toolbarTitle;
    private boolean start = true;
    public static ProfilActivity clientProfilActivity;
    public static ParseUser parseUser;
    private DrawerLayout drawer_layout;
    private ListView myDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ArrayList<Drawer> drawersItems = new ArrayList<Drawer>();
    private AdapterDrawer adapter;
    public OnNotificationUpdateProfil onNotificationUpdateProfil;
    public OnNotificationUpdateHistoric onNotificationUpdateHistoric;
    MyPagerAdapter mViewPagerAdapter;
    public static LocationUser locationUser=null;
    public static PermissionRuntime permissionRuntime;
    private  CoordinatorLayout coordinatorLayout;
    public static boolean isShop = true;


    public interface OnNotificationUpdateHistoric {
        void TaskOnNotification(String business, String sharepoints);
    }

    public interface OnNotificationUpdateProfil {
        void TaskOnNotification(String business, String sharepoints);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (actionBarDrawerToggle != null) {
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            parseUser = ParseUser.getCurrentUser();
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            permissionRuntime = new PermissionRuntime(ProfilActivity.this);


            clientProfilActivity = this;
            db = new DatabaseHandler(this);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            /*
            * Get local database to display nav drawer including Profil picture etc
            **/
            DatabaseHandler db = new DatabaseHandler(ProfilActivity.this);
                String objectId = getUserObjectId(ProfilActivity.this);
                User user = db.getUser(objectId);
                byte[] image = user.getPictureprofil();
                Bitmap PictureProfile = BitmapFactory.decodeByteArray(image, 0, image.length);
                String name= user.get_name();

            drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawersItems.add(0, new Drawer(null, "SHARITY", -1));
            drawersItems.add(1, new Drawer(PictureProfile, name, 0));
            drawersItems.add(2, new Drawer(null, "CGU", 1));
            drawersItems.add(3, new Drawer(null, "Contacts", 1));
            drawersItems.add(4, new Drawer(null, "Noter l'application", 1));
            drawersItems.add(5, new Drawer(null, "Déconnexion", 1));

            myDrawer = (ListView) findViewById(R.id.my_drawer);
            adapter = new AdapterDrawer(ProfilActivity.this, drawersItems);
            coordinatorLayout=(CoordinatorLayout) findViewById(R.id.coordinatorLayout);
            myDrawer.setAdapter(adapter);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(3);
            mViewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(2, true);
            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            actionBarDrawerToggle = new ActionBarDrawerToggle(ProfilActivity.this, drawer_layout,
                    toolbar, R.string.open, R.string.close) {
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                }
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
            drawer_layout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.green));
            actionBarDrawerToggle.syncState();

            //Navigation draw item Click
            myDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            break;
                        case 1:

                            break;
                        case 2:
                            break;
                        case 3:

                            break;
                        case 4:

                            break;

                        case 5:
                            //Disconnection
                            parseUser.logOut();
                            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                    }
                }
            });

            /*
            * Parse Live Query permit to listen live Update on CISSTransaction.
            * if a payment transaction is approved, it inform the user
            * */

            final ParseQuery<CISSTransaction> parseQuery = ParseQuery.getQuery(CISSTransaction.class);
            subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE, new
                    SubscriptionHandling.HandleEventCallback<CISSTransaction>() {
                        @Override
                        public void onEvent(ParseQuery<CISSTransaction> query, CISSTransaction object) {
                            if (object.getBoolean("processed") == true) {
                                if (object.getString("customer").equalsIgnoreCase(parseUser.getObjectId())) {
                                        boolean isApproved= object.getBoolean("approved");
                                    Intent intent = new Intent("custom-event-name");
                                    intent.putExtra("approved", isApproved);
                                    intent.putExtra("sharepoints", "");
                                    intent.putExtra("business", "");
                                    LocalBroadcastManager.getInstance(ProfilActivity.this).sendBroadcast(intent);
                                    final int amount = object.getInt("amount");

                                    final String text;
                                    if (isApproved){
                                        text="Votre paiment d'un montant de " + amount / 100 + "€ à bien été validé";
                                    }else {
                                        text="Votre paiment d'un montant de " + amount / 100 + "€ à été refusé";

                                    }
                                    if (!ProfilActivity.this.isFinishing()) {
                                        ProfilActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Utils.showDialog3(ProfilActivity.this, text, "Paiement", true, new Utils.Click() {
                                                    @Override
                                                    public void Ok() {
                                                    }

                                                    @Override
                                                    public void Cancel() {

                                                    }
                                                });
                                            }
                                        });
                                    }
                                    Log.d("AEOEO", object.getString("transactionId"));
                                }

                            }
                        }
                    });
        }

    }


    public ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager.setBackgroundColor(getResources().getColor(R.color.white));
            if (start) {
                mViewPagerAdapter.notifyDataSetChanged();
                start = false;
            }

            if (position == 2) {
            }
        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            bottomBar.setInActiveTabColor(getResources().getColor(R.color.white));
            bottomBar.setActiveTabColor(getResources().getColor(R.color.green));
            if (position == 0) {
                toolbarTitle.setText("HISTORIQUE");
            } else if (position == 1) {
                toolbarTitle.setText("PROFIL");
              final client_Profil_fragment clientProfilFragment=(client_Profil_fragment)mViewPagerAdapter.getRegisteredFragment(position);
               if (clientProfilFragment!=null){
                   if (clientProfilFragment.isAdded()){
                       //Permit to call the update function in fragment to reanimate circleprogress
                       new Handler().postDelayed(new Runnable() {
                           @Override
                           public void run() {
                               clientProfilFragment.update();

                           }
                            }, 500);
                   }
               }
            } else if (position == 2) {
                toolbarTitle.setText("PARTENAIRE");
            } else if (position == 3) {
                toolbarTitle.setText("MISSION");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        // Handle action buttons
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private void setIndicator(int index) {
        bottomBar.selectTabAtPosition(index);
    }

    private void buildTabs() {
        bottomBar.setOnTabSelectListener(this, true);
        setIndicator(2);
        bottomBar.setInActiveTabColor(getResources().getColor(R.color.white));
        bottomBar.setActiveTabColor(getResources().getColor(R.color.green));
    }

    //Bottom Bar onCLick
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case tab_option:
                pager.setCurrentItem(0, true);
                // updateProfil();
                break;
            case R.id.tab_profil:
                pager.setCurrentItem(1, true);
                //updateProfil();
                break;
            case R.id.tab_partenaire:
                pager.setCurrentItem(2, true);
                // updateProfil();
                break;
            case R.id.tab_mission:
                pager.setCurrentItem(3, true);
                // updateProfil();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   locationUser.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       if (pager.getCurrentItem() == 2) {
                           client_Container_Partenaire_fragment clientProfilFragment = (client_Container_Partenaire_fragment) mViewPagerAdapter.getRegisteredFragment(pager.getCurrentItem());
                           clientProfilFragment.buildGoogleApiClient();
                       }
                        Log.d("Network granted", "passed");

                }else {
                    // permission was not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfilActivity.this, permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION)) {
                        Utils.showDialogPermission(ProfilActivity.this,"L'application Sharity recquiert votre localisation pour le service de paiment et d'achat à proximité","Permission",false, new Utils.Click() {
                            @Override
                            public void Ok() {
                                ActivityCompat.requestPermissions(ProfilActivity.this,
                                        new String[]{permissionRuntime.MY_PERMISSIONS_ACCESS_FINE_LOCATION},
                                        permissionRuntime.Code_ACCESS_FINE_LOCATION);
                            }

                            @Override
                            public void Cancel() {

                            }
                        });
                    } else {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Autoriser l'accès à la localisation", Snackbar.LENGTH_LONG);
                        snackbar.setAction("PARAMETRES", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ProfilActivity.this == null) {
                                    return;
                                }
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", ProfilActivity.this.getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        });
                        snackbar.show();
                    }
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public static ProfilActivity getInstance() {
        return clientProfilActivity;
    }

    //Notification handler
    public void setProfilListener(ProfilActivity.OnNotificationUpdateProfil activityListener) {
        this.onNotificationUpdateProfil = activityListener;
    }

    public void setHistoricListener(ProfilActivity.OnNotificationUpdateHistoric activityListener) {
        this.onNotificationUpdateHistoric = activityListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    @Override
    public void onPause() {
        if (mIntent != null) {
            this.unregisterReceiver(mMessageReceiver);
            mIntent = null;
        }
        super.onPause();
    }

    //Broadcast which trigger Task in profil when Live Query update

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean approved = intent.getBooleanExtra("approved",false);
            String sharepoints = intent.getStringExtra("sharepoints");
            String business = intent.getStringExtra("business");
            int item = pager.getCurrentItem();
            if (item!=1){
                pager.setCurrentItem(1);
                onNotificationUpdateProfil.TaskOnNotification(business, sharepoints);
            }else {
                onNotificationUpdateProfil.TaskOnNotification(business, sharepoints);
            }
        }
    };


    protected class MyPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        // Return the Fragment associated with a specified position.
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return History_container_fragment.newInstance();
            } else if (position == 1) {
                return client_Profil_fragment.newInstance();
            } else if (position == 2) {
                return client_Container_Partenaire_fragment.newInstance();
            } else if (position == 3) {
                return client_Container_Mission_fragment.newInstance();
            }

            return null;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        };
    }

    private String getUserObjectId(Context context) {
        SharedPreferences pref = context.getSharedPreferences("Pref", context.MODE_PRIVATE);
        final String accountDisconnect = pref.getString("User_ObjectId", "");         // getting String
        return accountDisconnect;
    }

    @Override
    public void onBackPressed() {
        SparseArray<FragmentManager> managers = new SparseArray<>();
        traverseManagers(getSupportFragmentManager(), managers, 0);
        if (managers.size() > 0) {
            managers.valueAt(managers.size() - 1).popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    private void traverseManagers(FragmentManager manager, SparseArray<FragmentManager> managers, int intent) {
        if (manager.getBackStackEntryCount() > 0) {
            managers.put(intent, manager);
        }
        if (manager.getFragments() == null) {
            return;
        }
        for (Fragment fragment : manager.getFragments()) {
            if (fragment != null)
                traverseManagers(fragment.getChildFragmentManager(), managers, intent + 1);
        }
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }
}





