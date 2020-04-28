package com.cs360.michaelmesnikoff.lcs;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * Created by mp on 4/25/20.
 */

/*
 * This class will contain some basic, simple, helper methods.
 */
public class Helpers extends MainActivity {

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



    public void create_grid_cells(Dialog dialog) {
        //Placeholder
        /*
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView31));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView32));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView33));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView34));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView41));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView42));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView43));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView44));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView51));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView52));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView53));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView54));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView61));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView62));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView63));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView64));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView71));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView72));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView73));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView74));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView81));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView82));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView83));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView84));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView91));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView92));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView93));
        cart_list_cells.add(dialog.<TextView>findViewById(R.id.textView94));
        */
    }
}
