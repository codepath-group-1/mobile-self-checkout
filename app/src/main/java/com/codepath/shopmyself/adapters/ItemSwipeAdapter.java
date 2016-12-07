package com.codepath.shopmyself.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import java.util.ArrayList;

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
        SwipeViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_swipe, parent, false);

            viewHolder = new SwipeViewHolder();
            viewHolder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivSwipeItemImage);
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvSwipeTitle);
            viewHolder.tvItemPrice = (TextView) convertView.findViewById(R.id.tvSwipePrice);

            convertView.setTag(viewHolder);
        }
        viewHolder = (SwipeViewHolder) convertView.getTag();

        Glide.with(getContext()).load(item.getItem_image_url()).into(viewHolder.ivItemImage);
        viewHolder.tvItemName.setText(item.getItem_name());
        viewHolder.tvItemPrice.setText("$" + String.format("%.2f",item.getItem_price()));

        return convertView;
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


class SwipeViewHolder {
    ImageView ivItemImage;
    TextView tvItemName;
    TextView tvItemPrice;
}
