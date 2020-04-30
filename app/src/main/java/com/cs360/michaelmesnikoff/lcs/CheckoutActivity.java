package com.cs360.michaelmesnikoff.lcs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {
    /*
     * Create DBManager and DBHelper instances to access the LCS database for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;

    /*
     * Create ArrayList variables to hold the returned USERS table data, and individual String
     * variables to hold the individual values.
     */
    private ArrayList<String> usernames = new ArrayList<String>();
    private ArrayList<String> passwords = new ArrayList<String>();
    private ArrayList<String> emails = new ArrayList<String>();
    private ArrayList<String> cards = new ArrayList<String>();
    private ArrayList<String> cvvs = new ArrayList<String>();
    private ArrayList<String> expires = new ArrayList<String>();
    private String stringUsername = "";
    private String stringPassword = "";
    private String stringEmail = "";
    private String stringCard = "";
    private String stringPartialCard = "";
    private String stringCvv = "";
    private String stringExpire = "";
    private String stringDbUsername = "";
    private String stringDbPassword = "";
    private String stringDbEmail = "";
    private String stringDbCard = "";
    private String stringDbPartialCard = "";
    private String stringDbCvv = "";
    private String stringDbExpire = "";
    private String stringTotalPrice = "";

    /*
     * Objects to represent fields in the activity.
     */
    TextView tvUsername;
    TextView tvPrice;
    EditText etEmail;
    EditText etCard;
    EditText etCvv;
    EditText etExpire;
    ImageButton ibBack;
    ImageButton ibOrder;

    /*
     * Create a database Cursor variable
     */
    private Cursor cursor;

    /*
     * Create a Helpers instance.
     */
    Helpers helpers;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        /*
         * Set up the Action Bar to display the app's name and icon.
         */
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.mipmap.ic_launcher_round);
        }

        /*
         * Point the ImageButton variables to the corresponding layout buttons,
         */
        ImageButton button_Back = findViewById(R.id.imageButton_ChkOutBack);
        ImageButton button_Order = findViewById(R.id.imageButton_ChkOutOrder);

        /*
         * Instantiate the Helpers instance.
         */
        helpers = new Helpers(this);

        /*
         * Create a Shared Preferance instance and access the current user from it.
         */
        SharedPreferences login_usernamePref = getSharedPreferences(LoginActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(MainActivity.LOGIN_USERNAME_KEY, null);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(CheckoutActivity.this);
        dbManager.open_read();
        cursor = dbManager.row_fetch(DBHelper.USERS_TABLE_NAME, DBHelper.COLUMN_USERNAME, stringUsername);

        if (cursor.getCount() == 0) {
            /*
             * If the database does not return anything, this is not an error.  This may be a
             * user that was authenticated via their social media credentials.  If so, skip
             * the database retrieval parsing.
             */
            Toast.makeText(CheckoutActivity.this, "USERS database returned no data.", Toast.LENGTH_LONG).show();
        }
        else {
            /*
             * Load the username/password/email/card/cvv/expire information into a series of
             * Strings to pass into other methods.
             */
            cursor.moveToFirst();
            do {
                stringDbUsername = cursor.getString(cursor.getColumnIndex("username"));
                stringDbPassword = cursor.getString(cursor.getColumnIndex("password"));
                stringDbEmail = cursor.getString(cursor.getColumnIndex("email"));
                stringDbCard = cursor.getString(cursor.getColumnIndex("card"));
                stringDbCvv = cursor.getString(cursor.getColumnIndex("cvv"));
                stringDbExpire = cursor.getString(cursor.getColumnIndex("expire"));
            } while (cursor.moveToNext());

            /*
             * Parse the credit card number to show a partially hidden value for security purposes.
             *
             * This would be the normal way to do things.  The logic for doing this is left here
             * for inspection.  For the purpose of this project, we will not be masking the card
             * number.
             */
            int ccLength = stringDbCard.length();
            if (ccLength > 4)
            {
                String lastFour = stringDbCard.substring(ccLength - 4);
                for (int counter = 0; counter < (ccLength - 4); counter++) {
                    stringDbPartialCard += "*";
                }
                stringDbPartialCard += lastFour;
            }
            else {
                stringDbPartialCard = "";
            }
        }

        /*
         * Now get the items for the order.
         *
         * First, create a Shared Preference instance for accessing the persistent data.
         * Also create the variables and method to hold and access the data.
         */
        SharedPreferences order_listPref = this.getSharedPreferences(MainActivity.ORDER_STATUS_PREFS, MODE_PRIVATE);
        String stringOrderList;
        String stringOrderQuantity;
        int intQuantity;

        /*
         * A float for the "grand total" price.  Initialized to zero;
         */
        float floatGrandTotal = 0;

        /*
         * Get the number of database items.
         */
        int numberOfItems = helpers.getNumberOfEntries(this, DBHelper.ITEMS_TABLE_NAME);

        /*
         * Create the variables used to generate the "Key" for the shared preference.
         */
        String stringItemPreface = "item_";
        String stringItemSuffix = "_key";
        String itemKey;

        /*
         * Now run a loop to retrieve one "key:0" pair for each available item.
         */
        for (int counter = 1; counter <= numberOfItems; counter++) {
            /*
             * First, get the quantity of items for each individual item.
             *
             * For that we first build up the key string.
             */
            itemKey = stringItemPreface;
            itemKey += Integer.toString(counter);
            itemKey += stringItemSuffix;
            stringOrderQuantity = order_listPref.getString(itemKey, null);
            intQuantity = Integer.parseInt(stringOrderQuantity);

            /*
             * if the quantity is not zero, create the table row and add it to the table.
             */
            if (intQuantity != 0) {
                /*
                 * Query the items database to get the name and price for each ordered item.
                 */
                cursor = dbManager.row_fetch(DBHelper.ITEMS_TABLE_NAME, DBHelper.COLUMN_ID, Integer.toString(counter));

                /*
                 * Now parse that data into strings as reguired for the shopping cart view.
                 *
                 * Not currently using anything other than the price to generate the Grand Total,
                 * but that may change.
                 */
                String stringItemName = cursor.getString(cursor.getColumnIndex("item_name"));
                float floatItemPrice = cursor.getFloat(cursor.getColumnIndex("item_price"));
                @SuppressLint("DefaultLocale") String stringItemPrice = String.format("%.2f", floatItemPrice);
                float floatTotalPrice = floatItemPrice * intQuantity;
                floatGrandTotal += floatTotalPrice;
            }

            /*
             * Now populate the layout fields.
             */
            stringTotalPrice = String.format("%.2f", floatGrandTotal);
            tvUsername = findViewById(R.id.textView_ChkOutUsername);
            tvUsername.setText(stringDbUsername);
            tvPrice = findViewById(R.id.textView_ChkOutPrice);
            tvPrice.setText(stringTotalPrice);
            etEmail = findViewById(R.id.editText_ChkOutEmail);
            etEmail.setText(stringDbEmail);
            etCard = findViewById(R.id.editText_ChkOutCard);
            etCard.setText(stringDbCard);
            etCvv = findViewById(R.id.editText_ChkOutCvv);
            etExpire = findViewById(R.id.editText_ChkOutExpire);
            etExpire.setText(stringDbExpire);

        }
    }


    /*
     * This method is the "Back" button listener.  It simply calls the finish() method to
     * end this activity and send the user back to the Main user activity.  Nothing else is
     * changed, the possible order is still there, not zeroed out.
     */
    public void onClickBack(View view) {
        finish();
    }


    /*
     * This method is the "Order" button listener.  After data validation (credit card info),
     * it start the order processing (assuming valid data).  This actual functionality is very
     * rudimentary, just to fulfil the task requirements.  In a "real-world" app the data
     * validation would need to be far more thorough, and many more actions would take place
     * to "make the order happen".
     */
    public void onClickOrder(View view) {
        /*
         * The first step in this method is to determine whether this use is "app-local" which
         * implies stored payment information, or "social-media-authenticated", in which case all
         * the required payment information must be entered on the activity page.
         *
         * The first "sanity" chack is whether there is even a username existant in the database.
         * This was effectively tested when  this activity started by using the Shared Preference
         * username to query the database.  If the query-generated username String is empty, then
         * this is a non-local user.  Branch appropriately.
         */
        if (stringDbUsername != "") {
            /*
             * This IS a local user, so now we will check whether the database-stored credit card
             * number is the same as what is in the EditText field.  While it is not realistically
             * a valid for of authentication, we will assume, for the purpose of this project, that
             * if there is a different credit card number, so long as its length is correct (15 or
             * 16 digits) as well as valid CVV and expiration fields (3 digits for 16-digit card
             * numbers, 4 digits for 15-digit card numbers), and a 5-character expire field (we
             * are NOT actually checking the date info here), then we will just use this user-
             * input data.  This would be the type of situation where a user chose to use a non-
             * stored card (this does happen).  It will also be the same for non-app-local users.
             *
             * If the card number is the same (EditText field to database query) then the user
             * apparently made no changes, so we will just proceed from there, and use the stored
             * data.  In this case, we compare the user-entered CVV (this is required as a
             * security measure by many real-world apps).  If they are the same, we process the
             * order.  If not, we show an appropriate "Toast" message and send the user back to
             * make a correction in the CVV field.
             */
            stringCard = etCard.getText().toString();
            stringCvv = etCvv.getText().toString();
            if (stringDbCard.equals(stringCard)) {
                /*
                 * So we are now assuming that all the database-queried data is what we will use.
                 * Now compare the CVVs for validity.  If they are valid process the order.  If not
                 * send the user back to try again.
                 */
                if (stringDbCvv.equals(stringCvv)) {
                    /*
                     * All is well, process the order.
                     * The result is either the order number for success, or 0.
                     */
                    int result = processOrder();
                    if (result != 0) {
                        Toast.makeText(CheckoutActivity.this, "Order processed: "+result, Toast.LENGTH_LONG).show();
//////// Put the Intent stuff here after testing.
                    }
                    else {
                        Toast.makeText(CheckoutActivity.this, "Order failed.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    finish();
                }
                else {
                    Toast.makeText(CheckoutActivity.this, "CVV does not match, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        }
        else {
            /*
             * This is the "non-local" user path.  It is also used to process local users who
             * have entered card information that is different from the stored database stored
             * data.
             *
             * We are going to assume that the user-entered data is correct.  We will do a very
             * slight sanity check here, just to demonstrate that we know how.  What we will do
             * is make sure that all fields have been entered, that the email address looks like
             * an email address, that the credit card number is 15 or 16 digits, that the CVV is
             * 3-digits for a 16-digit card (MC/Visa/Discover) and 4-digits for a 15-digit card
             * (AMEX), and finally that the expiration is formatted 5 characters, mm/yy.  Note:
             * we are not going to see if that date is valid.
             *
             * This is completely inssufficient for a real-world app, but here we are just
             * proving out the ability.
             *
             * First sanity-check is just making sure all the fields are filled.  If not, show
             * an appropriate "Toast" message (generic for all fields in this case) and send the
             * user back to try again.
             */
            if ( (etEmail.getText().toString().equals("")) ||
                    (etCard.getText().toString().equals("")) ||
                    (etCvv.getText().toString().equals("")) ||
                    (etExpire.getText().toString().equals("")) ) {
                Toast.makeText(CheckoutActivity.this, "All fields ust be filled, please try again.", Toast.LENGTH_LONG).show();
                return;
            }

            /*
             * Now look at the credit-card/cvv-field comparisons.
             */
            int tmpInt1;
            int tmpInt2;
            if ( (((tmpInt1 = etCard.getText().toString().length()) == 16) &&
                  ((tmpInt2 = etCvv.getText().toString().length()) != 3)) ||
                  (((tmpInt1 = etCard.getText().toString().length()) == 15) &&
                  ((tmpInt2 = etCvv.getText().toString().length()) != 4)) ||
                  ((tmpInt1 = etCard.getText().toString().length()) < 15) ) {
                /*
                 * Something in either the card or cvv field is invalid.  Show an appropriate "Toast"
                 * message and return the user to try again.
                 */
                Toast.makeText(CheckoutActivity.this, "Credit Card or CVV field is invalid, please try again.", Toast.LENGTH_LONG).show();
                return;
            }

            /*
             * So far, so good.  Now check the email address for valid form.
             *
             * It would be better to perform this check via a "focus lost" listener, so the user
             * can be notified immediately, but that intorduces an additional level of complexity
             * that is unnecessary for this project.  We have done such checking elsewhere in
             * this app, so the ability to do so has been proven.
             *
             * Finally, some of the checking, like 3-16 and/or 4-15 in the previous block could
             * also be done in that way for more immediate testing and response to the user.
             */
            if (!helpers.isValidEmail(etEmail.getText().toString())) {
                /*
                 * If the email address does not pass muster, show an appropriate "Toast" mesage to
                 * the user, and then return them to try again.
                 */
                Toast.makeText(CheckoutActivity.this, "Email Address is invalid, please try again.", Toast.LENGTH_LONG).show();
                return;
            }

            /*
             * Finally, check the expiration string for format.  We should also check here for
             * valid dates, but the additional complexity is not necessary for the purpose of
             * this project.
             */
            String tmpStr = etExpire.getText().toString();
            if ( !tmpStr.matches("\\d{2}/\\d{2}") ) {
                /*
                 * Once again, if there is an invalidity here show a useful "Toast" message to the
                 * user and send them back to try again.
                 */
            }
            Toast.makeText(CheckoutActivity.this, "Expiration Date is invalid, please try again.", Toast.LENGTH_LONG).show();
            return;
        }
        /*
         * If we have made it this far all is well.  Process the order and let the user know what
         * happens.
         */
        int result = processOrder();
        if (result != 0) {
            Toast.makeText(CheckoutActivity.this, "Order processed: "+result, Toast.LENGTH_LONG).show();
//////// Put the Intent stuff here after testing.
        }
        else {
            Toast.makeText(CheckoutActivity.this, "Order failed.", Toast.LENGTH_LONG).show();
            return;
        }

        finish();
        /*
         * Last step.  Create and start a new Main activity.  This ends this Checkout Activity,
         * and in creating a new Main activity (rather than using finish() and going back to
         * it, we force a new instantiation, which clears out all the buffers, and resets the
         * shopping cart to zero.
         */
        //Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MainActivity");
        //startActivity(intent);
    }


    private int processOrder() {
        return 1;
    }
}
