package com.cs360.michaelmesnikoff.lcs;

/**
 * Created by mp on 4/25/20.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This class will contain some basic, simple, helper methods.
 */
@SuppressLint("Registered")
public class Helpers extends MainActivity {
    /*
     * Create DBManager and DBHelper instances to access the LCS database for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;

    /*
     * Create and instantiate the variables used for utilizing the Shared Preference persistent data.
     */
    protected String stringUsername;
    protected static final String LOGIN_PREFS = "My_Login_Prefs";
    protected static final String ORDER_STATUS_PREFS = "My_OrderStatus_Prefs";
    protected static final String ORDER_LIST_KEY = "order_list_key";
    SharedPreferences.Editor sharedPref_cartEditor;


    public Helpers(Context c) {
        // Basic, do-nothing constructor.
    }

    /*
     * Start a Mailer activity when the "Email Us" button is clicked.
     *
     * This is "hardwired" to "lcs@lcs.com" (not a real email address).
     */

    /*
     * These two methods convert to/from DPs/pixels for view element sizing.
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }



    /*
     * These two methods respond to left/right arrow clicks to scroll the "items" view.
     *
     * They both take a reference to the calling HorizontalScrollView.  No return.
     *
     * They both get the current position from the pseudo-global scrollPos variable,
     * and then scroll 200 pixels from that.
     * They also set the pseudo-global scrollPos variable to this new position.
     *
     * They both use the dpToPx helper to convert pixel density info.
     */
    public void scrollLeft(HorizontalScrollView hScrollView) {
        hScrollView.clearFocus();
        if (scrollPos < 200) {
            scrollPos = 0;
        }
        else {
            int scrollMod = scrollPos % 200;
            scrollPos -= scrollMod;
            scrollPos -= 200;
        }
        hScrollView.smoothScrollTo(dpToPx(scrollPos), 0);
    }

    public void scrollRight(HorizontalScrollView hScrollView) {
        hScrollView.clearFocus();
        if (scrollPos > ((itemsList.size() - 1) * 200)) {
            scrollPos = ((itemsList.size() - 1) * 200);
            return;
        }
        else {
            int scrollMod = scrollPos % 200;
            scrollPos -= scrollMod;
            scrollPos += 200;
        }
        hScrollView.smoothScrollTo(dpToPx(scrollPos), 0);
    }

    public void set_Visible(FloatingActionButton fab2,
                            FloatingActionButton fab3,
                            TextView fab2_label,
                            TextView fab3_label) {
        fab2.setVisibility(View.VISIBLE);
        fab2_label.setVisibility(View.VISIBLE);
        fab3.setVisibility(View.VISIBLE);
        fab3_label.setVisibility(View.VISIBLE);
    }

    public void set_Invisible(FloatingActionButton fab2,
                              FloatingActionButton fab3,
                              TextView fab2_label,
                              TextView fab3_label) {
        fab2.setVisibility(View.INVISIBLE);
        fab2_label.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
        fab3_label.setVisibility(View.INVISIBLE);
    }



    /*
     * Method to initialize the Shared Preference Shopping Cart values.  This is run every time
     * the app is started to reset the cart to zero.
     */
    @SuppressLint("ApplySharedPref")
    public void initialize_Shopping_Cart(Context c) {
        /*
         * Create a Shared Preference instance for accessing the persistent data.
         * Also create the variables and method to hold and access the data.
         */
        Context context = this.context;
        SharedPreferences order_listPref = c.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE);
        String stringOrderList = order_listPref.getString(ORDER_LIST_KEY, null);
        sharedPref_cartEditor = c.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE).edit();

        /*
         * Get the number of database items.
         */
        int numberOfItems = getNumberOfEntries(c, DBHelper.ITEMS_TABLE_NAME);

        /*
         * Create the variables used to generate the "Key" for the shared preference.
         */
        String stringItemPreface = "item_";
        String stringItemSuffix = "_key";
        String itemKey;

        /*
         * Now run a loop to create one "key:0" pair for each available item.
         */
        for (int counter = 1; counter <= numberOfItems; counter++) {
            /*
             * Create a "key" string.
             */
            itemKey = stringItemPreface;
            itemKey += Integer.toString(counter);
            itemKey += stringItemSuffix;

            /*
             * Now write out the key to the shared preferences.
             */
            SharedPreferences cart_itemPref = c.getSharedPreferences(itemKey, MODE_PRIVATE);
            sharedPref_cartEditor.putString(itemKey, "0");
            sharedPref_cartEditor.commit();
        }
        return;
    }



    /*
     * This method is used to get the number of entries (rows) in the selected database table.
     * This is an action performed several times in the app, so it is better to have repeatable
     * code in a called method.
     *
     * Arguments: The passed-in Context, The table name string
     *
     * Return: An integer value
     */
    public int getNumberOfEntries(Context c, String tableName) {

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(c);
        dbManager.open_read();
        Cursor cursor = dbManager.fetch(DBHelper.ITEMS_TABLE_NAME);

        /*
         * Notify the user in the event of a database access failure.
         */
        if (cursor.getCount() == 0) {
            Toast.makeText(c, R.string.alert_init_shopping_cart_fail, Toast.LENGTH_LONG).show();
        }

        /*
         * Get the number of available items.
         * Then close the database connection.
         */
        int numberOfItems = cursor.getCount();
        dbManager.close();

        /*
         * Now return the number of entries.
         */
        return numberOfItems;
    }
}
