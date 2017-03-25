package com.sharity.sharityUser.fragment.testpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sharity.sharityUser.fragment.pro.Pro_History_fragment;

public class SampleAdapter extends FragmentPagerAdapter {
  Context ctxt=null;

  public SampleAdapter(Context ctxt, FragmentManager mgr) {
    super(mgr);
    this.ctxt=ctxt;
  }

  @Override
  public int getCount() {
    return(2);
  }

  // Return the Fragment associated with a specified position.
  @Override
  public Fragment getItem(int position) {
    if (position == 0) {
      return Pro_History_fragment.newInstance("payements");
    } else if (position == 1) {
      return Pro_History_fragment.newInstance("dons");
    }
    return null;
  }

  @Override
  public String getPageTitle(int position) {
    return String.valueOf(position);
  }
}