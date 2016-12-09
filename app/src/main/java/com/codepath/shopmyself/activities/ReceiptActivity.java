package com.codepath.shopmyself.activities;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.ReceiptArrayAdapter;
import com.codepath.shopmyself.models.Item;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.graphics.Color.WHITE;

public class ReceiptActivity extends AppCompatActivity {

    ImageView ivBalloons;
    RecyclerView rvReceipt;
    ArrayList<Item> items;
    TextView tvTotalPrice;
    double total;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting cart information from intents
        items = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
        total = getIntent().getExtras().getDouble("total");

        ReceiptArrayAdapter itemsAdapter = new ReceiptArrayAdapter(this, items);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvReceipt);
        listView.setAdapter(itemsAdapter);


        tvTotalPrice = (TextView) findViewById(R.id.tvReceiptPrice);
        tvTotalPrice.setText("(" + items.size() + " Items) " + String.format("$%.2f", total));

        ivBalloons = (ImageView) findViewById(R.id.ivBalloons);
        startBalloonAnimator(ivBalloons);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receipt, menu);
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem closingMenuItem = menu.findItem(R.id.menu_closer);
        tintMenuIcon(this,closingMenuItem, WHITE);
        return true;
    }

    //setting color of x in appbar to white
    public static void tintMenuIcon(Context context, MenuItem item, int color) {
        Drawable normalDrawable = item.getIcon();
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, color);

        item.setIcon(wrapDrawable);
    }


    public void onCloseReceipt(MenuItem mi) {
        // clear cart
        Item.clearFirebaseCart();
        //go back to main activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void startBalloonAnimator(final ImageView balloons) {
        animWobbleBalloon(balloons);
    }

    public void animWobbleBalloon (ImageView balloon) {
        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.wobble2);
        balloon.startAnimation(animShake);
    }
}
