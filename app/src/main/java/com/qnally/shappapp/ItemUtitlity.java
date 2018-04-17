package com.qnally.shappapp;

import com.qnally.shappapp.Model.Item;

import java.util.ArrayList;

public class ItemUtitlity {

    static ArrayList<Item> itemutil = new ArrayList<>();

    // Methods for Cart
    public void addCartListImageUri(Item cart_add) {
        this.itemutil.add(0,cart_add);
    }

    public void removeCartListImageUri(int position) {
        this.itemutil.remove(position);
    }

    public ArrayList<Item> getCartListImageUri(){ return this.itemutil; }
}
