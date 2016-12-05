package com.codepath.shopmyself.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.shopmyself.AdapterCommunicator;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by supsingh on 12/3/2016.
 */

public class CartItemsAdapter extends ArrayAdapter<Item> {

    AdapterCommunicator aComm;

    public void setaComm(AdapterCommunicator aComm) {
        this.aComm = aComm;
    }

    public CartItemsAdapter(Context context, List<Item> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        ViewHolder viewHolder = null;

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
        Picasso.with(getContext()).load(item.getItem_image_url()).transform(new RoundedCornersTransformation(5, 5)).into(viewHolder.ivItemImage);
        viewHolder.tvItemName.setText(item.getItem_name());
        viewHolder.tvItemPrice.setText("$" + item.getItem_price());
        viewHolder.ibDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                aComm.deleteEntry(position);
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
