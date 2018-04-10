package com.qnally.shappapp.Model;

public class Customer {

    private String first_name, last_name, password, email;
    //private String creditcard;
    private BillingAddress billing_Address;
    private ShippingAddress shipping_Address;
    private Payment Payment;

    public Customer() {
    }

    public Customer(String first_name, String last_name, String password, String email, ShippingAddress sa, BillingAddress ba, Payment pay) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email;
        this.billing_Address = ba;
        this.shipping_Address = sa;
        Payment = pay;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ShippingAddress getShipping_Address() {
        return shipping_Address;
    }

    public void setShipping_Address(ShippingAddress shipping_Address) {
        this.shipping_Address = shipping_Address;
    }

    public BillingAddress getBilling_Address() {
        return billing_Address;
    }

    public void setBilling_Address(BillingAddress billing_Address) {
        this.billing_Address = billing_Address;
    }

    public Payment getPayment() {
        return Payment;
    }

    public void setPayment(Payment payment) {
        this.Payment = payment;
    }
}
