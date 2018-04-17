package com.qnally.shappapp.Model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class PlacedOrder implements Comparable<PlacedOrder>, Serializable{
    private String name, email, total;
    private ShippingAddress sa;
    private BillingAddress ba;
    private List<Order> items;
    private String status;
    private long order_id;
    private String date;
    private int daysToShip;
    private String canCancel;

    public PlacedOrder() {
    }

    public PlacedOrder(String name, String email, String total, ShippingAddress sa, BillingAddress ba, List<Order> items, String date, long id, int daysToShip) {
        this.name = name;
        this.email = email;
        this.total = total;
        this.sa = sa;
        this.ba = ba;
        this.items = items;
        status = "0"; // Default is 0, 1: Placed, 2: Shipped, 3: Delivered
        this.date = date;
        this.order_id = id;
        this.daysToShip = daysToShip;
        canCancel = "true";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ShippingAddress getSa() {
        return sa;
    }

    public void setSa(ShippingAddress sa) {
        this.sa = sa;
    }

    public BillingAddress getBa() {
        return ba;
    }

    public void setBa(BillingAddress ba) {
        this.ba = ba;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress(){
        return sa.getAddress() + ", " + sa.getCity() + ", " + sa.getState() + "  " + sa.getZip();
    }

    public int getDaysToShip() {
        return daysToShip;
    }

    public void setDaysToShip(int daysToShip) {
        this.daysToShip = daysToShip;
    }

    public String isCanCancel() {
        return canCancel;
    }

    public void setCanCancel(String canCancel) {
        this.canCancel = canCancel;
    }

    @Override
    public int compareTo(@NonNull PlacedOrder o) {
        return getDate().compareTo(o.getDate());
    }
}
