package com.sharity.sharityUser.fragment.pagerHistoric;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharity.sharityUser.R;

public class PagerFragment extends Fragment {
  public ViewPager pager;
  private OnSelection onSelection;
  public SampleAdapter adapter;
  public interface OnSelection{
    public void OnSelect(int i);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
                           ViewGroup container,
                           Bundle savedInstanceState) {
    View result=inflater.inflate(R.layout.pager, container, false);
    pager=(ViewPager)result.findViewById(R.id.pager);
    pager.setAdapter(buildAdapter());
    pager.setOnPageChangeListener(mPageChangeListener);
    return(result);
  }

  public PagerAdapter buildAdapter() {
    adapter=new SampleAdapter(getActivity(), getChildFragmentManager());
    return adapter;
  }

  public SampleAdapter getAdapter() {
    return adapter;
  }

  private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
      onSelection.OnSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
  };

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);


    // check if parent Fragment implements listener
    if (getParentFragment() instanceof OnSelection) {
      onSelection = (OnSelection) getParentFragment();
    } else {
      throw new RuntimeException("The parent fragment must implement OnSelection");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    onSelection = null;
  }

}