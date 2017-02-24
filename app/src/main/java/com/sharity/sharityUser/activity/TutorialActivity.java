package com.sharity.sharityUser.activity;

/*
* This application is on the copyright of Guilla.Lab and associates.
*
* */


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sharity.sharityUser.CustomViewPager;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.DummyPagerFragment;
import com.sharity.sharityUser.fragment.tutorial.fragment_Tutorial1;
import com.sharity.sharityUser.fragment.tutorial.fragment_Tutorial2;
import com.sharity.sharityUser.fragment.tutorial.fragment_Tutorial3;
import com.sharity.sharityUser.fragment.tutorial.fragment_Tutorial4;


public class TutorialActivity extends AppCompatActivity {

    static int TOTAL_PAGES=2;
    CustomViewPager pager;
    PagerAdapter pagerAdapter;
    LinearLayout circles;
    Button btnSkip;
    Button btnDone;
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_tutorial);

            TOTAL_PAGES=4;

        btnSkip = Button.class.cast(findViewById(R.id.btn_skip));
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
            }
        });

        btnNext = Button.class.cast(findViewById(R.id.btn_next));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem()==0){
                    if (Utils.isConnected(getApplicationContext())){
                        pager.setCurrentItem(pager.getCurrentItem() + 1, true);
                    }
                    else {
                        Utils.showDialog3(TutorialActivity.this, getString(R.string.dialog_network),getString(R.string.network),true, new Utils.Click() {
                            @Override
                            public void Ok() {

                            }
                            @Override
                            public void Cancel() {

                            }
                        });
                    }
                }
                else
                    pager.setCurrentItem(pager.getCurrentItem() + 1, true);
            }
        });

        btnDone = Button.class.cast(findViewById(R.id.done));
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTutorial();
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });

        pager = (CustomViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlideAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        //   pager.setPageTransformer(true, new CrossfadePageTransformer());
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position < TOTAL_PAGES){
                    pager.setPagingEnabled(true);
                    btnSkip.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setIndicator(position);

                if (position < TOTAL_PAGES) {
                    btnSkip.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnDone.setVisibility(View.GONE);
                }
                if (position == TOTAL_PAGES - 1) {
                    btnSkip.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.GONE);
                    btnDone.setVisibility(View.VISIBLE);

                }
                else if (position == TOTAL_PAGES) {
                    endTutorial();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pager != null) {
            pager.clearOnPageChangeListeners();
        }
    }

    private void buildCircles() {
        circles = LinearLayout.class.cast(findViewById(R.id.circles));

        float scale = getResources().getDisplayMetrics().density;
        int padding = (int) (5 * scale + 0.5f);

        for (int i = 0; i < TOTAL_PAGES; i++) {
            ImageView circle = new ImageView(this);
            circle.setImageResource(R.drawable.whiteindicator);
            circle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            circle.setAdjustViewBounds(true);
            circle.setPadding(padding, 0, padding, 0);
            circles.addView(circle);
        }

        setIndicator(0);
    }

    private void setIndicator(int index) {
        if (index < TOTAL_PAGES) {
            for (int i = 0; i < TOTAL_PAGES; i++) {
                ImageView circle = (ImageView) circles.getChildAt(i);
                if (i == index) {
                    circle.setColorFilter(getResources().getColor(R.color.white));
                } else {
                    circle.setColorFilter(getResources().getColor(R.color.greylight));
                }
            }
        }
    }

    private void endTutorial() {
        finish();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }



    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            Utils.showDialog3(TutorialActivity.this, "Quit application?","Quit",false, new Utils.Click() {
                @Override
                public void Ok() {
                    TutorialActivity.this.finish();
                }

                @Override
                public void Cancel() {

                }
            });

        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlideAdapter extends FragmentStatePagerAdapter {

        public ScreenSlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DummyPagerFragment welcomeScreenFragment = null;

                switch (position) {

                    case 0:
                        fragment_Tutorial1 fragment_Tutorial1 = new fragment_Tutorial1();
                        return fragment_Tutorial1;
                    case 1:
                        fragment_Tutorial2 fragment_tutorial2 = new fragment_Tutorial2();
                        return fragment_tutorial2;

                    case 2:
                        fragment_Tutorial3 fragment_tutorial3 = new fragment_Tutorial3();
                        return fragment_tutorial3;

                    case 3:
                        fragment_Tutorial4 fragment_tutorial4 = new fragment_Tutorial4();
                        return fragment_tutorial4;

                    case 4:
                        welcomeScreenFragment = DummyPagerFragment.newInstance(R.layout.tutorial_fragment4);
                        break;
            }


            return welcomeScreenFragment;
        }

        @Override
        public int getCount() {
            return TOTAL_PAGES;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoginActivity.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case 102: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

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


