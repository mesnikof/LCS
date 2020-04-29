package com.cs360.michaelmesnikoff.lcs;

/*
 * This activity displays a "Splash Screen" on app startup.
 * No inputs, no real outputs, just calls the "Login Activity"
 * after the defined 3000 milliseconds of display.
 */

/*
 * First, import some needed libraries for thier internal methods.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
 * Now declare the class, just to make things work.
 */
public class SplashScreenActivity extends AppCompatActivity {
    /*
     * Create a Context instance variable.
     */
    Context context;

    /*
     * Create an instane of the Helpers class.
     */
    Helpers helpers;

    /*
     * Create and instantiate the variables used for utilizing the Shared Preference persistent data.
     */
    protected static final String ORDER_STATUS_PREFS = "My_OrderStatus_Prefs";
    protected static final String ORDER_LIST_KEY = "order_list_key";
    SharedPreferences.Editor sharedPref_myEditor;

    /*
     * Declare a DBmanager instance to perform the first-time app use database
     * initialization steps.
     */
    private DBManager dbManager;

    /*
     * Define the override for the onCreate method to set up our own
     * creation of the splash screen.  This also points to the "layout"
     * file that contains the, well, layout of the screen when the
     * splash screen displays.  No comments are contained in the layout
     * file.
     */
    @SuppressLint("ApplySharedPref")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*
         * Set up to completely exit the app when the "Logoff" button is pressed.
         */
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }

        /*
         * Set up the "Action Bar" (the band along the top of the screen)
         * with the app's icon.
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_round);
        }

        /*
         * Instantiate the Helpers class.
         */
        helpers = new Helpers(this);

        /*
         * Run the first-time-app-use database initialization tasks.  This will run
         * each time the app is started, but won't actually do anything f the database
         * and records already exist.
         */
        dbManager = new DBManager(SplashScreenActivity.this);
        dbManager.open();
        long initItemsReturn = dbManager.initializeDB();

        /*
         * Clear out the Order List Shared Preferences string.
         *
         * First, create a Shared Preference instance for maintaining persistent data.
         * Then clear the Order List data.  Nte: This only takes place on app "ground-zero"
         * startup, not on app resume.  This is a deliberate design choice.
         *
         * Finally, initialize the shopping cart shared preference values.
         */
        SharedPreferences order_listPref = this.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE);
        String stringOrderList = order_listPref.getString(ORDER_LIST_KEY, null);
        sharedPref_myEditor = this.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE).edit();
        sharedPref_myEditor.clear();
        sharedPref_myEditor.commit();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
