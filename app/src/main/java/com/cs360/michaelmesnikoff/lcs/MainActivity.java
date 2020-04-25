package com.cs360.michaelmesnikoff.lcs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import android.database.Cursor;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.SessionManager;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.*;


public class MainActivity extends AppCompatActivity {
    /*
     * Create DBManager and DBHelper instances to access the LCS database for username/password info.
     */
    DBManager dbManager;
    DBHelper dbHelper;

    /*
     * Create a Context instance variable.
     */
    Context context;

    Helpers helpers;

    /*
     * Create and instantiate the variables used for utilizing the Shared Preference persistent data.
     */
    protected String stringUsername;
    protected static final String LOGIN_PREFS = "My_Login_Prefs";
    protected static final String LOGIN_USERNAME_KEY = "login_username_key";
    protected static final String LOGIN_USERID_KEY = "login_userid_key";
    protected static final String LOGIN_EMAIL_KEY = "login_email_key";
    protected static final String LOGIN_TOKEN_KEY = "login_token_key";
    protected static final String LOGIN_SECRET_KEY = "login_secret_key";
    SharedPreferences.Editor sharedPref_myEditor;

    protected TwitterAuthClient twitterAuthClient;
    //protected TwitterLoginButton twitterLoginButton;
    protected ImageButton twitterLoginButton;
    protected TwitterSession twitterSession;
    protected String twitterToken;
    protected String twitterSecret;
    protected String twitterEmail;
    protected String twitterUsername;
    protected long twitterUserID;
    public static final String TWITTER_PREFS = "com.twitter.sdk.android:twitter-core:session_store";
    SharedPreferences.Editor twitterPref_myEditor;

    /*
     * A database Cursor object.
     */
    Cursor cursor;

    /*
     * Create several class variables for referencing layout objects.
     */
    protected static int scrollerWidth;
    protected HorizontalScrollView hScrollView;
    protected LinearLayout linearLayout;
    protected TableRow rowHeader;
    protected TextView textViewWelcomeTitle;
    protected ImageButton button_Logoff;
    protected ImageButton button_ContactUs;
    protected ImageButton button_RateUs;
    protected Button login_button;

    protected List<EditText> itemQuantities = new ArrayList<EditText>();

    /*
     * A logging string.
     */
    protected static final String TAG = "AndroidClarified";

    /*
         * This is the basic onCreate method.  For the Main Activity this sets up
         * the ActionBar, as well as setting up all the basic items on the Main Activity
         * Layout, such as the available items from the ITEMS database, the button
         * listeners, etc.
         */
    @SuppressLint("CommitPrefEdits")
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
        helpers = new Helpers(context);

        /*
         * Set up the Action Bar to display the app's name and icon.
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_round);
        }

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(MainActivity.this);
        dbManager.open_read();
        cursor = dbManager.fetch(DBHelper.USERS_TABLE_NAME);

        if (cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, R.string.toast_database_no_data, Toast.LENGTH_LONG).show();
        }

        /*
         * Create a Shared Preference instance for maintaining persistent data.
         * Also create the variables and method to hold and access/update the data.
         */
        SharedPreferences login_usernamePref = getSharedPreferences(MainActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);
        sharedPref_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

        /*
         * A variable pointing to this actvity's welcome TextView layout object.
         */
        textViewWelcomeTitle = findViewById(R.id.textViewWelcomeTitle);

