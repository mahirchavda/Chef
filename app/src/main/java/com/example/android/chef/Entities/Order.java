package com.example.android.chef.Entities;

import java.io.Serializable;

/**
 * Created by jenil on 08-03-2018.
 */

public class Order implements Serializable {
    private String uid;
    private String item_name;
    private String status;
    private int quantity;

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

    }

    public Order(String uid, String item_name, String status, int quantity) {

        this.uid = uid;
        this.item_name = item_name;
        this.status = status;
        this.quantity = quantity;
    }
}
