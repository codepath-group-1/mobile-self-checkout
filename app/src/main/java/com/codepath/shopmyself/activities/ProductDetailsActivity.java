package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.codepath.shopmyself.HTTPClient;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.ItemSwipeAdapter;
import com.codepath.shopmyself.models.Item;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import in.arjsna.swipecardlib.SwipeCardView;

public class ProductDetailsActivity extends AppCompatActivity {

    private final String TAG = "sup:";
    private final String cacheFile = "jsoncache.txt";
    HTTPClient client;
    Item newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        long upc = getIntent().getLongExtra("upc", 0);
        Log.d(TAG, "ProductDetailsActivity upc = " + upc);

        client = HTTPClient.getInstance();
        client.lookupUPC(upc, new JsonHttpResponseHandler(){

            public void processSuccess(JSONObject response) {
                newItem = Item.fromJSON(response);
                Log.d(TAG, "item: " + newItem.toString());

                //renderOnFragment(newItem);
                setupSwipeCards(newItem);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                processSuccess(response);

                // writeToFile();
                File filesDir = getFilesDir();
                File todoFile = new File(filesDir, cacheFile);
                try {
                    FileUtils.writeStringToFile(todoFile, response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, "sup:1 onFailure");
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, "sup:2 onFailure");
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, "sup:3 onFailure");

                File filesDir = getFilesDir();
                File todoFile = new File(filesDir, cacheFile);
                try {
                    String json = FileUtils.readFileToString(todoFile).toString();
                    try {
                        JSONObject response = new JSONObject(json);
                        processSuccess(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });




    }

/*
    private void renderOnFragment(Item newItem) {
        ImageView ivItemImage2 = (ImageView) findViewById(R.id.ivItemImage2);
        TextView tvTitle2 = (TextView) findViewById(R.id.tvTitle2);
        TextView tvPrice2 = (TextView) findViewById(R.id.tvPrice2);
        Picasso.with(this).load(newItem.getItem_image_url()).transform(new RoundedCornersTransformation(10, 10)).into(ivItemImage2);
        tvTitle2.setText(newItem.getItem_name());
        tvPrice2.setText("$" + newItem.getItem_price());
    }
*/

    public void onClickAddWishlist(View v) {
        if(newItem != null) {
            newItem.addToFirebaseWishList();
            Toast.makeText(this, newItem.getItem_name() + "Added to wishlist" , Toast.LENGTH_LONG).show();
        }
    }


    // Swipe Cards
    private ArrayList<Item> al;
    SwipeCardView swipeItemView;
    private  void setupSwipeCards(Item item) {


        al = new ArrayList<>();
        al.add(item);
        ItemSwipeAdapter arrayAdapter = new ItemSwipeAdapter(this, al);
        swipeItemView = (SwipeCardView) findViewById(R.id.svItem);
        swipeItemView.setAdapter(arrayAdapter);
        swipeItemView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {
                Log.d(TAG, "Swipe Left!");
            }

            @Override
            public void onCardExitRight(Object dataObject) {
                Log.d(TAG, "Swipe Right!");
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {
                Log.d(TAG, "Swipe Top!");
                Toast.makeText(getApplicationContext(), "Item discarded", Toast.LENGTH_SHORT).show();
                backToScanningActivity();
            }

            @Override
            public void onCardExitBottom(Object dataObject) {
                Log.d(TAG, "Swipe Bottom!");
                Toast.makeText(getApplicationContext(), "Item added to cart", Toast.LENGTH_SHORT).show();
                backToScanningActivity();
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    public void backToScanningActivity () {
        this.onBackPressed();
    }


}
