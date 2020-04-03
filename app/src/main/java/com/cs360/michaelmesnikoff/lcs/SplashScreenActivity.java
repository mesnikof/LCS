package com.cs360.michaelmesnikoff.lcs;

/*
 * This activity displays a "Splash Screen" on app startup.
 * No inputs, no real outputs, just calls the "Login Activity"
 * after the defined 3000 milliseconds of display.
 */

/*
 * First, import some needed libraries for thier internal methods.
 */
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
 * Now declare the class, just to make things work.
 */
public class SplashScreenActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /*
         * Set up the "Action Bar" (the band along the top of the screen)
         * with the app's icon.
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);

        /*
         * Run the first-time-app-use database initialization tasks.  This will run
         * each time the app is started, but won't actually do anything f the database
         * and records already exist.
         *
         * ToDo: Add error and exception testing.
         */
        dbManager = new DBManager(SplashScreenActivity.this);
        dbManager.open();
        long initItemsReturn = dbManager.initializeDB();

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
