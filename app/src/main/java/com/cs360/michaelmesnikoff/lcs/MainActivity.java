package com.cs360.michaelmesnikoff.lcs;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import android.graphics.drawable.Drawable;

import android.database.Cursor;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetUi;

public class MainActivity extends AppCompatActivity {

    /*
     * Create DBManager and DBHelper instances to access the LCS database for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;

    /*
     * Create a Context instance variable.
     */
    public Context context;

    /*
     * Create and instantiate the variables used for utilizing the Shared Preference persistent data.
     */
    private String stringUsername;
    private static final String LOGIN_PREFS = "My_Login_Prefs";
    private static final String LOGIN_USERNAME_KEY = "login_username_key";
    private static final String LOGIN_USERID_KEY = "login_userid_key";
    private static final String LOGIN_EMAIL_KEY = "login_email_key";
    private static final String LOGIN_TOKEN_KEY = "login_token_key";
    private static final String LOGIN_SECRET_KEY = "login_secret_key";
    SharedPreferences.Editor sharedPref_myEditor;

    private TwitterAuthClient twitterAuthClient;
    private TwitterLoginButton twitterLoginButton;
    private TwitterSession twitterSession;
    private String twitterToken;
    private String twitterSecret;
    private String twitterEmail;
    private String twitterUsername;
    private long twitterUserID;
    public static final String TWITTER_PREFS = "com.twitter.sdk.android:twitter-core:session_store";
    SharedPreferences.Editor twitterPref_myEditor;

    /*
     * A database Cursor object.
     */
    Cursor cursor;

    /*
     * Create several class variables for referencing layout objects.
     */
    private static int scrollerWidth;
    private LinearLayout linearLayout;
    private TableRow rowHeader;
    private TextView textViewWelcomeTitle;
    private ImageButton button_Logoff;
    private ImageButton button_ContactUs;
    private ImageButton button_RateUs;
    private Button login_button;

    /*
     * A logging string.
     */
    private static final String TAG = "AndroidClarified";

    /*
     * This is the basic onCreate method.  For the Main Activity this sets up
     * the ActionBar, as well as setting up all the basic items on the Main Activity
     * Layout, such as the available items from the ITEMS database, the button
     * listeners, etc.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * An integer variable for later use.
         */
        int counter = 0;

        /*
         * Instantiate the context variable.
         */
        context = this;

        /*
         * Set up the Action Bar to display the app's name and icon.
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(MainActivity.this);
        dbManager.open_read();
        cursor = dbManager.fetch(DBHelper.USERS_TABLE_NAME);

        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "USERS database returned no data.", Toast.LENGTH_LONG).show();
        }

        /*
         * Create a Shared Preference instance for maintaining persistent data.
         * Also create the variables and method to hold and access/update the data.
         */
        SharedPreferences login_usernamePref = getSharedPreferences(MainActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);
        sharedPref_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

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
         * A variable pointing to this actvity's welcome TextView layout object.
         */
        textViewWelcomeTitle = findViewById(R.id.textViewWelcomeTitle);

        /*
         * Now display the welcome message including the user's name in that object,
         * If the username is not found in the persistent data show an appropriate "Toast" message.
         */
        if (!TextUtils.isEmpty(stringUsername)) {
            textViewWelcomeTitle.setText("Welcome: " + stringUsername);
        } else {
            Toast.makeText(MainActivity.this, "Error: username not passed in.", Toast.LENGTH_LONG).show();
        }

        /*
         * A variable and OnClickListener callback method for this activity's "Logoff" button object.
         */
        button_Logoff = findViewById(R.id.button_Logoff);
        button_Logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Contact Us section */

        /*
         * A variable and OnClickListener callback method for this activity's "Contact Us" button object.
         */
        button_ContactUs = findViewById(R.id.button_Contact);
        // Add button listener.
        button_ContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Custom dialog.
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.contact_us);
                ImageButton dialogButton = dialog.findViewById(R.id.dialogImageButtonOK);
                // If button is clicked, close the custom dialog.
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Dismissed..!!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Rate Us section */

        /*
         * A variable and OnClickListener callback method for this activity's "Rate Us" button object.
         */
        button_RateUs = findViewById(R.id.button_RateUs);
        // Add button listener.
        button_RateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.rate_us);
                ImageButton dialogButton = dialog.findViewById(R.id.dialogImageButtonOK);
                // If button is clicked, close the custom dialog.
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Dismissed..!!", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
            }
        });

