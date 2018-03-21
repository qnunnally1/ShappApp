package com.qnally.shappapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.qnally.shappapp.Common.Common;
import com.qnally.shappapp.Model.Customer;

import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity {

    EditText usremail, usrpasswd;
    Button signup, signin;
    ImageButton skip;
    Intent register, tohomepage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usremail = (EditText) findViewById(R.id.email_field);
        usrpasswd = (EditText) findViewById(R.id.passwdField);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("Customer");

        signin = (Button) findViewById(R.id.signinbtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.show();

                table_customer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String email2string = usremail.getText().toString().replace(".com","");

                        //check if customer exists in database
                        if(dataSnapshot.child(email2string).exists()) {

                            //get customer info
                            mDialog.dismiss();
                            Customer cust = dataSnapshot.child(email2string).getValue(Customer.class);
                            if (cust != null) {
                                if (cust.getPassword().equals(usrpasswd.getText().toString())) {
                                    tohomepage = new Intent(getApplicationContext(), Homepage.class);
                                    Common.current = cust;
                                    startActivity(tohomepage);
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Failed Sign In", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            mDialog.dismiss();
                            Toast.makeText(Login.this, "Credentials Not Recognized", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });



        signup = (Button) findViewById(R.id.create_new_acct);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register = new Intent(getApplicationContext(), RegistrationPersonalInfo.class);
                startActivity(register);
            }
        });

        skip = (ImageButton) findViewById(R.id.skip_button);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tohomepage = new Intent(getApplicationContext(), Homepage.class);
                startActivity(tohomepage);
            }
        });
    }
}
