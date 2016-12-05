package com.codepath.shopmyself.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.shopmyself.AdapterCommunicator;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.CartItemsAdapter;
import com.codepath.shopmyself.models.Item;

import java.util.ArrayList;

public class CartFragment extends Fragment implements AdapterCommunicator {

    ListView lvCart;
    ArrayList<Item> itemList;
    CartItemsAdapter cartItemsAdapter;
    TextView tvTotal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("sup", "3");
        View v = inflater.inflate(R.layout.fragment_cart, container, false);

        tvTotal = (TextView) v.findViewById(R.id.tvTotal);
        lvCart = (ListView) v.findViewById(R.id.lvCart);
        lvCart.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setaComm(this);
        cartItemsAdapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("sup", "2");
        itemList = Item.getCartItems();
        Log.d("sup", "2. size = " + itemList.size());
        cartItemsAdapter = new CartItemsAdapter(getActivity(), itemList);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void deleteEntry(int position) {
        Log.d("sup:", "Deleting position " + position);
        itemList.remove(position);
        cartItemsAdapter.notifyDataSetChanged();
        updateTotal();
    }

    @Override
    public void onResume() {
        Log.d("sup:", "onResume");
        cartItemsAdapter.notifyDataSetChanged();
        updateTotal();
        super.onResume();
    }

    public void updateTotal () {
        double total = Item.getTotal();
        tvTotal.setText("$" + total);
    }
}
