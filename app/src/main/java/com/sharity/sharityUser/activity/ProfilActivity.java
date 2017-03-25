package com.sharity.sharityUser.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterNews;
import com.sharity.sharityUser.fragment.client.client_Profil_fragment;
import com.sharity.sharityUser.fragment.client.client_Historique_fragment;
import com.sharity.sharityUser.fragment.client.client_PartenaireMap_fragment;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.tab_option;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilActivity extends AppCompatActivity implements OnTabSelectListener {
    static int TOTAL_PAGES=3;
    private ViewPager pager;
    private LocationManager manager;
    boolean isOpaque = true;
    private Toolbar toolbar;
    private BottomBar bottomBar;
    private TextView toolbarTitle;
    boolean start =true;
    public static ParseUser parseUser;
    private DrawerLayout drawer_layout;
    private ListView myDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ArrayList<Drawer> drawersItems= new ArrayList<Drawer>();
    private AdapterNews adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            parseUser = ParseUser.getCurrentUser();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            drawer_layout=(DrawerLayout) findViewById(R.id.drawer_layout);
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
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(1, true);
            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            //   pager.setPageTransformer(true, new CrossfadePageTransformer());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            toolbar.setNavigationIcon(R.drawable.ic_drawer);

            actionBarDrawerToggle = new ActionBarDrawerToggle(ProfilActivity.this, drawer_layout,
                    toolbar, R.string.open, R.string.close) {

                /** Called when a drawer has settled in a completely closed state. */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);

                    // Do whatever you want here
                }

                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    // Do whatever you want here
                }
            };
// Set the drawer toggle as the DrawerListener
            drawer_layout.addDrawerListener(actionBarDrawerToggle);

            myDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
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

    private FragmentStatePagerAdapter mViewPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }

        // Return the Fragment associated with a specified position.
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return client_Historique_fragment.newInstance();
            } else if (position == 1) {
                return client_Profil_fragment.newInstance();
            } else if (position == 2) {
                return client_PartenaireMap_fragment.newInstance();
            }

            return null;
        }


        // Remove a page for the given position. The adapter is responsible for removing the view from its container.
        @Override
        public void destroyItem(android.view.ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        };

        @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof client_Historique_fragment) {
                ((client_Historique_fragment) object).update();
            }
            if (object instanceof client_Profil_fragment) {
                ((client_Profil_fragment) object).update();
            }

            if (object instanceof client_PartenaireMap_fragment) {
                ((client_PartenaireMap_fragment) object).update();
            }

            return super.getItemPosition(object);
        };
    };



    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pager.setBackgroundColor(getResources().getColor(R.color.white));

            if (start){
                mViewPagerAdapter.notifyDataSetChanged();
                start=false;
            }

            if (position==2){
            }
        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            bottomBar.setInActiveTabColor(getResources().getColor(R.color.black));
            bottomBar.setActiveTabColor(getResources().getColor(R.color.green));
            if (position==0){
                toolbarTitle.setText("HISTORIQUE");
            }else if (position==1){
                toolbarTitle.setText("PROFIL");
            }else if (position==2){
                toolbarTitle.setText("PARTENAIRE");

              /*  try{
                    if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER) ) {
                        Utils.showDialog3(ProfilActivity.this,"Please Enable your GPS","Location",false, new Utils.Click() {
                            @Override
                            public void Ok() {
                            }
                            @Override
                            public void Cancel() {

                            }
                        });
                    }
                }catch (NullPointerException e){

                }*/
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
        switch(item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
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
        bottomBar.setOnTabSelectListener(this,true);
        setIndicator(1);
        bottomBar.setInActiveTabColor(getResources().getColor(R.color.black));
        bottomBar.setActiveTabColor(getResources().getColor(R.color.green));
    }

    //Bottom Bar onCLick
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case tab_option:
                pager.setCurrentItem(0,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_profil:
                pager.setCurrentItem(1,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_partenaire:
                pager.setCurrentItem(2,true);
                mViewPagerAdapter.notifyDataSetChanged();
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
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)   {
                    client_PartenaireMap_fragment client_PartenaireMap_fragment = (client_PartenaireMap_fragment) getSupportFragmentManager().findFragmentByTag("client_Partenaire_fragment");

                    if (client_PartenaireMap_fragment != null && client_PartenaireMap_fragment.isVisible()) {
                        client_PartenaireMap_fragment.update();
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
}



