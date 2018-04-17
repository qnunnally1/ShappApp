package com.qnally.shappapp.Helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.qnally.shappapp.R;
import com.qnally.shappapp.cart_list;


public class DialogHelper extends AppCompatDialogFragment {
    private EditText dEmail, dPassword;
    private Button create;
    private DialogHelperListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_customer = database.getReference("Customer");

        AlertDialog.Builder login = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.login_dialog, null);

        login.setView(mView)
                .setTitle("")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = dEmail.getText().toString();
                        String password = dPassword.getText().toString();
                        listener.tryCredentials(username,password);
                    }
                });

        dEmail = (EditText) mView.findViewById(R.id.dg_email);
        dPassword = (EditText) mView.findViewById(R.id.dg_password);
        create = (Button) mView.findViewById(R.id.dg_create_new_acct);

        return login.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (DialogHelperListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogHelperListener");
        }
    }

    public interface DialogHelperListener{
        void tryCredentials(String username, String password);
    }
}
