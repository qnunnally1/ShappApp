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
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;
//import com.stripe.android.model.Card;
//import com.stripe.android.view.CardMultilineWidget;


public class RegistrationPayment extends AppCompatActivity implements OnCardFormSubmitListener,
        CardEditText.OnCardTypeChangedListener{

    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX};

    private SupportedCardTypesView mSupportedCardTypesView;
    protected CardForm mCardForm;

    Toolbar toolbar;
    PlacesAutocompleteTextView billing_address;
    EditText card_name;
    TextView bcity, bstate, bzip;
    Button create;
    Intent create_account;
    CheckBox bill_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_payment);

        //setting up toolbar to give option to close if registration is not completed
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting up navigation button to go back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //hide app title within toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //set up views to declare with autocomplete
        billing_address = (PlacesAutocompleteTextView) findViewById(R.id.billing_address_autocomplete);
        bcity = (TextView) findViewById(R.id.city);
        bstate = (TextView) findViewById(R.id.state);
        bzip = (TextView) findViewById(R.id.zip);
        card_name = (EditText) findViewById(R.id.card_name_form);

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

        create = (Button) findViewById(R.id.create_acct_btn);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean info_filled = false;

                /*if(mCardForm.getCardNumber().equals("") || mCardForm.getExpirationMonth().equals("") || mCardForm.getCvv().equals("")){
                    mCardForm.setCardNumberError("Enter card information!");
                }*/
                if(card_name.getText().toString().equals("")){
                    card_name.setHintTextColor(Color.RED);
                    card_name.setHint("Enter card name!");
                }
                if(!bill_check.isChecked() && (billing_address.getText().toString().equals("") || bcity.getText().toString().equals("")
                    || bstate.getText().toString().equals("") || bzip.getText().toString().equals(""))){
                    billing_address.setHintTextColor(Color.RED);
                    billing_address.setHint("Enter billing address!");
                }

                if((bill_check.isChecked())){
                    if(!card_name.getText().toString().equals("")){
                        info_filled = true;
                    }
                }else{
                    if(!card_name.getText().toString().equals("") && !billing_address.getText().toString().equals("") && bcity.getText().toString().equals("")
                            && !bstate.getText().toString().equals("") && !bzip.getText().toString().equals("")){
                        info_filled = true;
                    }
                }

                if(info_filled) {
                    create_account = new Intent(getApplicationContext(), RegistrationPayment.class);
                    startActivity(create_account);
                }
            }
        });
    }


    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Toast.makeText(this, R.string.valid, Toast.LENGTH_SHORT).show();
        } else {
            mCardForm.validate();
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }

}
