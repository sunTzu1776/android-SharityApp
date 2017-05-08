package com.sharity.sharityUser.Utils;

import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sharity.sharityUser.BO.CharityDons;
import com.sharity.sharityUser.BO.TPE;
import com.sharity.sharityUser.BO.TPEBO;
import com.sharity.sharityUser.BO.UserLocation;
import com.sharity.sharityUser.R;
import com.sharity.sharityUser.fragment.Inscription1CallBack;
import com.sharity.sharityUser.fragment.pro.Pro_Paiment_StepTwo_fragment;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.sharity.sharityUser.R.id.annuler;
import static com.sharity.sharityUser.R.id.box_TPE;


/**
 * Created by Moi on 06/05/2017.
 */

public class Dialog_TPE_Businness extends DialogFragment {

    private ArrayList<ParseObject> tpeList = new ArrayList<ParseObject>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> list = new ArrayList<String>();
    private ListView listView;
    private TPEDialog tpeDialog;
    private String selectedFromList;
    public interface TPEDialog{
        public void onTPEValidate(ParseObject tpe1, boolean isCheck);
        public void onTPECancel();
    }

    public static Dialog_TPE_Businness newInstance() {
        Dialog_TPE_Businness myFragment = new Dialog_TPE_Businness();
        Bundle args = new Bundle();
        myFragment.setArguments(args);
        return myFragment;
    }

    public void setOnDialogClickedListener(TPEDialog l){
        tpeDialog = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View inflate = inflater.inflate(R.layout.layout_dialog_single_choice, container, false);
        listView = (ListView) inflate.findViewById(R.id.listview);
        TextView valider=(TextView) inflate.findViewById(R.id.valider);
        TextView annuler=(TextView) inflate.findViewById(R.id.annuler);
        final CheckBox box_TPE=(CheckBox) inflate.findViewById(R.id.box_TPE);

        get_TPE();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromList = (listView.getItemAtPosition(i).toString());
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedFromList!=null){
                    for (ParseObject tpe : tpeList){
                        if (selectedFromList.equalsIgnoreCase(tpe.getString("name"))){
                            boolean check=false;
                            if (box_TPE.isChecked()){
                                check=true;
                            }
                            ParseObject tpe1= tpe;
                            tpeDialog.onTPEValidate(tpe1,check);
                            break;
                        }
                    }
                }else {
                    Toast.makeText(getActivity(),"Veuillez séléctionner un TPE",Toast.LENGTH_LONG).show();
                }
            }
        });
        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpeDialog.onTPECancel();
            }
        });
        return inflate;
    }


    private void get_TPE() {
        try {
            tpeList.clear();
            list.clear();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("TPE");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> commentList, ParseException e) {
                    if (commentList != null) {
                        for (final ParseObject object : commentList) {
                            final String name = object.getString("name");
                            final String TPEid = String.valueOf(object.getInt("TPEid"));
                            final String id = object.getObjectId();
                            final Date createdAt = object.getCreatedAt();
                            final Date updatedAt = object.getUpdatedAt();

                            tpeList.add(object);
                            list.add(name);
                        }
                        String[] array = list.toArray(new String[0]);

                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice,array);
                        listView.setAdapter(adapter);

                    }
                }
            });
        } catch (NullPointerException f) {

        }
    }

    private void setTPE_Preferences(ParseObject caisseid) {
        SharedPreferences pref = getActivity().getSharedPreferences("Pref", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(caisseid); // myObject - instance of MyObject
        editor.putString("TPEpref", json);
        editor.commit();

    }
}



