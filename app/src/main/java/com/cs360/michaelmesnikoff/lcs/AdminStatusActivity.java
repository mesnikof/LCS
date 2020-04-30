package com.cs360.michaelmesnikoff.lcs;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AdminStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_status);

        /*
         * Set up the Action Bar to display the app's name and icon.
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_round);
        }

        /*
         * Now create the Shared Preference tools to store the various status info that is passed
         * back and forth between the LCS staff and the user.
         */
        SharedPreferences order_statusPref = getSharedPreferences(MainActivity.ORDER_STATUS_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor order_StatusEditor = getSharedPreferences(MainActivity.ORDER_STATUS_PREFS, MODE_PRIVATE).edit();

        EditText etOrderNumber = findViewById(R.id.editText_OrderNumber);

        final RadioButton rbReceived = findViewById(R.id.RadioButtonReceived);
        final RadioButton rbProcessing = findViewById(R.id.RadioButtonProcessing);
        final RadioButton rbReady =  findViewById(R.id.RadioButtonReady);
        final RadioButton rbComplete = findViewById(R.id.RadioButtonComplete);

        Button submit = findViewById(R.id.buttonUpdateStatus);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbReceived.isChecked()) {
                    order_StatusEditor.putString("666", "Received");
                } else if (rbProcessing.isChecked()) {
                    order_StatusEditor.putString("666", "Processing - stnd by...");
                } else if (rbReady.isChecked()) {
                    order_StatusEditor.putString("666", "Ready for pickup...");
                } else if (rbComplete.isChecked()) {
                    order_StatusEditor.putString("666", "Order complete");
                }
                order_StatusEditor.commit();

                Toast.makeText(getApplicationContext(), "Status updated...", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }
}
