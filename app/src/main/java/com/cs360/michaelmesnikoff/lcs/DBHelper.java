package com.cs360.michaelmesnikoff.lcs;

/*
 * Created by mp on 3/5/20.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    /*
     * Create the table names for later.
     */
    public static final String USERS_TABLE_NAME = "USERS";
    public static final String ITEMS_TABLE_NAME = "ITEMS";

    /*
     * Create the table columns for later.
     */
    public static final String COLUMN_ID = "_id";
    public static final String ID_INFO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_FAV_ORDER = "fav_order";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_IMAGE_FILE = "item_image_file";
    public static final String COLUMN_ITEM_IMAGE = "item_image";
    public static final String TEXT_TYPE = " TEXT";
    public static final String REAL_TYPE = " REAL";
    public static final String BLOB_TYPE = " BLOB";
    public static final String NOT_NULL = " NOT NULL";
    public static final String COMMA_SEPARATOR = ", ";

    /*
     * Database name information.
     */
    static final String DB_NAME = "LCS.db";

    /*
     * Database version information.
     */
    static final int DB_VERSION = 1;

    /*
     * Create the CREATE_USERS_TABLE and CREATE_ITEMS_TABLE strings.
     */
    private static final String CREATE_USERS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            USERS_TABLE_NAME + "(" +
            COLUMN_ID + ID_INFO + COMMA_SEPARATOR +
            COLUMN_USERNAME + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_PASSWORD + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_EMAIL + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_FAV_ORDER + TEXT_TYPE + COMMA_SEPARATOR +
            "UNIQUE(username, email))";

    private static final String CREATE_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            ITEMS_TABLE_NAME + "(" +
            COLUMN_ID + ID_INFO + COMMA_SEPARATOR +
            COLUMN_ITEM_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_ITEM_PRICE + REAL_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_ITEM_IMAGE_FILE + TEXT_TYPE + COMMA_SEPARATOR +
            COLUMN_ITEM_IMAGE + BLOB_TYPE + COMMA_SEPARATOR +
            "UNIQUE(" + COLUMN_ITEM_NAME + "))";

    /*
     * The default class constructor.
     *
     * Arguments: A Context instance.
     *
     * Returns: None.
     */
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /*
     * The basic onCreate() method.  For this class it acts to create the database tables
     * if they do not already exist (a first-app-use item).
     *
     * Arguments: a SQLiteDatabase instance.
     *
     * Returns: None.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    /*
     * For this class we provide an override for the onUpgrade method that first "drops"
     * the app databases, and then calls the onCreate() mothod to re-create them.
     *
     * For now this method is not being used.
     *
     * It would be necessary for a real-world app to deal with app updates that alter
     * the database schema.  If it were actually being used, it would be necessary to make
     * this method first move the sotred data to temporary tables, and then re-enter it
     * into the new database schema tables, to protect against loss of data.
     *
     * Arguments: a SQLiteDatabase instance, the old version number int, the new version numbere int.
     *
     * Returns: None.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE_NAME);
        onCreate(db);
    }
}
