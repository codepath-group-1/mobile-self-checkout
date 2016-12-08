package com.codepath.shopmyself.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.ReceiptAdapter;
import com.codepath.shopmyself.models.Item;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ReceiptActivity extends AppCompatActivity {

    ImageView ivBalloons;
    RecyclerView rvReceipt;
    ArrayList<Item> items;
    //TextView tvNumberOf;
    //TextView tvTotalPrice;
    double total;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //tvNumberOf = (TextView) findViewById(R.id.tvReceiptTotalNumberOf);
        //tvTotalPrice = (TextView) findViewById(R.id.tvReceiptPrice);

       // ivBalloons = (ImageView) findViewById(R.id.ivBalloons);
        //ivBalloons.setVisibility(View.INVISIBLE); //set ballons to invisible at first
        rvReceipt = (RecyclerView) findViewById(R.id.rvReceipt);

        //getting cart information from intents
        items = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
        total = getIntent().getExtras().getDouble("total");

        //tvNumberOf.setText(String.valueOf(items.size()));
        //tvTotalPrice.setText(String.format("$%.2f", total));

        // Create adapter passing in the items data
        ReceiptAdapter adapter = new ReceiptAdapter(this, items);

        // Attach the adapter to the recyclerview to populate items
        rvReceipt.setAdapter(adapter);

        // Set layout manager to position the items
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvReceipt.setLayoutManager(linearLayout);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvReceipt.addItemDecoration(itemDecoration);

        //call ballon animation
       // balloonAnimator(ivBalloons);
    }

    private void balloonAnimator(View view) {
        RelativeLayout root = (RelativeLayout) findViewById( R.id.root_layout_receipt);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics( dm );
        int statusBarOffset = dm.heightPixels - root.getMeasuredHeight();

        int originalPos[] = new int[2];
        view.getLocationOnScreen( originalPos );

        int xDest = dm.widthPixels;
        xDest -= (view.getMeasuredWidth());
        int yDest = dm.heightPixels - (view.getMeasuredHeight()) - statusBarOffset;

        TranslateAnimation animate = new TranslateAnimation( 0, xDest - originalPos[0] , 0, yDest - originalPos[1] );
        animate.setDuration(1000);
        animate.setFillAfter( true );
        ivBalloons.setVisibility(View.VISIBLE);
        ivBalloons.startAnimation(animate);
        ivBalloons.setVisibility(View.GONE);
    }

}
