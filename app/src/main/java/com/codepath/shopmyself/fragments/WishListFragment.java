package com.codepath.shopmyself.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.adapters.WishListArrayAdapter;
import com.codepath.shopmyself.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class WishListFragment extends Fragment {

    private ListView lvWishList;
    private ArrayList<Item> wishList;
    private WishListArrayAdapter wishListAdapter;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_wish_list, container, false);

        lvWishList = (ListView) v.findViewById(R.id.lvWishList);
        wishList = new ArrayList<>();
        wishListAdapter = new WishListArrayAdapter(getContext(), wishList);
        lvWishList.setAdapter(wishListAdapter);

        setupListViewListeners();

        return  v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserId = mFirebaseUser.getUid();
    }

    private void setupListViewListeners() {
        mDatabase
                .child("users")
                .child(mUserId)
                .child("wish_list")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Item wishListItem
                                = new Item((Map<String, Object>)dataSnapshot.getValue());
                        wishListAdapter.add(wishListItem);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        long itemCode
                                = (Long)dataSnapshot.child("item_code").getValue();
                        for (int i = 0; i < wishList.size(); i++) {
                            Item wishListItem = wishList.get(i);
                            if (wishListItem.getItem_code() == itemCode) {
                                wishList.remove(i);
                                wishListAdapter.notifyDataSetChanged();
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

        lvWishList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int position,
                                           long id) {
                mDatabase.child("users").child(mUserId).child("wish_list")
                        .orderByChild("item_code")
                        .equalTo(wishList.get(position).getItem_code())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()) {
                                    DataSnapshot firstChild
                                            = dataSnapshot.getChildren()
                                            .iterator()
                                            .next();
                                    firstChild.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                return true;
            }
        });

    }
}
