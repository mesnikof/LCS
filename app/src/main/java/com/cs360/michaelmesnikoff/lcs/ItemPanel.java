package com.cs360.michaelmesnikoff.lcs;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.View.generateViewId;

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
     * The "Inner" ScrollView.
     */
    protected HorizontalScrollView hScrollView;

    /*
     * The various parts of the item.
     */
    protected TextView TvItemId;
    protected EditText EtItemQuantity;
    protected TextView TvItemName;
    protected TextView TvItemPrice;
    protected ImageView IvItemImage;

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
        EtItemQuantity.setWidth(helpers.dpToPx(200));
        EtItemQuantity.setPadding(0, 25, 0, 0);
        EtItemQuantity.setHint(R.string.prompt_quantity);
        EtItemQuantity.setPaintFlags(android.graphics.Paint.UNDERLINE_TEXT_FLAG);
        EtItemQuantity.setTextSize(18);
        EtItemQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        EtItemQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        EtItemQuantity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        EtItemQuantity.clearFocus();
        EtItemQuantity.setBackgroundResource(R.drawable.item_quantity_style);
        //EtItemQuantity.addTextChangedListener(textWatcher);
        //EtItemQuantity.setOnFocusChangeListener(fcl);

        TvItemName = new TextView(context);
        TvItemName.setId(generateViewId());
        TvItemName.setMinimumWidth(helpers.dpToPx(200));
        TvItemName.setWidth(helpers.dpToPx(200));
        TvItemName.setPadding(0, 25, 0, 0);
        TvItemName.setBackgroundResource(R.drawable.item_name_style);
        TvItemName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TvItemName.setTextColor(Color.BLUE);
        TvItemName.setTextSize(18);

        TvItemPrice = new TextView(context);
        TvItemPrice.setId(generateViewId());
        TvItemPrice.setMinimumWidth(helpers.dpToPx(200));
        TvItemPrice.setWidth(helpers.dpToPx(200));
        TvItemPrice.setPadding(0, 25, 0, 0);
        TvItemPrice.setBackgroundResource(R.drawable.item_price_style);
        TvItemPrice.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TvItemPrice.setTextColor(Color.BLACK);
        TvItemPrice.setTextSize(18);

        IvItemImage = new ImageView(context);
        IvItemImage.setBackgroundColor(0xFAFAFA);
        IvItemImage.setMaxWidth(helpers.dpToPx(200));

        panel.addView(TvItemId);
        panel.addView(EtItemQuantity);
        panel.addView(TvItemName);
        panel.addView(TvItemPrice);
        panel.addView(IvItemImage);
    }
}
