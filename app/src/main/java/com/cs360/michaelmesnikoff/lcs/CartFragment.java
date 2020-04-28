package com.cs360.michaelmesnikoff.lcs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mp on 4/27/20.
 */

/*
 * This class defines the Shopping Cart fragment and all its internal methods.
 *
 * These are used to show the usr the current cart items and allow modifications.
 */
public class CartFragment extends Fragment {

    /*
     * A view instance.
     */
    View thisView;

    /*
     * Create DBManager and DBhelper instances to access the LCS database for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;

    /*
     * A context instance.
     */
    public Context context;

    /*
     * Instances to represent the fragment layout objects.
     */
    private ImageButton button_Back;
    private ImageButton button_Checkout;
    private ImageButton button_Discard;

    /*
     * Create an instance of the Helpers class.
     */
    Helpers helpers;

    /*
     * Create and instantiate the variables used for utilizing the Shared Preference persistent data.
     */
    protected String stringUsername;
    protected static final String ORDER_STATUS_PREFS = "My_OrderStatus_Prefs";
    protected static final String ORDER_LIST_KEY = "order_list_key";
    SharedPreferences.Editor sharedPref_cartEditor;

    /*
     * A FrameLayout instance for the shopping cart fragment
     */
    private FrameLayout cartFrag;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment, and create a "context" instance.
        thisView = inflater.inflate(R.layout.fragment_cart, container, false);
        context = getContext();

        cartFrag = thisView.findViewById(R.id.cartFragLayout);

        /*
         * Create a series of variables, including several to represent layout items.
         */
        button_Back = thisView.findViewById(R.id.imageButton_Cart_Back);
        button_Checkout = thisView.findViewById(R.id.imageButton_Cart_Checkout);
        button_Discard = thisView.findViewById(R.id.imageButton_Cart_Discard);

        int counter = 4;

        /*
         * Access the LCS database to get the ITEMS table info.
         */
        dbManager = new DBManager(context);
        dbManager.open_read();

        /*
         * Create a TableLayout instance to represent the table on the view that contains
         * the items info for selection.
         */
        TableLayout itemsTable = thisView.findViewById(R.id.tableLayout_Cart_Items);

        /*
         * Create a Shared Preference instance for accessing the persistent data.
         * Also create the variables and method to hold and access the data.
         */
        SharedPreferences order_listPref = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE);
        String stringOrderList = order_listPref.getString(ORDER_LIST_KEY, null);
        sharedPref_cartEditor = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE).edit();
        //sharedPref_cartEditor.clear();
        //sharedPref_cartEditor.commit();

        if (stringOrderList == null) {
            return thisView;
        }
        /*
         * Parse the dataString, creating the ContentValues along the way.
         *
         * It is assumed that the dataString will be in the form of:
         * key:value,key:value,...
         *
         * Note: This likely could be done in some JSON manner, but this seemed
         * quicker and simpler for this task.
         */
        String outerTokens[] = stringOrderList.split(",");
        for(String outerToken: outerTokens) {
            String innerTokens[] = outerToken.split(":");

            /*
             * Having gotten this far, we will now create the shopping cart list by
             * combining the item:quantity data from the Shared Preferences with the
             * associated database values, and totaling the costs.
             *
             * First, get the data returned from the database query.
             */
            Cursor cursor = dbManager.row_fetch(DBHelper.ITEMS_TABLE_NAME, DBHelper.COLUMN_ID, innerTokens[0]);

            /*
             * Now parse that data into strings as reguired for the shopping cart view.
             */
            String stringItemName = cursor.getString(cursor.getColumnIndex("item_name"));
            float floatItemPrice = cursor.getFloat(cursor.getColumnIndex("item_price"));
            String stringItemPrice = Float.toString(floatItemPrice);
            float floatTotalPrice = floatItemPrice * Integer.parseInt(innerTokens[1]);
            String stringTotalPrice = Float.toString(floatTotalPrice);

            /*
             * Now create the horizontal linear layout to be added to the view. After that
             * create the TextView fields that will go into the linear layout.
             */
            TextView newItem = new TextView(context);
            TextView newPrice = new TextView(context);
            EditText newQty = new EditText(context);
            TextView newTotal = new TextView(context);

            /*
             * Now set up the parameters for each of these fields, and load the associated
             * text string data.
             */
            newItem.setId(View.generateViewId());
            newItem.setWidth(Helpers.dpToPx(150));
            newItem.setTextColor(0x000000);
            newItem.setTypeface(Typeface.DEFAULT_BOLD);
            newItem.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            newItem.setText(stringItemName);

            newPrice.setId(View.generateViewId());
            newPrice.setWidth(Helpers.dpToPx(50));
            newPrice.setTextColor(0x000000);
            newPrice.setTypeface(Typeface.DEFAULT_BOLD);
            newPrice.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            newPrice.setText(stringItemPrice);

            newQty.setId(View.generateViewId());
            newQty.setWidth(Helpers.dpToPx(50));
            newQty.setTextColor(0x0000FF);
            newQty.setTypeface(Typeface.DEFAULT_BOLD);
            newQty.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            newQty.setText(innerTokens[1]);

            newTotal.setId(View.generateViewId());
            newTotal.setWidth(Helpers.dpToPx(50));
            newTotal.setTextColor(0x000000);
            newTotal.setTypeface(Typeface.DEFAULT_BOLD);
            newTotal.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            newTotal.setText(stringTotalPrice);

            //cart_list_cells.get(0).setText(stringItemName);
            //cart_list_cells.get(1).setText(stringItemPrice);
            //cart_list_cells.get(2).setText(innerTokens[1]);
            //cart_list_cells.get(3).setText(stringTotalPrice);

            /*
             * Now, create the row to be inserted in the selection table.  Set the "label" with the
             * string created earlier.
             */
            TableRow newRow = new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
            newRow.setLayoutParams(lp);
            newRow.setId(View.generateViewId());

            /*
             * Now set the onClick tasks.  For these buttons we will just pass the info from the
             * selected item to the EditText items elsewhere in this view for acting on.
             * The "Toast" message is just for verification of activity purposes.
             */
            /*
            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etItemName.setText(stringItemName);
                    etItemPrice.setText(stringItemPrice);
                    etItemID.setText(stringItemID);
                    Toast.makeText(context, "Selected", Toast.LENGTH_SHORT).show();
                }
            });
            */

            /*
             * Finally, add the created field objects to the created row object.
             * After that add the created row to the existing table object.
             */
            newRow.addView(newItem);
            newRow.addView(newPrice);
            newRow.addView(newQty);
            newRow.addView(newTotal);
            itemsTable.addView(newRow, counter);

            /*
             * Increment the counter,
             */
            counter++;
        }

        /*
         * Set up a "Back" button and listener.
         */
        button_Back.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onClick(View v) {
                cartFrag.setVisibility(View.INVISIBLE);
            }
        });

        return thisView;
    }
}