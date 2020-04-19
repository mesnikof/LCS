package com.cs360.michaelmesnikoff.lcs;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.util.Log;
import android.view.View;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import android.database.Cursor;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetUi;

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
    public static final String LOGIN_USERID_KEY = "login_userid_key";
    public static final String LOGIN_EMAIL_KEY = "login_email_key";
    public static final String LOGIN_TOKEN_KEY = "login_token_key";
    public static final String LOGIN_SECRET_KEY = "login_secret_key";

    SharedPreferences.Editor sharedPref_myEditor;

    public static final String TWITTER_PREFS = "com.twitter.sdk.android_twitter-core_session_store";
    public static final String TWITTER_GS0 = "guestsession0";
    public static final String TWITTER_AGS = "active_guestsession";

    /*
     * Create a series of variables for representing widgets on the Login layout.
     */
    EditText login_usernameET;
    EditText login_passwordET;
    private static EditText editTextUsername;
    private static EditText editTextPassword;
    private static TextView textViewAttempts;
    private static ImageButton login_button;
    private static TextView textDisplay;

    private static final String TAG = "AndroidClarified";
    private SignInButton googleSignInButton;
    private GoogleSignInClient googleSignInClient;

    private TwitterAuthClient twitterAuthClient;
    private TwitterLoginButton twitterLoginButton;
    private TwitterSession twitterSession;
    private String twitterToken;
    private String twitterSecret;
    private String twitterEmail;
    private String twitterUsername;
    private long twitterUserID;

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

        /*
         * Setup the activity view from the login layout.
         */
        setContentView(R.layout.activity_login);

        /*
         * Set the ActionBar to show the LCS icon.
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);


/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Standard Sign-In section using the internal app database */

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

            /*
             * The textDisplaySring is for test purposes only.  This is left in for future
             * testing only.
             */
            textDisplayString += cursor.getString(cursor.getColumnIndex("username"));
            textDisplayString += ", ";
            textDisplayString += cursor.getString(cursor.getColumnIndex("password"));
            textDisplayString += ", ";
            textDisplayString += cursor.getString(cursor.getColumnIndex("email"));
            textDisplayString += "\n";
        } while (cursor.moveToNext());
        /*
         * The textDisplaySring is for test purposes only.  Left in for future testing.
         */
        //textDisplay.setText(textDisplayString);

        /*
         * Close the database for cleanliness.
         */
        dbManager.close();

        /*
         * Check the shared preferences for user info.
         * If it is there, load it into the Username widget.
         */
        login_usernameET = findViewById(R.id.editText_username);
        login_passwordET = findViewById(R.id.editText_password);
        SharedPreferences login_usernamePref = getSharedPreferences(LoginActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);
        sharedPref_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

        if (!TextUtils.isEmpty(stringUsername)) {
            login_usernameET.setText(stringUsername);
        } else {
            Toast.makeText(LoginActivity.this, "No saved user...", Toast.LENGTH_LONG).show();
        }


/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Google Sign-In section */

        /*
         * Initialize a reference to the Google Signin button.
         */
        //googleSignInButton = findViewById(R.id.g_sign_in_button);
        googleSignInButton = findViewById(R.id.button_loginGoogle);

        /*
         * Now create a TextView instance that will be placed into the GoogleSignInButton to customize
         * the look.  The functionality will remain the same.  All these commands in the following
         * {} block set up the parameters for the TextView that "sort of" overwrites the default
         * look of the SignInButton, the final .setLayoutParams places the TextView into the
         * SignInButton layout resource.
         */
        View g = googleSignInButton.getChildAt(0);
        if (g instanceof TextView) {
            TextView tv = (TextView) g;
            tv.setTextSize(18);
            tv.setTypeface(null, Typeface.NORMAL);
            tv.setText("Sign-In with Google");
            tv.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.google_signin));
            tv.setSingleLine(true);
            tv.setPadding(2, 2, 2, 2);
            ViewGroup.LayoutParams params = tv.getLayoutParams();
            /*
             * Using -1 here represents "FILL_PARENT".
             */
            params.width = -1;
            params.height = -1;
            tv.setLayoutParams(params);
        }

        /*
         * Configure sign-in to request the user's ID, email address, and basic
         * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
         *
         * Note: The requestIdToken was generated from the Google developer site previously.
         *
         * .requestIdToken("287582727787-0p75sg16alt0nmu4jpdoco8tomogli2f.apps.googleusercontent.com")
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.google_signin_key))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        /*
         * Create a Google sign-in client using the parameters/options created above.
         */
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        /*
         * Now create the OnClickListener to setup and call the SignIn intent to allow the user to
         * log in via their Google account
         */
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });


