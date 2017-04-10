package com.sharity.sharityUser.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sharity.sharityUser.R;

import static android.R.attr.tag;
import static android.R.id.message;
import static android.R.string.ok;


/**
 * Created by Moi on 29/01/2017.
 */

public class Utils {


    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }

    public static boolean isConnected2(Activity context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else return false;
    }



    public interface Click {
        void Ok();

        void Cancel();
    }


    public static void replaceFragmentWithAnimation(int containerId,android.support.v4.app.Fragment fragment, FragmentManager fm, String tag){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void replaceFragmentWithAnimationVertical(int containerId,android.support.v4.app.Fragment fragment, FragmentManager fm, String tag){
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        transaction.replace(containerId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void expand(final View v) {
        v.measure(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? WindowManager.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    public static void showDialog3(Context activity, String message, String title, Boolean hideCancel, final Click ok) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_network_connectivity);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(message);

        TextView titleTV = (TextView) dialog.findViewById(R.id.titletext);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
        if (hideCancel) {
            cancelBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setClickable(false);
        } else {
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setClickable(true);
        }

        titleTV.setText(title);
        titleTV.setVisibility(View.VISIBLE);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.Ok();
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ok.Cancel();
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public interface ProcessEmail {
        void SetEmail(String email);

        void Cancel();
    }

    public static void ForgottenPasswordDialog(Context activity, Boolean hideCancel, final ProcessEmail processEmail) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_forgot_password);

        final EditText email = (EditText) dialog.findViewById(R.id.email);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_btn);
        if (hideCancel) {
            cancelBtn.setVisibility(View.INVISIBLE);
            cancelBtn.setClickable(false);
        } else {
            cancelBtn.setVisibility(View.VISIBLE);
            cancelBtn.setClickable(true);
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processEmail.SetEmail(email.getText().toString());
                dialog.dismiss();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processEmail.Cancel();
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public static void performWithBackStackTransaction(final android.support.v4.app.FragmentManager fragmentManager, String tag, android.support.v4.app.Fragment fragment, int content) {
        final int newBackStackLength = fragmentManager.getBackStackEntryCount() + 1;

        fragmentManager.beginTransaction()
                .replace(content, fragment, tag)
                // .addToBackStack(tag)
                .commit();

        fragmentManager.addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int nowCount = fragmentManager.getBackStackEntryCount();
                if (newBackStackLength != nowCount) {
                    // we don't really care if going back or forward. we already performed the logic here.
                    fragmentManager.removeOnBackStackChangedListener(this);

                    if (newBackStackLength > nowCount) { // user pressed back
                        fragmentManager.popBackStackImmediate();
                    }
                }
            }
        });
    }

   /* private void replaceFragment (android.support.v4.app.Fragment fragment){
        String backStateName = fragment.getClass().getName();

        android.support.v4.app.FragmentManager manager = getSupprtFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }*/



    public static String SplitTime(String time) {
        String string = time;
        String[] parts = string.split("-");
        String part1 = parts[0];
        String part2 = parts[1];
        return part2;
    }
}


