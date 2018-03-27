package com.qnally.shappapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Model.Order;
import com.qnally.shappapp.Model.PlacedOrder;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.List;
//import com.stripe.android.model.Card;
//import com.stripe.android.view.CardMultilineWidget;


public class RegistrationPayment extends AppCompatActivity{

    private CreditCardForm form;

    Toolbar toolbar;
    PlacesAutocompleteTextView billing_address;
    EditText card_name;
    TextView bcity, bstate, bzip;
    Button completeOrder;
    Intent complete;
    CheckBox bill_check;

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

        //setting up navigation button to go back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //hide app title within toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set up views to declare with autocomplete
        billing_address = (PlacesAutocompleteTextView) findViewById(R.id.billing_address_autocomplete);
        bcity = (TextView) findViewById(R.id.payment_city);
        bstate = (TextView) findViewById(R.id.payment_state);
        bzip = (TextView) findViewById(R.id.payment_zip);
        form = (CreditCardForm) findViewById(R.id.crdt_info);

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

        //setting up checkbox to use same billing address used in prior registration
        bill_check = (CheckBox) findViewById(R.id.billing_chkbx);
        bill_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    billing_address.setVisibility(View.INVISIBLE);
                    bcity.setVisibility(View.INVISIBLE);
                    bstate.setVisibility(View.INVISIBLE);
                    bzip.setVisibility(View.INVISIBLE);
                }else{
                    billing_address.setVisibility(View.VISIBLE);
                    bcity.setVisibility(View.VISIBLE);
                    bstate.setVisibility(View.VISIBLE);
                    bzip.setVisibility(View.VISIBLE);
                }
            }
        });

        Intent intent = getIntent();
        name = (intent.getExtras().getString("First Name") + intent.getExtras().getString("Last Name"));
        email = intent.getExtras().getString("Email");
        total = intent.getExtras().getString("Total");
        address = billing_address.getText().toString();
        city = bcity.getText().toString();
        state = bstate.getText().toString();
        zip = bzip.getText().toString();


        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacedOrder order = new PlacedOrder(
                        name,
                        email,
                        address + ", " + city + ", " + state +  ", " + zip,
                        total,
                        ((List<Order>) getIntent().getExtras().getSerializable("Order"))
                );

                mReference.child(String.valueOf(System.currentTimeMillis())).setValue(order);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(RegistrationPayment.this, "Thank you! You order has been placed.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

}
