package com.qnally.shappapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.qnally.shappapp.Database.Database;
import com.qnally.shappapp.Model.Customer;

import java.util.Timer;
import java.util.TimerTask;

public class Login extends AppCompatActivity{

    private static final String TAG = "Email-Password";
    private FirebaseAuth mAuth;

    EditText usremail, usrpasswd;
    Button signup, signin, forgot;
    ImageButton skip;
    Intent register, tohomepage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            //startActivity(SignedInActivity.createIntent(this, null));
            finish(); }

        usremail = (EditText) findViewById(R.id.email_field);
        usrpasswd = (EditText) findViewById(R.id.passwdField);
        forgot = (Button)findViewById(R.id.forgot_passwd);

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("Customer");

        signin = (Button) findViewById(R.id.signinbtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.show();

                //signIn(usremail.getText().toString(), usrpasswd.getText().toString());

                if(!usremail.getText().toString().equals("") && !usrpasswd.getText().toString().equals("")) {
                    table_customer.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String email2string = usremail.getText().toString().replace(".com", "");

                            //check if customer exists in database
                            if (dataSnapshot.child(email2string).exists()) {

                                //get customer info
                                mDialog.dismiss();
                                Customer cust = dataSnapshot.child(email2string).getValue(Customer.class);
                                if (cust != null) {
                                    if (cust.getPassword().equals(usrpasswd.getText().toString())) {
                                        tohomepage = new Intent(getApplicationContext(), Homepage.class);
                                        Customer cust2 = new Customer(cust.getFirst_name(), cust.getLast_name(), cust.getPassword(), email2string, cust.getShipping_Address(), cust.getBilling_Address(), cust.getPayment());
                                        Common.current = cust2;
                                        new Database(getBaseContext()).cleanCart();
                                        startActivity(tohomepage);
                                        finish();

                                        table_customer.removeEventListener(this);
                                    } else {
                                        Toast.makeText(Login.this, "Failed Sign In", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(Login.this, "Credentials Not Recognized", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    mDialog.dismiss();
                    if(usrpasswd.getText().toString().equals("")){
                        usrpasswd.setError("Password field is empty");
                    }

                    if(usremail.getText().toString().equals("")){
                        usremail.setError("Email field is empty");
                    }
                }
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
                skipSignIn();
                Common.current = null;
                tohomepage = new Intent(getApplicationContext(), Homepage.class);
                startActivity(tohomepage);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });
    }

    private void skipSignIn() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn: "+ email);

        final ProgressDialog mDialog = new ProgressDialog(Login.this);
        mDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
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
                            Toast.makeText(Login.this, "Failed Sign In", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
