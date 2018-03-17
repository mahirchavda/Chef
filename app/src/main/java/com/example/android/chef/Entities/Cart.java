package com.example.android.chef.Entities;

/**
 * Created by jenil on 07-03-2018.
 */

public class Cart {
    private Item item;
    private int quantitiy;

    public Cart(Item item, int quantitiy) {
        this.item = item;
        this.quantitiy = quantitiy;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantitiy() {
        return quantitiy;
    }

    public void setQuantitiy(int quantitiy) {
        this.quantitiy = quantitiy;
    }

    public boolean equals(Object obj){
        //System.out.println("In equals");
        if (obj instanceof Cart) {
            Cart pp = (Cart) obj;
            //Toast.makeText(WelcomeActivity, "g", Toast.LENGTH_SHORT).show();
            System.out.println(pp.getItem().getName()+" "+pp.getItem().getName()+" "+pp.item.getName().compareTo(this.item.getName()));
            return (pp.item.getName().compareTo(this.item.getName())==0);
        } else {
            return false;
        }
    }
}
