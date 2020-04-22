package com.cs360.michaelmesnikoff.lcs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
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
        Button dialogButton = dialog.findViewById(R.id.cu_dlg_button_Back);
        // If button is clicked, close the custom dialog.
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(context, "Dismissed..!!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
}
