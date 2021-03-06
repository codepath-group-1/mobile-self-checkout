package com.codepath.shopmyself.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.fragments.CartFragment;
import com.codepath.shopmyself.fragments.WishListFragment;
import com.codepath.shopmyself.models.Item;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationItemView cartItemView;
    private BottomNavigationItemView wishListItemView;
    private BottomNavigationItemView historyItemView;
    private BottomNavigationItemView previousItemView;
    private Fragment previousFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        } else {
            mUserId = mFirebaseUser.getUid();

            //Once user is logged in then, we can have
            //bottom navigation view to load up the selected fragment.
            //Scanner is the default fragment
            bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectFragmentItem(item);
                        return true;
                    }
                });

            cartItemView = (BottomNavigationItemView)bottomNavigationView
                           .findViewById(R.id.action_cart);
            wishListItemView = (BottomNavigationItemView)bottomNavigationView
                               .findViewById(R.id.action_wish_list);
           // historyItemView = (BottomNavigationItemView)bottomNavigationView
             //                 .findViewById(R.id.action_history);
            previousItemView = cartItemView;
            cartItemView.performClick();
        }
    }

    private void selectFragmentItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.action_scan:
                IntentIntegrator integrator
                        = new IntentIntegrator(this);
                integrator.setOrientationLocked(false)
                        .setCaptureActivity(ContinuousCaptureActivity.class)
                        .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                startActivity(integrator.createScanIntent());

                previousItemView.performClick();

                return;
            case R.id.action_cart:
                Log.d("sup", "1");
                fragmentClass = CartFragment.class;
                previousItemView = cartItemView;
                break;
            case R.id.action_wish_list:
                fragmentClass = WishListFragment.class;
                previousItemView = wishListItemView;
                break;
            /*case R.id.action_history:
                fragmentClass = HistoryFragment.class;
                previousItemView = historyItemView;
                break;*/
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        Class previousFragmentClass = null;
        if (previousFragment != null) {
            previousFragmentClass = previousFragment.getClass();
        }

        if (fragmentClass != previousFragmentClass) {
            // Insert the fragment by replacing any existing fragment
            try {
                fragment = (Fragment)fragmentClass.newInstance();
                previousFragment = fragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
        }
    }

    //will load login activity if user is not logged in
    private void loadLogInView() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if user signs out
        if (id == R.id.action_logout) {
            Item.clearFirebaseCart();
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }
}
