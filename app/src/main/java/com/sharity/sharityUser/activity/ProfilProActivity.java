package com.sharity.sharityUser.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
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
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.sharity.sharityUser.BO.Business;
import com.sharity.sharityUser.BO.BusinessTransaction;
import com.sharity.sharityUser.BO.CISSTransaction;
import com.sharity.sharityUser.BO.Drawer;
import com.sharity.sharityUser.LocalDatabase.DatabaseHandler;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.AdapterNews;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.client.client_Container_Mission_fragment;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_Profil_Container_fragment;

import java.util.ArrayList;

import static com.parse.SubscriptionHandling.Event.UPDATE;
import static com.sharity.sharityUser.Application.parseLiveQueryClient;
import static com.sharity.sharityUser.Application.subscriptionHandling;
import static com.sharity.sharityUser.R.id.tab_historique;
import static com.sharity.sharityUser.R.id.tab_mission;
import static com.sharity.sharityUser.R.id.tab_utilisateur;


/**
 * Created by Moi on 07/05/2016.
 */
public class ProfilProActivity extends AppCompatActivity implements OnTabSelectListener {

    private IntentFilter mIntent;
    private Boolean emailVerified;
    public static DatabaseHandler db;
    static int TOTAL_PAGES=4;
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
    private OnConfirmationPaiment onConfirmationPaiment;
    String client="";
    ParseObject sale;
    MyPagerAdapter mViewPagerAdapter;
    public interface ListenFromActivity {
        void doSomethingInFragment(String frag);
    }

    public interface OnConfirmationPaiment{
        void TaskOnConfirmation(String amount, String clientName,boolean isApproved);
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

             /*
            * set & Display nav drawer
            **/
            drawer_layout=(DrawerLayout) findViewById(R.id.drawer_layout);
            drawersItems.add(0, new Drawer(R.drawable.logo, "", 0));
            drawersItems.add(1, new Drawer(R.drawable.logo, "Finir mon inscription", 1));
            drawersItems.add(2, new Drawer(R.drawable.logo, "Informations de profil", 1));
            drawersItems.add(3, new Drawer(R.drawable.logo, "CGU", 1));
            drawersItems.add(4, new Drawer(R.drawable.logo, "Contacts", 1));
            drawersItems.add(5, new Drawer(R.drawable.logo, "Noter l'app", 1));
            drawersItems.add(6, new Drawer(R.drawable.logo, "Déconnexion", 1));

            myDrawer = (ListView) findViewById(R.id.my_drawer);
            adapter = new AdapterNews(ProfilProActivity.this, drawersItems);
            myDrawer.setAdapter(adapter);

            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(0);
            mViewPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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

            /*
            * drawer onclick
            **/
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

        /*
            * Live QUery permit to listen in real time for Update in CISSTransaction if paiment has been approved or refused
            **/

            final ParseQuery<CISSTransaction> parseQuery = ParseQuery.getQuery(CISSTransaction.class);
        subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(UPDATE, new
                SubscriptionHandling.HandleEventCallback<CISSTransaction>() {
                    @Override
                    public void onEvent(ParseQuery<CISSTransaction> query, final CISSTransaction object) {
                        Log.d("BusinessCISSTransaction","UPDATE");
                        if (object.getBoolean("processed") == true && object.getInt("transactionType")==1) {
                            if (!db.getAllTransactions().isEmpty()){
                            for (BusinessTransaction businessTransaction : db.getAllTransactions()) {
                                if (object.getString("transactionId").equalsIgnoreCase(businessTransaction.getTransactionId())) {
                                    boolean isApproved= object.getBoolean("approved");

                                     sale = ParseObject.create("Transaction");
                                    //Get Pointer customer
                                    try {
                                        sale=object.getParseObject("transaction").fetchIfNeeded();
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                         client=sale.getString("sender_name");
                                         db.deleteTransaction(object.getString("transactionId"));
                                        final int amount=object.getInt("amount");

                                        Intent intent = new Intent("paiment_businessEvent");
                                        intent.putExtra("amount", String.valueOf(amount/100));
                                        intent.putExtra("clientName",client);
                                        intent.putExtra("approved", isApproved);

                                    LocalBroadcastManager.getInstance(ProfilProActivity.this).sendBroadcast(intent);
                                }

                            }
                            }

                        }
                    }
                });

    }

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
                return Pro_Profil_Container_fragment.newInstance(profileSource);
            } else if (position == 2) {
                return Pro_Paiment_fragment.newInstance();
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
        }

