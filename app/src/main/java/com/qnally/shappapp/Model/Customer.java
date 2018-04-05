package com.qnally.shappapp.Model;

public class Customer {

    private String first_name, last_name, password, email;
    private String creditcard;
    private BillingAddress ba;
    private ShippingAddress sa;

    public Customer() {
    }

    public Customer(String first_name, String last_name, String password, String email) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.email = email;
        ba = null;
        sa = null;
        creditcard = null;
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

    public String getCreditcard() {
        return creditcard;
    }

    public void setCreditcard(String creditcard) {
        this.creditcard = creditcard;
    }

    public BillingAddress getBa() {
        return ba;
    }

    public void setBa(BillingAddress ba) {
        this.ba = ba;
    }

    public String getSa() {
        return sa.getFullAddress();
    }

    public void setSa(ShippingAddress sa) {
        this.sa = sa;
    }
}
