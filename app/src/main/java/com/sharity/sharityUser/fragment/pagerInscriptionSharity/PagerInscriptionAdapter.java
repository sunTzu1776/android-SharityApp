package com.sharity.sharityUser.fragment.pagerInscriptionSharity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.client.client_Historique_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_History_fragment;
import com.sharity.sharityUser.fragment.sharity.Charity_Inscription1_fragment;
import com.sharity.sharityUser.fragment.sharity.Charity_Inscription2_fragment;

import java.util.HashMap;
import java.util.Map;

public class PagerInscriptionAdapter extends FragmentPagerAdapter {
  Charity_Inscription1_fragment m1stFragment;
  Charity_Inscription2_fragment m2stFragment;
  FragmentManager mgr;
  Context ctxt=null;
  private Map<Integer, String> mFragmentTags;

  public PagerInscriptionAdapter(Context ctxt, FragmentManager mgr) {
    super(mgr);
    this.mgr=mgr;
    this.ctxt=ctxt;
    mFragmentTags = new HashMap<Integer, String>();

  }

  @Override
  public int getCount() {
    return(2);
  }

  // Return the Fragment associated with a specified position.
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
        return Charity_Inscription1_fragment.newInstance("charite");
    } else if (position == 1) {
        return Charity_Inscription2_fragment.newInstance("charite");
    }
    return null;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
    // save the appropriate reference depending on position
    switch (position){
      case 0:
          m1stFragment = (Charity_Inscription1_fragment) createdFragment;
          break;
      case 1:
          m2stFragment = (Charity_Inscription2_fragment) createdFragment;
        break;
    }
    return createdFragment;
  }

  @Override
  public String getPageTitle(int position) {
    return String.valueOf(position);
  }

  public void PerformRegistration() {
    if (m1stFragment != null) {
      Log.d("m1stFragment","ok passed");
      // m1stFragment.onRefresh();
    }
    if (m2stFragment != null) {
     // m2stFragment.PerformRegistration();
      Log.d("m2stFragment","ok passed");
      // m1stFragment.onRefresh();
    }
  }

  public void PerfomCallPicture() {
    if (m1stFragment != null) {
      Log.d("m1stFragment","ok passed");
       m1stFragment.GetPicture();
    }
  }
}
