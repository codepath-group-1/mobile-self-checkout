package com.codepath.shopmyself.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import java.util.List;

public class WishListArrayAdapter extends ArrayAdapter<Item> {

    private static class ViewHolder {
        ImageView ivWishListItemImage;
        TextView tvWishListItemName;
        Button btnAddToCart;
    }

    public WishListArrayAdapter(Context context, List<Item> wishList) {
        super(context, R.layout.item_wish_list, wishList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item wishListItem = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_wish_list, parent,
                                           false);

            viewHolder.ivWishListItemImage
                    = (ImageView)convertView
                      .findViewById(R.id.ivWishListItemImage);
            viewHolder.tvWishListItemName
                    = (TextView)convertView
                      .findViewById(R.id.tvWishListItemName);
            viewHolder.btnAddToCart
                    = (Button)convertView
                      .findViewById(R.id.btnAddToCart);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.ivWishListItemImage.setImageResource(0);
        String imagePath = wishListItem.getItem_image_url();
        Glide.with(getContext()).load(imagePath)
               .placeholder(R.mipmap.ic_launcher)
               .into(viewHolder.ivWishListItemImage);

        viewHolder.tvWishListItemName.setText(wishListItem.getItem_name());

        viewHolder.btnAddToCart.setTag(wishListItem);
        viewHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item wishListItem = (Item)view.getTag();
                wishListItem.addToFirebaseCart();
            }
        });

        return convertView;
    }
}
