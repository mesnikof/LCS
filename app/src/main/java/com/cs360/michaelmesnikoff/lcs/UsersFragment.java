package com.cs360.michaelmesnikoff.lcs;

/*
 * Created by mp on 3/5/20.
 */

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

/*
 * This class defines the administration items tools fragment and all its internal methods.
 *
 * These are used to maintain the database ITEMS table.
 */
public class UsersFragment extends Fragment {

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
    private Button button_Add;
    private Button button_Update;
    private Button button_Delete;
    private Button button_Back;
    private Button button_Clear;


    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment, and create a "context" instance.
        thisView = inflater.inflate(R.layout.fragment_users, container, false);
        context = getContext();

        /*
         * Create a series of variables, including several to represent layout items.
         */
        button_Add = (Button) thisView.findViewById(R.id.button_Add);
        button_Update = (Button) thisView.findViewById(R.id.button_Update);
        button_Delete = (Button) thisView.findViewById(R.id.button_Delete);
        button_Back = (Button) thisView.findViewById(R.id.button_Back);
        button_Clear = (Button) thisView.findViewById(R.id.button_Clear);

        final EditText etUsername = (EditText) thisView.findViewById(R.id.editText_username);
        final EditText etPassword = (EditText) thisView.findViewById(R.id.editText_password);
        final EditText etEmail = (EditText) thisView.findViewById(R.id.editText_email);
        final EditText etUserID = (EditText) thisView.findViewById(R.id.editText_userID);
        final EditText etFavOrder = (EditText) thisView.findViewById(R.id.editText_fav_order);

        int counter = 0;

        /*
         * Access the LCS database to get the ITEMS table info.
         */
        dbManager = new DBManager(context);
        dbManager.open_read();
        Cursor cursor = dbManager.fetch(DBHelper.USERS_TABLE_NAME);

        /*
         * Create a TableLayout instance to represent the table on the view that contains
         * the items info for selection.
         */
        TableLayout usersTable = (TableLayout) thisView.findViewById(R.id.tableLayout_Users);

        /*
         * Iterate through the returned database item information.
         * Perform the operations as documented below.
         */
        do {
            /*
             * First, extract the _id, username, and password information.  Use that to create the text
             * "Label" for the dynamically created button that will populate the selection table.
             * Also, store separate strings for passing to the other tasks in this fragment.
             */
            int intUserID = cursor.getInt(cursor.getColumnIndex("_id"));
            final String stringUserID = Integer.toString(intUserID);
            String buttonString = stringUserID;
            buttonString += ") ";
            final String stringUsername = cursor.getString(cursor.getColumnIndex("username"));
            buttonString += stringUsername;

            /*
             * Get the other column data for the row.
             */
            final String stringPassword = cursor.getString(cursor.getColumnIndex("password"));
            final String stringEmail = cursor.getString(cursor.getColumnIndex("email"));
            final String stringFavOrder = cursor.getString(cursor.getColumnIndex("fav_order"));

            /*
             * Now, create the row to be inserted in the selection table.  Set the "label" with the
             * string created earlier.
             */
            TableRow row = new TableRow(context);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            Button itemButton = new Button(context);
            itemButton.setText(buttonString);

            /*
             * Set the ID value for the created button for possible later use.
             */
            itemButton.setId(counter);

            /*
             * Now set the onClick tasks.  For these buttons we will just pass the info from the
             * selected item to the EditText items elsewhere in this view for acting on.
             * The "Toast" message is just for verification of activity purposes.
             */
            itemButton.setOnClickListener(new View.OnClickListener() {
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

            /*
             * Finally, add the created button object to the created row object.
             * After that add the created row to the existing table object.
             */
            row.addView(itemButton);
            usersTable.addView(row, counter);

            /*
             * Lastly, increment the counter used for the loop.
             */
            counter++;
        } while(cursor.moveToNext());


        /*
         * This method is used for performing inserts into the ITEMS database table.
         */
        button_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * A long variable to hold the insert results.
                */
                long queryResult = 0;

                /*
                 * A String variable to hold the data to be inserted.
                 * Load it with the data.
                 */
                String insertString = "username:";
                insertString += etUsername.getText();
                insertString += ",password:";
                insertString += etPassword.getText();
                insertString += ",email:";
                insertString += etEmail.getText();

                /*
                 * Open a DBmanager instance on the database.
                 */
                dbManager = new DBManager(context);
                dbManager.open_write();

                /*
                 * Run the query, get the result for testing.
                 */
                queryResult = dbManager.insert(DBHelper.USERS_TABLE_NAME, insertString);

                /*
                 * Show an appropriate "Toast" message for either failure or success.
                 * If success, clear_icon the data from the view objects.
                 */
                if(queryResult == 0) {
                    Toast.makeText(context, "User Add Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "User Added", Toast.LENGTH_SHORT).show();
                    etUsername.setText("");
                    etPassword.setText("");
                    etEmail.setText("");
                    etFavOrder.setText("");
                    etUserID.setText("");
                }

                /*
                 * Close the databae for cleanliness.
                 */
                dbManager.close();
            }
        });


