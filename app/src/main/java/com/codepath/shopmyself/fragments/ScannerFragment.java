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
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.activities.CustomScannerActivity;
import com.codepath.shopmyself.activities.ProductDetailsActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerFragment extends Fragment {

    protected static final String TAG = ScannerFragment.class
                                                       .getSimpleName();

    private long upc;
    private Button btnScanBarcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_scanner, container, false);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btnScanBarcode = (Button)view.findViewById(R.id.btn_scan_barcode);

        View.OnClickListener scanBarcodeClickListener
            = new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  scanCustomScanner();
              }
        };
        btnScanBarcode.setOnClickListener(scanBarcodeClickListener);
    }

    public void scanCustomScanner() {
        IntentIntegrator integrator
            = IntentIntegrator.forSupportFragment(this);
        integrator.setOrientationLocked(false)
                  .setCaptureActivity(CustomScannerActivity.class)
                  .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
                  .initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        IntentResult result
            = IntentIntegrator
              .parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Log.d(TAG, "Cancelled scan");
                Toast.makeText(getActivity(), "Cancelled",
                               Toast.LENGTH_LONG).show();
            } else {
                String scannedMessage = "Scanned: " + result.getContents();
                Log.d(TAG, scannedMessage);
                Toast.makeText(getActivity(), scannedMessage,
                               Toast.LENGTH_LONG).show();
                upc = Long.valueOf(result.getContents());
                launchDetailActivity();
            }
        } else {
            Log.d(TAG, "Scan ERROR");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void launchDetailActivity() {
        Intent i = new Intent(getActivity(), ProductDetailsActivity.class);
        i.putExtra("upc", upc);
        startActivity(i);

    }
}


