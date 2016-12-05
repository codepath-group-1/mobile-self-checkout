package com.codepath.shopmyself.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.shopmyself.AdapterCommunicator;
import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.CartItemsAdapter;
import com.codepath.shopmyself.models.Item;

import java.util.ArrayList;

public class CartFragment extends Fragment implements AdapterCommunicator {

    ListView lvCart;
    ArrayList<Item> itemList;
    CartItemsAdapter cartItemsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);


        lvCart = (ListView) v.findViewById(R.id.lvCart);
        lvCart.setAdapter(cartItemsAdapter);
        cartItemsAdapter.setaComm(this);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        itemList = Item.getCartItems();
        cartItemsAdapter = new CartItemsAdapter(getActivity(), itemList);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void deleteEntry(int position) {
        Log.d("sup:", "Deleting position " + position);
        /*itemList.remove(position);
        cartItemsAdapter.notifyDataSetChanged();*/
    }


/*

    public static CartFragment newInstance(ArrayList<Item> itemListArg) {
        CartFragment cartFragment = new CartFragment();
        return cartFragment;
    }
*/

}
