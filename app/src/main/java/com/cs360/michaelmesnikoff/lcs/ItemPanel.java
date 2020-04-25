package com.cs360.michaelmesnikoff.lcs;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by mp on 4/24/20.
 */

/*
 * This class will hold the "panel" information for each item to be displayed and/or ordered
 * in the Main Activity.
 */
public class ItemPanel {
    /*
     * Create a class-global view instance.
     */
    View thisView;

    Helpers helpers;

    /*
     * Create class-global DBManager and DBhelper instances to access the LCS database
     * for username/password info.
     */
    private DBManager dbManager;
    private DBHelper dbHelper;
    private Cursor cursor;

    /*
     * A class-global Context instance.
     */
    private Context context;

    /*
     * Now, reate a series of class-global instances of the necessary "panel" view items.
     */

    /*
     * A couple of LinearLayout instances.
     */
    protected LinearLayout linearLayout;
    protected LinearLayout panel;

    /*
     * The "Inner" ScrollView.
     */
    protected HorizontalScrollView hScrollView;

    /*
     * The various parts of the item.
     */
    protected TextView TvItemId;
    protected EditText EtItemQuantity;
    protected TextView TvItemName;
    protected TextView TvItemProce;
    protected ImageView TvItemImage;

    /*
     * Finally, the internal variables holding the data.
     */
    private int intItemID;
    private String stringItemID;
    private String stringItemName;
    private float floatItemPrice;
    private String stringItemPrice;
    private String stringItemImageFile;

    /*
     * The class constructor.
     */
    public ItemPanel(Context c) {
        /*
         * Initialize the context variable with the calling context.
         */
        context = c;
        helpers = new Helpers(context);

        /*
         * Access the LCS database to get the username/password info.
         */
        dbManager = new DBManager(context);
        dbManager.open_read();
        cursor = dbManager.fetch(DBHelper.ITEMS_TABLE_NAME);

        /*
         * If the database query did not return the needed data show an appropriate "Toast" message.
         * Otherwise create the selectable TableRow objects.
         */
        if (cursor.getCount() == 0) {
            Toast.makeText(context, "ITEMS database returned no data.", Toast.LENGTH_LONG).show();
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
                intItemID = cursor.getInt(cursor.getColumnIndex("_id"));
                stringItemID = Integer.toString(intItemID);

                stringItemName = cursor.getString(cursor.getColumnIndex("item_name"));

                floatItemPrice = cursor.getFloat(cursor.getColumnIndex("item_price"));
                stringItemPrice = Float.toString(floatItemPrice);

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
//                itemQuantity.setMinimumWidth(scrollerWidth);
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
//                itemName.setMinimumWidth(scrollerWidth);
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
//                itemPrice.setMinimumWidth(scrollerWidth);
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
//                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                itemImage.setBackgroundColor(0xFAFAFA);
                itemImage.setMaxWidth(helpers.dpToPx(200));
//                itemImage.setImageResource(imageResource);

                /*
                 * Set the ID value for the created quantity selector for later use.
                 */
//                itemQuantity.setId(counter);

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
//                linearLayout.addView(panel, counter);
//                linearLayout.setOnFocusChangeListener(fcl);

            } while (true);
        }
    }
}
