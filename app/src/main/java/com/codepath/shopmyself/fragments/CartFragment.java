package com.codepath.shopmyself.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.activities.CheckoutActivity;
import com.codepath.shopmyself.adapters.CartItemsAdapter;
import com.codepath.shopmyself.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    ListView lvCart;
    ArrayList<Item> itemList;
    CartItemsAdapter cartItemsAdapter;
    TextView tvTotal;
    Button checkoutButton;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;
    private double total = 0;
    private int itemCount = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserId = mFirebaseUser.getUid();

        Log.d("sup", "2");
        itemList = new ArrayList<>();
        Log.d("sup", "2. size = " + itemList.size());
        cartItemsAdapter = new CartItemsAdapter(getActivity(), itemList);

        setupListListeners();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("sup", "3");
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvTotal = (TextView)view.findViewById(R.id.tvTotal);
        lvCart = (ListView)view.findViewById(R.id.lvCart);
        checkoutButton = (Button) view.findViewById(R.id.btnCheckout);
        checkoutButton.setEnabled(false);
        lvCart.setAdapter(cartItemsAdapter);
    }

    private void setupListListeners() {
        mDatabase
        .child("users")
        .child(mUserId)
        .child("cart")
        .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Item wishListItem
                    = new Item(dataSnapshot);
                cartItemsAdapter.add(wishListItem);
                cartItemsAdapter.notifyDataSetChanged();
                updateTotal();
                if(checkoutButton.isEnabled()) {
                    checkoutButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //check if list has content
                            if(itemList != null && total > 0) {
                                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                                intent.putExtra("itemList", Parcels.wrap(itemList));
                                Bundle bundle = new Bundle();
                                bundle.putDouble("total", total);
                                bundle.putInt("count", itemCount);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                long itemCode
                    = (Long)dataSnapshot.child("item_code").getValue();
                for (int i = 0; i < itemList.size(); i++) {
                    Item wishListItem = itemList.get(i);
                    if (wishListItem.getItem_code() == itemCode) {
                        itemList.remove(i);
                        cartItemsAdapter.notifyDataSetChanged();
                        updateTotal();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateTotal() {
        total = Item.getTotal(itemList);
        itemCount = Item.getSize(itemList);
        //make button enabled
        if(total > 0) {
            checkoutButton.setEnabled(true);
        }else {
            checkoutButton.setEnabled(false);
        }
        tvTotal.setText("(" + itemCount + " Items) - $" + String.format("%.2f", total));
    }

}


