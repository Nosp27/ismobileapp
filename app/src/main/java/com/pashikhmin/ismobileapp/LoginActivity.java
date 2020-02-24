package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import com.pashikhmin.ismobileapp.R;
import com.pashikhmin.ismobileapp.network.ProductionConnector;

public class LoginActivity extends AppCompatActivity {
    private static final String SERVERNAME = ProductionConnector.getServerAddress();
    private static final String TAG = "LoginActivity";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Need Login");
        setContentView(R.layout.activity_login);
//        CookieManager.getInstance().removeAllCookies(null);
        webView = findViewById(R.id.webview);
        findViewById(R.id.to_app).setOnClickListener(e -> toApp());
        loginInWebView();
    }

    private void toApp() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("token", CookieManager.getInstance().getCookie(SERVERNAME));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void loginInWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(SERVERNAME + "/secure_ping");
    }
}
