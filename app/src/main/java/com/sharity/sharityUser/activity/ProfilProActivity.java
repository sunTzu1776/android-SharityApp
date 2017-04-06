package com.sharity.sharityUser.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterNews;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Profil_Container_fragment;

import java.util.ArrayList;

import static com.sharity.sharityUser.R.id.tab_utilisateur;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilProActivity extends AppCompatActivity implements OnTabSelectListener {


    private Boolean emailVerified;
    public static DatabaseHandler db;
    static int TOTAL_PAGES=3;
    private ViewPager pager;
    private Toolbar toolbar;
    private BottomBar bottomBar;
    private TextView toolbarTitle;
    private boolean start =true;
    public static ParseUser parseUser;
    private DrawerLayout drawer_layout;
    private ListView myDrawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ArrayList<Drawer> drawersItems= new ArrayList<Drawer>();
    private AdapterNews adapter;
    public static ProfilProActivity profilProActivity;
    public static String profileSource;
    public ListenFromActivity activityListener;

    public interface ListenFromActivity {
        void doSomethingInFragment(String frag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pro);


        if (savedInstanceState == null) {
            db = new DatabaseHandler(this);
            profilProActivity=this;
            parseUser=ParseUser.getCurrentUser();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            bottomBar = (BottomBar) findViewById(R.id.bottomBar);
            toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

            drawer_layout=(DrawerLayout) findViewById(R.id.drawer_layout);
            drawersItems.add(0, new Drawer(R.drawable.logo, "", 0));
            drawersItems.add(1, new Drawer(R.drawable.logo, "Finir mon inscription", 1));
            drawersItems.add(2, new Drawer(R.drawable.logo, "Informations de profil", 1));
            drawersItems.add(3, new Drawer(R.drawable.logo, "CGU", 1));
            drawersItems.add(4, new Drawer(R.drawable.logo, "Contacts", 1));
            drawersItems.add(5, new Drawer(R.drawable.logo, "Noter l'application", 1));
            drawersItems.add(6, new Drawer(R.drawable.logo, "Déconnexion", 1));

            myDrawer = (ListView) findViewById(R.id.my_drawer);
            adapter = new AdapterNews(ProfilProActivity.this, drawersItems);
            myDrawer.setAdapter(adapter);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(0);
            pager.setAdapter(mViewPagerAdapter);
            pager.setCurrentItem(1,true);
            profileSource="Profil";

            pager.setOnPageChangeListener(mPageChangeListener);
            mViewPagerAdapter.notifyDataSetChanged();
            buildTabs();

            //   pager.setPageTransformer(true, new CrossfadePageTransformer());
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            actionBarDrawerToggle = new ActionBarDrawerToggle(ProfilProActivity.this, drawer_layout,
                    toolbar, R.string.open, R.string.close) {

                public void onDrawerClosed(View view) {
                    supportInvalidateOptionsMenu();

                }
                public void onDrawerOpened(View drawerView) {
                    supportInvalidateOptionsMenu();
                }
            };
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.red));
            drawer_layout.setDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();



            if (db.getEmailverified().equals("true")){
            }else {
                if (Utils.isConnected(this)){
                    Check_email_validate();
                }else {
                     Intent intent= new Intent(ProfilProActivity.this, LoginActivity.class);
                    intent.putExtra("emailVerified","false");
                    startActivity(intent);
                    finish();
                }
            }
        }

        myDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        break;
                    case 1:
                        pager.setCurrentItem(1,true);
                        if (null != activityListener) {
                            activityListener.doSomethingInFragment("Finalize_inscription");
                        }
                        break;
                    case 2:
                        pager.setCurrentItem(1,true);
                        if (null != activityListener) {
                            activityListener.doSomethingInFragment("Profilinfo");
                        }
                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:
                        break;
                    case 6:
                        //Disconnection
                        parseUser.logOut();
                        Intent intent = new Intent(ProfilProActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                }
            }
        });

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
                return Pro_Paiment_fragment.newInstance();
            } else if (position == 1) {
                return Pro_Profil_Container_fragment.newInstance(profileSource);
            } else if (position == 2) {
                return History_container_fragment.newInstance();
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
            if (object instanceof Pro_Paiment_fragment) {
                ((Pro_Paiment_fragment) object).update();
            }
            if (object instanceof Pro_Profil_Container_fragment) {
                ((Pro_Profil_Container_fragment) object).update();
            }

            if (object instanceof History_container_fragment) {
                ((History_container_fragment) object).update();
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
        }

        @Override
        public void onPageSelected(int position) {
            setIndicator(position);
            bottomBar.setInActiveTabColor(getResources().getColor(R.color.black));
            bottomBar.setActiveTabColor(getResources().getColor(R.color.red));
            if (position==0){
                toolbarTitle.setText("PAIMENT");
            }else if (position==1){
                toolbarTitle.setText("PROFIL");
            }else if (position==2){
                toolbarTitle.setText("HISTORIQUE");
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
        bottomBar.setActiveTabColor(getResources().getColor(R.color.red));
    }

    //Bottom Bar onCLick
    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case tab_utilisateur:
                pager.setCurrentItem(0,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_profil:
                pager.setCurrentItem(1,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_historique:
                pager.setCurrentItem(2,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
        }
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
            if (fragment != null) traverseManagers(fragment.getChildFragmentManager(), managers, intent + 1);
        }
    }


    private String Check_email_validate(){
        parseUser= ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        if (parseUser!=null) {
            query.getInBackground(parseUser.getObjectId(), new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (object != null) {
                        emailVerified = object.getBoolean("emailVerified");
                        if (emailVerified) {
                            if (db.getBusinessCount() > 0) {
                                String objectid = db.getBusinessId();
                                Business business = new Business(objectid, "true");
                                db.UpdateEmailVerified(business);
                                db.close();
                            }
                        } else {
                            Intent intent= new Intent(ProfilProActivity.this, LoginActivity.class);
                            intent.putExtra("emailVerified","false");
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            });
        }
        return String.valueOf(emailVerified);
    }

    public void setActivityListener(ListenFromActivity activityListener) {
        this.activityListener = activityListener;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

}



