package com.example.android.chef.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jenil on 08-03-2018.
 */

public class Order implements Serializable {
    private String uid;
    private String item_name;
    private String status;
    private int quantity;
    private int remaining;
    private String ordernumber;
    private HashMap<String,String> chefs;
    private long waiting_time;
    private int item_waiting_time;

    public long getWaiting_time() {
        return waiting_time;
    }

    public void setWaiting_time(long waiting_time) {
        this.waiting_time = waiting_time;
    }

    public int getItem_waiting_time() {
        return item_waiting_time;
    }

    public void setItem_waiting_time(int item_waiting_time) {
        this.item_waiting_time = item_waiting_time;
    }

    public HashMap<String,String> getChefs() {
        return chefs;
    }

    public void setChefs(HashMap<String,String> chefs)  {
        this.chefs = chefs;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Order() {
        chefs=new HashMap<>();
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public Order(String uid, String item_name, String status, int quantity, int remaining) {

        this.uid = uid;
        this.item_name = item_name;
        this.status = status;
        this.quantity = quantity;
        this.remaining=remaining;
    }

    public boolean equals(Object o)
    {
        Order os=(Order)o;
        return(o!=null && os!=null && os.getOrdernumber()!=null && getOrdernumber()!=null && ordernumber.compareTo(os.getOrdernumber())==0 && remaining==os.getRemaining());
    }

}
