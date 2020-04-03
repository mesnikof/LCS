package com.cs360.michaelmesnikoff.lcs;

import android.content.Intent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class AdminActivity extends AppCompatActivity {

    /*
     * Create some local variables to hold "pointers" to the layout objects and the database.
     */
    private DBManager dbManager;

    private Button button_InputItem;
    private Button button_InputUser;
    private Button button_Back;

    private FrameLayout itemFrag;
    private FrameLayout userFrag;
    private FrameLayout blankFrag;


    /*
     * The standard onCreate method.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        /*
         * Set up the "Action Bar" with the LCS icon and label.
         */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.ic_launcher_round);

        /*
         * Initialize the class variables with "pointers" to the matching layout object.
         */
        button_InputItem = (Button) findViewById(R.id.button_InputItem);
        button_InputUser = (Button) findViewById(R.id.button_InputUser);
        button_Back = (Button) findViewById(R.id.button_Back);

        itemFrag = (FrameLayout) findViewById(R.id.itemFragLayout);
        userFrag = (FrameLayout) findViewById(R.id.userFragLayout);
        blankFrag = (FrameLayout) findViewById(R.id.blankFragLayout);

        blankFrag.setVisibility(View.VISIBLE);
        itemFrag.setVisibility(View.INVISIBLE);
        userFrag.setVisibility(View.INVISIBLE);

        /*
         * Create the botton listener methods.
         * The User and Item bottons function by making the appropriate fragment visible, and
         * the other fragments invisible.  This is not the best method, but serves the purpose
         * for this task.
         * The Back button uses the finish() call to end this activity and go back to the previous
         * one (Login Activity).
         */
        button_InputItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "InputItem", Toast.LENGTH_SHORT).show();
                blankFrag.setVisibility(View.INVISIBLE);
                userFrag.setVisibility(View.INVISIBLE);
                itemFrag.setVisibility(View.VISIBLE);
            }
        });

        button_InputUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AdminActivity.this, "UserItem", Toast.LENGTH_SHORT).show();
                blankFrag.setVisibility(View.INVISIBLE);
                itemFrag.setVisibility(View.INVISIBLE);
                userFrag.setVisibility(View.VISIBLE);
            }
        });

        button_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /*
     * The onPause method.
     */
    protected void onPause() {
        super.onPause();
    }


    /*
     * Part of the fragment tools.  Not used at this time.
     */
    public void addFragment(Fragment fragment, boolean addToBackStack, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(tag);
        }
        ft.replace(R.id.container_frame, fragment, tag);
        ft.commitAllowingStateLoss();
    }


    /*
     * Return to the Login activity when the "Back" button is clicked.
     */
    public void button_BackOnClick(View v) {
        Intent intent = new Intent("com.cs360.michaelmesnikoff.lcs.LoginActivity");
        startActivity(intent);
    }


    /*
     * Method to swap fragment being viewed for blank (basic Admin view).
     * Simply sets the "Blank" fragment to visible, and the others to invisible.
     * Also shows a "Toast" message for informational purposes.
     */
    public void returnFromFragment(View v) {
        blankFrag.setVisibility(View.VISIBLE);
        itemFrag.setVisibility(View.INVISIBLE);
        userFrag.setVisibility(View.INVISIBLE);
        Toast.makeText(AdminActivity.this, "Back", Toast.LENGTH_SHORT).show();
    }

}
