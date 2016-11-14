package com.codepath.shopmyself.Models;

import android.database.Cursor;

import com.codepath.shopmyself.Database.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by supsingh on 11/11/2016.
 */

public class Items {

    public long getItem_code() {
        return item_code;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_description() {
        return item_description;
    }

    public String getItem_manufacturer() {
        return item_manufacturer;
    }

    public double getItem_price() {
        return item_price;
    }

    public String getItem_image_url() {
        return item_image_url;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    long item_code;
    String item_name;
    String item_description;
    String item_manufacturer;
    double item_price;
    String item_image_url;
    int item_quantity;

    public Items() {
    }
    public Items(Cursor res) {
        this.item_code = res.getLong(res.getColumnIndex(DBHelper.COLUMN_ITEM_CODE));
        this.item_name = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_NAME));
        this.item_description = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_DESCRIPTION));
        this.item_manufacturer = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_MANUFACTURER));
        this.item_price = res.getDouble(res.getColumnIndex(DBHelper.COLUMN_ITEM_PRICE));
        this.item_image_url = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_IMAGEURL));
        this.item_quantity = res.getInt(res.getColumnIndex(DBHelper.COLUMN_ITEM_QUANTITY));
    }
    public Items(long item_code, String item_name, String item_description, String item_manufacturer, double item_price, String item_image_url, int item_quantity) {
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_manufacturer = item_manufacturer;
        this.item_price = item_price;
        this.item_image_url = item_image_url;
        this.item_quantity = item_quantity;
    }

    @Override
    public String toString() {
        return "Items{" +
                "item_code=" + item_code +
                ", item_name='" + item_name + '\'' +
                ", item_description='" + item_description + '\'' +
                ", item_manufacturer='" + item_manufacturer + '\'' +
                ", item_price=" + item_price +
                ", item_image_url='" + item_image_url + '\'' +
                ", item_quantity='" + item_quantity + '\'' +
                '}';
    }

    public static Items fromJSON(JSONObject response) {
        Items newItem = new Items();

        try {
            JSONArray itemsArray = response.getJSONArray("items");
            JSONObject itemsDetails = itemsArray.getJSONObject(0);
            JSONArray imagesArray = itemsDetails.getJSONArray("images");

            newItem.item_code = itemsDetails.getLong("ean");
            newItem.item_name = itemsDetails.getString("title");
            newItem.item_description = itemsDetails.getString("description");
            newItem.item_manufacturer = itemsDetails.getString("brand");
            newItem.item_price = itemsDetails.getDouble("lowest_recorded_price");
            newItem.item_image_url = imagesArray.getString(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newItem;
    }
}
