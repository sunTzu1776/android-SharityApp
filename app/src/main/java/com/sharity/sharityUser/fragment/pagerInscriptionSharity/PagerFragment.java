package com.sharity.sharityUser.fragment.pagerInscriptionSharity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.FragOne;
import com.sharity.sharityUser.fragment.Inscription2CallBack;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;

public class PagerFragment extends Fragment implements FragOne, Inscription2CallBack {
  public ViewPager pager;
  private OnSelection onSelection;
  FragOne onSelect;
  Inscription2CallBack inscription2CallBack;
  public PagerInscriptionAdapter adapter;

  @Override
  public void CallToRegister(Object[] view,Object[] adress) {
    inscription2CallBack.CallToRegister(view,adress);
  }

  @Override
  public void OnSelector(Object[] i) {
    onSelect.OnSelector(i);
  }

  public interface OnSelection{
    public void OnSelect(int i);
  }


  public static PagerFragment newInstance() {
    PagerFragment myFragment = new PagerFragment();
    Bundle args = new Bundle();
    myFragment.setArguments(args);
    return myFragment;
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
    adapter=new PagerInscriptionAdapter(getActivity(), getChildFragmentManager());
    return adapter;
  }

  public PagerInscriptionAdapter getAdapter() {
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

    // check if parent Fragment implements listener
    if (getParentFragment() instanceof FragOne) {
      onSelect = (FragOne) getParentFragment();
    } else {
      throw new RuntimeException("The parent fragment must implement OnSelection");
    }

    // check if parent Fragment implements listener
    if (getParentFragment() instanceof Inscription2CallBack) {
      inscription2CallBack = (Inscription2CallBack) getParentFragment();
    } else {
      throw new RuntimeException("The parent fragment must implement Inscription2CallBack");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    onSelection = null;
    onSelect=null;
    inscription2CallBack=null;
  }

}