      /*  @Override
        // To update fragment in ViewPager, we should override getItemPosition() method,
        // in this method, we call the fragment's public updating method.
        public int getItemPosition(Object object) {
            if (object instanceof History_container_fragment) {
                ((History_container_fragment) object).update();
            }
            if (object instanceof Pro_Profil_Container_fragment) {
                ((Pro_Profil_Container_fragment) object).update();
            }

            if (object instanceof Pro_Paiment_fragment) {
                ((Pro_Paiment_fragment) object).update();
            }
            if (object instanceof Pro_Paiment_fragment) {
                ((Pro_Paiment_fragment) object).update();
            }

            return super.getItemPosition(object);
        };*/
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
                toolbarTitle.setText("HISTORIQUE");
            }else if (position==1){
                toolbarTitle.setText("PROFIL");
            }else if (position==2){
                toolbarTitle.setText("PAIMENT");
            }
            else if (position==3){
                toolbarTitle.setText("MISSIONS");
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
            case tab_historique:
                pager.setCurrentItem(0,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case R.id.tab_profil:
                pager.setCurrentItem(1,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case tab_utilisateur:
                pager.setCurrentItem(2,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
            case tab_mission:
                pager.setCurrentItem(3,true);
                mViewPagerAdapter.notifyDataSetChanged();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        SparseArray<FragmentManager> managers = new SparseArray<>();
        try {
            traverseManagers(getSupportFragmentManager(), managers, 0);
            if (managers.size() > 0) {
                managers.valueAt(managers.size() - 1).popBackStackImmediate();
            } else {
                super.onBackPressed();
            }
        }catch (IllegalStateException e){

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

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            final boolean approved = intent.getBooleanExtra("approved",false);
            final String amount = intent.getStringExtra("amount");
            final String clientName = intent.getStringExtra("clientName");
            int item = pager.getCurrentItem();
            if (item!=2){
                if(!ProfilProActivity.this.isFinishing()){
                    ProfilProActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Utils.showDialog3(ProfilProActivity.this, SetMontantRecu(amount,clientName,approved), "Paiement", true, new Utils.Click() {
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
            }else {
                Pro_Paiment_fragment pro_paiment_fragment=(Pro_Paiment_fragment) mViewPagerAdapter.getRegisteredFragment(2);
                if (pro_paiment_fragment!=null){
                    if (pro_paiment_fragment.isAdded()){
                        onConfirmationPaiment.TaskOnConfirmation(amount, clientName,approved);
                    }else {
                        if(!ProfilProActivity.this.isFinishing()){
                            ProfilProActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    Utils.showDialog3(ProfilProActivity.this, SetMontantRecu(amount,clientName,approved), "Paiement", true, new Utils.Click() {
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
                    }
                }
               // FragmentManager fm = getSupportFragmentManager();
              //  Utils.replaceFragmentWithAnimationVertical(R.id.container, Pro_Paiment_Confirmation_fragment.newInstance(amount,clientName,approved),fm,"Display_Paiment_Confirmation",true);
            }
        }
    };

    //Notification handler
    public void setConfirmationListener(ProfilProActivity.OnConfirmationPaiment onConfirmationPaiment) {
        this.onConfirmationPaiment = onConfirmationPaiment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //registerReceiver(statusReceiver,mIntent);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("paiment_businessEvent"));
    }

    @Override
    public void onPause() {
        if (mIntent != null) {
            this.unregisterReceiver(mMessageReceiver);
            mIntent = null;
        }
        super.onPause();
    }



    private String SetMontantRecu(String recu, String client,boolean approved){
        String result;
        if (approved){
            SpannableStringBuilder builder = new SpannableStringBuilder();

            String red = "+" +recu+"€";
            SpannableString redSpannable= new SpannableString(red);
            redSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, red.length(), 0);
            builder.append(redSpannable);

            String black = " RECUE.";
            SpannableString whiteSpannable= new SpannableString(black);
            whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, black.length(), 0);
            builder.append(whiteSpannable);

            String green = " +" +recu+"SP";
            SpannableString greenpannable= new SpannableString(green);
            greenpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 0, green.length(), 0);
            builder.append(greenpannable);

            String black2 = " envoyés à "+client;
            SpannableString blackSpannable= new SpannableString(black2);
            blackSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, black2.length(), 0);
            builder.append(blackSpannable);
             result= builder.toString();
        }else {
            result="PAIMENT REFUSE";
        }
        return result;
    }



    private void SettextMontantSP(){

    }
}



