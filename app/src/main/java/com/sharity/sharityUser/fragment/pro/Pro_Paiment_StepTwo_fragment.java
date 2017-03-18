package com.sharity.sharityUser.fragment.pro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.Updateable;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_StepTwo_fragment extends Fragment implements Updateable, View.OnClickListener {
    private View inflate;
    GridView grid;
    private TextView valider;
    private TextView amount_paiment;
    private TextView sharepoint_supplementary;
    private CircleImageView picture_profil;
    private TextView username_login;
    private ImageView cash;
    private ImageView CB;


    public static Pro_Paiment_StepTwo_fragment newInstance() {
        Pro_Paiment_StepTwo_fragment myFragment = new Pro_Paiment_StepTwo_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paiment_steptwo_normal_pro, container, false);
        valider=(TextView)inflate.findViewById(R.id.valider);
        amount_paiment=(TextView)inflate.findViewById(R.id.amount_paiment);
        sharepoint_supplementary=(TextView)inflate.findViewById(R.id.sharepoint_supplementary);
        picture_profil=(CircleImageView)inflate.findViewById(R.id.picture_profil);
        username_login=(TextView)inflate.findViewById(R.id.username_login);
        cash=(ImageView)inflate.findViewById(R.id.cash);
        CB=(ImageView)inflate.findViewById(R.id.CB);


        cash.setOnClickListener(this);
        CB.setOnClickListener(this);
        valider.setOnClickListener(this);


        return inflate;
    }

    @Override
    public void update() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.valider:


                break;
            case R.id.cash:
                cash.setImageResource(R.drawable.cash_icone_on);
                CB.setImageResource(R.drawable.cb_icone);

                break;
            case R.id.CB:
                cash.setImageResource(R.drawable.cash_icone);
                CB.setImageResource(R.drawable.cb_icone_on);

                break;

        }
    }
}