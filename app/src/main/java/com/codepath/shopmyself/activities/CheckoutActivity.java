package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import org.parceler.Parcels;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    ArrayList<Item> itemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemArrayList = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
    }
}