        /*
         * This method is used for performing inserts into the ITEMS database table.
         */
        button_Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * A long variable to hold the insert results.
                */
                long queryResult = 0;

                /*
                 * A String variable to hold the data to be inserted.
                 * Load it with the data.
                 */
                String insertString = "username:";
                insertString += etUsername.getText();
                insertString += ",password:";
                insertString += etPassword.getText();
                insertString += ",email:";
                insertString += etEmail.getText();

                String stringID = "";
                stringID += etUserID.getText();
                int intID = Integer.parseInt(stringID);

                /*
                 * Open a DBmanager instance on the database.
                 */
                dbManager = new DBManager(context);
                dbManager.open_write();

                /*
                 * Run the query, get the result for testing.
                 */
                queryResult = dbManager.update(DBHelper.USERS_TABLE_NAME, intID, insertString);

                /*
                 * Show an appropriate "Toast" message for either failure or success.
                 * If success, clear_icon the data from the view objects.
                 */
                if(queryResult == 0) {
                    Toast.makeText(context, "User Update Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "User Updated", Toast.LENGTH_SHORT).show();
                    etUsername.setText("");
                    etPassword.setText("");
                    etEmail.setText("");
                    etFavOrder.setText("");
                    etUserID.setText("");
                }

                /*
                 * Close the databae for cleanliness.
                 */
                dbManager.close();
            }
        });


        /*
         * This method is used for performing deletes from the ITEMS database table.
         *
         * Clearly, in a "real-world" app, some form of "Are You Sure?" would be implemented,
         * but for the purpose of this task, the listener simply calls the database delete()
         * method with no confirmation checking.
         */
        button_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                * A long variable to hold the insert results.
                */
                long queryResult = 0;

                String stringID = "";
                stringID += etUserID.getText();
                int intID = Integer.parseInt(stringID);

                /*
                 * Open a DBmanager instance on the database.
                 */
                dbManager = new DBManager(context);
                dbManager.open_write();

                /*
                 * Run the query, get the result for testing.
                 */
                queryResult = dbManager.delete(DBHelper.USERS_TABLE_NAME, intID);

                /*
                 * Show an appropriate "Toast" message for either failure or success.
                 * If success, clear_icon the data from the view objects.
                 */
                if(queryResult == 0) {
                    Toast.makeText(context, "User Delete Failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "User Deleted", Toast.LENGTH_SHORT).show();
                    etUsername.setText("");
                    etPassword.setText("");
                    etEmail.setText("");
                    etFavOrder.setText("");
                    etUserID.setText("");
                }

                /*
                 * Close the databae for cleanliness.
                 */
                dbManager.close();
            }
        });


        /*
         * This method clears the data objects in the view.
         */
        button_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUsername.setText("");
                etPassword.setText("");
                etEmail.setText("");
                etFavOrder.setText("");
                etUserID.setText("");
                Toast.makeText(context, "Cleared", Toast.LENGTH_SHORT).show();
            }
        });


        /*
         * Close the database for cleanliness.
         */
        dbManager.close();

        return thisView;
    }

}
