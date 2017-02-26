package com.sharity.sharityUser.fragment.client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.CustomSpinnerAdapter;
import com.sharity.sharityUser.Utils.YearpinnerAdapter;
import com.sharity.sharityUser.fragment.Updateable;

import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.y;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Option_fragment extends Fragment implements Updateable, View.OnClickListener {
    private View inflate;
    private ImageView CB;
    private ImageView MasterCard;
    private ImageView VISA;
    Spinner month_expiration;
    Spinner year_expiration;

    ArrayList<Integer> month=new ArrayList<>();
    ArrayList<Integer> years=new ArrayList<>();

    public static client_Option_fragment newInstance() {
        client_Option_fragment myFragment = new client_Option_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_option, container, false);
        month_expiration=(Spinner)inflate.findViewById(R.id.month_expiration);
        year_expiration=(Spinner)inflate.findViewById(R.id.year_expiration);
         CB=(ImageView)inflate.findViewById(R.id.card_CB);
         VISA=(ImageView)inflate.findViewById(R.id.card_VISA);
         MasterCard=(ImageView)inflate.findViewById(R.id.card_MasterCard);

        ImageView [] cardList= new ImageView[]{CB,VISA,MasterCard};
        for (ImageView imageView : cardList){
            imageView.setOnClickListener(this);
        }

        for (int i=0;i<=12;i++){
            month.add(i);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for (int i=year-3;i<year+3;i++){
            years.add(i);
        }

        YearpinnerAdapter adapter_year=new YearpinnerAdapter(getActivity(),years);
        CustomSpinnerAdapter adapter_month=new CustomSpinnerAdapter(getActivity(),month,"month");

        month_expiration.setAdapter(adapter_month);
        year_expiration.setAdapter(adapter_year);

        return inflate;
    }

    @Override
    public void update() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_CB:
                CB.setImageResource(R.drawable.cb_pressed);
                VISA.setImageResource(R.drawable.visa_unpressed);
                MasterCard.setImageResource(R.drawable.mastercard_unpressed);
                break;
            case R.id.card_VISA:
                CB.setImageResource(R.drawable.cb_unpressed);
                VISA.setImageResource(R.drawable.visa_pressed);
                MasterCard.setImageResource(R.drawable.mastercard_unpressed);
                break;
            case R.id.card_MasterCard:
                CB.setImageResource(R.drawable.cb_unpressed);
                VISA.setImageResource(R.drawable.visa_unpressed);
                MasterCard.setImageResource(R.drawable.mastercard_pressed);
                break;

        }
    }
}