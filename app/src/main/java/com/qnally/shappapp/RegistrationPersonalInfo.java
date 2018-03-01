package com.qnally.shappapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

public class RegistrationPersonalInfo extends AppCompatActivity {

    Toolbar toolbar;
    PlacesAutocompleteTextView address;
    EditText firstname, lastname, email, phone;
    TextView city, state, zip;
    Button nextbtn;
    Intent topayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_personal_info);

        //setting up toolbar to give option to close if registration is not completed
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting up navigation button to go back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //hide app title within toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set up textviews to declare with autocomplete
        firstname = (EditText) findViewById(R.id.first_name_form);
        lastname = (EditText) findViewById(R.id.last_name_form);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone_number);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        zip = (TextView) findViewById(R.id.zip);

        address = (PlacesAutocompleteTextView) findViewById(R.id.billing_address_autocomplete);
        address.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                address.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        address.setText(placeDetails.name);
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
                                        city.setText(component.long_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_1:
                                        state.setText(component.short_name);
                                        break;
                                    case ADMINISTRATIVE_AREA_LEVEL_2:
                                        break;
                                    case COUNTRY:
                                        break;
                                    case POSTAL_CODE:
                                        zip.setText(component.long_name);
                                        break;
                                    case POLITICAL:
                                        break;
                                }
                            }
                        }
                        address.dismissDropDown();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d("test", "failure " + throwable);
                    }
                });
            }
        });

        //format phone number input as user inputs phone information
        phone.addTextChangedListener(new TextWatcher() {

            int keyDel;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            keyDel = 1;
                        return false;
                    }
                });

                if (keyDel == 0) {
                    int len = phone.getText().length();
                    if(len == 3 || len == 7) {
                        phone.setText(phone.getText() + "-");
                        phone.setSelection(phone.getText().length());
                    }
                } else {
                    keyDel = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        nextbtn = (Button) findViewById(R.id.next_btn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstname.getText().toString().equals("")){
                    firstname.setHintTextColor(Color.RED);
                    firstname.setHint("Enter first name!");
                }
                if(lastname.getText().toString().equals("")){
                    lastname.setHintTextColor(Color.RED);
                    lastname.setHint("Enter last name!");
                }
                if(address.getText().toString().equals("")){
                    address.setHintTextColor(Color.RED);
                    address.setHint("Enter address!");
                }
                if(city.getText().toString().equals("")){
                    city.setHintTextColor(Color.RED);
                    city.setHint("Enter address!");
                }
                if(state.getText().toString().equals("")){
                    state.setHintTextColor(Color.RED);
                    state.setHint("Enter address!");
                }
                if(zip.getText().toString().equals("")){
                    zip.setHintTextColor(Color.RED);
                    zip.setHint("Enter address!");
                }
                if(email.getText().toString().equals("")){
                    email.setHintTextColor(Color.RED);
                    email.setHint("Enter email!");
                }
                if(phone.getText().toString().equals("")){
                    phone.setHintTextColor(Color.RED);
                    phone.setHint("Enter phone number!");
                }

                if(!firstname.getText().toString().equals("") && !lastname.getText().toString().equals("") && !address.getText().toString().equals("")
                        && !city.getText().toString().equals("") && !state.getText().toString().equals("") && !zip.getText().toString().equals("")
                        && !email.getText().toString().equals("") && !phone.getText().toString().equals(""))
                {
                    topayment = new Intent(getApplicationContext(),RegistrationPayment.class);
                    startActivity(topayment);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("First Name", firstname.getText().toString());
        savedInstanceState.putString("Last Name", lastname.getText().toString());
        savedInstanceState.putString("Street Address", address.getText().toString());
        savedInstanceState.putString("City", city.getText().toString());
        savedInstanceState.putString("State", state.getText().toString());
        savedInstanceState.putString("Zip Code", zip.getText().toString());
        savedInstanceState.putString("Email", email.getText().toString());
        savedInstanceState.putString("Phone", phone.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        String myfirstname = savedInstanceState.getString("First Name");
        String mylastname = savedInstanceState.getString("Last Name");
        String myaddress = savedInstanceState.getString("Street Address");
        String mycity = savedInstanceState.getString("City");
        String mystate = savedInstanceState.getString("State");
        String myzip = savedInstanceState.getString("Zip Code");
        String myemail = savedInstanceState.getString("Email");
        String myphone = savedInstanceState.getString("Phone");
    }
}
