package com.sharity.sharityUser.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterNews;
import com.sharity.sharityUser.fragment.client.client_Container_Mission_fragment;
import com.sharity.sharityUser.fragment.client.client_Container_Partenaire_fragment;
import com.sharity.sharityUser.fragment.client.client_Partenaire_list_fragment;
import com.sharity.sharityUser.fragment.client.client_Profil_fragment;
import com.sharity.sharityUser.fragment.client.client_PartenaireMap_fragment;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.latitude;
import static com.sharity.sharityUser.R.id.tab_option;
import static com.sharity.sharityUser.activity.ProfilActivity.TOTAL_PAGES;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilActivity extends AppCompatActivity implements OnTabSelectListener {

    public static DatabaseHandler db;
    private IntentFilter mIntent;
    static int TOTAL_PAGES = 4;
    private ViewPager pager;
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
    private AdapterNews adapter;
    public OnNotificationUpdateProfil onNotificationUpdateProfil;
    public OnNotificationUpdateHistoric onNotificationUpdateHistoric;
    MyPagerAdapter mViewPagerAdapter;

    public interface OnNotificationUpdateHistoric {
        void TaskOnNotification(String business, String sharepoints);
    }

    public interface OnNotificationUpdateProfil {
        void TaskOnNotification(String business, String sharepoints);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            clientProfilActivity = this;
            db = new DatabaseHandler(this);
            parseUser = ParseUser.getCurrentUser();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawersItems.add(0, new Drawer(R.drawable.logo, "", 0));
            drawersItems.add(1, new Drawer(R.drawable.logo, "CGU", 1));
            drawersItems.add(2, new Drawer(R.drawable.logo, "Contacts", 1));
            drawersItems.add(3, new Drawer(R.drawable.logo, "Noter l'application", 1));
            drawersItems.add(4, new Drawer(R.drawable.logo, "Déconnexion", 1));

            myDrawer = (ListView) findViewById(R.id.my_drawer);
            adapter = new AdapterNews(ProfilActivity.this, drawersItems);
            myDrawer.setAdapter(adapter);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(0);
            mViewPagerAdapter=new MyPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(1, true);
            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            //   pager.setPageTransformer(true, new CrossfadePageTransformer());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            actionBarDrawerToggle = new ActionBarDrawerToggle(ProfilActivity.this, drawer_layout,
                    toolbar, R.string.open, R.string.close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
            drawer_layout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.green));
            actionBarDrawerToggle.syncState();

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
                            //Disconnection
                            parseUser.logOut();
                            Intent intent = new Intent(ProfilActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
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
            } else if (position == 2) {
                toolbarTitle.setText("PARTENAIRE");
            }
            else if (position == 3) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void setIndicator(int index) {
        bottomBar.selectTabAtPosition(index);
    }

    private void buildTabs() {
        bottomBar.setOnTabSelectListener(this, true);
        setIndicator(1);
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.pager + ":" + pager.getCurrentItem());
                    if (pager.getCurrentItem() == 1 && page != null) {
                     //   ((client_Container_Partenaire_fragment)page);
                        Log.d("BIGGRAF", "passed");
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
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

    public void updateProfil() {
        ProfilActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                mViewPagerAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onNotificationReceived_Display() {
        if (bottomBar.getId() != R.id.tab_profil) {
            mPageChangeListener.onPageSelected(1);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (actionBarDrawerToggle!=null) {
            actionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (actionBarDrawerToggle!=null) {
            actionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            String sharepoints = intent.getStringExtra("sharepoints");
            String business = intent.getStringExtra("business");
            int item = pager.getCurrentItem();
            onNotificationUpdateHistoric.TaskOnNotification(business, sharepoints);
            onNotificationUpdateProfil.TaskOnNotification(business, sharepoints);
            //onNotificationReceived_Display();

            // ((ProfilActivity)getActivity()).onNotificationReceived_Display();
            // Popup_onNotification onNotification=new Popup_onNotification();
            // onNotification.displayPopupWindow(do_donationTV,getActivity(),business,sharepoints);
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
            }
            else if (position == 3) {
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
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        };
    };


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
            if (fragment != null) traverseManagers(fragment.getChildFragmentManager(), managers, intent + 1);
        }
    }
}





