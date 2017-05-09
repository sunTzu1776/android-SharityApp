package com.sharity.sharityUser.fragment.pro;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.Utils.Utils;
import com.sharity.sharityUser.fragment.Updateable;

import static com.sharity.sharityUser.R.id.montant_SP;
import static com.sharity.sharityUser.R.id.montant_recue;
import static com.sharity.sharityUser.R.id.payment;
import static com.sharity.sharityUser.R.id.sharepoint_supplementary;


/**
 * Created by Moi on 14/11/15.
 */
public class Pro_Paiment_Confirmation_fragment extends Fragment implements Updateable {
    private View inflate;
    private TextView montant_recue;
    private TextView montant_SP;

    private String mMontant_recue;
    private String client_name;
    private boolean approved;
    private TextView ticket;
    private TextView state;

    public static Pro_Paiment_Confirmation_fragment newInstance(String montant_recue, String client_name,boolean approved) {
        Pro_Paiment_Confirmation_fragment myFragment = new Pro_Paiment_Confirmation_fragment();
        Bundle args = new Bundle();
        args.putString("client_name",client_name);
        args.putString("montant_recue",montant_recue);
        args.putBoolean("approved",approved);

        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_paiment_pro_confirmation, container, false);
        montant_recue=(TextView)inflate.findViewById(R.id.montant_recue);
        montant_SP=(TextView)inflate.findViewById(R.id.montant_SP);
        ticket=(TextView)inflate.findViewById(R.id.ticket);
        state=(TextView)inflate.findViewById(R.id.state);

        client_name=getArguments().getString("client_name");
        mMontant_recue=getArguments().getString("montant_recue");
        approved=getArguments().getBoolean("approved");

        if (approved){
            state.setText("PAIEMENT VALIDE");
            ticket.setVisibility(View.VISIBLE);
            montant_SP.setVisibility(View.VISIBLE);
            montant_recue.setVisibility(View.VISIBLE);
            SettextMontantRecu();
            SettextMontantSP();
        }else {
            state.setText("PAIEMENT REFUSE");
            ticket.setVisibility(View.INVISIBLE);
            montant_SP.setVisibility(View.INVISIBLE);
            montant_recue.setVisibility(View.INVISIBLE);
        }

        return inflate;
    }

    @Override
    public void update() {
    }

    private void SettextMontantRecu(){
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String red = "+" +mMontant_recue+"€";
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.red)), 0, red.length(), 0);
        builder.append(redSpannable);

        String black = " RECUE";
        SpannableString whiteSpannable= new SpannableString(black);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, black.length(), 0);
        builder.append(whiteSpannable);
        montant_recue.setText(builder, TextView.BufferType.SPANNABLE);
    }



    private void SettextMontantSP(){
        SpannableStringBuilder builder = new SpannableStringBuilder();

        String red = "+" +mMontant_recue+"SP";
        SpannableString redSpannable= new SpannableString(red);
        redSpannable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.green)), 0, red.length(), 0);
        builder.append(redSpannable);

        String black = " envoyés à "+client_name;
        SpannableString whiteSpannable= new SpannableString(black);
        whiteSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")), 0, black.length(), 0);
        builder.append(whiteSpannable);
        montant_SP.setText(builder, TextView.BufferType.SPANNABLE);
    }


}