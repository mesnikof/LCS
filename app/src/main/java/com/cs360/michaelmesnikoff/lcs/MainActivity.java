package com.cs360.michaelmesnikoff.lcs;

import android.content.SharedPreferences;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;

import android.support.v7.app.ActionBar;

import android.text.InputType;
import android.view.View;
import android.text.TextUtils;
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
    SharedPreferences.Editor login_myEditor;

    /*
     * Create several class variables for referencing layout objects.
     */
    private static LinearLayout linearLayout;
    private static int scrollerWidth;
    private static TableRow rowHeader;
    private static TextView textViewWelcomeTitle;
    private static ImageButton button_logoff;
    private static Button login_button;

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
         * Create a Shared Preferance instance for maintaining persistent data.
         * Also create the variables and method to hold and access/update the data.
         */
        SharedPreferences login_usernamePref = getSharedPreferences(MainActivity.LOGIN_PREFS, MODE_PRIVATE);
        stringUsername = login_usernamePref.getString(LOGIN_USERNAME_KEY, null);
        login_myEditor = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE).edit();

        /*
         * A variable pointing to this actvity's welcome TextView layout object.
         */
        textViewWelcomeTitle = findViewById(R.id.textViewWelcomeTitle);

        /*
         * Now display the welcome message including the user's name in that object,
         * If the username is not found in the persistent data show an appropriate "Toast" message.
         */
        if(!TextUtils.isEmpty(stringUsername)) {
            textViewWelcomeTitle.setText("Welcome: " + stringUsername);
        }
        else {
            Toast.makeText(MainActivity.this, "Error: username not passed in.", Toast.LENGTH_LONG).show();
        }

        /*
         * A variable and OnClickListener callback method for this activity's "Logoff" button object.
         */
        button_logoff = (ImageButton) findViewById(R.id.button_Logoff);
        button_logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*
         * Reference to the Outer LinearLayout.
         * Set the scrollerWidth variable via ...getWidth().
         * Reset the reference to the Inner LinearLayout.
         */
        linearLayout = (LinearLayout) findViewById(R.id.scrollOuterLinearLayout);
        scrollerWidth = linearLayout.getWidth();
        linearLayout = (LinearLayout) findViewById(R.id.scrollInnerLinearLayout);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(MainActivity.this);
        dbManager.open_read();
        Cursor cursor = dbManager.fetch(DBHelper.ITEMS_TABLE_NAME);

        /*
         * If the database query did not return the needed data show an appropriate "Toast" message.
         * Otherwise create the selectable TableRow objects.
         */
        if(cursor.getCount() == 0) {
            Toast.makeText(MainActivity.this, "ITEMS database returned no data.", Toast.LENGTH_LONG).show();
        }
        else {
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
                if(stringItemImageFile == null) {
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
                itemQuantity.setPadding(0,40,0,0);
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
                itemName.setPadding(0,40,0,0);
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
                itemPrice.setPadding(0,40,0,0);
                itemPrice.setBackgroundResource(R.drawable.item_price_style);
                itemPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                itemPrice.setTextColor(Color.BLACK);
                itemPrice.setTextSize(22);

                /*
                 * Now the item image (if it exists) from the database and the drawable resources.
                 */
                ImageView itemImage = new ImageView(context);
                String uri = "@drawable/"+ stringItemImageFile;
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
            } while(cursor.moveToNext());

            /*
             * Close the database.
             */
            dbManager.close();

        }
    }


    public void onMapClick(View v) {
        // Start the maps activity when the "Find Us - Map" button is clicked.
        Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.MapsActivity");
        startActivity(intent);
    }
}
