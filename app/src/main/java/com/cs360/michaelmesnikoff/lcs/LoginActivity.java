package com.cs360.michaelmesnikoff.lcs;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.view.View;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import android.database.Cursor;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    /*
     * Create DBManager and DBHelper instances to access the LCS database for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;

    /*
     * Create ArrayList variables to hold the returned USERS table data.
     */
    ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> passwords = new ArrayList<String>();
    ArrayList<String> emails = new ArrayList<String>();

    /*
     * Create a series of variables for use later dealing with the shared preferences.
     */
    public String stringUsername;
    public static final String LOGIN_PREFS = "My_Login_Prefs";
    public static final String LOGIN_USERNAME_KEY = "login_username_key";
    EditText login_usernameET;
    SharedPreferences.Editor login_myEditor;

    /*
     * Create a series of variables for representing widgets on the Login layout.
     */
    private static EditText editTextUsername;
    private static EditText editTextPassword;
    private static TextView textViewAttempts;
    private static ImageButton login_button;
    private static TextView textDisplay;

    /*
     * Also a widget variable, this one for keeping count of login attempts.
     */
    int attempt_counter = 5;


    /*
     * The basic onCreate() method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
         * Set the ActionBar to show the LCS icon.
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(LoginActivity.this);
        dbManager.open_read();
        Cursor cursor = dbManager.fetch(DBHelper.USERS_TABLE_NAME);

        if (cursor.getCount() == 0) {
            Toast.makeText(LoginActivity.this, "USERS database returned no data.", Toast.LENGTH_LONG).show();
        }

        /*
         * Load the username/password/email information into a series of String arrays to pass into
         * other methods.
         *
         * Display the USERS table information.
         *
         * This is ONLY for demonstration purposes.  This MUST be removed for production use.
         */
        textDisplay = findViewById(R.id.textDisplay);
        String textDisplayString = "";
        cursor.moveToFirst();
        do {
            usernames.add(cursor.getString(cursor.getColumnIndex("username")));
            passwords.add(cursor.getString(cursor.getColumnIndex("password")));
            emails.add(cursor.getString(cursor.getColumnIndex("email")));

            textDisplayString += cursor.getString(cursor.getColumnIndex("username"));
            textDisplayString += ", ";
            textDisplayString += cursor.getString(cursor.getColumnIndex("password"));
            textDisplayString += ", ";
            textDisplayString += cursor.getString(cursor.getColumnIndex("email"));
            textDisplayString += "\n";
        } while(cursor.moveToNext());
        textDisplay.setText(textDisplayString);

        /*
         * Close the database for cleanliness.
         */
        dbManager.close();

        /*
         * Check the shared preferences for user info.
         * If it is there, load it into the Username widget.
         */
        login_usernameET = findViewById(R.id.editText_username);
        SharedPreferences login_usernamePref = getSharedPreferences(LoginActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);
        login_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

        if (!TextUtils.isEmpty(stringUsername)) {
            login_usernameET.setText(stringUsername);
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.first_time_user, Toast.LENGTH_LONG).show();
        }

        /*
         * Now call the method to listen for activity on the login button.
         */
        LoginButton(usernames, passwords, emails);
    }


    /*
     * The onPause method.
     * Write out the Shared Preferences information.
     */
    protected void onPause() {
        super.onPause();
        login_myEditor.putString(LOGIN_USERNAME_KEY, login_usernameET.getText().toString());
        login_myEditor.apply();
    }


    /*
     * This method simply clears the information in the Username widget, and from the stored
     * Shared Preferences Username key:value data.
     *
     * A "Toast" message is generated for either success or failure.
     */
    public void clearLoginPreferences(View view) {
        if (!TextUtils.isEmpty(stringUsername)) {
            login_myEditor.clear();
            login_myEditor.apply();
            login_usernameET.setText("");
            Toast.makeText(LoginActivity.this, R.string.preference_deleted, Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(LoginActivity.this, R.string.no_preference_removed, Toast.LENGTH_LONG).show();
        }
    }


    /*
     * This is the method that listens for login activity.
     *
     * It checks username/password info for validity.  It also counts the number of login
     * attempts, and exits on too mainy failures
     *
     * (Appropriate "Toast" messages are displayed when necessary.
     *
     * It calls the appropriate activity based upon which user logs in.
     *
     * Arguments: Three ArrayList variables containing username/password/email information retrieved
     *            from the app's database.
     *
     * Returns: None
     */
    public void LoginButton(ArrayList unames, ArrayList pwords, ArrayList emls) {

        /*
         * Initialize the widget variables.
         */
        editTextUsername = findViewById(R.id.editText_username);
        editTextPassword = findViewById(R.id.editText_password);
        textViewAttempts = findViewById(R.id.textView_tryCount);
        login_button = findViewById(R.id.button_login);

        textViewAttempts.setText(Integer.toString(attempt_counter));

        /*
         * "Convert" the passed-in Array:ist variables to arrays for later use.
         */
        final String[] sUnames = (String[]) unames.toArray(new String[unames.size()]);
        final String[] sPwords = (String[]) pwords.toArray(new String[pwords.size()]);
        final String[] sEmails = (String[]) emls.toArray(new String[emls.size()]);

        /*
         * Start the button widget listener.
         *
         * Note: This is NOT the best way to perform username/password validation.  It is inefficient
         *       and insecure but suffices for the purpose of this app demonstration.  This would
         *       require changing in a production app.
         */
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                         * Create an integer variable for later use.
                         * Initialize it to -1 for test purposes.
                         */
                        int passCounter = -1;

                        /*
                         * If either username or password is empty show an appropriate "Toast" message.
                         * Do NOT decrement the login attempt counter for this.
                         */
                        if (editTextUsername.getText().toString().isEmpty() ||
                                editTextPassword.getText().toString().isEmpty()) {
                            Toast.makeText(LoginActivity.this, R.string.u_p_not_blank, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int indexCounter = 0;

                        /*
                         * If there are entries in both username and password check them for validity.
                         * We loop through all of them, setting a flag as required.
                         * This is not the most efficient way to do this, but suffices for this app.
                         */
                        for (String uname: sUnames) {
                            /*
                             * If the entered username is found in the database...
                             */
                            if (editTextUsername.getText().toString().equals(uname)) {
                                /*
                                 * And if the matching database index password equals the entered password...
                                 */
                                if (editTextPassword.getText().toString().equals(sPwords[indexCounter])) {
                                    /*
                                     * Set the passCounter variable to the index of the username/password/email
                                     * data for later use.
                                     */
                                    passCounter = indexCounter;
                                }
                                /*
                                 * To get to here we have a valid (exists in the database) username,
                                 * but the password was incorrect.  Set the passCounter variable to
                                 * -2 to indicate this situation
                                 */
                                else {
                                    passCounter = -2;
                                }
                            }
                            /*
                             * Increment the counter.
                             */
                            indexCounter++;
                        }

                        /*
                         * Now, check if we found a valid username, and if it also had a valid password.
                         *
                         * If passCounter is less than 0, then one or the other was incorrect.
                         *
                         * If we have a valid pair, then start the correct activity based on the
                         * username.
                         */
                        if (passCounter > -1) {
                            // Valid username and password
                            Toast.makeText(LoginActivity.this, R.string.u_p_correct, Toast.LENGTH_SHORT).show();
                            if (editTextUsername.getText().toString().equals("admin")) {
                                // Start the admin activity if we are logged in as admin.
                                Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.AdminActivity");
                                startActivity(intent);
                            }
                            else {
                                // Start the user activity for everyone else.
                                Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
                                startActivity(intent);
                            }
                        }
                        else if (passCounter == -2) {
                            /*
                             * Valid username, invalid password
                             *
                             * Show an appropriate "Toast" message and update/decrement the login
                             * attempt counter.
                             */
                            Toast.makeText(LoginActivity.this, R.string.p_incorrect, Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            textViewAttempts.setText(Integer.toString(attempt_counter));
                        }
                        else {
                            /*
                             * Valid username, invalid password
                             *
                             * Show an appropriate "Toast" message and update/decrement the login
                             * attempt counter.
                             */
                            Toast.makeText(LoginActivity.this, R.string.u_incorrect, Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            textViewAttempts.setText(Integer.toString(attempt_counter));
                        }
                    }
                }
        );
    }

}
