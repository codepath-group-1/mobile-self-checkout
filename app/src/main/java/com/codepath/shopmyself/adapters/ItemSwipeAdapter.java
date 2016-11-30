package com.codepath.shopmyself.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.crashlytics.android.beta.Beta.TAG;

/**
 * Created by supsingh on 11/28/2016.
 */

public class ItemSwipeAdapter extends ArrayAdapter<Item> {
    private final ArrayList<Item> items;
    private final LayoutInflater layoutInflater;

    public ItemSwipeAdapter(Context context, ArrayList<Item> items) {
        super(context, -1);
        this.items = items;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "sup: getView for pos: " + position);
        Item item = items.get(position);
        View view = layoutInflater.inflate(R.layout.item_swipe, parent, false);
        Picasso.with(getContext()).load(item.getItem_image_url()).transform(new RoundedCornersTransformation(10, 10)).into(((ImageView) view.findViewById(R.id.ivSwipeItemImage)));
        ((TextView)view.findViewById(R.id.tvSwipeTitle)).setText(item.getItem_name());
        ((TextView)view.findViewById(R.id.tvSwipePrice)).setText("$" + item.getItem_price());
        return view;
    }

    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}

