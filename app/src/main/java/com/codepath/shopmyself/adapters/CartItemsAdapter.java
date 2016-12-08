package com.codepath.shopmyself.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import java.util.List;

/**
 * Created by supsingh on 12/3/2016.
 */

public class CartItemsAdapter extends ArrayAdapter<Item> {

    public CartItemsAdapter(Context context, List<Item> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("sup", "4");
        Item item = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_cart, null);
            viewHolder = new ViewHolder();
            viewHolder.ivItemImage = (ImageView) convertView.findViewById(R.id.ivCartItemImage);
            viewHolder.tvItemName = (TextView) convertView.findViewById(R.id.tvCartItemName);
            viewHolder.tvItemPrice = (TextView) convertView.findViewById(R.id.tvCartItemPrice);
            viewHolder.ibDeleteButton = (ImageButton) convertView.findViewById(R.id.ibDeleteButton);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();
        Glide.with(getContext()).load(item.getItem_image_url()).into(viewHolder.ivItemImage);
        viewHolder.tvItemName.setText(item.getItem_name());
        viewHolder.tvItemPrice.setText("$" + String.format("%.2f", item.getItem_price()));
        viewHolder.ibDeleteButton.setTag(item);
        viewHolder.ibDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item cartItem = (Item)view.getTag();
                cartItem.removeFromFirebaseCart();
            }
        });

        return convertView;
    }
}

class ViewHolder {
    ImageView ivItemImage;
    TextView tvItemName;
    TextView tvItemPrice;
    ImageButton ibDeleteButton;
}
