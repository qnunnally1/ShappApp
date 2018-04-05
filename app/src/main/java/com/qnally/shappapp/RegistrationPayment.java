package com.qnally.shappapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;
import com.devmarvel.creditcardentry.library.CreditCardForm;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Model.BillingAddress;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Model.PlacedOrder;
import com.qnally.shappapp.Model.ShippingAddress;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//import com.stripe.android.model.Card;
//import com.stripe.android.view.CardMultilineWidget;


public class RegistrationPayment extends AppCompatActivity{

    private CreditCardForm form;

    Toolbar toolbar;
    PlacesAutocompleteTextView billing_address, ship_address;
    EditText card_name;
    TextView bcity, bstate, bzip, ship_city, ship_state, ship_zip, totalPrice;
    Button completeOrder;
    Intent complete;
    CheckBox bill_check;

    BillingAddress ba;
    ShippingAddress sa;

    FirebaseDatabase mDatabase;
    DatabaseReference mReference;

    String name = "", email = "", city = "", zip = "", state = "", address = "", total = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_payment);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference("Orders");

        //setting up toolbar to give option to close if registration is not completed
        toolbar = (Toolbar) findViewById (R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Information");

        //setting up navigation button to go back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //hide app title within toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set up views to declare with autocomplete
        billing_address = (PlacesAutocompleteTextView) findViewById(R.id.billing_address_autocomplete);
        ship_address = (PlacesAutocompleteTextView) findViewById(R.id.address_autocomplete);
        bcity = (TextView) findViewById(R.id.payment_city);
        bstate = (TextView) findViewById(R.id.payment_state);
        bzip = (TextView) findViewById(R.id.payment_zip);
        form = (CreditCardForm) findViewById(R.id.crdt_info);
        ship_city = (TextView) findViewById(R.id.add_city);
        ship_state = (TextView) findViewById(R.id.add_state);
        ship_zip = (TextView) findViewById(R.id.add_zip);
        completeOrder = (Button) findViewById(R.id.completeOrder);
        totalPrice = (TextView) findViewById(R.id.total);
        totalPrice.setText(getIntent().getStringExtra("Total"));


        billing_address.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                billing_address.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        billing_address.setText(placeDetails.name);
                        for (AddressComponent component : placeDetails.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        bcity.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        bstate.setText(component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        bzip.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("test", "failure " + throwable);
                    }
                });
            }
        });

        ba = new BillingAddress(billing_address.getText().toString(), bcity.getText().toString(),
                bstate.getText().toString(), bzip.getText().toString());

        Common.current.setBa(ba);

        ship_address.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                ship_address.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        ship_address.setText(placeDetails.name);
                        for (AddressComponent component : placeDetails.address_components) {
                            for (AddressComponentType type : component.types) {
                                switch (type) {
                                    case STREET_NUMBER:
                                        break;
                                    case ROUTE:
                                        break;
                                    case NEIGHBORHOOD:
                                        break;
                                    case SUBLOCALITY_LEVEL_1:
                                        break;
                                    case SUBLOCALITY:
                                        break;
                                    case LOCALITY:
                                        ship_city.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        ship_state.setText(component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        ship_zip.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("test", "failure " + throwable);
                    }
                });
            }
        });

        //setting up checkbox to use same billing address used in prior registration
        bill_check = (CheckBox) findViewById(R.id.billing_chkbx);
        bill_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    ship_address.setVisibility(View.INVISIBLE);
                    ship_city.setVisibility(View.INVISIBLE);
                    ship_state.setVisibility(View.INVISIBLE);
                    ship_zip.setVisibility(View.INVISIBLE);

                    sa = new ShippingAddress(billing_address.getText().toString(), bcity.getText().toString(),
                            bstate.getText().toString(), bzip.getText().toString());

                    Common.current.setSa(sa);
                }else{
                    ship_address.setVisibility(View.VISIBLE);
                    ship_city.setVisibility(View.VISIBLE);
                    ship_state.setVisibility(View.VISIBLE);
                    ship_zip.setVisibility(View.VISIBLE);
                }
            }
        });

        sa = new ShippingAddress(ship_address.getText().toString(), ship_city.getText().toString(),
                ship_state.getText().toString(), ship_zip.getText().toString());

        Common.current.setSa(sa);

        Intent intent = getIntent();
        name = (intent.getExtras().getString("First Name") + intent.getExtras().getString("Last Name"));
        email = intent.getExtras().getString("Email");
        total = intent.getExtras().getString("Total");
        city = bcity.getText().toString();
        state = bstate.getText().toString();
        zip = bzip.getText().toString();

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

                String cred = form.getCreditCard().getCardNumber();
                Common.current.setCreditcard(cred);

                Date date = new Date();
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, yyyy");
                String orderDate = dateFormatter.format(date);

                PlacedOrder order = new PlacedOrder(
                        name,
                        email,
                        total,
                        sa,
                        ba,
                        ((List<Order>) getIntent().getExtras().getSerializable("Order")),
                        orderDate,
                        String.valueOf(System.currentTimeMillis())
                );

                mReference.child(String.valueOf(System.currentTimeMillis())).setValue(order);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(RegistrationPayment.this, "Thank you! You order has been placed.", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(RegistrationPayment.this, Homepage.class));
            }
        });
    }

    private void check() {
            if(bcity.getText().toString().equals("")){
                bcity.setHintTextColor(Color.RED);
                bcity.setHint("Enter address");
            }
            if(bcity.getText().toString().equals("")){
                bcity.setHintTextColor(Color.RED);
                bcity.setHint("Enter address");
            }
            if(bstate.getText().toString().equals("")) {
                bstate.setHintTextColor(Color.RED);
                bstate.setHint("Enter address");
            }
            if(bzip.getText().toString().equals("")){
                bzip.setHintTextColor(Color.RED);
                bzip.setHint("Enter address");
            }
            if(!bill_check.isChecked()){

                if(ship_address.getText().toString().equals("")) {
                    ship_address.setHintTextColor(Color.RED);
                    ship_address.setHint("Enter address");
                }
                if(ship_city.getText().toString().equals("")){
                    ship_city.setHintTextColor(Color.RED);
                    ship_city.setHint("Enter address");
                }
                if(ship_state.getText().toString().equals("")) {
                    ship_state.setHintTextColor(Color.RED);
                    ship_state.setHint("Enter address");
                }
                if(ship_zip.getText().toString().equals("")){
                    ship_zip.setHintTextColor(Color.RED);
                    ship_zip.setHint("Enter address");
                }
            }
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
            startActivity(new Intent(RegistrationPayment.this, cart_list.class));
            return true;
        } else if ( id == R.id.action_home) {
            startActivity(new Intent(RegistrationPayment.this, Homepage.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
