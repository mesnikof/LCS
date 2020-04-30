package com.cs360.michaelmesnikoff.lcs;

import android.annotation.SuppressLint;

import android.app.Dialog;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

import android.graphics.Color;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;

import android.view.View;
import static android.view.View.generateViewId;

import android.widget.EditText;
import android.widget.ImageButton;
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

    /*
     * An instance of the Helpers class.
     */
    Helpers helpers;

    /*
     * A class-global Context instance.
     */
    private Context context;

    /*
     * Now, reate a series of class-global instances of the necessary "panel" view items.
     */

    /*
     * A LinearLayout instances.
     */
    protected LinearLayout panel;

    /*
     * The various parts of the passed-in item.
     */
    protected TextView TvItemId;
    protected EditText EtItemQuantity;
    protected TextView TvItemName;
    protected TextView TvItemPrice;
    protected ImageView IvItemImage;

    /*
     * Instantiate a new class-global dialog object.
     */
    protected Dialog itemDialog;

    /*
     * Set up class-global instances of the data boxes in the dialog.
     */
    protected EditText itemDialog_EditText;
    protected TextView itemDialog_Name;
    protected TextView itemDialog_Price;
    protected TextView itemDialog_Total;
    protected TextView itemDialog_ID;

    protected static final String ORDER_STATUS_PREFS = "My_OrderStatus_Prefs";
    protected static final String ORDER_LIST_KEY = "order_list_key";
    SharedPreferences.Editor sharedPref_myEditor;

    /*
     * A class-global float.
     */
    private float globalPrice;



    /*
     * The class constructor.
     */
    public ItemPanel(Context c) {
        /*
         * Initialize the context variable with the calling context.
         */
        context = c;
        helpers = new Helpers(context);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        panel = new LinearLayout(context);
        panel.setLayoutParams(lp);
        panel.setOrientation(LinearLayout.VERTICAL);

        TvItemId = new TextView(context);
        TvItemId.setVisibility(View.INVISIBLE);

        EtItemQuantity = new EditText(context);
        EtItemQuantity.setId(generateViewId());
        EtItemQuantity.setWidth(Helpers.dpToPx(200));
        EtItemQuantity.setPadding(0, 25, 0, 0);
        EtItemQuantity.setHint(R.string.prompt_quantity);
        EtItemQuantity.setPaintFlags(android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        EtItemQuantity.setTextSize(18);
        EtItemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        EtItemQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        EtItemQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        EtItemQuantity.clearFocus();
        EtItemQuantity.setBackgroundResource(R.drawable.item_quantity_style);
        EtItemQuantity.addTextChangedListener(textWatcher);
        //EtItemQuantity.setOnFocusChangeListener(fcl);

        TvItemName = new TextView(context);
        TvItemName.setId(generateViewId());
        TvItemName.setMinimumWidth(Helpers.dpToPx(200));
        TvItemName.setWidth(Helpers.dpToPx(200));
        TvItemName.setPadding(0, 25, 0, 0);
        TvItemName.setBackgroundResource(R.drawable.item_name_style);
        TvItemName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TvItemName.setTextColor(Color.BLUE);
        TvItemName.setTextSize(18);

        TvItemPrice = new TextView(context);
        TvItemPrice.setId(generateViewId());
        TvItemPrice.setMinimumWidth(Helpers.dpToPx(200));
        TvItemPrice.setWidth(Helpers.dpToPx(200));
        TvItemPrice.setPadding(0, 25, 0, 0);
        TvItemPrice.setBackgroundResource(R.drawable.item_price_style);
        TvItemPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TvItemPrice.setTextColor(Color.BLACK);
        TvItemPrice.setTextSize(18);

        IvItemImage = new ImageView(context);
        IvItemImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
        IvItemImage.setMaxWidth(Helpers.dpToPx(200));
        IvItemImage.setBackgroundColor(0xFAFAFA);
        IvItemImage.setMaxWidth(Helpers.dpToPx(200));

        panel.addView(TvItemId);
        panel.addView(EtItemQuantity);
        panel.addView(TvItemName);
        panel.addView(TvItemPrice);
        panel.addView(IvItemImage);
    }



    /*
     * This is the listener for changes to a quantity value on the Main Activity page.
     * It shows a dialog allowing the user to accept or change the quantity, displays the
     * total cost, and then allows the user to either discard the item, or add it to the
     * shopping cart.
     *
     * The three overrides are required.  We ignore beforeTextChanged() and
     * onTextChanged(), and only act upon afterTextChanged().
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Required override placeholder.
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Required override placeholder.
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void afterTextChanged(Editable editable) {
            String strTotal;

            /*
             * Point the class-global dialog instance to the "item_dialog" layout.
             */
            itemDialog = new Dialog(context);
            itemDialog.setContentView(R.layout.item_dialog);

            /*
             * Point the class-global instances to the item boxes.
             */
            itemDialog_EditText = itemDialog.findViewById(R.id.itemDialog_ET_Quantity);
            itemDialog_Name = itemDialog.findViewById(R.id.itemDialog_TV_Name);
            itemDialog_Price = itemDialog.findViewById(R.id.itemDialog_TV_Price);
            itemDialog_Total = itemDialog.findViewById(R.id.itemDialog_TV_Total);
            itemDialog_ID = itemDialog.findViewById(R.id.itemDialog_TV_ID);

            /*
             * Put a limit filter (max quantity 99, two digits) and set a listener for the
             * Quantity EditText item in the dialog.
             */
            itemDialog_EditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            itemDialog_EditText.addTextChangedListener(dialogTextWatcher);

            /*
             * Create variables to hold the passed-in data.
             */
            Editable getEditable = EtItemQuantity.getText();
            String strQuantity = getEditable.toString();
            String strName = TvItemName.getText().toString();
            String strID = TvItemId.getText().toString();
            String strPrice = TvItemPrice.getText().toString();

            /*
             * Parse the data from the View instance that called this dialog.
             *
             * Note: The variable names are simplistic as they are block-local.
             */
            if (editable.toString().equals("")) {
                strTotal = "0";
            }
            else {
                int iiii = Integer.parseInt(strQuantity);
                float ffff = Float.parseFloat(strPrice);
                globalPrice = ffff;
                float fTotal = iiii * ffff;
                strTotal = String.format("%.2f", fTotal);
            }

            /*
             * Now load the data into the dialog's data boxes.
             */
            itemDialog_EditText.setText(strQuantity);
            itemDialog_Name.setText(strName);
            itemDialog_Price.setText(strPrice);
            itemDialog_Total.setText(strTotal);
            itemDialog_ID.setText(strID);

            /*
             * Set up a "Back" button and listener.
             *
             * If the "Back" button is clicked, close the custom dialog.
             */
            ImageButton dialogBackButton = itemDialog.findViewById(R.id.imageButton_IB_Back);
            dialogBackButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"ApplySharedPref", "SetTextI18n"})
                @Override
                public void onClick(View v) {
                    itemDialog.dismiss();
                }
            });

            /*
             * Set up a "Discard" button and listener.
             *
             * If "Discard" button is clicked, clear the item and close the custom dialog.
             */
            ImageButton dialogDiscardButton = itemDialog.findViewById(R.id.itemDialog_IB_Discard);
            dialogDiscardButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint({"ApplySharedPref", "SetTextI18n"})
                @Override
                public void onClick(View v) {
                    /*
                     * "Turn off" the listener temporarily to allow for the value to be reset to
                     * "blank" because it was discarded.  After resetting the value turn the
                     * listener back on.
                     */
                    EtItemQuantity.removeTextChangedListener(textWatcher);
                    EtItemQuantity.setText("0");
                    EtItemQuantity.addTextChangedListener(textWatcher);

                    /*
                     * Now reset the values in the dialog (just in case) and dismiss the dialog,
                     * sending the user back to the Main Activity page.
                     */
                    itemDialog_EditText.setText("");
                    itemDialog_Total.setText("0.00");
                    itemDialog.dismiss();

                    /*
                     * Clear the value from the Shared Preferences entry.
                     */
                    SharedPreferences order_listPref = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE);
                    sharedPref_myEditor = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE).edit();

                    /*
                     * Set up the Shared Preference key to be modified.
                     */
                    String itemKey = "item_";
                    itemKey += itemDialog_ID.getText().toString();
                    itemKey += "_key";

                    /*
                     * Write out the zeroed shared preference.
                     */
                    sharedPref_myEditor.putString(itemKey, "0");
                    sharedPref_myEditor.commit();

                    Toast.makeText(context, "Dismissed..!!", Toast.LENGTH_SHORT).show();
                }
            });

            /*
             * Set up a "Add to Cart" button and listener.
             */
            ImageButton dialogAddButton = itemDialog.findViewById(R.id.itemDialog_IB_AddToCart);
            dialogAddButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ApplySharedPref")
                @Override
                public void onClick(View v) {
                    /*
                     * Create a Shared Preference instance for maintaining persistent data.
                     * Also create the variables and method to hold and access/update the data.
                     */
                    SharedPreferences order_listPref = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE);
                    sharedPref_myEditor = context.getSharedPreferences(ORDER_STATUS_PREFS, MODE_PRIVATE).edit();

                    /*
                     * Set up the Shared Preference key to be modified.
                     */
                    String itemKey = "item_";
                    itemKey += itemDialog_ID.getText().toString();
                    itemKey += "_key";

                    /*
                     * Write out the shared preference quantity value for the selected item.
                     * If it is "" (null), write out a "0" (the integer zero).
                     */
                    if (itemDialog_EditText.getText().toString() == "") {
                        sharedPref_myEditor.putString(itemKey, "0");
                    }
                    else {
                        sharedPref_myEditor.putString(itemKey, itemDialog_EditText.getText().toString());
                    }
                    sharedPref_myEditor.commit();

                    itemDialog.dismiss();
                    Toast.makeText(context, "Added..!!", Toast.LENGTH_SHORT).show();
                }
            });

            /*
             * Now that all the setup is done, display the dialog.
             */
            itemDialog.show();
        }
    };



    /*
     * This is the listener for changes to the quantity value in the item dialog page.
     * It updates the total price when the quantity value s changed in the dialog.
     *
     * The three overrides are required.  We ignore beforeTextChanged() and
     * onTextChanged(), and only act upon afterTextChanged().
     */
    TextWatcher dialogTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Required override placeholder.
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Required override placeholder.
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void afterTextChanged(Editable editable) {
            /*
             * Instantiate the class-global instances of dialog and the data boxes
             * in the dialog.
             */
            //itemDialog = new Dialog(context);
            String strTotal;

            /*
             * Point the class-global instances to the item boxes.
             */
            itemDialog_EditText = itemDialog.findViewById(R.id.itemDialog_ET_Quantity);
            itemDialog_Name = itemDialog.findViewById(R.id.itemDialog_TV_Name);
            itemDialog_Price = itemDialog.findViewById(R.id.itemDialog_TV_Price);
            itemDialog_Total = itemDialog.findViewById(R.id.itemDialog_TV_Total);

            /*
             * Parse the data from the quantity instance that called this dialog.
             */
            if (editable.toString().equals("")) {
                strTotal = "0";
            }
            else {
                int iiii = Integer.parseInt(editable.toString());
                float fTotal = iiii * globalPrice;
                strTotal = String.format("%.2f", fTotal);
            }

            /*
             * Now load the data into the dialog's data boxes.
             */
            itemDialog_Total.setText(strTotal);
        }
    };

}
