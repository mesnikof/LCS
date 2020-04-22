package com.cs360.michaelmesnikoff.lcs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by mp on 4/21/20.
 */

/*
 * This class contains the tools to populate the "Create New User Account" dialog, and then
 * act upon the user's actions within this dialog.  After activity control is retured to the
 * calling activity.
 *
 * Within the LCS app this should only be instantiated from the Login Activity when the user
 * clicks on the "Create New User/Account" icon/button.
 */
public class CreateNewUser {

    /*
     * Create some "class-global" variables.
     */
    private Context context;

    private EditText usernameET;
    private EditText passwordET;
    private EditText passwordConfET;
    private EditText emailET;
    private EditText emailConfET;
    private EditText cardET;
    private EditText cardConfET;
    private EditText cvvET;
    private EditText cvvConfET;
    private EditText expireET;
    private EditText expireConfET;

    /*
     * This is the basic class constructor.  It takes a single context argument.
     */
    public CreateNewUser(Context c) {

        /*
         * Initialize the context variable with the calling context.
         */
        context = c;

        /*
         * This "Toast" is for testing purposes to show entry into the constructor.
         */
        Toast.makeText(context, "In the class...", Toast.LENGTH_LONG).show();

        /*
         * Load and display the custom dialog.
         */
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.login_create_user);

        Button cu_dlg_BackButton = dialog.findViewById(R.id.cu_dlg_button_Back);
        Button cu_dlg_CreateButton = dialog.findViewById(R.id.cu_dlg_button_Add);

        // If button is clicked, close the custom dialog.
        cu_dlg_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(context, "Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });

        // If button is clicked, close the custom dialog.
        cu_dlg_CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Create local instances of all the data fields.
                 */
                usernameET = dialog.findViewById(R.id.cu_dlg_editText_username);
                passwordET = dialog.findViewById(R.id.cu_dlg_editText_password);
                passwordConfET = dialog.findViewById(R.id.cu_dlg_editText_password2);
                emailET = dialog.findViewById(R.id.cu_dlg_editText_email);
                emailConfET = dialog.findViewById(R.id.cu_dlg_editText_email2);
                cardET = dialog.findViewById(R.id.cu_dlg_editText_card);
                cardConfET = dialog.findViewById(R.id.cu_dlg_editText_card2);
                cvvET = dialog.findViewById(R.id.cu_dlg_editText_cvv);
                cvvConfET = dialog.findViewById(R.id.cu_dlg_editText_cvv2);
                expireET = dialog.findViewById(R.id.cu_dlg_editText_expire);
                expireConfET = dialog.findViewById(R.id.cu_dlg_editText_expire2);

                OnCreateClick(v);
                //dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void OnCreateClick(View v) {
        /*
         * Create a result variable.
         */
        int result;

        /*
         * String and int variables to load for the comparisons.
         */
        String strA;
        String strB;
        int intA;
        int intB;

        /*
         * Check for unfilled fields.  If there are any, show a "Toast message and return
         * to the dialog.
         */
        if ((usernameET == null) ||
                (passwordET == null) || (passwordConfET == null) ||
                (emailET == null) || (emailConfET == null) ||
                (cardET == null) || (cardConfET == null) ||
                (cvvET == null) || (cvvConfET == null) ||
                (expireET == null) || (expireConfET == null)) {
            Toast.makeText(context, "All fields must be filled.", Toast.LENGTH_LONG).show();
            return;
        }

        /*
         * Now check each pair of values;  If there are any non-matches show a "Toast" message
         * and return the focus to unmatched field.
         *
         * We just look at each.  No need to reset the result.  Once each result returns valid
         * we just fall through to the next task.
         */
        strA = passwordET.getText().toString();
        strB = passwordConfET.getText().toString();
        result = ComparePairs(strA, strB);
        if (result == 0) {
            Toast.makeText(context, "Passwords must match.", Toast.LENGTH_LONG).show();
            passwordET.setFocusableInTouchMode(true);
            passwordET.requestFocus();
            return;
        }

        strA = emailET.getText().toString();
        strB = emailConfET.getText().toString();
        result = ComparePairs(strA, strB);
        if (result == 0) {
            Toast.makeText(context, "Addresses must match.", Toast.LENGTH_LONG).show();
            emailET.setFocusableInTouchMode(true);
            emailET.requestFocus();
            return;
        }

        intA = Integer.parseInt(cardET.getText().toString());
        intB = Integer.parseInt(cardConfET.getText().toString());
        result = ComparePairs(intA, intB);
        if (result == 0) {
            Toast.makeText(context, "CC Numbers must match.", Toast.LENGTH_LONG).show();
            cardET.setFocusableInTouchMode(true);
            cardET.requestFocus();
            return;
        }

        intA = Integer.parseInt(cvvET.getText().toString());
        intB = Integer.parseInt(cvvConfET.getText().toString());
        result = ComparePairs(intA, intB);
        if (result == 0) {
            Toast.makeText(context, "CVV Numbers must match.", Toast.LENGTH_LONG).show();
            cvvET.setFocusableInTouchMode(true);
            cvvET.requestFocus();
            return;
        }

        strA = expireET.getText().toString();
        strB = expireConfET.getText().toString();
        result = ComparePairs(strA, strB);
        if (result == 0) {
            Toast.makeText(context, "Expire Dates must match.", Toast.LENGTH_LONG).show();
            expireET.setFocusableInTouchMode(true);
            expireET.requestFocus();
            return;
        }

        /*
         * If we got to here all the data integrity checks were valid.  Now do the actual
         * database insertion.
         */

        Toast.makeText(context, "Create Method..!!", Toast.LENGTH_LONG).show();
    }

    /*
     * This method performs the comparison between two strings (i.e. passowrds).
     *
     * It is also overloaded to compare ints.
     *
     * Return: 1 for ==, 0 for !=
     */
    private int ComparePairs(String stringA, String stringB) {
        if (stringA.equals(stringB)) {
            return 1;
        }
        else {
            return 0;
        }
    }

    /*
     * This method performs the comparison between two strings (i.e. passowrds).
     *
     * It is also overloaded to compare Strings.
     *
     * Return: 1 for ==, 0 for !=
     */
    private int ComparePairs(int intA, int intB) {
        if (intA == intB) {
            return 1;
        }
        else {
            return 0;
        }
    }
}