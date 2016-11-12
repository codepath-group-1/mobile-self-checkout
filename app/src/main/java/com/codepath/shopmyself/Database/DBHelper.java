package com.codepath.shopmyself.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by supsingh on 11/11/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private final boolean print_db = false;

    // Dtaabase Name, Table Name
    public static final String DATABASE_NAME = "Items.db";
    public static final String ITEMS_TABLE_NAME = "ItemsTable";

    // Column Namea
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";
    public static final String COLUMN_ITEM_MANUFACTURER = "item_manufacturer";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_ISLE_LOCATION = "item_isle_location";
    public static final String COLUMN_ITEM_QUANTITY_IN_STORE = "item_quantity_in_store";
    public static final String COLUMN_ITEM_RATING = "item_rating";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Lazy Singleton
    private static DBHelper mydbInstanceHolder = null;

    public static synchronized DBHelper getInstance(Context context) {
        if(mydbInstanceHolder == null)
            mydbInstanceHolder = new DBHelper(context);
        return mydbInstanceHolder;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ITEMS_TABLE_NAME + "(" +
                COLUMN_ITEM_CODE               + " long primary key, " +
                COLUMN_ITEM_NAME               + " text, " +
                COLUMN_ITEM_DESCRIPTION        + " text, " +
                COLUMN_ITEM_MANUFACTURER       + " text, " +
                COLUMN_ITEM_PRICE              + " real, " +
                COLUMN_ITEM_ISLE_LOCATION      + " text, " +
                COLUMN_ITEM_QUANTITY_IN_STORE  + " integer, " +
                COLUMN_ITEM_RATING             + " integer)");

        if (print_db) printTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllData(SQLiteDatabase db) {
        if (db == null)
            db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + ITEMS_TABLE_NAME, null);
        return res;
    }

    public void printTable(SQLiteDatabase db) {
        int pos = 0;
        if (db == null)
            db = this.getReadableDatabase();

        Cursor res = getAllData(db);
        res.moveToFirst();

        Log.e("Fields:", " pos,   " +
                "COLUMN_ITEM_CODE,   " +
                "COLUMN_ITEM_NAME,   " +
                "COLUMN_ITEM_DESCRIPTION,   " +
                "COLUMN_ITEM_MANUFACTURER,   " +
                "COLUMN_ITEM_PRICE");
        while (res.isAfterLast() == false) {
            Log.e("Table:",
                    pos + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_CODE)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_NAME)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_DESCRIPTION)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_MANUFACTURER)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_DESCRIPTION)));
            res.moveToNext();
            pos++;
        }
    }
}
