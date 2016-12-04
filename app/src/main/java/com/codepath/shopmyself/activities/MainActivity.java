package com.codepath.shopmyself.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.fragments.ScannerFragment;
import com.codepath.shopmyself.fragments.WishListFragment;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;

    private BottomNavigationView bottomNavigationView;

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

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment startFragment = new ScannerFragment();
            fragmentManager.beginTransaction().replace(R.id.flContainer, startFragment).commit();
        }

    }

    private void selectFragmentItem(MenuItem item) {
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (item.getItemId()) {
            case R.id.action_scan:
                fragmentClass = ScannerFragment.class;
                break;
            case R.id.action_cart:

                break;
            case R.id.action_wish_list:
                fragmentClass = WishListFragment.class;
                break;
            case R.id.action_history:

                break;
            default:
                fragmentClass = ScannerFragment.class;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Class currentFragmentClass = fragmentManager.findFragmentById(R.id.flContainer)
                                                    .getClass();

        if (fragmentClass != currentFragmentClass) {
            // Insert the fragment by replacing any existing fragment
            try {
                fragment = (Fragment) fragmentClass.newInstance();
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
            mFirebaseAuth.signOut();
            loadLogInView();
        }

        return super.onOptionsItemSelected(item);
    }


    public void paymentDetails (View v) {
        Intent i = new Intent(MainActivity.this, PaymentDetailsActivity.class);
        startActivity(i);
    }
}
