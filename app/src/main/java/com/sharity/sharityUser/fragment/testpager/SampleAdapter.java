package com.sharity.sharityUser.fragment.testpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.fragment.client.client_Historique_fragment;
import com.sharity.sharityUser.fragment.pro.Pro_History_fragment;

import java.util.HashMap;
import java.util.Map;

public class SampleAdapter extends FragmentPagerAdapter {
  client_Historique_fragment m1stFragment;
  Pro_History_fragment m2stFragment;
  FragmentManager mgr;
  Context ctxt=null;
  private Map<Integer, String> mFragmentTags;

  public SampleAdapter(Context ctxt, FragmentManager mgr) {
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
      if (ctxt instanceof ProfilActivity){
        return client_Historique_fragment.newInstance("payements");
      }else {
        return Pro_History_fragment.newInstance("payements");
      }
    } else if (position == 1) {
      if (ctxt instanceof ProfilActivity){
        return client_Historique_fragment.newInstance("dons");
      }else {
        return Pro_History_fragment.newInstance("dons");
      }
    }
    return null;
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
    // save the appropriate reference depending on position
    switch (position){
      case 0:
        if (ctxt instanceof ProfilActivity){
          m1stFragment = (client_Historique_fragment) createdFragment;
          break;
        }else {
          m2stFragment = (Pro_History_fragment) createdFragment;
        }
        break;

      case 1:
        if (ctxt instanceof ProfilActivity){
          m1stFragment = (client_Historique_fragment) createdFragment;
          break;
        }else {
          m2stFragment = (Pro_History_fragment) createdFragment;
        }
        break;
    }
    return createdFragment;
  }

  @Override
  public String getPageTitle(int position) {
    return String.valueOf(position);
  }

  public void FragmentOperation() {
    if (m1stFragment != null) {
      Log.d("m1stFragment","ok passed");
       m1stFragment.onRefresh();
    }

    if (m2stFragment != null) {
      // m2ndFragment.doSomeWorkToo();
    }
  }
}