        /*
         * Now display the welcome message including the user's name in that object,
         * If the username is not found in the persistent data show an appropriate "Toast" message.
         */
        textViewWelcomeTitle.setText(getString(R.string.string_welcome_prefix));
        if (!TextUtils.isEmpty(stringUsername)) {
            textViewWelcomeTitle.append(" ");
            textViewWelcomeTitle.append(stringUsername);
        } else {
            textViewWelcomeTitle.append(getString(R.string.string_unknown_user));
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
            SharedPreferences dialog_usernamePref;
            String dialogUsername;
            SharedPreferences.Editor dialogPref_myEditor;

            @Override
            public void onClick(View view) {
                /*
                 * Set up the custom Rate Us dialog.
                 */
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.rate_us);

                /*
                 * Store the Shared Preference username to restore, if necessary, when this
                 * dialog exits.
                 */
                dialog_usernamePref = getSharedPreferences(LoginActivity.LOGIN_PREFS, MODE_PRIVATE);
                dialogUsername = dialog_usernamePref.getString(LOGIN_USERNAME_KEY, null);
                dialogPref_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

                /*
                 * Set up the Twitter Auth for the Share on Twitter capability.
                 */
                twitterAuthClient = new TwitterAuthClient();
                twitterLoginButton = dialog.findViewById(R.id.dialogImageButton_TwitterShare);
                twitterLoginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        twitterAuthClient.authorize(MainActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                            @Override
                            public void success(Result<TwitterSession> twitterSessionResult) {
                                // Success
                                Toast.makeText(getApplicationContext(), "Twitter Login worked..!!", Toast.LENGTH_SHORT).show();

                                //Uri imageUri = FileProvider.getUriForFile(MainActivity.this,
                                //        BuildConfig.APPLICATION_ID + ".file_provider",
                                //        new File("/path/to/image"));

                                TweetComposer.Builder builder = new TweetComposer.Builder(context)
                                        .text("just setting up my Twitter Kit.");
                                        //.image(imageUri);
                                builder.show();
                            }
                            @Override
                            public void failure(TwitterException e) {
                                Toast.makeText(getApplicationContext(), "Twitter Login failed..!!", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        });
                    }
                });

                /*
                 * Set up a "Back" button and listener.
                 */
                ImageButton dialogButton = dialog.findViewById(R.id.dialogImageButtonOK);
                // If button is clicked, close the custom dialog.
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void onClick(View v) {
                        dialogPref_myEditor.putString(LOGIN_USERNAME_KEY, dialogUsername);
                        dialogPref_myEditor.commit();
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

        hScrollView = findViewById(R.id.horizontalScrollView);

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
                //itemQuantities.set(intItemID, new EditText(context));
                itemQuantity.setMinimumWidth(scrollerWidth);
                itemQuantity.setWidth(helpers.dpToPx(200));
                itemQuantity.setPadding(0, 25, 0, 0);
                itemQuantity.setHint(R.string.prompt_quantity);
                itemQuantity.setPaintFlags(android.graphics.Paint.UNDERLINE_TEXT_FLAG);
                itemQuantity.setTextSize(18);
                itemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                itemQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
                itemQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemQuantity.clearFocus();
                //itemQuantity.addTextChangedListener(textWatcher);
                //itemQuantity.setOnFocusChangeListener(fcl);
                itemQuantity.setBackgroundResource(R.drawable.item_quantity_style);

                /*
                 * Now the item name from the database.
                 */
                TextView itemName = new TextView(context);
                itemName.setText(" ");
                itemName.append(stringItemName);
                itemName.setMinimumWidth(scrollerWidth);
                itemName.setWidth(helpers.dpToPx(200));
                itemName.setPadding(0, 25, 0, 0);
                itemName.setBackgroundResource(R.drawable.item_name_style);
                itemName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemName.setTextColor(Color.BLUE);
                itemName.setTextSize(18);

                /*
                 * Now the item price from the database.
                 */
                TextView itemPrice = new TextView(context);
                itemPrice.setText(stringItemPrice);
                itemPrice.setMinimumWidth(scrollerWidth);
                itemPrice.setWidth(helpers.dpToPx(200));
                itemPrice.setPadding(0, 25, 0, 0);
                itemPrice.setBackgroundResource(R.drawable.item_price_style);
                itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemPrice.setTextColor(Color.BLACK);
                itemPrice.setTextSize(18);

                /*
                 * Now the item image (if it exists) from the database and the drawable resources.
                 */
                ImageView itemImage = new ImageView(context);
                String uri = "@drawable/" + stringItemImageFile;
                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                itemImage.setBackgroundColor(0xFAFAFA);
                itemImage.setMaxWidth(helpers.dpToPx(200));
                itemImage.setImageResource(imageResource);

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
                linearLayout.setOnFocusChangeListener(fcl);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

/* End of the onCreate Constructor section */
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/
/*************************************************************************************************************/



    /*
     * The onPause method.
     * Write out the Shared Preferences information.
     */
    @SuppressLint("ApplySharedPref")
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
     * These two methods respond to left/right arrow clicks to scroll the "items" view.
     */
    public void onClickScrollLeft(View view) {
        hScrollView.clearFocus();
        hScrollView.smoothScrollBy(helpers.dpToPx(-200), 0);
    }

    public void onClickScrollRight(View view) {
        hScrollView.clearFocus();
        hScrollView.smoothScrollBy(helpers.dpToPx(200), 0);
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



    /*
     * This method simply returns the pass/fail from the Twitter authorization task.
     */
    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        twitterAuthClient.onActivityResult(requestCode, responseCode, intent);
    }



    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            //here, after we introduced something in the EditText we get the string from it
            String answerString = editable.toString();

            //and now we make a Toast
            //modify "yourActivity.this" with your activity name .this
            Toast.makeText(MainActivity.this,"The string from EditText is: "+answerString,Toast.LENGTH_SHORT).show();
        }
    };



    View.OnFocusChangeListener fcl = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if(!hasFocus){
                EditText txtUser=(EditText)v;
                String userName=txtUser.getText().toString();
                Toast.makeText(MainActivity.this,"The string from EditText is: "+userName,Toast.LENGTH_SHORT).show();
            }
        }
    };
}
