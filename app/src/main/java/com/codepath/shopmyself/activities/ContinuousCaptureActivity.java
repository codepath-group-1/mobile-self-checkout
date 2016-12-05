package com.codepath.shopmyself.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.List;

/**
 * This performs continuous scanning. It does not return to calling activity
 * after a scan.
 */
public class ContinuousCaptureActivity
extends AppCompatActivity
implements DecoratedBarcodeView.TorchListener {

    private static final String TAG = ContinuousCaptureActivity.class.getSimpleName();
    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private MenuItem switchFlashlightMenuItem;

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            barcodeView.pause();
            beepManager.playBeepSoundAndVibrate();

            String scannedMessage = "Scanned: " + result.getText();
            Log.d(TAG, scannedMessage);
            Toast.makeText(ContinuousCaptureActivity.this, scannedMessage,
                           Toast.LENGTH_LONG).show();
            long upc = Long.valueOf(result.getText());
            launchDetailActivity(upc);

            barcodeView.resume();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public void launchDetailActivity(long upc) {
        Intent productDetailsIntent
            = new Intent(this, ProductDetailsActivity.class);
        productDetailsIntent.putExtra("upc", upc);
        startActivity(productDetailsIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barcodeView
            = (DecoratedBarcodeView)findViewById(R.id.zxing_barcode_scanner);
        barcodeView.setTorchListener(this);
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_continuous_capture, menu);

        switchFlashlightMenuItem = menu.findItem(R.id.action_flashlight);
        if (!hasFlash()) {
            switchFlashlightMenuItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_flashlight) {
            switchFlashlight(null);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        barcodeView.pause();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
    private boolean hasFlash() {
        return getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void switchFlashlight(View view) {
        if (getString(R.string.turn_on_flashlight).equals(switchFlashlightMenuItem.getTitle())) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }

    @Override
    public void onTorchOn() {
        switchFlashlightMenuItem.setTitle(R.string.turn_off_flashlight);
        switchFlashlightMenuItem.setIcon(R.drawable.ic_action_turn_off_flashlight);
    }

    @Override
    public void onTorchOff() {
        switchFlashlightMenuItem.setTitle(R.string.turn_on_flashlight);
        switchFlashlightMenuItem.setIcon(R.drawable.ic_action_turn_on_flashlight);
    }
}
