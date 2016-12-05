package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;

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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        long upc = getIntent().getLongExtra("upc", 0);
        Log.d(TAG, "ProductDetailsActivity upc = " + upc);

        client = HTTPClient.getInstance();
        client.lookupUPC(upc, new JsonHttpResponseHandler(){

            public void processSuccess(JSONObject response) {
                newItem = Item.fromJSON(response);
                Log.d(TAG, "item: " + newItem.toString());

                //renderOnFragment(newItem);
                setupSwipeCards(newItem);
                startRepeatedJiggle();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    private  void setupSwipeCards(final Item item) {


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
                addItemToCart(item);
                backToScanningActivity();
            }
        });
        arrayAdapter.notifyDataSetChanged();
    }

    public void backToScanningActivity () {
        this.onBackPressed();
    }

    public void startRepeatedJiggle () {
        Timer t1 = new Timer();
        t1.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animJiggleCard();
                    }
                });
            }
        }, 3, 5000); // start Jiggling after 1 second, Jiggle every 3 seconds


        Timer t2 = new Timer();
        t2.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        animWobbleCart();
                    }
                });

            }
        }, 2, 5000); // start Wobbling after 2 second, Jiggle every 5 seconds


    }

    public void animJiggleCard () {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.shake);
        swipeItemView = (SwipeCardView) findViewById(R.id.svItem);
        swipeItemView.startAnimation(animShake);
    }

    public void animWobbleCart () {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.wobble);
        ImageView ivShoppingCart= (ImageView) findViewById(R.id.ivShoppingCart);
        ivShoppingCart.startAnimation(animShake);
    }

    public void addItemToCart (Item addItem) {
        Item.addToCart(addItem);
    }
}
