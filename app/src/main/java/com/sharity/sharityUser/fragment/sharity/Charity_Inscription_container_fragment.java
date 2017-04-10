package com.sharity.sharityUser.fragment.sharity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.SignupPro.SignUpCharityPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProPresenter;
import com.sharity.sharityUser.SignupPro.SignUpProPresenterImpl;
import com.sharity.sharityUser.SignupPro.SignUpProView;
import com.sharity.sharityUser.activity.MapActivity;
import com.sharity.sharityUser.activity.ProfilActivity;
import com.sharity.sharityUser.activity.ProfilProActivity;
import com.sharity.sharityUser.fragment.FragOne;
import com.sharity.sharityUser.fragment.Inscription2CallBack;
import com.sharity.sharityUser.fragment.Updateable;
import com.sharity.sharityUser.fragment.pagerHistoric.PagerFragment;
import com.sharity.sharityUser.fragment.pro.History_container_fragment;

import static com.sharity.sharityUser.R.id.Siret;
import static com.sharity.sharityUser.R.id.username;


/**
 * Created by Moi on 14/11/15.
 */
public class Charity_Inscription_container_fragment extends Fragment implements Updateable, com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment.OnSelection,FragOne,Inscription2CallBack, SignUpProView {

    com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment.OnSelection onSelection;
    FragOne onSelect;
    private SignUpProPresenter presenter;
    ImageView circle_slide1;
    ImageView circle_slide2;
    View inflate;
    Object[] viewFirstSscreen;

    public static Charity_Inscription_container_fragment newInstance() {
        Charity_Inscription_container_fragment myFragment = new Charity_Inscription_container_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            inflate = inflater.inflate(R.layout.fragment_inscription_container_sharity, container, false);
        circle_slide1 = (ImageView) inflate.findViewById(R.id.circle_slide1);
        circle_slide2 = (ImageView) inflate.findViewById(R.id.circle_slide2);

        circle_slide1.setImageResource(R.drawable.circles_slide_on);
        circle_slide2.setImageResource(R.drawable.circles_slide_off);
        circle_slide2.setVisibility(View.INVISIBLE);
        circle_slide1.setVisibility(View.INVISIBLE);

        presenter = new SignUpCharityPresenterImpl(this);

        return inflate;
    }

    @Override
    public void update() {
    }


    @Override
    public void OnSelect(int i) {
        if (i == 0) {
            circle_slide1.setImageResource(R.drawable.circles_slide_on);
            circle_slide2.setImageResource(R.drawable.circles_slide_off);
        } else {
            circle_slide1.setImageResource(R.drawable.circles_slide_off);
            circle_slide2.setImageResource(R.drawable.circles_slide_on);
        }
    }


    public void CallGetPicture(){
        com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment fragment2 = (com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.getAdapter().PerfomCallPicture();
    }

    /*order fields for second screen*/
   //View[] fields={business_name,chief_name,description,Siret,phone,address,RIB,email};


    @Override
    public void CallToRegister(Object[] viewsSecondScreen,Object[] adress) {

        Object[] screenfields = concat(viewFirstSscreen,viewsSecondScreen);
        presenter.validateCredentialsSharity("charite",screenfields,adress);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setUsernameError() {

    }

    @Override
    public void setPasswordError() {

    }

    @Override
    public void setRC3Error() {

    }

    @Override
    public void setBusinessNameError() {

    }

    @Override
    public void setOwnerNameError() {

    }

    @Override
    public void setPhoneError() {

    }

    @Override
    public void setAddressError() {

    }

    @Override
    public void setRIBError() {

    }

    @Override
    public void setEmailError() {

    }

    @Override
    public void navigateToHome() {
        IsSharity();
        startActivity(new Intent(getActivity(), ProfilProActivity.class));
        getActivity().finish();
    }

    private void IsSharity(){
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("status", "Sharity");
        editor.commit();
    }

    public Object[] concat(Object[] a, Object[] b) {
        int aLen = a.length;
        int bLen = b.length;
        Object[] c= new Object[aLen+bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }

    @Override
    public void OnSelector(Object[] viewFirstscreen) {
        viewFirstSscreen= viewFirstscreen;
        com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment fragment2 = (com.sharity.sharityUser.fragment.pagerInscriptionSharity.PagerFragment) getChildFragmentManager().findFragmentById(R.id.content);
        fragment2.pager.setCurrentItem(1);
        fragment2.getAdapter().notifyDataSetChanged();
    }
}