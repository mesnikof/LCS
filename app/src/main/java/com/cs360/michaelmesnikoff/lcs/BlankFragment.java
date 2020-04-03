package com.cs360.michaelmesnikoff.lcs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mp on 3/21/20.
 *
 * This is just an empty fragment to keep the container blank when it is not being used.
 */

public class BlankFragment extends Fragment {
    View thisView;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        thisView = inflater.inflate(R.layout.fragment_blank, container, false);
        return thisView;
    }
}
