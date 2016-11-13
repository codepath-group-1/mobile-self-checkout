package com.codepath.shopmyself;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by supsingh on 11/12/2016.
 */
public class HTTPClient {
    private String TAG = "sup: HTTPClient";
    private String urlUPCAPI = "https://api.upcitemdb.com/prod/trial/lookup";
    private AsyncHttpClient asyncHttpClient = null;

    private static HTTPClient ourInstance = new HTTPClient();

    public static HTTPClient getInstance() {
        return ourInstance;
    }

    private HTTPClient() {
        asyncHttpClient = new AsyncHttpClient();
    }

    public AsyncHttpClient getClient() {
        return asyncHttpClient;
    }

    public void lookupUPC(long UPCNumber, AsyncHttpResponseHandler handler) {
        RequestParams params = new RequestParams();
        params.put("upc", UPCNumber);
        Log.d(TAG, "Item UPC lookup url: " + urlUPCAPI.toString() + "?" + params.toString());
        getClient().get(urlUPCAPI, params, handler);
    }
}
