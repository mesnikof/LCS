package com.cs360.michaelmesnikoff.lcs;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

/**
 * Created by mp on 4/25/20.
 */

/*
 * This class will contain some basic, simple, helper methods.
 */
public class Helpers {
    public Helpers(Context c) {
        // Basic, do-nothing constructor.
    }

    /*
     * These methods convert to/from DPs/pixels for view element sizing.
     */
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }
}
