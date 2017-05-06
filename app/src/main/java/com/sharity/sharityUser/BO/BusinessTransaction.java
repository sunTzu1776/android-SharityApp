package com.sharity.sharityUser.BO;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static com.sharity.sharityUser.BO.CISSTransaction.amount;
import static java.lang.Boolean.getBoolean;

public class BusinessTransaction {

    public  String approved;
    public  String needsProcessing;
    public   String TPEid;
    public   String amount ;
    public   String transactionId;
    public   String transactionType;
    public  String processed;
    public  String type;
    public String clientName;
    public BusinessTransaction(){

    }
    public BusinessTransaction(String transactionId, String approved,String amount,String clientName,String type){
        this.transactionId=transactionId;
        this.approved=approved;
        this.type=type;
        this.amount=amount;
        this.clientName=clientName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String isApproved() {
        return approved;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientName() {
        return clientName;
    }

    public String getAmount() {
        return amount;
    }
}