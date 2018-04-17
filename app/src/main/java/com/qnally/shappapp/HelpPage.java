package com.qnally.shappapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.qnally.shappapp.Adapter.ThreeLevelListAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class HelpPage extends AppCompatActivity {

    private ExpandableListView exp;
    private Toolbar mToolbar;

    String[] parent = new String[] {"Account", "Order", "Payment", "Shipping"};

    String[] acc_questions = new String[] {"How do I register for an account?", "I cannot login to my account.", "I forgot my password.",
            "How can I change my password?", "How do I know what orders I have placed?", "Where can I view my account information?"};
    String[] acc_q1 = new String[]{"To register for an account, go to the homepage and access the triple-striped button in the top left-hand corner of your device. " +
            "Once the navigation drawer slides out, you will notice two options. The second option will initiate the process of your account registration. Basic information " +
            "is necessary beyond this point such as your name, email, and a preferable password."};
    String[] acc_q2 = new String[]{"Check your login details carefully. Make sure the email address is the same " +
            "one you used for registration, and the password you entered is correct. t is possible that the issue is a result of our errors. Please wait 10 minutes and try again."};
    String[] acc_q3 = new String[]{"If you forgot your password, click the \"Forgot your Password?\" link on the login page. If you've already accessed the skip login option, click the three-striped button " +
            "in the top left-hand corner. This will allow you to access the navigation drawer. The first option is the link to navigate to the login page. Choose this option and click " +
            "\"Forgot your Password?\". Beyond this point, you will be asked to enter your valid email and your new password."};
    String[] acc_q4 = new String[]{"N/A"};
    String[] acc_q5 = new String[]{"To view order that you have placed, click the account icon to the left of the search icon. This will take you to your account settings. Underneath your account information, " +
            "you will find a section titled \"My Orders\". This section will display all past orders you have placed. If you have never placed any orders with us, a message will denote that.", "To view your account " +
            "information, click the account icon to the left of the search icon. This will take you to your account settings. Within this page is all of your information in regards to your shipping and billing"};
    String[] acc_q6 = new String[]{"To view your account information, click the account icon to the left of the search icon. This will take you to your account settings. Within this page is all of your information in regards to your shipping and billing" +
            " account icon to the left of the search icon. This will take you to your account settings. Within this page is all of your information in regards to your shipping and billing" +
            " information including all of your placed orders."};

    String[] order_question = new String[] {"How can I place an order?", "How long can items be stored in my cart?", "Can I cancel an order that I just placed?",
            "Is there a limit to how many items I can purchase?", "Can you give more meaning to the order status"};
    String[] order_q1 = new String[]{"By browsing through Shapp's wide variety of items, you can select any item you prefer and add it your cart by clicking on the item you wish to purchase " +
            "and clicking on the \"Add to Cart\" button near the bottom of the item's page. Following this is a review of your cart and the items you have added to your cart. After review of your cart, " +
            "you may continue by clicking \"Proceed to Checkout\" near the bottom of the cart page. You will need to input necessary payment, shipping, and billing information to complete your order."};
    String[] order_q2 = new String[]{"All items in your cart will remain in until you log in or log out of the application. Acting out either of these decisions will result in the cart be cleared of all of its items."};
    String[] order_q3 = new String[]{"You may cancel your order up to 2 hours after its placement. To cancel the order, navigate to your account setting by clicking the account icon next to the search icon in the top left " +
            "corner. Within your account settings, choose the order you would like to cancel in the \"My Orders\" section by clicking and holding on the preferred order until a popup appears. This " +
            "popup will give you the option to cancel the order. Choose this option and confirm the cancellation of this order on the following popup."};
    String[] order_q4 = new String[]{"There is no limit on the number of items you can purchase. However, there is a limit to how much of the same item you may buy. You may only buy a quantity of five of the same items."};
    String[] order_q5 = new String[]{"Orders have three statuses: placed, shipped, and delivered. An order is placed after all information is completed by the customer. Within two hours, there will remain an opportunity to " +
            "cancel the placed order. After 24 hours without cancellation, the order is finalized and is shipped. An order that is shipped is an order that has been shipped out and is in transit to the purchasing customer. " +
            "All orders are delivered within 3-5 business days. Once delivered, the order will receive a status of \"Delivered.\" A delivered order is an order that has been successfully delivered to the purchasing customer."};

    String[] pay_questions = new String[] {"What kind of payments are accepted?", "What currencies can I pay?", "Is my privacy and personal information secure on your site?"};
    String[] pay_q1 = new String[]{"We accept Visa, MasterCard, Discover, and American Express."};
    String[] pay_q2 = new String[]{"Unfortunately, American currency is only accepted at this time. In the near future, we look forward to providing our services in exchange for foreign currency."};
    String[] pay_q3 = new String[]{"We provide among the best e-commerce service in the industry, and guarantee secure payment processing at all times."};

    String[] ship_questions = new String[] {"Do you ship to my country?", "How much is the shipping cost?", "How long will it take to receive my order?"};
    String[] ship_q1 = new String[]{"Unfortunately, we only ship to U.S. addresses."};
    String[] ship_q2 = new String[]{"Shapp offers free shipping on all orders."};
    String[] ship_q3 = new String[]{"All orders are delivered within 3-5 business days."};

/*    String[] acc_answer = new String[] {"To register for an account, go to the homepage and access the triple-striped button in the top left-hand corner of your device. " +
            "Once the navigation drawer slides out, you will notice two options. The second option will initiate the process of your account registration. Basic information " +
            "is necessary beyond this point such as your name, email, and a preferable password.", "Check your login details carefully. Make sure the email address is the same " +
            "one you used for registration, and the password you entered is correct. t is possible that the issue is a result of our errors. Please wait 10 minutes and try again.",
            "If you forgot your password, click the \"Forgot your Password?\" link on the login page. If you've already accessed the skip login option, click the three-striped button " +
            "in the top left-hand corner. This will allow you to access the navigation drawer. The first option is the link to navigate to the login page. Choose this option and click " +
            "\"Forgot your Password?\". Beyond this point, you will be asked to enter your valid email and your new password.", "N/A", "To view order that you have placed, click the " +
            "account icon to the left of the search icon. This will take you to your account settings. Underneath your account information, you will find a section titled \"My Orders\". " +
            "This section will display all past orders you have placed. If you have never placed any orders with us, a message will denote that.", "To view your account information, click the" +
            " account icon to the left of the search icon. This will take you to your account settings. Within this page is all of your information in regards to your shipping and billing" +
            " information including all of your placed orders."};
    String[] order_answer = new String[] {"By browsing through Shapp's wide variety of items, you can select any item you prefer and add it your cart by clicking on the item you wish to purchase " +
            "and clicking on the \"Add to Cart\" button near the bottom of the item's page. Following this is a review of your cart and the items you have added to your cart. After review of your cart, " +
            "you may continue by clicking \"Proceed to Checkout\" near the bottom of the cart page. You will need to input necessary payment, shipping, and billing information to complete your order.",
            "All items in your cart will remain in until you log in or log out of the application. Acting out either of these decisions will result in the cart be cleared of all of its items.", "" +
            "You may cancel your order up to 2 hours after its placement. To cancel the order, navigate to your account setting by clicking the account icon next to the search icon in the top left " +
            "corner. Within your account settings, choose the order you would like to cancel in the \"My Orders\" section by clicking and holding on the preferred order until a popup appears. This " +
            "popup will give you the option to cancel the order. Choose this option and confirm the cancellation of this order on the following popup.", "There is no limit on the number of items you " +
            "can purchase. However, there is a limit to how much of the same item you may buy. You may only buy a quantity of five of the same items.", "Orders have three statuses: placed, shipped, and " +
            "delivered. An order is placed after all information is completed by the customer. Within two hours, there will remain an opportunity to cancel the placed order. After 24 hours without " +
            "cancellation, the order is finalized and is shipped. An order that is shipped is an order that has been shipped out and is in transit to the purchasing customer. All orders are delivered " +
            "within 3-5 business days. Once delivered, the order will receive a status of \"Delivered.\" A delivered order is an order that has been successfully delivered to the purchasing customer."};
    String[] pay_answers = new String[]{"We accept Visa, MasterCard, Discover, and American Express.", "Unfortunately, American currency is only accepted at this time. In the near future, we look forward " +
            "to providing our services in exchange for foreign currency.", "We provide among the best e-commerce service in the industry, and guarantee secure payment processing at all times."};
    String[] ship_answers = new String[]{"Unfortunately, we only ship to U.S. addresses.", "Shapp offers free shipping on all orders.", "All orders are delivered within 3-5 business days."};*/

    LinkedHashMap<String, String[]> lev_acct = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> lev_order = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> lev_payment = new LinkedHashMap<>();
    LinkedHashMap<String, String[]> lev_ship = new LinkedHashMap<>();

    List<String[]> secLev = new ArrayList<>();

    List<LinkedHashMap<String, String[]>> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_page);
        setUpAdapter();

        mToolbar = (Toolbar)findViewById(R.id.help_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Help");
    }

    private void setUpAdapter() {
        secLev.add(acc_questions);
        secLev.add(order_question);
        secLev.add(pay_questions);
        secLev.add(ship_questions);

        lev_acct.put(acc_questions[0], acc_q1);
        lev_acct.put(acc_questions[1], acc_q2);
        lev_acct.put(acc_questions[2], acc_q3);
        lev_acct.put(acc_questions[3], acc_q4);
        lev_acct.put(acc_questions[4], acc_q5);
        lev_acct.put(acc_questions[5], acc_q6);

        lev_order.put(order_question[0], order_q1);
        lev_order.put(order_question[1], order_q2);
        lev_order.put(order_question[2], order_q3);
        lev_order.put(order_question[3], order_q4);
        lev_order.put(order_question[4], order_q5);

        lev_payment.put(pay_questions[0], pay_q1);
        lev_payment.put(pay_questions[1], pay_q2);
        lev_payment.put(pay_questions[2], pay_q3);

        lev_ship.put(ship_questions[0], ship_q1);
        lev_ship.put(ship_questions[1], ship_q2);
        lev_ship.put(ship_questions[2], ship_q3);

        data.add(lev_acct);
        data.add(lev_order);
        data.add(lev_payment);
        data.add(lev_ship);
        exp = (ExpandableListView) findViewById(R.id.expand_faq);

        //passing three level of information to constructor
        ThreeLevelListAdapter threeLevelListAdapter = new ThreeLevelListAdapter(this, parent, secLev, data);
        exp.setAdapter(threeLevelListAdapter);
        exp.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    exp.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(HelpPage.this, cart_list.class));
            return true;
        } else if (id == R.id.action_home) {
            startActivity(new Intent(HelpPage.this, Homepage.class));
        } else if (id == R.id.action_search) {
            startActivity(new Intent(HelpPage.this, SearchedList.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
