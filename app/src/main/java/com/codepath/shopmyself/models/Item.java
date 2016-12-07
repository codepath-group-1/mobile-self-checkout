package com.codepath.shopmyself.models;

import android.database.Cursor;

import com.codepath.shopmyself.database.DBHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.List;
import java.util.Map;

/**
 * Created by supsingh on 11/11/2016.
 */

@Parcel
public class Item {

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

    public Item() {
    }
    public Item(Cursor res) {
        this.item_code = res.getLong(res.getColumnIndex(DBHelper.COLUMN_ITEM_CODE));
        this.item_name = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_NAME));
        this.item_description = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_DESCRIPTION));
        this.item_manufacturer = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_MANUFACTURER));
        this.item_price = res.getDouble(res.getColumnIndex(DBHelper.COLUMN_ITEM_PRICE));
        this.item_image_url = res.getString(res.getColumnIndex(DBHelper.COLUMN_ITEM_IMAGEURL));
        this.item_quantity = res.getInt(res.getColumnIndex(DBHelper.COLUMN_ITEM_QUANTITY));
    }
    public Item(long item_code, String item_name, String item_description, String item_manufacturer, double item_price, String item_image_url, int item_quantity) {
        this.item_code = item_code;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_manufacturer = item_manufacturer;
        this.item_price = item_price;
        this.item_image_url = item_image_url;
        this.item_quantity = item_quantity;
    }

    public Item(DataSnapshot dataSnapshot) {
        Map<String, Object> map = (Map<String, Object>)dataSnapshot.getValue();

        this.item_code = ((Number)map.get("item_code")).longValue();
        this.item_name = (String)map.get("item_name");
        this.item_description = (String)map.get("item_description");
        this.item_manufacturer = (String)map.get("item_manufacturer");
        this.item_price = ((Number)map.get("item_price")).doubleValue();
        this.item_image_url = (String)map.get("item_image_url");
        this.item_quantity = ((Number)map.get("item_quantity")).intValue();
    }

    public void addToFirebaseWishList() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase
            = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        mDatabase.child("users").child(mUserId).child("wish_list")
                 .orderByChild("item_code")
                 .equalTo(item_code)
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (!dataSnapshot.hasChildren()) {
                             dataSnapshot.getRef().push().setValue(Item.this);
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                });
    }

    public void removeFromFirebaseWishList() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase
                = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        mDatabase.child("users").child(mUserId).child("wish_list")
                 .orderByChild("item_code")
                 .equalTo(item_code)
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.hasChildren()) {
                             DataSnapshot firstChild
                                 = dataSnapshot.getChildren()
                                               .iterator()
                                               .next();
                             firstChild.getRef().removeValue();
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                });
    }

    public void addToFirebaseCart() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase
                = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        mDatabase.child("users").child(mUserId).child("cart")
                 .orderByChild("item_code")
                 .equalTo(item_code)
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (!dataSnapshot.hasChildren()) {
                             dataSnapshot.getRef().push().setValue(Item.this);
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                });
    }

    public void removeFromFirebaseCart() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        DatabaseReference mDatabase
                = FirebaseDatabase.getInstance().getReference();
        String mUserId = mFirebaseUser.getUid();
        mDatabase.child("users").child(mUserId).child("cart")
                 .orderByChild("item_code")
                 .equalTo(item_code)
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.hasChildren()) {
                             DataSnapshot firstChild
                                 = dataSnapshot.getChildren()
                                               .iterator()
                                               .next();
                             firstChild.getRef().removeValue();
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                     }
                });
    }

    @Override
    public String toString() {
        return "Item{" +
                "item_code=" + item_code +
                ", item_name='" + item_name + '\'' +
                ", item_description='" + item_description + '\'' +
                ", item_manufacturer='" + item_manufacturer + '\'' +
                ", item_price=" + item_price +
                ", item_image_url='" + item_image_url + '\'' +
                ", item_quantity='" + item_quantity + '\'' +
                '}';
    }

    public static Item fromJSON(JSONObject response) {
        Item newItem = new Item();

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

    public static double getTotal(List<Item> items) {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getItem_price();
        }
        return total;
    }
}