/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/* Setup the Ordering Menu section */

        /*
         * Reference to the Outer LinearLayout.
         * Set the scrollerWidth variable via ...getWidth().
         * Reset the reference to the Inner LinearLayout.
         */
        linearLayout = findViewById(R.id.scrollOuterLinearLayout);
        scrollerWidth = linearLayout.getWidth();
        linearLayout = findViewById(R.id.scrollInnerLinearLayout);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(MainActivity.this);
        dbManager.open_read();
        cursor = dbManager.fetch(DBHelper.ITEMS_TABLE_NAME);

        /*
         * If the database query did not return the needed data show an appropriate "Toast" message.
         * Otherwise create the selectable TableRow objects.
         */
        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "ITEMS database returned no data.", Toast.LENGTH_LONG).show();
        } else {
            /*
             * Iterate through the returned database item information.
             * Perform the operations as documented below.
             */
            do {
                /*
                 * First, extract the _id, item_name, and item_price information.
                 * Store separate strings for passing to the other tasks in this activity.
                 */
                int intItemID = cursor.getInt(cursor.getColumnIndex("_id"));
                final String stringItemID = Integer.toString(intItemID);
                final String stringItemName = cursor.getString(cursor.getColumnIndex("item_name"));
                float floatItemPrice = cursor.getFloat(cursor.getColumnIndex("item_price"));
                final String stringItemPrice = Float.toString(floatItemPrice);
                String stringItemImageFile;
                stringItemImageFile = cursor.getString(cursor.getColumnIndex("item_image_file"));
                if (stringItemImageFile == null) {
                    stringItemImageFile = "lcs_image";
                }

                /*
                 * Now, create the row to be inserted in the selection table.
                 *
                 * First, create a vertical linear layout to contain the "panel"
                 */
                LinearLayout panel = new LinearLayout(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                panel.setLayoutParams(lp);
                panel.setOrientation(LinearLayout.VERTICAL);

                /*
                 * Now an EditText field to hold the user-entered quantity.
                 *
                 * Note: For this and all the fields in the "panel, all the extra lines of code
                 * here are setting the various display parameters (text, size, input-type, etc).
                 */
                EditText itemQuantity = new EditText(context);
                itemQuantity.setMinimumWidth(scrollerWidth);
                itemQuantity.setWidth(750);
                itemQuantity.setPadding(0, 40, 0, 0);
                itemQuantity.setHint("Quantity?");
                itemQuantity.setPaintFlags(android.graphics.Paint.UNDERLINE_TEXT_FLAG);
                itemQuantity.setTextSize(22);
                itemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                itemQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemQuantity.clearFocus();
                itemQuantity.setBackgroundResource(R.drawable.item_quantity_style);

                /*
                 * Now the item name from the database.
                 */
                TextView itemName = new TextView(context);
                itemName.setText(" " + stringItemName);
                itemName.setMinimumWidth(scrollerWidth);
                itemName.setWidth(750);
                itemName.setPadding(0, 40, 0, 0);
                itemName.setBackgroundResource(R.drawable.item_name_style);
                itemName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemName.setTextColor(Color.BLUE);
                itemName.setTextSize(22);

                /*
                 * Now the item price from the database.
                 */
                TextView itemPrice = new TextView(context);
                itemPrice.setText(stringItemPrice);
                itemPrice.setMinimumWidth(scrollerWidth);
                itemPrice.setWidth(750);
                itemPrice.setPadding(0, 40, 0, 0);
                itemPrice.setBackgroundResource(R.drawable.item_price_style);
                itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemPrice.setTextColor(Color.BLACK);
                itemPrice.setTextSize(22);

                /*
                 * Now the item image (if it exists) from the database and the drawable resources.
                 */
                ImageView itemImage = new ImageView(context);
                String uri = "@drawable/" + stringItemImageFile;
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable resImage = getResources().getDrawable(imageResource);
                itemImage.setImageDrawable(resImage);

                /*
                 * Set the ID value for the created quantity selector for later use.
                 */
                itemQuantity.setId(counter);

                /*
                 * Now set the onClick tasks.  For these buttons we will just pass the info from the
                 * selected item to the EditText items elsewhere in this view for acting on.
                 * The "Toast" message is just for verification of activity purposes.
                 */
/*                itemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etUsername.setText(stringUsername);
                        etPassword.setText(stringPassword);
                        etEmail.setText(stringEmail);
                        etUserID.setText(stringUserID);
                        etFavOrder.setText(stringFavOrder);
                        Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show();
                    }
                });
*/
            /*
             * Finally, add the created button object to the created row object.
             * After that add the created row to the existing table object.
             */
                panel.addView(itemQuantity);
                panel.addView(itemName);
                panel.addView(itemPrice);
                panel.addView(itemImage);
                linearLayout.addView(panel, counter);

            /*
             * Lastly, increment the counter used for the loop.
             */
                counter++;
            } while (cursor.moveToNext());

            /*
             * Close the database.
             */
            dbManager.close();

        }
    }



    /*
     * The onPause method.
     * Write out the Shared Preferences information.
     */
    protected void onPause() {
        super.onPause();

        // Check for a Twitter session.
        SessionManager theAccountT;
        theAccountT = TwitterCore.getInstance().getSessionManager();

        // Check for a Google session
        GoogleSignInAccount theAccountG = GoogleSignIn.getLastSignedInAccount(this);

        // Clear the existing Google session, if it exists
        if (theAccountG != null) {
            /*
             * Sign-out is initiated by simply calling the googleSignInClient.signOut API.
             */
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getResources().getString(R.string.google_signin_key))
                    .build();

            /*
             * Create a Google sign-in client using the parameters/options created above.
             */
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
            googleSignInClient.signOut();

            /*
             * Clear the Google and Username Shared Preference data, then set the "Google Logout"
             * Shared Preference flag.
             */
            sharedPref_myEditor.clear();
            sharedPref_myEditor.commit();
            sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, "Google Logout");
            sharedPref_myEditor.commit();
            Log.d(TAG, "Logged Google client out");
        }

        // Clear the existing Twitter session, if it exists
        else if (theAccountT.getActiveSession() != null) {
            sharedPref_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();
            twitterPref_myEditor = getSharedPreferences(TWITTER_PREFS, MODE_PRIVATE).edit();

            CookieSyncManager.createInstance(this);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookie();
            theAccountT.clearActiveSession();

            /*
             * Clear the Twitter and Username Shared Preference data, then set the "Twitter Logout"
             * Shared Preference flag.
             */
            twitterPref_myEditor.clear();
            twitterPref_myEditor.commit();
            sharedPref_myEditor.clear();
            sharedPref_myEditor.commit();
            sharedPref_myEditor.putString(LOGIN_USERNAME_KEY, "Twitter Logout");
            sharedPref_myEditor.commit();
            Log.d(TAG, "Logged Twitter client out");
        } else {
            Log.d(TAG, "No Social Media logged in");
        }
    }


    public void onMapClick(View v) {
        // Start the maps activity when the "Find Us - Map" button is clicked.
        Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MapsActivity");
        startActivity(intent);
    }


