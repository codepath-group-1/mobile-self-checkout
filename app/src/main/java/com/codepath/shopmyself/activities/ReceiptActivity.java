package com.codepath.shopmyself.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ReceiptActivity extends AppCompatActivity {


    ImageView ivBalloons;
    RecyclerView rvReceipt;
    ArrayList<Item> items;
    double total;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_receipt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ivBalloons = (ImageView) findViewById(R.id.ivBalloons);
        rvReceipt = (RecyclerView) findViewById(R.id.rvReceipt);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        rvReceipt.setLayoutManager(linearLayout);

        //getting cart information from intents
        items = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
        total = getIntent().getExtras().getDouble("total");

        //call ballon animation
        balloonAnimator();
    }

    private void balloonAnimator() {
        View v = this.findViewById(android.R.id.content);
        TranslateAnimation animate = new TranslateAnimation(0,0,0, -v.getHeight()); // <------  slide up
        animate.setDuration(200);
        animate.setFillAfter(true);
        ivBalloons.startAnimation(animate);
        ivBalloons.setVisibility(View.GONE);
    }

}