/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Twitter Sign-In section */

        /*
         * Initialize the Twitter API tools.
         */
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
                .debug(true)
                .build();
        Twitter.initialize(config);

        /*
         * Now set up the Twitter user login actions.
         */
        twitterAuthClient = new TwitterAuthClient();
        TwitterCore twitterCore = TwitterCore.getInstance();
        TweetUi tweetUI = TweetUi.getInstance();
        TweetComposer tweetComposer = TweetComposer.getInstance();

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.button_loginTwitter);
        twitterLoginButton.setClickable(true);
        twitterLoginButton.setEnabled(true);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls

                Toast.makeText(LoginActivity.this, "Twitter Login Working...", Toast.LENGTH_LONG).show();

                twitterSession = result.data;
                twitterUsername = twitterSession.getUserName();
                twitterUserID = twitterSession.getUserId();

                twitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
                    @Override
                    public void success(Result<String> resultE) {
                        // Extract the email address from the result data.
                        twitterEmail = resultE.data;

                        // Put the returned email address into the Password EditText field for reference.
                        login_passwordET.setText(twitterEmail);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(LoginActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                // Put the returned username into the Usrname EditText field for reference.
                login_usernameET.setText(twitterUsername);

                // Put the returned ID info into the shared preference LOGIN_USERNAME_KEY.
                sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, login_usernameET.getText().toString());
                sharedPref_myEditor.commit();

                /*
                 * Finally (for Twitter), start the user activity for non-Admin users.
                 */
                Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(LoginActivity.this, "Twitter Login Failed...", Toast.LENGTH_LONG).show();
            }
        });

        /*
         * Now, if we are using the app's login method, call the method to listen for
         * activity on the login button.
         */
        LoginButton(usernames, passwords, emails);
    }



    @Override
    public void onStart() {
        super.onStart();
        // Placeholder
    }



    @Override
    public void onResume() {
        super.onResume();
        // Placeholder
        /*
         * Check the shared preferences for user info.
         * If it is there, load it into the Username widget.
         */
        login_usernameET = findViewById(R.id.editText_username);
        login_passwordET = findViewById(R.id.editText_password);
        SharedPreferences login_usernamePref = getSharedPreferences(LoginActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);

        /*
         * If the previous login was with Google or Twitter, clear_icon the info.
         */
        if ((stringUsername == "Google Logout") || (stringUsername == "Twitter Logout")) {
            sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, "");
            sharedPref_myEditor.commit();
            login_usernameET.setText("");
            login_passwordET.setText("");
        }
        /*
         * Otherwise, load the previous local username.
         */
        else {
            login_usernameET.setText(stringUsername);
        }
    }



    /*
     * The onPause method.
     * Write out the Shared Preferences information.
     */
    protected void onPause() {
        super.onPause();
        sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, login_usernameET.getText().toString());
        sharedPref_myEditor.commit();
    }



    /*
     * This method simply clears the information in the Username widget, and from the stored
     * Shared Preferences Username key:value data.
     *
     * A "Toast" message is generated for either success or failure.
     */
    public void clearLoginPreferences(View view) {
        if (!TextUtils.isEmpty(stringUsername)) {
            sharedPref_myEditor.clear();
            sharedPref_myEditor.commit();
            login_usernameET.setText("");
            login_passwordET.setText("");
            //Toast.makeText(LoginActivity.this, R.string.preference_deleted, Toast.LENGTH_LONG).show();
        }
        else if (true){
            //TwitterCore.getInstance().twitterSession.clearActiveSession();
            //Toast.makeText(LoginActivity.this, R.string.no_preference_removed, Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(LoginActivity.this, R.string.no_preference_removed, Toast.LENGTH_LONG).show();
        }
    }



    /*
     * This method is called after the return from the Google Sign-In button click and intent action.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * This is the Google login callback.
         */
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

        /*
         * This is the Twitter login callback.
         */
        if (requestCode == 140) {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        }
    }



    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(LoginActivity.this, account.getEmail(), Toast.LENGTH_LONG).show();
            login_usernameET.setText(account.getEmail());

            // Put the returned ID info into the shared preference LOGIN_USERNAME_KEY.
            sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, account.getEmail());
            sharedPref_myEditor.commit();

            // Start the user activity for non-Admin users.
            Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Google Sign-In failed...", Toast.LENGTH_LONG).show();
            finish();
        }
    }



    private static void requestEmailAddress(final Context context, TwitterSession session) {
        new TwitterAuthClient().requestEmail(session, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Toast.makeText(context, result.data, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    /**
     * get authenticates user session
     *
     * @return twitter session
     */
    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        //twitterToken = TwitterCore.getInstance().getSessionManager().getActiveSession().getAuthToken().token;
        twitterToken = session.getAuthToken().token;
        twitterSecret = session.getAuthToken().secret;

        return session;
    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        //Intent intent = new Intent(this, ProfileActivity.class);
        //intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);

        //startActivity(intent);
        finish();
    }



    /*
     * This is the method that listens for non-social-media (local app) login activity.
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
                        for (String uname : sUnames) {
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
                            } else {
                                // Start the user activity for everyone else.
                                Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
                                startActivity(intent);
                            }
                        } else if (passCounter == -2) {
                            /*
                             * Valid username, invalid password
                             *
                             * Show an appropriate "Toast" message and update/decrement the login
                             * attempt counter.
                             */
                            Toast.makeText(LoginActivity.this, R.string.p_incorrect, Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            textViewAttempts.setText(Integer.toString(attempt_counter));
                        } else {
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