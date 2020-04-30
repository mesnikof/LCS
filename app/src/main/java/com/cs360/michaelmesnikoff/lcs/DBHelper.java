package com.cs360.michaelmesnikoff.lcs;

/*
 * Created by mp on 3/5/20.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    /*
     * Create the table names for later.
     */
    protected static final String USERS_TABLE_NAME = "USERS";
    protected static final String CARDS_TABLE_NAME = "CARDS";
    protected static final String ITEMS_TABLE_NAME = "ITEMS";

    /*
     * Create the table columns for later.
     */
    protected static final String COLUMN_ID = "_id";
    protected static final String ID_INFO = " INTEGER PRIMARY KEY AUTOINCREMENT";
    protected static final String COLUMN_USERNAME = "username";
    protected static final String COLUMN_PASSWORD = "password";
    protected static final String COLUMN_EMAIL = "email";
    protected static final String COLUMN_FAV_ORDER = "fav_order";
    protected static final String COLUMN_CARD = "card";
    protected static final String COLUMN_EXPIRE = "expire";
    protected static final String COLUMN_CVV = "cvv";
    protected static final String COLUMN_ITEM_NAME = "item_name";
    protected static final String COLUMN_ITEM_PRICE = "item_price";
    protected static final String COLUMN_ITEM_IMAGE_FILE = "item_image_file";
    protected static final String COLUMN_ITEM_IMAGE = "item_image";
    private static final String TEXT_TYPE = " TEXT";
    private static final String UINT_TYPE = " UNSIGNED BIG INT";
    private static final String INT_TYPE = " INT";
    private static final String REAL_TYPE = " REAL";
    private static final String BLOB_TYPE = " BLOB";
    private static final String NOT_NULL = " NOT NULL";
    private static final String COMMA_SEPARATOR = ", ";

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
            COLUMN_CARD + UINT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_EXPIRE + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_CVV + INT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            "UNIQUE(username, email, card))";

    private static final String CREATE_CARDS_TABLE = "CREATE TABLE IF NOT EXISTS " +
            CARDS_TABLE_NAME + "(" +
            COLUMN_ID + ID_INFO + COMMA_SEPARATOR +
            COLUMN_CARD + UINT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_EXPIRE + TEXT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            COLUMN_CVV + INT_TYPE + NOT_NULL + COMMA_SEPARATOR +
            "UNIQUE(card))";

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
        db.execSQL(CREATE_CARDS_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE_NAME);
        onCreate(db);
    }
}
