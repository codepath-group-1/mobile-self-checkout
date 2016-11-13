package com.codepath.shopmyself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shopmyself.Models.Items;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProductDetailsActivity extends AppCompatActivity {

    private final String TAG = "sup:";
    HTTPClient client;
    Items newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        long upc = getIntent().getLongExtra("upc", 0);
        Log.d(TAG, "ProductDetailsActivity upc = " + upc);

        client = HTTPClient.getInstance();
        client.lookupUPC(upc, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                newItem = Items.fromJSON(response);
                Log.d(TAG, "item: " + newItem.toString());
                renderOnFragment(newItem);
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void renderOnFragment(Items newItem) {
        ImageView ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvPrice = (TextView) findViewById(R.id.tvPrice);
        Picasso.with(this).load(newItem.getItem_image_url()).transform(new RoundedCornersTransformation(10, 10)).into(ivItemImage);
        tvTitle.setText(newItem.getItem_name());
        tvPrice.setText("$" + newItem.getItem_price());
    }
}
