package com.sharity.sharityUser.BO;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import static com.facebook.AccessToken.USER_ID_KEY;

@ParseClassName("CISSTransaction")
public class CISSTransaction extends ParseObject {


    public static final String approved = "approved";
    public static final String needsProcessing = "needsProcessing";
    public static final String TPEid = "TPEid";
    public static final String amount = "amount";
    public static final String transaction = "transaction";
    public static final String transactionType = "transactionType";
    public static final String processed = "processed";


    public Boolean getUserId() {
        return getBoolean(approved);
    }

    public Boolean getNeedsProcessing() {
        return getBoolean(needsProcessing);
    }

    public static String getTPEid() {
        return String.valueOf(TPEid);
    }

    public static Boolean getProcessed() {
        return Boolean.getBoolean(processed);
    }
}