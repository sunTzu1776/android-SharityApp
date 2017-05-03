package com.sharity.sharityUser.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sharity.sharityUser.R;

public class Popup_onNotification {
  // ...onCreate above...
  Rect location;
  // Display popup attached to the button as a position anchor
  public void displayPopupWindow(View anchorView, Activity context,String business, String sharepoint) {
    PopupWindow popup = new PopupWindow(context);
    View layout = context.getLayoutInflater().inflate(R.layout.popup_received_notification, null);
    popup.setContentView(layout);
    TextView text = (TextView)layout.findViewById(R.id.tvCaption);
    Button approved = (Button)layout.findViewById(R.id.approved);
    Button decline = (Button)layout.findViewById(R.id.decline);

    text.setText(business.toUpperCase()+System.getProperty("line.separator")+"vous à envoyé "+ sharepoint+" SharePoints");
    // Set content width and height
    popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
    popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
    // Closes the popup window when touch outside of it - when looses focus
    popup.setOutsideTouchable(true);
    popup.setFocusable(true);
    // Show anchored to button
    popup.setBackgroundDrawable(new BitmapDrawable());
  //  popup.showAsDropDown(anchorView);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      popup.showAsDropDown(anchorView, -5, 0);
    }

  }

  public Rect locateView(View v)
  {
    int[] loc_int = new int[2];
    if (v == null) return null;
    try
    {
      v.getLocationOnScreen(loc_int);
    } catch (NullPointerException npe)
    {
      //Happens when the view doesn't exist on screen anymore.
      return null;
    }
    location = new Rect();
    location.left = loc_int[0];
    location.top = loc_int[1];
    location.right = location.left + v.getWidth();
    location.bottom = location.top + v.getHeight();
    return location;
  }
}  