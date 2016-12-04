package com.codepath.shopmyself.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.activities.ContinuousCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;

public class ScannerFragment extends Fragment {

    protected static final String TAG = ScannerFragment.class
                                                       .getSimpleName();

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
                  .setCaptureActivity(ContinuousCaptureActivity.class)
                  .setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        startActivity(integrator.createScanIntent());
    }
}
