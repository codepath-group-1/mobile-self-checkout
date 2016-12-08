package com.codepath.shopmyself.adapters;


import android.content.Context;
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

public class ReceiptArrayAdapter extends ArrayAdapter<Item> {

    public ReceiptArrayAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data model based on position
        Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_receipt, parent, false);
        }

        // Set item views based on your views and data model
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tvReceiptItemPrice);
        tvPrice.setText(String.format("$%.2f", item.getItem_price()));

        TextView tvNumberOf = (TextView) convertView.findViewById(R.id.tvReceiptNumberOf);
        tvNumberOf.setText(String.format("x%d", 1));

        TextView tvName = (TextView) convertView.findViewById(R.id.tvReceiptItemName);
        tvName.setText(item.getItem_name());

        ImageView ivItem = (ImageView) convertView.findViewById(R.id.ivReceiptItemImage);;
        Glide.with(getContext()).load(item.getItem_image_url()).into(ivItem);

        // Return the completed view to render on screen
        return convertView;
    }

}
