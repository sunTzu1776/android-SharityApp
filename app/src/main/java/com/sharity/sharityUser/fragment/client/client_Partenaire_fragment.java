package com.sharity.sharityUser.fragment.client;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.Updateable;


/**
 * Created by Moi on 14/11/15.
 */
public class client_Partenaire_fragment extends Fragment implements Updateable {
    private View inflate;

    public static client_Partenaire_fragment newInstance() {
        client_Partenaire_fragment myFragment = new client_Partenaire_fragment();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_partenaire, container, false);

        return inflate;
    }

    @Override
    public void update() {

    }
}