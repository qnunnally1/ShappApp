package com.qnally.shappapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Model.Customer;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

public class RegistrationPersonalInfo extends AppCompatActivity {

    private static final String TAG = "Email-Password";

    FirebaseAuth mAuth;

    Toolbar toolbar;
    PlacesAutocompleteTextView address;
    EditText firstname, lastname, email, password, password_verify;
    TextView city, state, zip;
    Button signup;
    Intent tohome;

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
        password = (EditText) findViewById(R.id.password_form);
        password_verify = (EditText) findViewById(R.id.password_verify_form);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("Customer");



        /*//format phone number input as user inputs phone information
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
        });*/

        signup = (Button) findViewById(R.id.next_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();

                //createUser(email.getText().toString(), password.getText().toString());

                final ProgressDialog mDialog = new ProgressDialog(RegistrationPersonalInfo.this);
                mDialog.show();

                if(!firstname.getText().toString().equals("") && !lastname.getText().toString().equals("")
                        && !email.getText().toString().equals("") && !password.getText().toString().equals("")
                        && !password.getText().toString().equals("")
                        && !password_verify.getText().toString().equals("")){

                    table_customer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String email2string = email.getText().toString().replace(".com","");

                            if(dataSnapshot.child(email2string).exists()){
                                mDialog.dismiss();
                                Toast.makeText(RegistrationPersonalInfo.this, "Email already exists", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mDialog.dismiss();
                                Customer customer = new Customer(firstname.getText().toString(),lastname.getText().toString(),password.getText().toString(),email2string, null, null, null);
                                table_customer.child(email2string).setValue(customer);
                                finish();
                                tohome = new Intent(getApplicationContext(), Homepage.class);
                                Common.current = customer;
                                startActivity(tohome);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    check();
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void check(){

        if(firstname.getText().toString().equals("")){
            firstname.setHintTextColor(Color.RED);
            firstname.setHint("Enter first name");
        }
        if(lastname.getText().toString().equals("")) {
            lastname.setHintTextColor(Color.RED);
            lastname.setHint("Enter last name");
        }
        if(email.getText().toString().equals("")){
            email.setHintTextColor(Color.RED);
            email.setHint("Enter email");
        }
        if(password.getText().toString().equals("")){
            password.setHintTextColor(Color.RED);
            password.setHint("Enter password");
        }
        if(password_verify.getText().toString().equals("")){
            password_verify.setHintTextColor(Color.RED);
            password_verify.setHint("Re-enter password");
        }
    }

    private void createUser(String email, String password) {
        Log.d(TAG, "signIn: "+ email);

        final ProgressDialog mDialog = new ProgressDialog(RegistrationPersonalInfo.this);
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mDialog.dismiss();
                            Log.d(TAG, "sign:signInWithEmailSuccessful");
                            FirebaseUser user = mAuth.getCurrentUser();
                        }
                        else{
                            Log.w(TAG, "signInWithEmailFailure", task.getException());
                            Toast.makeText(RegistrationPersonalInfo.this, "Failed Sign In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
