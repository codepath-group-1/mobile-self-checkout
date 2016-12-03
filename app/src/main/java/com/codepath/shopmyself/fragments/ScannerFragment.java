package com.codepath.shopmyself.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.activities.CustomScannerActivity;
import com.codepath.shopmyself.activities.ProductDetailsActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static android.app.Activity.RESULT_OK;

public class ScannerFragment extends Fragment {

    private long upc;
    private static final String TAG = "Scanner Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_custom_scanner, container, false);
        //call custom scanner
        scanCustomScanner();

        return v;
    }

    public void scanCustomScanner() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setOrientationLocked(false)
                .setCaptureActivity(CustomScannerActivity.class)
                .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                .initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result
                    = IntentIntegrator
                    .parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
                    Log.d(TAG, "Cancelled scan");
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    String scannedMessage = "Scanned: " + result.getContents();
                    Log.d(TAG, scannedMessage);
                    Toast.makeText(getActivity(), scannedMessage,
                            Toast.LENGTH_LONG).show();
                    upc = Long.valueOf(result.getContents());
                    Log.d("SCANNED: ", "upc: " + upc);
                    Toast.makeText(getActivity(), "Scanned: " + upc, Toast.LENGTH_LONG).show();
                    launchDetailActivity();
                }
            } else {
                Log.d(TAG, "Scan ERROR");
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void launchDetailActivity() {
        Intent i = new Intent(getActivity(), ProductDetailsActivity.class);
        i.putExtra("upc", upc);
        startActivity(i);

    }
}


