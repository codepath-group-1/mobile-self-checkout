package com.codepath.shopmyself.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.codepath.shopmyself.Models.Item;

import java.util.ArrayList;

/**
 * Created by supsingh on 11/11/2016.
 * Currently unused. Will delete if not needed
 */

public class DBHelper extends SQLiteOpenHelper {

    private final boolean print_db = false;

    // Dtaabase Name, Table Name
    public static final String DATABASE_NAME = "Item.db";
    public static final String ITEMS_TABLE_NAME = "ItemsTable";

    // Column Names
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";
    public static final String COLUMN_ITEM_MANUFACTURER = "item_manufacturer";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_IMAGEURL = "item_imageurl";
    public static final String COLUMN_ITEM_ISLE_LOCATION = "item_isle_location";
    public static final String COLUMN_ITEM_QUANTITY_IN_STORE = "item_quantity_in_store";
    public static final String COLUMN_ITEM_RATING = "item_rating";
    public static final String COLUMN_ITEM_QUANTITY = "item_quantity";
    public static final String COLUMN_ITEM_TIMESTAMP = "item_timestamp";


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
                COLUMN_ITEM_IMAGEURL           + " text, " +
                COLUMN_ITEM_ISLE_LOCATION      + " text, " +
                COLUMN_ITEM_QUANTITY_IN_STORE  + " integer, " +
                COLUMN_ITEM_RATING             + " integer, " +
                COLUMN_ITEM_QUANTITY           + " integer, " +
                COLUMN_ITEM_TIMESTAMP          + " integer)");

        if (print_db) printTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getDataAt(int position) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + ITEMS_TABLE_NAME + " ORDER BY " + COLUMN_ITEM_TIMESTAMP + " LIMIT 1 OFFSET " + position, null);

        if (print_db) printTable(db);
        return res;
    }

    public Cursor getAllData(SQLiteDatabase db) {
        if (db == null)
            db = this.getReadableDatabase();

        Cursor res = db.rawQuery("select * from " + ITEMS_TABLE_NAME, null);
        return res;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> item_list = new ArrayList();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = getAllData(db);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            item_list.add(new Item(res));
            res.moveToNext();
        }

        if (print_db) printTable(db);
        return item_list;
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
                "COLUMN_ITEM_PRICE,   " +
                "COLUMN_ITEM_IMAGEURL,   " +
                "COLUMN_ITEM_QUANTITY,   " +
                "COLUMN_ITEM_TIMESTAMP");
        while (res.isAfterLast() == false) {
            Log.e("Table:",
                    pos + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_CODE)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_NAME)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_DESCRIPTION)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_MANUFACTURER)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_PRICE)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_IMAGEURL)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_QUANTITY)) + ",   " +
                            res.getString(res.getColumnIndex(COLUMN_ITEM_TIMESTAMP)));
            res.moveToNext();
            pos++;
        }
    }


    public boolean insertItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_CODE, item.getItem_code());
        values.put(COLUMN_ITEM_NAME, item.getItem_name());
        values.put(COLUMN_ITEM_DESCRIPTION, item.getItem_description());
        values.put(COLUMN_ITEM_MANUFACTURER, item.getItem_manufacturer());
        values.put(COLUMN_ITEM_PRICE, item.getItem_price());
        values.put(COLUMN_ITEM_IMAGEURL, item.getItem_image_url());
        values.put(COLUMN_ITEM_QUANTITY, item.getItem_quantity());
        values.put(COLUMN_ITEM_TIMESTAMP, System.currentTimeMillis());

        db.insert(ITEMS_TABLE_NAME, null, values);

        if (print_db) printTable(db);
        return true;
    }

    public void deleteItemPosition(int position) {
        Cursor res = getDataAt(position);
        res.moveToFirst();
        long item_code = res.getLong(res.getColumnIndex(COLUMN_ITEM_CODE));
        deleteItem(item_code);
    }
    public int deleteItem(long item_code) {
        int ret = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.delete(ITEMS_TABLE_NAME, COLUMN_ITEM_CODE + " = ? ", new String[]{String.valueOf(item_code)});

        if (print_db) printTable(db);
        return ret;
    }
}



