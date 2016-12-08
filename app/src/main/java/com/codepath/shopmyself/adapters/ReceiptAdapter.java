package com.codepath.shopmyself.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.Item;

import java.util.List;

public class ReceiptAdapter extends
        RecyclerView.Adapter<ReceiptAdapter.ViewHolder> {


    // Store a member variable for the contacts
    private List<Item> itemList;
    // Store the context for easy access
    private Context context;

    // Pass in the contact array into the constructor
    public ReceiptAdapter(Context context, List<Item> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView receiptItemImage;
        public TextView receiptNumberOf;
        public TextView receiptItemPrice;
        public TextView receiptItemName;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            receiptItemImage = (ImageView) itemView.findViewById(R.id.ivReceiptItemImage);
            receiptNumberOf = (TextView) itemView.findViewById(R.id.tvReceiptNumberOf);
            receiptItemPrice = (TextView) itemView.findViewById(R.id.tvReceiptItemPrice);
            receiptItemName = (TextView) itemView.findViewById(R.id.tvReceiptItemName);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ReceiptAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View receiptView = inflater.inflate(R.layout.item_receipt, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(receiptView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ReceiptAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Item item = itemList.get(position);

        // Set item views based on your views and data model
        TextView tvPrice = viewHolder.receiptItemPrice;
        tvPrice.setText(String.format("$%.2f", item.getItem_price()));

        TextView tvNumberOf = viewHolder.receiptNumberOf;
        tvNumberOf.setText(String.format("x%d", item.getItem_quantity()));

        TextView tvName = viewHolder.receiptItemName;
        tvName.setText(item.getItem_name());

        ImageView ivItem = viewHolder.receiptItemImage;
        Glide.with(context).load(item.getItem_image_url()).into(ivItem);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
