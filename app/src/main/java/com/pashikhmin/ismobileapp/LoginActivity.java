package com.pashikhmin.ismobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ismobileapp.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private WebView webView;
    private boolean jsOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CookieManager.getInstance().removeAllCookies(null);
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        findViewById(R.id.secure_ping_btn).setOnClickListener(e -> loginInWebView());
        findViewById(R.id.switch_js_btn).setOnClickListener(e -> switchJs());
        findViewById(R.id.to_app).setOnClickListener(e -> toApp());
    }

    private void toApp() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("token", CookieManager.getInstance().getCookie("http://192.168.1.56:8080"));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void loginInWebView() {
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://192.168.1.56:8080/secure_ping");
    }

    private void switchJs() {
        jsOn = !jsOn;
        webView.getSettings().setJavaScriptEnabled(jsOn);
        String newMessage = "Switch " + (jsOn ? "OFF" : "ON") + " JS";
        ((Button) findViewById(R.id.switch_js_btn)).setText(newMessage);
    }
}
