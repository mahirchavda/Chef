package com.example.android.chef.Entities;

/**
 * Created by jenil on 21-02-2018.
 */

public class Item  {
    String name;
    String catagory;
    String average_making_time,image,rating,price;

    public Item(){}

    public Item(String name, String catagory, String average_making_time,String image,String rating,String price) {
        this.name = name;
        this.catagory = catagory;
        this.average_making_time = average_making_time;
        this.image=image;
        this.rating=rating;
        this.price=price;
    }

    public void setAverage_making_time(String average_making_time) {
        this.average_making_time = average_making_time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getAverage_making_time() {
        return average_making_time;
    }


}
