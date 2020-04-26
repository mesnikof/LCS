package com.cs360.michaelmesnikoff.lcs;

/*
 * Created by mp on 3/5/20.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/*
 * The DBManager class contains all the methods that are called to access and use the
 * SQLite internal app database, its tables, and the stored values.
 */
public class DBManager {

    /*
     * Define a few class local values.
     */
    private DBHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    /*
     * This is the required constructor for the class.
     * */
    public DBManager(Context c) {
        context = c;
    }

    /*
     * This is the generic constructor for the class.
     * It opens the database for writing, and returns the context.
     * For clearer purposes, there is an identical method named open_write below.
     */
    public DBManager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }


    /*
     * This is the constructor for the class.
     * It opens the database for reading, and returns the context.
     */
    public DBManager open_read() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getReadableDatabase();
        return this;
    }


    /*
     * This is the constructor for the class.
     * It opens the database for writing, and returns the context.
     */
    public DBManager open_write() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }


    /*
     * This is a somewhat dirty function used to create an initial set of items and users
     * in the ITEMS and USERS tables.  It uses the "standard" item add table insert method,
     * with a set of values in an array that is iterated over.
     *
     * However, as this simply needs to work to demonstrate that a database, table, and
     * values exist, it has been done in this graunchy static way.
     */
    public long initializeDB() {
        /*
         * A long variable to hold the insert results.
         */
        long queryResult = 0;

        // A couple of syntax examples for future use.
        //INSERT OR IGNORE INTO bookmarks(users_id, lessoninfo_id) VALUES(123, 456)
        //database.execSQL(CREATE_ITEM0);

        /*
         * Create a pair of string arrays holding the USERS and ITEMS table initialization
         * values, respectively.
         */
        String[] initUserStrings = {
                "username:user,password:user,email:user@lcs.com,card:333388888899999,expire:01/23,cvv:1234",
                "username:admin,password:admin,email:admin@lcs.com,card:0000000000000000,expire:00/00,cvv:000",
                "username:michael,password:michael,email:michael@lcs.com,card:4444777788889999,expire:12/24,cvv:765"
        };

        /*
         * Future use, separation of user and card info.
         *
        String[] initCardStrings = {
                "username:user,password:user,email:user@lcs.com,card:333388888899999,expire:01/23,cvv:1234",
                "username:admin,password:admin,email:admin@lcs.com,card:0000000000000000,expire:00/00,cvv:000",
                "username:michael,password:michael,email:michael@lcs.com,card:4444777788889999,expire:12/24,cvv:765"
        };
        */

        String[] initItemStrings = {
                "item_name:Small Coffee,item_price:1.99,item_image_file:small_coffee",
                "item_name:Medium Coffee,item_price:2.49,item_image_file:medium_coffee",
                "item_name:Large Coffee,item_price:2.99,item_image_file:large_coffee",
                "item_name:Small Tea,item_price:1.99,item_image_file:small_tea",
                "item_name:Medium Tea,item_price:2.49,item_image_file:medium_tea",
                "item_name:Large Tea,item_price:2.99,item_image_file:large_tea",
                "item_name:Cafe Latte,item_price:3.49,item_image_file:latte",
                "item_name:Cappucino,item_price:3.49,item_image_file:cappuccino",
                "item_name:Bagel,item_price:1.00,item_image_file:bagel",
                "item_name:Donut,item_price:1.00,item_image_file:donut",
                "item_name:Muffin,item_price:2.49,item_image_file:muffin",
                "item_name:12 Munchkins,item_price:2.99,item_image_file:munchkins"
        };

        /*
         * Now iterate over the String arrays, once for USERS, once for ITEMS.
         *
         * ToDo: Add error/exception checking.
         */
        for(String insertString: initUserStrings) {
            queryResult = this.insert(DBHelper.USERS_TABLE_NAME, insertString);
        }

        /*
         * Future use, separation of user and card info.
         *
        for(String insertString: initCardStrings) {
            queryResult = this.insert(DBHelper.CARDS_TABLE_NAME, insertString);
        }
        */

        for(String insertString: initItemStrings) {
            queryResult = this.insert(DBHelper.ITEMS_TABLE_NAME, insertString);
        }

        return(queryResult);
    }


    /*
     * This function closes the database instance.
     */
    public void close() {
        dbHelper.close();
    }


    /*
     * This is the "standard" database insert method.
     *
     * Arguments: String table_name - a java String containing the table to be inserted into.
     *            String insert_string - a JSON-like string containg the insert values.
     */
    public long insert(String table_name, String insert_string) {
        /*
         * Create a new ContentValues instance to hold the data to insert.
         */
        ContentValues contentValue = new ContentValues();

        /*
         * Open the writable database.
         */
        database = dbHelper.getWritableDatabase();

        /*
         * Parse the insert_string, creating the ContentValues along the way.
         *
         * It is assumed that the insert_string will be in the form of:
         * key:value,key:value,...
         *
         * Note: This likely could be done in some JSON manner, but this seemed
         * quicker and simpler for this task.
         */
        String outerTokens[] = insert_string.split(",");
        for(String outerToken: outerTokens) {
            String innerTokens[] = outerToken.split(":");
            contentValue.put(innerTokens[0], innerTokens[1]);
        }

        /*
         * Now perform the insert, saving the result in a "long" for examination.
         */
        long queryResult = database.insert(table_name, null, contentValue);

        /*
         * Close the database.
         */
        this.close();

        return(queryResult);
    }


    public Cursor fetch(String table_name) {
        String[] columns = new String[] {};
        Cursor cursor = database.query(table_name, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


    /*
     * This is the standard update method for the LCS database.
     *
     * Arguments: String - table name, long - row id, String - data string
     *
     * Return: long with the query result
     */
    public long update(String table_name, long _id, String dataString) {
        /*
         * Create a new ContentValues instance to hold the data to insert.
         */
        ContentValues contentValue = new ContentValues();

        /*
         * Open the writable database.
         */
        database = dbHelper.getWritableDatabase();

        /*
         * Parse the dataString, creating the ContentValues along the way.
         *
         * It is assumed that the dataString will be in the form of:
         * key:value,key:value,...
         *
         * Note: This likely could be done in some JSON manner, but this seemed
         * quicker and simpler for this task.
         */
        String outerTokens[] = dataString.split(",");
        for(String outerToken: outerTokens) {
            String innerTokens[] = outerToken.split(":");
            contentValue.put(innerTokens[0], innerTokens[1]);
        }

        /*
         * Now perform the update, saving the result in a "long" for examination.
         */
        long queryResult = database.update(table_name, contentValue, DBHelper.COLUMN_ID + " = " + _id, null);

        /*
         * Close the database.
         */
        this.close();

        return(queryResult);
    }


    /*
     * This method is used to perform database deletes.
     */
    public long delete(String table_name, long _id) {
        /*
         * Open the writable database.
         */
        database = dbHelper.getWritableDatabase();

        /*
         * Now perform the delete, saving the result in a "long" for examination.
         */
        long queryResult = database.delete(table_name,"_ID" + "=" + _id, null);

        /*
         * Close the database.
         */
        this.close();

        return(queryResult);
    }

}
