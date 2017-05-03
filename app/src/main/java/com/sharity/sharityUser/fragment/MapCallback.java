package com.sharity.sharityUser.fragment;

import com.sharity.sharityUser.BO.LocationBusiness;

import java.util.ArrayList;

public interface MapCallback {
   public void onOpen(ArrayList<LocationBusiness> data, boolean type);
   public void onClose();
}