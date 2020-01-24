package com.pashikhmin.ismobileapp.network;

import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginWebClient extends WebViewClient {
    private static final String TAG = "LoginWebClient";

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.i(TAG, "Loading URL " + request.getUrl().toString());
        return super.shouldOverrideUrlLoading(view, request);
    }
}
