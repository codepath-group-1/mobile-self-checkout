package com.codepath.shopmyself.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    long upc = 38000786693L;

    private final int REQUEST_CODE = 20;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not logged in, launch the Log In activity
            loadLogInView();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //will load login activity if user is not logged in
    private void loadLogInView() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivityForResult(intent, REQUEST_CODE);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickLaunchDetailActivity (View v) {
        Intent i = new Intent(MainActivity.this, ProductDetailsActivity.class);
        i.putExtra("upc", upc);
        startActivity(i);
    }

    public void scanCustomScanner(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false)
                  .setCaptureActivity(CustomScannerActivity.class)
                  .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                  .initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {

            IntentResult result =
                    IntentIntegrator
                            .parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.d(TAG, "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Scanned");
                    Toast.makeText(this, "Scanned: " + result.getContents(),
                            Toast.LENGTH_LONG).show();
                    Log.d("SCANNED: ", result.getContents());
                    upc = Long.valueOf(result.getContents());
                }
            } else {
                // This is important, otherwise the result will not be passed to the
                // fragment.
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}