/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
    /*************************************************************************************************************/
/* About Us section */
    public void onAboutUsClick(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Set the Alert Dialog title.
        alertDialogBuilder.setTitle("About Us...");
        // Set the Alert Dialog icon.
        alertDialogBuilder.setIcon(R.drawable.about_us_icon);
        // Set the Alert Dialog message.
        alertDialogBuilder.setMessage(R.string.about_us_popup);
        alertDialogBuilder.setCancelable(false);

        /*
         * Now, define the action to perform depending on which of the Alert Dialog
         * buttons the user selects.
         *
         * As this is a "standard" Alert Dialog, we make use of the setPositive and
         * setNegative options, rather than creating a custom Alert Dialog.  Positive
         * will be "Email Us" and Negative will be "Call Us".  Cancel will simply exit
         * the Alert Dialog with no other action, taking us back to the Main (User) Activity
         * page.
         */
        alertDialogBuilder.setPositiveButton("Email Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // Email placeholder
                button_ContactUs.callOnClick();
                Toast.makeText(MainActivity.this, "You clicked over Email Us", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        alertDialogBuilder.setNegativeButton("Call Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Call placeholder
                button_ContactUs.callOnClick();
                Toast.makeText(MainActivity.this, "You clicked over Call Us", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    /*
     * Start a Mailer activity when the "Email Us" button is clicked.
     *
     * This is "hardwired" to "lcs@lcs.com" (not a real email address).
     */
    public void onEmailClick(View view) {
        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:lcs@lcs.com")));
        return;
    }



    /*
     * Start a Dialer activity when the "Call Us" button is clicked.
     *
     * This is hardcoded to "1-603-555-1212" (not a real telephone number).
     */
    public void onTelephoneClick(View view) {
        Intent surf = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:16035551212"));
        startActivity(surf);
        return;
    }



    /*
     * Action performed when a "Rate Us" click is encountered.
     *
     * Starts a "market" URI intent activity, catches lack of Play Store cleanly.
     */
    public void onRateClick(View view) {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id="+getPackageName())));
        }
        catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
        }
    }



    public void shareOnTwitter(View view) {
        /*
         * Other button onClick ->
         * check for logged in ->
         * if not ->
         *   use callOnClick for  twitterLoginButton
         * then do the tweet
         */

        /*
         * Set up a reference to the ImageButton.
         */
        ImageButton button_ShareOnTwitter = findViewById(R.id.dialogImageButton_TwitterShare);

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

        TwitterLoginButton twitterLoginButton = findViewById(R.id.button_loginTwitter);
        twitterLoginButton.setClickable(true);
        twitterLoginButton.setEnabled(true);
        twitterLoginButton.callOnClick();

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls

                Toast.makeText(MainActivity.this, "Twitter Login Working...", Toast.LENGTH_LONG).show();

                twitterSession = result.data;
                twitterUsername = twitterSession.getUserName();
                twitterUserID = twitterSession.getUserId();

                twitterAuthClient.requestEmail(twitterSession, new Callback<String>() {
                    @Override
                    public void success(Result<String> resultE) {
                        // Extract the email address from the result data.
                        twitterEmail = resultE.data;
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(MainActivity.this, "Failed to authenticate. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

                /*
                 * Finally (for Twitter), start the user activity for non-Admin users.
                 */
                Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(MainActivity.this, "Twitter Login Failed...", Toast.LENGTH_LONG).show();
            }
        });

    }
}
