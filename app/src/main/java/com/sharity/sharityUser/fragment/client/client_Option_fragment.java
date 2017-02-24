package com.sharity.sharityUser.fragment.client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class client_Option_fragment extends Fragment implements Updateable {
    private View inflate;
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

        for (int i=0;i<=12;i++){
            month.add(i);
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for (int i=0;i<year-3 && i>year+3;i++){
            years.add(i);
        }

        CustomSpinnerAdapter adapter_month=new CustomSpinnerAdapter(getActivity(),month,"month");
        YearpinnerAdapter adapter_year=new YearpinnerAdapter(getActivity(),years);

        month_expiration.setAdapter(adapter_month);
        year_expiration.setAdapter(adapter_year);

        return inflate;
    }

    @Override
    public void update() {

    }
}